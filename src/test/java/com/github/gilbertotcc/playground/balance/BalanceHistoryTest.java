package com.github.gilbertotcc.playground.balance;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

public class BalanceHistoryTest {

  @Test
  void addTransactionShouldSucceed() {
    var startingBalance = Balance.of(LocalDate.of(2021, 7, 21), new BigDecimal("1.23"));
    var newTransaction1 = Transaction.of(LocalDate.of(2021, 7, 22), new BigDecimal(("1.00")));
    var newTransaction2 = Transaction.of(LocalDate.of(2021, 7, 22), new BigDecimal(("1.00")));
    var newTransaction3 = Transaction.of(LocalDate.of(2021, 7, 23), new BigDecimal(("1.01")));
    var newTransaction4 = Transaction.of(LocalDate.of(2021, 7, 20), new BigDecimal(("1.00")));
    var newTransaction5 = Transaction.of(LocalDate.of(2021, 7, 20), new BigDecimal(("1.00")));
    var newTransaction6 = Transaction.of(LocalDate.of(2021, 7, 19), new BigDecimal(("1.01")));

    var balanceHistory = BalanceHistory.from(startingBalance)
      .addTransaction(newTransaction1)
      .addTransaction(newTransaction2)
      .addTransaction(newTransaction3)
      .addTransaction(newTransaction4)
      .addTransaction(newTransaction5)
      .addTransaction(newTransaction6);

    var balances = balanceHistory.getBalances();

    assertThat(balances.size()).isEqualTo(5);
    assertThat(balances.get(0)).isEqualTo(Balance.of(LocalDate.of(2021, 7, 19), new BigDecimal("-1.78")));
    assertThat(balances.get(1)).isEqualTo(Balance.of(LocalDate.of(2021, 7, 20), new BigDecimal("-0.77")));
    assertThat(balances.get(2)).isEqualTo(Balance.of(LocalDate.of(2021, 7, 21), new BigDecimal("1.23")));
    assertThat(balances.get(3)).isEqualTo(Balance.of(LocalDate.of(2021, 7, 22), new BigDecimal("3.23")));
    assertThat(balances.get(4)).isEqualTo(Balance.of(LocalDate.of(2021, 7, 23), new BigDecimal("4.24")));
  }

  @Test
  void addTransactionInTheFutureShouldSucceed() {
    var startingBalance = Balance.of(LocalDate.of(2021, 7, 21), new BigDecimal("1.23"));
    var newTransaction1 = Transaction.of(LocalDate.of(2021, 7, 22), new BigDecimal(("1.00")));
    var newTransaction2 = Transaction.of(LocalDate.of(2021, 7, 22), new BigDecimal(("1.00")));
    var newTransaction3 = Transaction.of(LocalDate.of(2021, 7, 23), new BigDecimal(("1.01")));

    var balanceHistory = BalanceHistory.from(startingBalance)
      .addTransaction(newTransaction1)
      .addTransaction(newTransaction2)
      .addTransaction(newTransaction3);

    var balances = balanceHistory.getBalances();

    assertThat(balances.size()).isEqualTo(3);
    assertThat(balances.get(0)).isEqualTo(Balance.of(LocalDate.of(2021, 7, 21), new BigDecimal("1.23")));
    assertThat(balances.get(1)).isEqualTo(Balance.of(LocalDate.of(2021, 7, 22), new BigDecimal("3.23")));
    assertThat(balances.get(2)).isEqualTo(Balance.of(LocalDate.of(2021, 7, 23), new BigDecimal("4.24")));
  }

  @Test
  void addTransactionInThePastShouldSucceed() {
    var startingBalance = Balance.of(LocalDate.of(2021, 7, 21), new BigDecimal("1.23"));
    var newTransaction1 = Transaction.of(LocalDate.of(2021, 7, 20), new BigDecimal(("1.00")));
    var newTransaction2 = Transaction.of(LocalDate.of(2021, 7, 20), new BigDecimal(("1.00")));
    var newTransaction3 = Transaction.of(LocalDate.of(2021, 7, 19), new BigDecimal(("1.01")));

    var balanceHistory = BalanceHistory.from(startingBalance)
      .addTransaction(newTransaction1)
      .addTransaction(newTransaction2)
      .addTransaction(newTransaction3);

    var balances = balanceHistory.getBalances();

    assertThat(balances.size()).isEqualTo(3);
    assertThat(balances.get(2)).isEqualTo(Balance.of(LocalDate.of(2021, 7, 21), new BigDecimal("1.23")));
    assertThat(balances.get(1)).isEqualTo(Balance.of(LocalDate.of(2021, 7, 20), new BigDecimal("-0.77")));
    assertThat(balances.get(0)).isEqualTo(Balance.of(LocalDate.of(2021, 7, 19), new BigDecimal("-1.78")));
  }
}
