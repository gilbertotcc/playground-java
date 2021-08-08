package com.github.gilbertotcc.playground.balance;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import lombok.With;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;

@Value(staticConstructor = "of")
@EqualsAndHashCode
@ToString
public class Balance {

  LocalDate date;
  @With
  BigDecimal amount;

  /**
   * Return a comparator that compares balances ordering them by dates.
   *
   * @return Instance of the comparator.
   */
  public static Comparator<Balance> dateComparator() {
    return Comparator.comparing(Balance::getDate);
  }
}
