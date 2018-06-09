package com.github.gilbertotcc.playground.stream;

import java.util.Random;
import java.util.stream.Stream;

public class StreamPlayground {


    public static void main(String[] args) {
        final Random random = new Random();
        Stream.generate(() -> random.nextLong())
                .filter(number -> number >0 && number % 2 == 0)
                .forEach(System.err::println);
    }
}
