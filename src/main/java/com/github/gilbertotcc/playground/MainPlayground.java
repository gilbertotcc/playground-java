package com.github.gilbertotcc.playground;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import com.sun.org.apache.xpath.internal.operations.String;

public class MainPlayground {

    public static void main(String[] args) {
        System.out.println(ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
    }

    private MainPlayground() {}
}
