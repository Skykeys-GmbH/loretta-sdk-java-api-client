package aero.loretta.client.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

class ApiError {
    @JsonProperty("message")
    private String message;
    @JsonProperty("correlation_id")
    private String correlationId;

    public String getMessage() {
        return message;
    }

    public ApiError setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public ApiError setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApiError apiError = (ApiError) o;
        return Objects.equals(message, apiError.message) &&
                Objects.equals(correlationId, apiError.correlationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                message,
                correlationId);
    }

    @Override
    public String toString() {
        return "ApiError{" +
                "message='" + message + '\'' +
                ", correlationId='" + correlationId + '\'' +
                '}';
    }
}
