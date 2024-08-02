package aero.loretta.client.api;

import aero.loretta.client.exception.AuthenticationFailedException;
import aero.loretta.client.exception.InvalidGufiException;
import aero.loretta.client.exception.LorettaClientException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@EnabledIfEnvironmentVariable(named = "LORETTA_TEST_API", matches = "https://.*")
@EnabledIfEnvironmentVariable(named = "LORETTA_TEST_USERNAME", matches = ".+")
@EnabledIfEnvironmentVariable(named = "LORETTA_TEST_PASSWORD", matches = ".+")
class LorettaApiTest {
    final String gufi = "95de28de-3d58-4e46-aa3b-3b785a4116da";

    @Test
    void uploadXmlFlightPlan() throws IOException {
        LorettaApi api = buildStandardClient();

        var response = Assertions.assertDoesNotThrow(() -> api.uploadFlightPlan(readXml(), getGufi(), getEmployees()));

        assertEquals("ok", response.getMessage());
        assertEquals("95de28de-3d58-4e46-aa3b-3b785a4116da", response.getGufi());
    }


    @Test
    void uploadXmlFlightPlanWithoutGufi() throws IOException {
        LorettaApi api = buildStandardClient();

        var response = Assertions.assertDoesNotThrow(() -> api.uploadFlightPlan(readXml(), getEmployees()));

        assertEquals("ok", response.getMessage());
        assertEquals("FKYD-20240724T140000-FKY42-LOWW-LFPG", response.getGufi());
    }

    @Test
    void failedGufi() throws IOException {
        LorettaApi api = buildStandardClient();

        Assertions.assertThrows(InvalidGufiException.class, () -> api.uploadFlightPlan(readXml(),
                "20240724T140000-FKY42-LOWW-LFPG", getEmployees()));
    }

    @Test
    void uploadEffFlightPlan() throws IOException {
        LorettaApi api = buildStandardClient();

        var response = Assertions.assertDoesNotThrow(() -> api.uploadEff(readEff(), getEmployees()));

        assertEquals("ok", response.getMessage());
        assertEquals("95de28de-3d58-4e46-aa3b-3b785a4116da", response.getGufi());
    }

    @Test
    void failedAuthentication() throws IOException {
        LorettaApi api = buildBadAuthClient();

        assertThrows(AuthenticationFailedException.class, () -> api.uploadFlightPlan(readXml(), getGufi(), getEmployees()));
    }

    @Test
    void badLocation() throws IOException {
        LorettaApi api = buildBadUrlClient();

        assertThrows(LorettaClientException.class, () -> api.uploadFlightPlan(readXml(), getGufi(), getEmployees()));
    }

    private List<String> getEmployees() {
        return List.of("employee1", "employee2");
    }

    private String readXml() throws IOException {
        byte[] xmlBytes = this.getClass().getClassLoader()
                .getResourceAsStream("example.xml")
                .readAllBytes();
        return new String(xmlBytes);
    }

    private String getGufi() {
        return gufi;
    }

    private byte[] readEff() throws IOException {
        byte[] effBytes = this.getClass().getClassLoader()
                .getResourceAsStream("example.eff")
                .readAllBytes();
        return effBytes;
    }

    private static LorettaApi buildStandardClient() {
        return LorettaApiFactory.newBuilder()
                .customEnv(System.getenv("LORETTA_TEST_API"))
                .operator("FKYD")
                .authentication(System.getenv("LORETTA_TEST_USERNAME"), System.getenv("LORETTA_TEST_PASSWORD"))
                .build();
    }

    private static LorettaApi buildBadUrlClient() {
        return LorettaApiFactory.newBuilder()
                .customEnv(System.getenv("LORETTA_TEST_API") + "/invalid")
                .operator("FKYD")
                .authentication(System.getenv("LORETTA_TEST_USERNAME"), System.getenv("LORETTA_TEST_PASSWORD"))
                .build();
    }

    private static LorettaApi buildBadAuthClient() {
        return LorettaApiFactory.newBuilder()
                .customEnv(System.getenv("LORETTA_TEST_API"))
                .operator("FKYD")
                .authentication("test", "invalid-on-purpose")
                .build();
    }
}
