package aero.loretta.client.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

class FlightPlanXmlRequest {
    @JsonProperty("flight_plan_xml")
    private String flightPlanXml;

    @JsonProperty("metadata")
    private FlightMetadata metadata;

    public String getFlightPlanXml() {
        return flightPlanXml;
    }

    public FlightPlanXmlRequest setFlightPlanXml(String flightPlanXml) {
        this.flightPlanXml = flightPlanXml;
        return this;
    }

    public FlightMetadata getMetadata() {
        return metadata;
    }

    public FlightPlanXmlRequest setMetadata(FlightMetadata metadata) {
        this.metadata = metadata;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FlightPlanXmlRequest that = (FlightPlanXmlRequest) o;
        return Objects.equals(flightPlanXml, that.flightPlanXml) &&
                Objects.equals(metadata, that.metadata);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                flightPlanXml,
                metadata);
    }

    @Override
    public String toString() {
        return "FlightPlanXmlRequest{" +
                "flightPlanXml='" + flightPlanXml + '\'' +
                ", metadata=" + metadata +
                '}';
    }
}
