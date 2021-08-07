package com.github.gilbertotcc.playground.balance;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;

@Value(staticConstructor = "of")
@Getter
@EqualsAndHashCode
public class Balance {

  LocalDate date;
  BigDecimal amount;

  public static Comparator<Balance> dateComparator() {
    return Comparator.comparing(Balance::getDate);
  }
}
