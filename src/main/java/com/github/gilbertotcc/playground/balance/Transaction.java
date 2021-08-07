package com.github.gilbertotcc.playground.balance;

import lombok.Getter;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDate;

@Value(staticConstructor = "of")
@Getter
public class Transaction {
  LocalDate bookedDate;
  BigDecimal amount;
}
