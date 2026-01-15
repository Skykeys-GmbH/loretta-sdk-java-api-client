package aero.loretta.client.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

class LorettaFlight {
    @JsonProperty("uid")
    private String uid;

    @JsonProperty("callsign")
    private String callsign;

    @JsonProperty("departure")
    private String departure;

    @JsonProperty("destination")
    private String destination;

    @JsonProperty("std")
    private Instant std;

    @JsonProperty("sta")
    private Instant sta;

    @JsonProperty("aircraftRegistration")
    private String aircraftRegistration;

    @JsonProperty("commercialFlightNumber")
    private String commercialFlightNumber;

    @JsonProperty("aircraftIcaoType")
    private String aircraftIcaoType;

    @JsonProperty("gufi")
    private String gufi;

    @JsonProperty("release")
    private String release;

    public String getUid() {
        return uid;
    }

    public String getCallsign() {
        return callsign;
    }

    public String getDeparture() {
        return departure;
    }

    public String getDestination() {
        return destination;
    }

    public Instant getStd() {
        return std;
    }

    public Instant getSta() {
        return sta;
    }

    public String getAircraftRegistration() {
        return aircraftRegistration;
    }

    public String getCommercialFlightNumber() {
        return commercialFlightNumber;
    }

    public String getAircraftIcaoType() {
        return aircraftIcaoType;
    }

    public String getGufi() {
        return gufi;
    }

    public String getRelease() {
        return release;
    }
}
