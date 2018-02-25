package com.github.gilbertotcc.playground.function;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ConditionalSupplierTest {

    @Test
    public void testFalseAndFalse() {
        final Boolean a = Boolean.FALSE;
        final Boolean b = Boolean.FALSE;

        ConditionalSupplier<Boolean> conditionalSupplier = new ConditionalSupplier<Boolean>()
                .when(() -> a == false && b == false).thenReturn(Boolean.FALSE)
                .when(() -> a == false && b == true).thenReturn(Boolean.FALSE)
                .when(() -> a == true && b == false).thenReturn(Boolean.FALSE)
                .when(() -> a == true && b == true).thenReturn(Boolean.TRUE);

        assertFalse(conditionalSupplier.get());
    }

    @Test
    public void testFalseAndTre() {
        final Boolean a = Boolean.FALSE;
        final Boolean b = Boolean.TRUE;

        ConditionalSupplier<Boolean> conditionalSupplier = new ConditionalSupplier<Boolean>()
                .when(() -> a == false && b == false).thenReturn(Boolean.FALSE)
                .when(() -> a == false && b == true).thenReturn(Boolean.FALSE)
                .when(() -> a == true && b == false).thenReturn(Boolean.FALSE)
                .when(() -> a == true && b == true).thenReturn(Boolean.TRUE);

        assertFalse(conditionalSupplier.get());
    }

    @Test
    public void testTrueAndFalse() {
        final Boolean a = Boolean.TRUE;
        final Boolean b = Boolean.FALSE;

        ConditionalSupplier<Boolean> conditionalSupplier = new ConditionalSupplier<Boolean>()
                .when(() -> a == false && b == false).thenReturn(Boolean.FALSE)
                .when(() -> a == false && b == true).thenReturn(Boolean.FALSE)
                .when(() -> a == true && b == false).thenReturn(Boolean.FALSE)
                .when(() -> a == true && b == true).thenReturn(Boolean.TRUE);

        assertFalse(conditionalSupplier.get());
    }

    @Test
    public void testTrueAndTrue() {
        final Boolean a = Boolean.TRUE;
        final Boolean b = Boolean.TRUE;

        ConditionalSupplier<Boolean> conditionalSupplier = new ConditionalSupplier<Boolean>()
                .when(() -> a == false && b == false).thenReturn(Boolean.FALSE)
                .when(() -> a == false && b == true).thenReturn(Boolean.FALSE)
                .when(() -> a == true && b == false).thenReturn(Boolean.FALSE)
                .when(() -> a == true && b == true).thenReturn(Boolean.TRUE);

        assertTrue(conditionalSupplier.get());
    }
}
