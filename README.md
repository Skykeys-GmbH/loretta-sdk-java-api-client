# Loretta API client SDK
Sending your flight plans to Loretta has never been easier!

See also: [Maven Central: aero.loretta:sdk-api-client](https://central.sonatype.com/artifact/aero.loretta/sdk-api-client)

## Getting started
Have a look at `LorettaApi.java` and `ExampleTest.java`

## What is a GUFI?
A [**G**]lobally [**U**]nique [**F**]light [**I**]dentifier lets Loretta and other
receiving systems know which **physical flight** the sender refers to.

In other words, if two flight plans sent to Loretta have two different GUFIs,
they belong to two separate physical flights.

The GUFI can come from one of multiple sources:
- some modern flight planning solutions fill the `<FlightKeyIdentifier>` field
  in the XML flightplan. This is both the simplest, and most reliable situation.
  No need to worry about anything.
- some other flight planning vendors produce a GUFI for every flight in the
  form of a UUID (`xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx`), but don't put it into
  the XML. You are encouraged to use the UUID as the GUFI.
- yet other planning systems generate a numerical sequence number for every
  physical flight. You may use this numerical ID, prefixed with your operator's
  ICAO code.
- you can come up with various concatenations of callsign, flight number,
  origin, destination, DOO, STD,... again, prefixed with your operator's ICAO
  code, please
- finally, **this SDK** can auto-generate high-quality GUFIs from data

**In most situations, with this SDK, you will not have to worry about the GUFI,
'null' will 'just work'**

## Instantiate the Loretta client
```java
LorettaApi apiClient = LorettaApiFactory.newBuilder()
        // use a pre-defined API endpoint
        .wellKnownEnv(LorettaApiFactory.Environment.TEST)
        // set your operator ICAO code
        .operator("icao_code")
        // set credentials
        .authentication("username", "password")
        .build();
```

## Send a flight plan - basic
```java
String arinc633FlightPlan = ...;
LorettaApi.UploadResponse response = apiClient.uploadFlightPlan(arinc633FlightPlan);
```

## Send a flight plan - with custom GUFI
```java
String arinc633FlightPlan = ...;
String gufi = ...;
LorettaApi.UploadResponse response = apiClient.uploadFlightPlan(arinc633FlightPlan, gufi);
```

## Send a flight plan - with assigned pilot IDs
```java
String arinc633FlightPlan = ...;
List<String> pilotIds = ...;
LorettaApi.UploadResponse response = apiClient.uploadFlightPlan(arinc633FlightPlan, pilotIds);
```

