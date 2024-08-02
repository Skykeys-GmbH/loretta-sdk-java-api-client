package aero.loretta.client.api;

import aero.loretta.client.api.arinc633.Metadata;
import aero.loretta.client.exception.ApiErrorException;
import aero.loretta.client.exception.AuthenticationFailedException;
import aero.loretta.client.exception.InvalidGufiException;
import aero.loretta.client.exception.LorettaClientException;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.*;
import java.util.regex.Pattern;

class LorettaApiImpl implements LorettaApi {
    private static final Logger log = LoggerFactory.getLogger(LorettaApi.class);
    private static final Pattern RX_UUID = Pattern.compile(
            "^[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}$");
    private final DateTimeFormatter STD_GUFI_FORMATTER = (new DateTimeFormatterBuilder())
            .parseCaseInsensitive()
            .appendValue(ChronoField.YEAR, 4)
            .appendValue(ChronoField.MONTH_OF_YEAR, 2)
            .appendValue(ChronoField.DAY_OF_MONTH, 2)
            .appendLiteral("T")
            .appendValue(ChronoField.HOUR_OF_DAY, 2)
            .appendValue(ChronoField.MINUTE_OF_HOUR, 2)
            .appendValue(ChronoField.SECOND_OF_MINUTE, 2)
            .toFormatter();

    private final String baseUrl;
    private final String operator;
    private final String operatorGufiPrefix;
    private final String authorization;
    private final ObjectMapper objectMapper;

    LorettaApiImpl(String baseUrl, String operator, String username, String password) {
        this.baseUrl = URI.create(baseUrl)
                .normalize()
                .toString()
                .replaceAll("/*$", "");

        this.operator = operator.toUpperCase();
        this.operatorGufiPrefix = operator.toUpperCase();

        String authorizationPlain = "%s:%s".formatted(username, password);
        this.authorization = Base64.getEncoder().encodeToString(authorizationPlain.getBytes(StandardCharsets.UTF_8));

        this.objectMapper = new ObjectMapper();
        this.objectMapper.findAndRegisterModules();
        this.objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        this.objectMapper.disable(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS);
    }

    @Override
    public UploadResponse uploadFlightPlan(String flightPlan, String gufi, List<String> employeeIds) throws LorettaClientException {
        Objects.requireNonNull(flightPlan, "Flightplan is required");
        if (Objects.nonNull(gufi)) {
            validateGufi(gufi);
        }

        Metadata metadata = Metadata.parse(flightPlan);
        String GUFI = Optional.ofNullable(gufi)
                .orElseGet(() -> getOrGenerateGufi(metadata));

        FlightPlanXmlRequest request = new FlightPlanXmlRequest()
                .setFlightPlanXml(flightPlan)
                .setMetadata(new FlightMetadata()
                        .setGufi(GUFI)
                        .setEmployeeIds(employeeIds));

        String message = upload("flightplanxml", request);
        return new UploadResponseImpl(message, GUFI);
    }

    @Override
    public UploadResponse uploadEff(byte[] eff, String gufi, List<String> employeeIds) throws LorettaClientException {
        Objects.requireNonNull(eff, "eff is required");

        String xml = extractFlightPlanXml(eff);
        return uploadFlightPlan(xml, gufi, employeeIds);
    }

    private String getOrGenerateGufi(Metadata metadata) {
        return metadata.getGufi().orElseGet(() -> generateGufi(metadata));
    }

    private String generateGufi(Metadata metadata) {
        return "%s-%s-%s-%s-%s".formatted(
                operatorGufiPrefix,
                metadata.getStd().atZone(ZoneOffset.UTC).format(STD_GUFI_FORMATTER),
                metadata.getCallsign().toUpperCase(),
                metadata.getDeparture().toUpperCase(),
                metadata.getArrival().toUpperCase()
        );
    }

    private <T> String upload(String suffix, T request) {
        String bodyPayload;
        try {
            bodyPayload = objectMapper.writeValueAsString(request);
        } catch (JsonProcessingException ex) {
            throw new LorettaClientException("Unable to serialize request", ex);
        }

        URI uri = URI.create("%s/%s/%s".formatted(baseUrl, operator, suffix));

        log.debug("Sending request: URI={}, Body={}", uri, bodyPayload);

        HttpRequest httpRequest = HttpRequest.newBuilder(uri)
                .header("Content-Type", "application/json")
                .header("Authorization", "Basic " + authorization)
                .timeout(Duration.ofSeconds(60))
                .POST(HttpRequest.BodyPublishers.ofString(bodyPayload))
                .build();

        HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(30))
                .followRedirects(HttpClient.Redirect.NEVER)
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        HttpResponse<String> response;
        try {
            response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            log.debug("Request failed", e);
            throw new LorettaClientException("Unable to send request", e);
        }

        if (response.statusCode() == 200) {
            try {
                ApiSuccess success = objectMapper.readValue(response.body(), ApiSuccess.class);
                log.debug("Successful response received: {}", success.getMessage());
                return success.getMessage();
            } catch (IOException ex) {
                log.debug("Successful response received, but response couldn't be read", ex);
                throw new LorettaClientException("Request was successful but couldn't read response", ex);
            }
        } else if (response.statusCode() == 401) {
            log.debug("Unauthorized response received");
            throw new AuthenticationFailedException("Provided credential are invalid");
        } else if (response.statusCode() == 404) {
            log.warn("Request to {} failed (not found)", uri);
            throw new LorettaClientException("API endpoint %s does not exist (not found)".formatted(uri));
        } else {
            try {
                ApiError error = objectMapper.readValue(response.body(), ApiError.class);
                log.debug("Error response received: Code={}, Message={}", response.statusCode(), error.getMessage());
                if (Objects.isNull(error.getMessage()) && Objects.isNull(error.getCorrelationId())) {
                    throw new LorettaClientException("Request failed. Response code: " + response.statusCode());
                }
                throw new ApiErrorException(error.getMessage(), error.getCorrelationId());
            } catch (IOException ex) {
                log.debug("Error response received: Code={}", response.statusCode(), ex);
                throw new LorettaClientException("Request failed. Response code: " + response.statusCode(), ex);
            }
        }
    }

    private void validateGufi(String gufi) {
        if (!(gufiIsUuid(gufi) || gufiIsPrefixed(gufi))) {
            throw new InvalidGufiException("Gufi '%s' is neither UUID nor prefixed with operator".formatted(gufi));
        }
    }

    private boolean gufiIsUuid(String gufi) {
        return RX_UUID.matcher(gufi.toLowerCase()).matches();
    }

    private boolean gufiIsPrefixed(String gufi) {
        return gufi.substring(0, operatorGufiPrefix.length()).equalsIgnoreCase(operatorGufiPrefix);
    }

    private static String extractFlightPlanXml(byte[] eff) {
        try {
            Map<String, byte[]> effFiles = IOUtils.readZipArchive(eff);
            byte[] innerArchive = effFiles.values()
                    .stream()
                    .filter(IOUtils::isZipArchive)
                    .findFirst()
                    .orElseThrow(() -> new LorettaClientException("Unable to find flightplan in eff"));
            effFiles = IOUtils.readZipArchive(innerArchive);

            for(var p : effFiles.entrySet()) {
                var filename = p.getKey();
                var content = p.getValue();

                String xml;
                try {
                    xml = new String(content);
                    if (xml.contains("<FlightPlan ")) {
                        log.debug("{} has flightplan marker", filename);
                        try {
                            Metadata.parse(xml);
                            log.debug("{} has been parsed successfully", filename);
                            return xml;
                        } catch (Throwable ex) {
                            log.warn("{} could not be parsed", filename, ex);
                        }
                    } else {
                        log.debug("{} has no flightplan marker", filename);
                    }
                } catch (Throwable ignored) {
                    log.debug("{} is binary file", filename);
                }
            }

            throw new LorettaClientException("Unable to find flightplan in eff");
        } catch (IOException ex) {
            throw new LorettaClientException("Unable to read eff", ex);
        }
    }

    private static class UploadResponseImpl implements UploadResponse {
        private final String message;
        private final String gufi;

        private UploadResponseImpl(String message, String gufi) {
            this.message = message;
            this.gufi = gufi;
        }

        @Override
        public String getMessage() {
            return message;
        }

        @Override
        public String getGufi() {
            return gufi;
        }
    }
}
