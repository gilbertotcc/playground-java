package com.github.gilbertotcc.playground.jackson;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import io.vavr.control.Try;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JsonPatcher {

  private final ObjectMapper mapper;

  /**
   * Create a target to which apply a JSON merge patch.
   *
   * @param target target object
   * @param <T>    type of the target object
   * @return the target to which apply the patch
   */
  public <T> Target<T> patch(T target) {
    return new Target<>(mapper, target);
  }

  @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
  public static final class Target<T> {

    private final ObjectMapper mapper;
    private final T resource;

    /**
     * Apply to the target resource the JSON merge patch document using the semantics defined on
     * <a href="https://tools.ietf.org/html/rfc7386">RFC 7386</a>.
     *
     * @param patch JSON merge patch document
     * @return the target resource after applying the patch
     */
    public T with(JsonNode patch) {
      JsonNode targetJson = mapper.convertValue(resource, JsonNode.class);
      return Try.of(() -> JsonMergePatch.fromJson(patch))
        .mapTry(jsonMergePatch -> jsonMergePatch.apply(targetJson))
        .mapTry(updatedTarget -> (T) mapper.convertValue(updatedTarget, resource.getClass()))
        .getOrElseThrow(error -> new RuntimeException("Cannot patch target", error));
    }
  }
}
