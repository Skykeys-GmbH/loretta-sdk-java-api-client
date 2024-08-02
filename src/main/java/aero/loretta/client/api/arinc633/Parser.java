package aero.loretta.client.api.arinc633;

import aero.loretta.client.exception.XmlParsingException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

class Parser {
    private static final int MAX_ARINC633_VERSION = 5;
    private static final Logger log = LoggerFactory.getLogger(Parser.class);
    private static final AtomicBoolean versionWarned = new AtomicBoolean(false);
    private final XmlMapper objectMapper;

    public Parser() {
        this.objectMapper = new XmlMapper();
        this.objectMapper.findAndRegisterModules();
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    public Metadata parse(String xml) {
        try {
            FlightPlan flightPlan = objectMapper.readValue(xml, FlightPlan.class);

            int version = Optional.ofNullable(flightPlan)
                    .map(FlightPlan::getM633Header)
                    .map(FlightPlan.M633Header::getVersionNumber)
                    .orElseThrow(() -> new XmlParsingException("VersionNumber is missing"));

            if (version > MAX_ARINC633_VERSION) {
                if (versionWarned.compareAndSet(false, true)) {
                    log.warn("Unexpected ARINC 633 version number {}. Is you library outdated?", version);
                }
            }

            String gufi = Optional.ofNullable(flightPlan)
                    .map(FlightPlan::getM633SupplementaryHeader)
                    .map(FlightPlan.M633SupplementaryHeader::getFlightKeyIdentifier)
                    .orElse(null);

            Instant std = Optional.ofNullable(flightPlan)
                    .map(FlightPlan::getM633SupplementaryHeader)
                    .map(FlightPlan.M633SupplementaryHeader::getFlight)
                    .map(FlightPlan.M633SupplementaryHeader.Flight::getScheduledTimeOfDeparture)
                    .map(OffsetDateTime::toInstant)
                    .orElseThrow(() -> new IllegalArgumentException("ScheduledTimeOfDeparture not found"));

            String callsign = Optional.ofNullable(flightPlan)
                    .map(FlightPlan::getM633SupplementaryHeader)
                    .map(FlightPlan.M633SupplementaryHeader::getFlight)
                    .map(FlightPlan.M633SupplementaryHeader.Flight::getFlightIdentification)
                    .map(FlightPlan.M633SupplementaryHeader.Flight.FlightIdentification::getFlightIdentifier)
                    .orElseThrow(() -> new IllegalArgumentException("FlightIdentifier not found"));

            String departure = Optional.ofNullable(flightPlan)
                    .map(FlightPlan::getM633SupplementaryHeader)
                    .map(FlightPlan.M633SupplementaryHeader::getFlight)
                    .map(FlightPlan.M633SupplementaryHeader.Flight::getDepartureAirport)
                    .map(FlightPlan.M633SupplementaryHeader.Flight.Airport::getAirportICAOCode)
                    .orElseThrow(() -> new IllegalArgumentException("DepartureAirport not found"));

            String arrival = Optional.ofNullable(flightPlan)
                    .map(FlightPlan::getM633SupplementaryHeader)
                    .map(FlightPlan.M633SupplementaryHeader::getFlight)
                    .map(FlightPlan.M633SupplementaryHeader.Flight::getArrivalAirport)
                    .map(FlightPlan.M633SupplementaryHeader.Flight.Airport::getAirportICAOCode)
                    .orElseThrow(() -> new IllegalArgumentException("ArrivalAirport not found"));

            return new Metadata.MetadataImpl(gufi, std, callsign, departure, arrival);
        } catch (JsonProcessingException e) {
            throw new XmlParsingException("Unable to parse flightplan", e);
        }
    }
}
