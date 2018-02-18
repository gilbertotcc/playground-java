package com.github.gilbertotcc.playground;

import java.util.function.BooleanSupplier;

public class DummySupplier implements BooleanSupplier {

    @Override
    public boolean getAsBoolean() {
        return true;
    }
}
