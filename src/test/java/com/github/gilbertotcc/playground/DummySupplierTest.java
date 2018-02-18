package com.github.gilbertotcc.playground;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DummySupplierTest {

    @Test
    public void testGetAsBooleanSuccess() {
        DummySupplier s = new DummySupplier();
        assertTrue(s.getAsBoolean());
    }
}
