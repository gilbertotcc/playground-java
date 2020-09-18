package com.github.gilbertotcc.playground.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JsonPatcherTest {

  private static final ObjectMapper mapper = new ObjectMapper();
  private static final JsonPatcher patcher = new JsonPatcher(mapper);
  private static final TestResource targetResource = new TestResource("A", "B");

  private static JsonNode jsonNodeFrom(String json) throws JsonProcessingException {
    return mapper.readTree(json);
  }

  @Test
  void deletePropertyShouldSuccess() throws JsonProcessingException {
    JsonNode patch = jsonNodeFrom("{ \"propertyA\": null }");

    TestResource result = patcher.patch(targetResource).with(patch);

    assertEquals(new TestResource(null, "B"), result);
  }

  @Test
  void updatePropertyShouldSuccess() throws JsonProcessingException {
    JsonNode patch = jsonNodeFrom("{ \"propertyA\": \"C\" }");

    TestResource result = patcher.patch(targetResource).with(patch);

    assertEquals(new TestResource("C", "B"), result);
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  private static class TestResource {
    String propertyA;
    String propertyB;
  }
}
