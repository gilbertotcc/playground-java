package com.github.gilbertotcc.playground.jackson;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.io.Serializable;
import java.util.stream.Stream;

@JsonInclude
public class Message<T extends Serializable> {

  @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXTERNAL_PROPERTY, property = "type")
  @JsonSubTypes({
      @JsonSubTypes.Type(value = String.class, name = "STRING"),
      @JsonSubTypes.Type(value = Long.class, name = "LONG")
  })
  private T payload;

  public T getPayload() {
    return payload;
  }

  public void setPayload(final T payload) {
    this.payload = payload;
  }

  public enum Type {
    STRING,
    LONG
  }

  public static class TypeDeserializer extends JsonDeserializer<Type> {

    @Override
    public Type deserialize(final JsonParser jsonParser,
                            final DeserializationContext deserializationContext) throws IOException {
      final String typeLabel = jsonParser.getValueAsString();
      return Stream.of(Type.values())
        .filter(typeEnum -> typeEnum.name().equals(typeLabel))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException(String.format("Illegal '%s' type", typeLabel)));
    }
  }
}
