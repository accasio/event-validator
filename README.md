# Event Validation Lambda (Apache Camel + AWS)

This project implements an **AWS Lambda function** that uses **Apache Camel** to validate incoming producer events
against predefined schemas before forwarding them to an event broker (e.g. AWS EventBridge).

---

## Features
- **Apache Camel** route for event validation (`direct:validateEvent`)
- **AWS Lambda** handler (`AWSLambdaHandler`) that triggers the Camel route
- **Schema validation** (JSON-based)
- **Logging via Camel `.log()`**
- **Local testing support** via `main()` and Maven

---

## Prerequisites
- Java 21+
- Maven 3.9+

---

## Running Locally

###  Direct Java execution
Run the Lambda handler locally using its built-in `main()` method:
```bash
mvn compile exec:java -Dexec.mainClass=org.postnl.validation.AWSLambdaHandler
```

## Example Input 
```json 
{
  "eventId": "123",
  "eventType": "org.postnl.parcel.created:v1",
  "payload": {
    "parcelId": "P12345",
    "status": "CREATED"
  }
}
```

You’ll see logs in the console, and generated files under:

- output/valid.json — for valid events
- output/invalid.json — for invalid ones


Run tests

```bash
mvn test
```