package aero.loretta.client.api;

import aero.loretta.client.exception.LorettaClientException;

import java.util.List;

/**
 * Loretta API client for accessing public Loretta API.
 * <p>
 * For creating instance of Loretta API client, please use {@link LorettaApiFactory}
 */
public interface LorettaApi {
    /**
     * Standard API response
     */
    interface UploadResponse {
        /**
         * Response message from API.
         * @return response message
         */
        String getMessage();

        /**
         * Gufi that was provided or generated in case where no gufi was provided
         * @return gufi
         */
        String getGufi();
    }

    /**
     * @see LorettaApi#uploadFlightPlan(String, String, List)
     */
    default UploadResponse uploadFlightPlan(String flightPlan) throws LorettaClientException {
        return uploadFlightPlan(flightPlan, null, null);
    }

    /**
     * @see LorettaApi#uploadFlightPlan(String, String, List)
     */
    default UploadResponse uploadFlightPlan(String flightPlan, String gufi) throws LorettaClientException {
        return uploadFlightPlan(flightPlan, gufi, null);
    }

    /**
     * @see LorettaApi#uploadFlightPlan(String, String, List)
     */
    default UploadResponse uploadFlightPlan(String flightPlan, List<String> employeeIds) throws LorettaClientException {
        return uploadFlightPlan(flightPlan, null, employeeIds);
    }

    /**
     * Upload ARINC633 Flightplan to Loretta system. This will create or update flightplan in Loretta system, whereby
     * for update GUFI needs to match the GUFI that was used to upload flightplan for first time.
     * <p>
     * If your flightplan does not contain GUFI (FlightKeyIdentifier), then you MAY provide it as argument. Provided
     * GUFI should be either UUID or it should be prefixed with your operator ICAO code. If you don't provide GUFI
     * and your flightplan does not contain it, then API client itself will generate GUFI out of flight data in form
     * of ${OPERATOR}-${STD}-${CALLSIGN}-${DEP}-${DST}.
     *
     * @param flightPlan ARINC633 Flightplan in XML format
     * @param gufi (Optional) The gufi of flightplan
     * @param employeeIds (Optional) The ids of employees
     * @return {@link LorettaApi.UploadResponse} in case of success
     * @throws LorettaClientException when API fails or provided arguments are invalid
     */
    UploadResponse uploadFlightPlan(String flightPlan, String gufi, List<String> employeeIds) throws LorettaClientException;

    /**
     * @see LorettaApi#uploadEff(byte[], String, List)
     */
    default UploadResponse uploadEff(byte[] eff) {
        return uploadEff(eff, null, null);
    }

    /**
     * @see LorettaApi#uploadEff(byte[], String, List)
     */
    default UploadResponse uploadEff(byte[] eff, String gufi) throws LorettaClientException {
        return uploadEff(eff, gufi, null);
    }

    /**
     * @see LorettaApi#uploadEff(byte[], String, List)
     */
    default UploadResponse uploadEff(byte[] eff, List<String> employeeIds) throws LorettaClientException {
        return uploadEff(eff, null, employeeIds);
    }

    /**
     * Extract ARINC633 flightplan from EFF package and calls {@link LorettaApi#uploadFlightPlan(String, String, List)}
     */
    UploadResponse uploadEff(byte[] eff, String gufi, List<String> employeeIds) throws LorettaClientException;
}
