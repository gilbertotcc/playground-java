package com.github.gilbertotcc.playground.vavr;

import io.vavr.collection.List;
import io.vavr.control.Try;

public class ListPlayground {

  private static final List<Integer> DEFAULT_LIST = List.of(1, 2, 3, 4, 5);
  private static final List<Integer> ODD_NUMBERS_LIST = List.of(1, 3, 5);

  static List<Integer> listOfOddNumbers(List<Integer> list) {
    return list
      .map(number -> number % 2 == 1 ? Try.success(number) : Try.<Integer>failure(new IllegalArgumentException()))
      .foldLeft(
        Try.success(List.<Integer>empty()),
        (accumulator, number) -> accumulator.isFailure() || number.isFailure()
          ? Try.failure(new IllegalArgumentException())
          : Try.success(accumulator.get().append(number.get()))
        )
      .getOrElseThrow(error -> (IllegalArgumentException) error);
  }

  public static void main(String[] args) {
    try {
      System.out.println("Test with DEFAULT_LIST");
      listOfOddNumbers(DEFAULT_LIST);
      System.out.println("Success");
    } catch (Exception e) {
      System.out.println("Failed");
    }

    try {
      System.out.println("Test with ODD_NUMBERS_LIST");
      listOfOddNumbers(ODD_NUMBERS_LIST);
      System.out.println("Success");
    } catch (Exception e) {
      System.out.println("Failed");
    }
  }
}
