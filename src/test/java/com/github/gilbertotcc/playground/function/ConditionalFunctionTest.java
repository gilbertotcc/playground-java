package com.github.gilbertotcc.playground.function;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ConditionalFunctionTest {

    private static class Tuple<A, B> {
        A a;
        B b;

        public Tuple(A a, B b) {
            this.a = a;
            this.b = b;
        }

        public A getA() {
            return a;
        }

        public B getB() {
            return b;
        }
    }

    @Test
    public void testFalseAndFalse() {
        final Tuple<Boolean, Boolean> input0 = new Tuple<>(Boolean.FALSE, Boolean.FALSE);
        final Tuple<Boolean, Boolean> input1 = new Tuple<>(Boolean.FALSE, Boolean.TRUE);
        final Tuple<Boolean, Boolean> input2 = new Tuple<>(Boolean.TRUE, Boolean.FALSE);
        final Tuple<Boolean, Boolean> input3 = new Tuple<>(Boolean.TRUE, Boolean.TRUE);

        ConditionalFunction<Tuple<Boolean, Boolean>, Boolean> conditionalFunction = new ConditionalFunction<Tuple<Boolean, Boolean>, Boolean>()
                .when(t -> !t.a && !t.b).thenReturn(false)
                .when(t -> !t.a && t.b).thenReturn(Boolean.FALSE)
                .when(t -> t.a && !t.b).thenReturn(Boolean.FALSE)
                .when(t -> t.a && t.b).thenReturn(Boolean.TRUE);

        assertFalse(conditionalFunction.apply(input0));
        assertFalse(conditionalFunction.apply(input1));
        assertFalse(conditionalFunction.apply(input2));
        assertTrue(conditionalFunction.apply(input3));
    }
}
