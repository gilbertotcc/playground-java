package com.github.gilbertotcc.playground.balance;

import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.With;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;

@Value(staticConstructor = "of")
@EqualsAndHashCode
public class Balance {

  LocalDate date;

  @With
  BigDecimal amount;

  public static Comparator<Balance> dateComparator() {
    return Comparator.comparing(Balance::getDate);
  }

  public Balance addTransaction(Transaction transaction) {
    if (transaction.getBookedDate().isEqual(date)) {
      return this.withAmount(this.getAmount().add(transaction.getAmount()));
    }
    throw new IllegalArgumentException("Transaction booked date incompatible with balance date");
  }

  public Balance subtractTransaction(Transaction transaction) {
    if (transaction.getBookedDate().isEqual(date)) {
      return this.withAmount(this.getAmount().subtract(transaction.getAmount()));
    }
    throw new IllegalArgumentException("Transaction booked date incompatible with balance date");
  }
}
