package org.postnl.validation;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.impl.DefaultCamelContext;

public class AWSLambdaHandler implements RequestHandler<String, String> {

    private static final CamelContext context = new DefaultCamelContext();
    private static final ProducerTemplate producer;

    static {
        try {
            context.addRoutes(new ValidationRoute());
            context.start();
            producer = context.createProducerTemplate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String handleRequest(String input, Context awsContext) {
        return producer.requestBody("direct:validateEvent", input, String.class);
    }

    // execute local test
    public static void main(String[] args) {
        AWSLambdaHandler handler = new AWSLambdaHandler();

        // Valid JSON
        String validEvent = "{\"eventId\":\"123\",\"eventType\":\"org.postnl.parcel.created:v1\",\"payload\":{\"parcelId\":\"P12345\",\"status\":\"CREATED\"}}";

        // Invalid JSON (missing payload.status)
        String invalidEvent = "{\"eventId\":\"456\",\"eventType\":\"org.postnl.parcel.created:v1\",\"payload\":{\"parcelId\":\"P99999\"}}";

        System.out.println("\n=== Sending VALID Event ===");
        String result = handler.handleRequest(validEvent, null);
        producer.sendBody("direct:validateEvent", validEvent);
        System.out.println("valid result: " + result);

        System.out.println("\n=== Sending INVALID Event ===");
        result = handler.handleRequest(invalidEvent, null);
        System.out.println("invalid result: " + result);
    }
}
