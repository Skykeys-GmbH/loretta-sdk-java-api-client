package aero.loretta.client.api;

/**
 * Standard LorettaApi factory
 */
public class LorettaApiFactory {
    public enum Environment {
        TEST("https://test.loretta.aero/api/v1"),
        PROD("https://prod.loretta.aero/api/v1");

        private final String baseUri;

        Environment(String baseUri) {
            this.baseUri = baseUri;
        }

        public String getBaseUri() {
            return baseUri;
        }
    }

    /**
     * Endpoint configuration factory
     */
    public interface EndpointSettings {
        /**
         * Provide one of the well known environment
         * @param environment well known (test, prod) environment to be used
         * @return next factory class
         */
        OperatorSettings wellKnownEnv(Environment environment);

        /**
         * Provide custom environment
         * @param baseUri base URI of custom environment
         * @return next factory class
         */
        OperatorSettings customEnv(String baseUri);
    }

    /**
     * Operator configuration factory
     */
    public interface OperatorSettings {
        /**
         * Provide primary operator to be used with API
         * @param operator 3-letter operator code
         * @return next factory class
         */
        AuthenticationSettings operator(String operator);
    }



    /**
     * Authentication factory
     */
    public interface AuthenticationSettings {
        /**
         * Provide username and password for API authentication
         * @param username username
         * @param password password
         * @return next factory class
         */
        Builder authentication(String username, String password);
    }

    /**
     * Final builder factory
     */
    public interface Builder {
        /**
         * Build LorettaApi
         * @return instance of LorettaApi
         */
        LorettaApi build();
    }

    private static class Settings {
        private String baseUri;
        private String operator;
        private String username;
        private String password;
    }

    public static EndpointSettings newBuilder() {
        Settings settings = new Settings();

        Builder builder = () -> new LorettaApiImpl(settings.baseUri, settings.operator, settings.username, settings.password);

        AuthenticationSettings authenticationSettings = ((username, password) -> {
            settings.username = username;
            settings.password = password;
            return builder;
        });

        OperatorSettings operatorSettings = operator -> {
            settings.operator = operator;
            return authenticationSettings;
        };

        EndpointSettings endpointSettings = new EndpointSettings() {
            @Override
            public OperatorSettings wellKnownEnv(Environment environment) {
                settings.baseUri = environment.getBaseUri();
                return operatorSettings;
            }

            @Override
            public OperatorSettings customEnv(String baseUri) {
                settings.baseUri = baseUri;
                return operatorSettings;
            }
        };

        return endpointSettings;
    }
}
