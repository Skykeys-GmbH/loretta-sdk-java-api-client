package aero.loretta.client.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

class ApiSuccess {
    @JsonProperty("message")
    private String message;

    public String getMessage() {
        return message;
    }

    public ApiSuccess setMessage(String message) {
        this.message = message;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApiSuccess that = (ApiSuccess) o;
        return Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                message);
    }

    @Override
    public String toString() {
        return "ApiSuccess{" +
                "message='" + message + '\'' +
                '}';
    }
}
