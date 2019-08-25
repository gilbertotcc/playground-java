package com.github.gilbertotcc.playground.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MessageTest {

  public static final String MESSAGE_JSON_STRING = "{" +
    "\"type\" : \"STRING\"," +
    "\"payload\" : \"ABC\"" +
    "}";

  public static final String MESSAGE_JSON_LONG = "{" +
    "\"type\" : \"LONG\"," +
    "\"payload\" : 1" +
    "}";

  @Test
  public void deserializeJson() throws IOException {
    Message message = new ObjectMapper().readValue(MESSAGE_JSON_STRING, Message.class);

    assertTrue(String.class.isInstance(message.getPayload()));
    String payload = String.class.cast(message.getPayload());
    assertEquals("ABC", payload);
  }

  @Test
  public void serializeJson() throws JsonProcessingException, JSONException {
    Long payload = Long.valueOf(1L);

    Message<Long> message = new Message<>();
    message.setPayload(payload);

    String messageJson = new ObjectMapper().writeValueAsString(message);
    System.err.println(messageJson);
    JSONAssert.assertEquals(MESSAGE_JSON_LONG, messageJson, JSONCompareMode.NON_EXTENSIBLE);

  }
}
