package com.github.gilbertotcc.playground.function;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

public class ConditionalSupplier<T> implements Supplier<T> {

    private final Map<BooleanSupplier, T> conditionalSupplierValues = new LinkedHashMap<>();

    public ConditionalAcceptor<T> when(BooleanSupplier condition) {
        return new ConditionalAcceptor(this, condition);
    }

    public static class ConditionalAcceptor<T> {
        private final ConditionalSupplier<T> conditionalSupplier;
        private final BooleanSupplier condition;

        public ConditionalAcceptor(ConditionalSupplier<T> conditionalSupplier, BooleanSupplier condition) {
            this.conditionalSupplier = conditionalSupplier;
            this.condition = condition;
        }

        public ConditionalSupplier<T> thenReturn(T value) {
            this.conditionalSupplier.conditionalSupplierValues.put(condition, value);
            return this.conditionalSupplier;
        }
    }

    @Override
    public T get() {
        return conditionalSupplierValues.entrySet().stream()
                .filter(entry -> entry.getKey().getAsBoolean())
                .findFirst()
                .map(Map.Entry::getValue)
                .orElse(null);
    }
}
