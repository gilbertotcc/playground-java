package com.github.gilbertotcc.playground.function;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class ConditionalFunction<I, O> implements Function<I, O> {

    private static class Entry<I, O> {
        private Predicate<I> predicate;
        private O returnValue;

        public Entry(Predicate<I> p, O v) {
            this.predicate = p;
            this.returnValue = v;
        }
    }

    private final List<Entry<I, O>> conditionalSupplierValues = new ArrayList<>();

    public ConditionalAcceptor<I, O> when(Predicate<I> condition) {
        return new ConditionalAcceptor(this, condition);
    }

    public static class ConditionalAcceptor<I, O> {
        private final ConditionalFunction<I, O> conditionalFunction;
        private final Predicate<I> condition;

        public ConditionalAcceptor(ConditionalFunction<I, O> conditionalFunction, Predicate<I> condition) {
            this.conditionalFunction = conditionalFunction;
            this.condition = condition;
        }

        public ConditionalFunction<I, O> thenReturn(O value) {
            this.conditionalFunction.conditionalSupplierValues.add(new Entry<>(condition, value));
            return this.conditionalFunction;
        }
    }

    @Override
    public O apply(I v) {
        return conditionalSupplierValues.stream()
                .filter(entry -> entry.predicate.test(v))
                .findFirst()
                .map(e -> e.returnValue)
                .orElse(null);
    }
}
