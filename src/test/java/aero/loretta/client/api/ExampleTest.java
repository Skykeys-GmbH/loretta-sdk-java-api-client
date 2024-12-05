package aero.loretta.client.api;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.UUID;

@EnabledIfEnvironmentVariable(named = "LORETTA_USERNAME", matches = ".+")
@EnabledIfEnvironmentVariable(named = "LORETTA_PASSWORD", matches = ".+")
public class ExampleTest {
    private static final String MY_OPERATOR = "FKYD";
    private static final Logger log = LoggerFactory.getLogger(ExampleTest.class);

    @Test
    void uploadFlightPlan() throws IOException {
        LorettaApi apiClient = LorettaApiFactory.newBuilder()
                .wellKnownEnv(LorettaApiFactory.Environment.TEST)
                .operator(MY_OPERATOR)
                .authentication(System.getenv("LORETTA_USERNAME"), System.getenv("LORETTA_PASSWORD"))
                .build();

        LorettaApi.UploadResponse response = apiClient.uploadFlightPlan(readXml());

        log.info("FlightPlan for gufi {} uploaded successfully. Response message from API: {}",
                 response.getGufi(), response.getMessage());
    }

    private String readXml() throws IOException {
        byte[] xmlBytes = this.getClass().getClassLoader()
                .getResourceAsStream("example.xml")
                .readAllBytes();
        return new String(xmlBytes);
    }
}
