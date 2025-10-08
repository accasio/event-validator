package org.postnl.validation;

import org.apache.camel.Exchange;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidationRouteTest extends CamelTestSupport {

    @Override
    protected ValidationRoute createRouteBuilder() {
        return new ValidationRoute();
    }

    @Test
    void validEventShouldPassValidation() throws Exception {
        String valid = "{\"eventId\":\"1\",\"eventType\":\"org.postnl.parcel.created:v1\",\"payload\":{\"parcelId\":\"P12345\",\"status\":\"CREATED\"}}";

        Exchange exchange = template.request("direct:validateEvent", e -> e.getIn().setBody(valid));
        assertEquals("Validation successful", exchange.getMessage().getBody(String.class));
    }

    @Test
    void invalidEventShouldFailValidation() throws Exception {
        String invalid = "{\"eventId\":\"2\",\"eventType\":\"org.postnl.parcel.created:v1\",\"payload\":{\"parcelId\":\"P99999\"}}";

        Exchange exchange = template.request("direct:validateEvent", e -> e.getIn().setBody(invalid));
        assertTrue(exchange.getException() != null || exchange.getMessage().getBody(String.class).contains("failed"));
    }
}
