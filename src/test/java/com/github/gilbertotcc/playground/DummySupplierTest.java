package com.github.gilbertotcc.playground;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class DummySupplierTest {

  @Test
  void testGetAsBooleanSuccess() {
    DummySupplier s = new DummySupplier();
    assertTrue(s.getAsBoolean());
  }
}
