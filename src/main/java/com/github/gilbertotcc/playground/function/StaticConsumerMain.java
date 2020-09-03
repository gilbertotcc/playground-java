package com.github.gilbertotcc.playground.function;

import java.util.function.Supplier;

public class StaticConsumerMain {

  public static final String CONSTANT_A = getConstantString();
  private static final Supplier<String> constantStringSupplier = () -> "constant_supplier";
  public static final String CONSTANT_B = constantStringSupplier.get();

  private StaticConsumerMain() {
  }

  private static String getConstantString() {
    return "constant_method";
  }

  /**
   * Main function.
   */
  public static void main(String[] args) {
    final String constantA = CONSTANT_A;
    final String constantB = CONSTANT_B;
    System.out.println(constantA);
    System.out.println(constantB);
  }
}
