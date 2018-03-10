package com.github.gilbertotcc.playground.jackson;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.Serializable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

public class MessageTest {

    public static class Payload implements Serializable {

        private String content;

        public String getContent() {
            return content;
        }

        public void setContent(final String content) {
            this.content = content;
        }
    }

    public static final String MESSAGE_JSON = "{" +
            "\"type\" : \"com.github.gilbertotcc.playground.jackson.MessageTest$Payload\"," +
            "\"payload\" : { \"content\" : " + "\"ABC\"}" +
            "}";

    @Test
    public void deserializeJson() throws IOException {
        Message message = new ObjectMapper().readValue(MESSAGE_JSON, Message.class);

        assertTrue(Payload.class.isInstance(message.getPayload()));
        Payload payload = Payload.class.cast(message.getPayload());
        assertEquals("ABC", payload.getContent());
    }

    @Test
    public void serializeJson() throws JsonProcessingException, JSONException {
        Payload payload = new Payload();
        payload.setContent("ABC");

        Message<Payload> message = new Message<>();
        message.setPayload(payload);

        String messageJson = new ObjectMapper().writeValueAsString(message);
        System.err.println(messageJson);
        JSONAssert.assertEquals(MESSAGE_JSON, messageJson, JSONCompareMode.NON_EXTENSIBLE);

    }
}
