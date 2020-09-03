package com.github.gilbertotcc.playground.function;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConditionalFunctionTest {

  @Test
  void testFalseAndFalse() {
    final Tuple<Boolean, Boolean> input0 = new Tuple<>(Boolean.FALSE, Boolean.FALSE);
    final Tuple<Boolean, Boolean> input1 = new Tuple<>(Boolean.FALSE, Boolean.TRUE);
    final Tuple<Boolean, Boolean> input2 = new Tuple<>(Boolean.TRUE, Boolean.FALSE);
    final Tuple<Boolean, Boolean> input3 = new Tuple<>(Boolean.TRUE, Boolean.TRUE);

    ConditionalFunction<Tuple<Boolean, Boolean>, Boolean> conditionalFunction =
      new ConditionalFunction<Tuple<Boolean, Boolean>, Boolean>()
        .when(t -> !t.firstElement && !t.secondElement).thenReturn(false)
        .when(t -> !t.firstElement && t.secondElement).thenReturn(Boolean.FALSE)
        .when(t -> t.firstElement && !t.secondElement).thenReturn(Boolean.FALSE)
        .when(t -> t.firstElement && t.secondElement).thenReturn(Boolean.TRUE);

    assertFalse(conditionalFunction.apply(input0));
    assertFalse(conditionalFunction.apply(input1));
    assertFalse(conditionalFunction.apply(input2));
    assertTrue(conditionalFunction.apply(input3));
  }

  private static class Tuple<A, B> {
    A firstElement;
    B secondElement;

    public Tuple(A firstElement, B secondElement) {
      this.firstElement = firstElement;
      this.secondElement = secondElement;
    }
  }
}
