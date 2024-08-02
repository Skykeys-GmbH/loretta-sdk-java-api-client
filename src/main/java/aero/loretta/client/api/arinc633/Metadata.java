package aero.loretta.client.api.arinc633;

import aero.loretta.client.exception.XmlParsingException;

import java.time.Instant;
import java.util.Optional;

public interface Metadata {
    Optional<String> getGufi();

    Instant getStd();

    String getCallsign();

    String getDeparture();

    String getArrival();

    static Metadata parse(String xml) throws XmlParsingException {
        return new Parser().parse(xml);
    }

    class MetadataImpl implements Metadata {
        private final String gufi;
        private final Instant std;
        private final String callsign;
        private final String departure;
        private final String arrival;

        public MetadataImpl(String gufi, Instant std, String callsign, String departure, String arrival) {
            this.gufi = gufi;
            this.std = std;
            this.callsign = callsign;
            this.departure = departure;
            this.arrival = arrival;
        }

        @Override
        public Optional<String> getGufi() {
            return Optional.ofNullable(gufi);
        }

        @Override
        public Instant getStd() {
            return std;
        }

        @Override
        public String getCallsign() {
            return callsign;
        }

        @Override
        public String getDeparture() {
            return departure;
        }

        @Override
        public String getArrival() {
            return arrival;
        }
    }
}
