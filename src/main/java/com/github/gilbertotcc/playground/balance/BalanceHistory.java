package com.github.gilbertotcc.playground.balance;

import io.vavr.collection.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BalanceHistory {

  final Balance initialBalance;
  final SortedSet<Balance> backwardBalances;
  final SortedSet<Balance> onwardBalances;

  public static BalanceHistory from(Balance initialBalance) {
    return new BalanceHistory(
      initialBalance,
      TreeSet.empty(Balance.dateComparator().reversed()),
      TreeSet.empty(Balance.dateComparator())
    );
  }

  public BalanceHistory addTransaction(Transaction transaction) {
    if (transaction.getBookedDate().isEqual(initialBalance.getDate())) {
      var newInitialBalance = Balance.of(
        transaction.getBookedDate(),
        initialBalance.getAmount().add(transaction.getAmount())
      );
      var newBackwardBalances = this.addAmountBackwards(transaction.getAmount(), backwardBalances);
      var newOnwardBalances = this.addAmountOnwards(transaction.getAmount(), onwardBalances);
      return new BalanceHistory(newInitialBalance, newBackwardBalances, newOnwardBalances);
    }
    if (transaction.getBookedDate().isBefore(initialBalance.getDate())) {
      var newBackwardBalances = this.addTransactionBackwards(transaction, backwardBalances, initialBalance);
      return new BalanceHistory(initialBalance, newBackwardBalances, onwardBalances);
    }
    if (transaction.getBookedDate().isAfter(initialBalance.getDate())) {
      var newOnwardBalances = this.addTransactionOnwards(transaction, onwardBalances, initialBalance);
      return new BalanceHistory(initialBalance, backwardBalances, newOnwardBalances);
    }
    throw new RuntimeException("Unexpected case");
  }

  private SortedSet<Balance> addTransactionOnwards(Transaction transaction,
                                                   SortedSet<Balance> balances,
                                                   Balance referenceBalance) {
    if (balances.isEmpty()) {
      return TreeSet.of(
        Balance.dateComparator(),
        Balance.of(transaction.getBookedDate(), referenceBalance.getAmount().add(transaction.getAmount()))
      );
    }

    var firstBalance = balances.head();

    if (transaction.getBookedDate().isEqual(firstBalance.getDate())) {
      var newBalance = Balance.of(firstBalance.getDate(), firstBalance.getAmount().add(transaction.getAmount()));
      var updatedBalances = addAmountOnwards(transaction.getAmount(), balances.tail());
      return TreeSet.of(Balance.dateComparator(), newBalance)
        .addAll(updatedBalances);
    }

    if (transaction.getBookedDate().isBefore(firstBalance.getDate())) {
      var newBalance = Balance.of(
        transaction.getBookedDate(),
        transaction.getAmount().add(referenceBalance.getAmount())
      );
      var updatedBalances = addAmountOnwards(transaction.getAmount(), balances);
      return TreeSet.of(Balance.dateComparator(), newBalance)
        .addAll(updatedBalances);
    }

    if (transaction.getBookedDate().isAfter(firstBalance.getDate())) {
      return TreeSet.of(Balance.dateComparator(), firstBalance)
        .addAll(addTransactionOnwards(transaction, balances.tail(), firstBalance));
    }

    throw new RuntimeException("Unexpected case");
  }

  private SortedSet<Balance> addAmountOnwards(BigDecimal amount, SortedSet<Balance> balances) {
    return balances.map(balance -> Balance.of(
      balance.getDate(),
      balance.getAmount().add(amount)
    ));
  }

  private SortedSet<Balance> addTransactionBackwards(Transaction transaction,
                                                     SortedSet<Balance> balances,
                                                     Balance referenceBalance) {
    if (balances.isEmpty()) {
      return TreeSet.of(
        Balance.dateComparator().reversed(),
        Balance.of(transaction.getBookedDate(), referenceBalance.getAmount().subtract(transaction.getAmount()))
      );
    }

    var firstBalance = balances.head();

    if (transaction.getBookedDate().isEqual(firstBalance.getDate())) {
      var newBalance = Balance.of(
        transaction.getBookedDate(),
        firstBalance.getAmount().subtract(transaction.getAmount())
      );
      var updatedBalances = addAmountBackwards(transaction.getAmount(), balances.tail());
      return TreeSet.of(Balance.dateComparator().reversed(), newBalance)
        .addAll(updatedBalances);
    }

    if (transaction.getBookedDate().isAfter(firstBalance.getDate())) {
      var newBalance = Balance.of(
        transaction.getBookedDate(),
        transaction.getAmount().subtract(referenceBalance.getAmount())
      );
      var updatedBalances = addAmountBackwards(transaction.getAmount(), balances);
      return TreeSet.of(Balance.dateComparator().reversed(), newBalance)
        .addAll(updatedBalances);
    }

    if (transaction.getBookedDate().isBefore(firstBalance.getDate())) {
      return TreeSet.of(Balance.dateComparator().reversed(), firstBalance)
        .addAll(addTransactionBackwards(transaction, balances.tail(), firstBalance));
    }

    throw new RuntimeException("Unexpected case");
  }

  private SortedSet<Balance> addAmountBackwards(BigDecimal amount, SortedSet<Balance> balances) {
    return balances.map(balance -> Balance.of(
      balance.getDate(),
      balance.getAmount().subtract(amount)
    ));
  }

  public List<Balance> getBalances() {
    return backwardBalances.toSortedSet(Balance.dateComparator())
      .add(initialBalance)
      .addAll(onwardBalances)
      .toList();
  }
}
