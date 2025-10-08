# Event Validation Lambda (Apache Camel + AWS)

This project implements an **AWS Lambda function** that uses **Apache Camel** to validate incoming producer events
against predefined schemas before forwarding them to an event broker (e.g. AWS EventBridge).

It is part of the broader **PostNL-style event broker platform**, where:
- Producers register events and schemas via a self-service portal.
- Events are validated before entering the EventBridge.
- Validated events are published to the EventBridge.

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

