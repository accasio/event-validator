package org.postnl.validation;

import jakarta.enterprise.context.ApplicationScoped;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

@ApplicationScoped
public class ValidationRoute extends RouteBuilder {

    @Override
    public void configure() {
        // Handle invalid events
        onException(Exception.class)
                .handled(true)
                .log(LoggingLevel.ERROR, "Event failed validation: ${body}")
                .log(LoggingLevel.ERROR, "Error: ${exception.message}")
                .to("direct:rejectEvent");

        from("direct:validateEvent")
                .routeId("simple-event-validation")
                .log(LoggingLevel.INFO, "Received event: ${body}")
                .log("Inside Camel Route Received Payload ==> ${body}")
                .to("json-validator:myschema.json")
                .log(LoggingLevel.INFO, "Event passed schema validation.")
                .to("direct:nextStep"); // Forward to next pipeline stage

        // If valid, forward (here we just log)
        from("direct:nextStep")
                .routeId("next-step-route")
                .log(LoggingLevel.INFO, "Producing VALID event to next step: ${body}")
                // Example output to disk would be to EventListener
                .to("file:output?fileName=valid.json");

        // If invalid, simulate DLQ or quarantine
        from("direct:rejectEvent")
                .routeId("reject-event-route")
                .log(LoggingLevel.WARN, "Sending INVALID event to reject endpoint: ${body}")
                // Example local dead-letter handling
                .to("file:output?fileName=invalid.json");
    }
}
