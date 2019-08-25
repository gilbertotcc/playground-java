package com.github.gilbertotcc.playground;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class MainPlayground {

  private MainPlayground() {
  }

  public static void main(String[] args) {
    System.out.println(ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
  }
}
