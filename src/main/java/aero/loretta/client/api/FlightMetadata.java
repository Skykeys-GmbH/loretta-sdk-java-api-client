package aero.loretta.client.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;

class FlightMetadata {
    @JsonProperty("gufi")
    private String gufi;
    @JsonProperty("employee_ids")
    private List<String> employeeIds;

    public String getGufi() {
        return gufi;
    }

    public FlightMetadata setGufi(String gufi) {
        this.gufi = gufi;
        return this;
    }

    public List<String> getEmployeeIds() {
        return employeeIds;
    }

    public FlightMetadata setEmployeeIds(List<String> employeeIds) {
        this.employeeIds = employeeIds;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FlightMetadata that = (FlightMetadata) o;
        return Objects.equals(gufi, that.gufi) &&
                Objects.equals(employeeIds, that.employeeIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                gufi,
                employeeIds);
    }

    @Override
    public String toString() {
        return "FlightMetadata{" +
                "gufi=" + gufi +
                ", employeeIds=" + employeeIds +
                '}';
    }
}
