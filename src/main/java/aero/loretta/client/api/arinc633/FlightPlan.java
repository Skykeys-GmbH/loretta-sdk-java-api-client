package aero.loretta.client.api.arinc633;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.time.OffsetDateTime;

@JacksonXmlRootElement(namespace = "http://aeec.aviation-ia.net/633")
class FlightPlan {
    @JsonProperty("M633Header")
    private M633Header m633Header;

    @JsonProperty("M633SupplementaryHeader")
    private M633SupplementaryHeader m633SupplementaryHeader;

    public M633Header getM633Header() {
        return m633Header;
    }

    public FlightPlan setM633Header(M633Header m633Header) {
        this.m633Header = m633Header;
        return this;
    }

    public M633SupplementaryHeader getM633SupplementaryHeader() {
        return m633SupplementaryHeader;
    }

    public FlightPlan setM633SupplementaryHeader(M633SupplementaryHeader m633SupplementaryHeader) {
        this.m633SupplementaryHeader = m633SupplementaryHeader;
        return this;
    }

    static class M633Header {
        private Integer versionNumber;

        public Integer getVersionNumber() {
            return versionNumber;
        }

        public M633Header setVersionNumber(Integer versionNumber) {
            this.versionNumber = versionNumber;
            return this;
        }
    }

    static class M633SupplementaryHeader {
        @JsonProperty("Flight")
        private M633SupplementaryHeader.Flight flight;

        @JsonProperty("FlightKeyIdentifier")
        private String flightKeyIdentifier;

        public M633SupplementaryHeader.Flight getFlight() {
            return flight;
        }

        public M633SupplementaryHeader setFlight(M633SupplementaryHeader.Flight flight) {
            this.flight = flight;
            return this;
        }

        public String getFlightKeyIdentifier() {
            return flightKeyIdentifier;
        }

        public M633SupplementaryHeader setFlightKeyIdentifier(String flightKeyIdentifier) {
            this.flightKeyIdentifier = flightKeyIdentifier;
            return this;
        }

        static class Flight {
            @JsonProperty("scheduledTimeOfDeparture")
            private OffsetDateTime scheduledTimeOfDeparture;
            @JsonProperty("FlightIdentification")
            private M633SupplementaryHeader.Flight.FlightIdentification flightIdentification;
            @JsonProperty("DepartureAirport")
            private M633SupplementaryHeader.Flight.Airport departureAirport;
            @JsonProperty("ArrivalAirport")
            private M633SupplementaryHeader.Flight.Airport arrivalAirport;

            public OffsetDateTime getScheduledTimeOfDeparture() {
                return scheduledTimeOfDeparture;
            }

            public Flight setScheduledTimeOfDeparture(OffsetDateTime scheduledTimeOfDeparture) {
                this.scheduledTimeOfDeparture = scheduledTimeOfDeparture;
                return this;
            }

            public M633SupplementaryHeader.Flight.FlightIdentification getFlightIdentification() {
                return flightIdentification;
            }

            public M633SupplementaryHeader.Flight setFlightIdentification(M633SupplementaryHeader.Flight.FlightIdentification flightIdentification) {
                this.flightIdentification = flightIdentification;
                return this;
            }

            public M633SupplementaryHeader.Flight.Airport getDepartureAirport() {
                return departureAirport;
            }

            public M633SupplementaryHeader.Flight setDepartureAirport(M633SupplementaryHeader.Flight.Airport departureAirport) {
                this.departureAirport = departureAirport;
                return this;
            }

            public M633SupplementaryHeader.Flight.Airport getArrivalAirport() {
                return arrivalAirport;
            }

            public M633SupplementaryHeader.Flight setArrivalAirport(M633SupplementaryHeader.Flight.Airport arrivalAirport) {
                this.arrivalAirport = arrivalAirport;
                return this;
            }

            static class FlightIdentification {
                @JsonProperty("FlightIdentifier")
                private String flightIdentifier;

                public String getFlightIdentifier() {
                    return flightIdentifier;
                }

                public M633SupplementaryHeader.Flight.FlightIdentification setFlightIdentifier(String flightIdentifier) {
                    this.flightIdentifier = flightIdentifier;
                    return this;
                }
            }

            static class Airport {
                @JsonProperty("AirportICAOCode")
                private String airportICAOCode;

                public String getAirportICAOCode() {
                    return airportICAOCode;
                }

                public M633SupplementaryHeader.Flight.Airport setAirportICAOCode(String airportICAOCode) {
                    this.airportICAOCode = airportICAOCode;
                    return this;
                }
            }
        }


    }
}
