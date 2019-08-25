package com.github.gilbertotcc.playground;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class DummySupplierTest {

  @Test
  public void testGetAsBooleanSuccess() {
    DummySupplier s = new DummySupplier();
    assertTrue(s.getAsBoolean());
  }
}
