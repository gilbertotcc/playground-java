package com.github.gilbertotcc.playground.balance;

import io.vavr.collection.List;
import io.vavr.collection.SortedSet;
import io.vavr.collection.TreeSet;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.util.function.Predicate;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BalanceHistory {

  final Balance referenceBalance;
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
    return Match(transaction).of(
      Case($(dateIsEqual(referenceBalance)), this::updateReferenceBalance),
      Case($(dateIsBefore(referenceBalance)), this::addBackwardTransaction),
      Case($(dateIsAfter(referenceBalance)), this::addOnwardTransaction)
    );
  }

  public List<Balance> getBalances() {
    return backwardBalances
      .toSortedSet(Balance.dateComparator())
      .add(referenceBalance)
      .addAll(onwardBalances)
      .toList();
  }

  private Predicate<Transaction> dateIsEqual(Balance balance) {
    return transaction -> transaction.getBookedDate().isEqual(balance.getDate());
  }

  private Predicate<Transaction> dateIsBefore(Balance balance) {
    return transaction -> transaction.getBookedDate().isBefore(balance.getDate());
  }

  private Predicate<Transaction> dateIsAfter(Balance balance) {
    return transaction -> transaction.getBookedDate().isAfter(balance.getDate());
  }

  private BalanceHistory updateReferenceBalance(Transaction transaction) {
    var newReferenceBalance = Balance.of(
      referenceBalance.getDate(),
      referenceBalance.getAmount().add(transaction.getAmount())
    );
    var newBackwardBalances = addAmountBackwards(transaction.getAmount(), backwardBalances);
    var newOnwardBalances = addAmountOnwards(transaction.getAmount(), onwardBalances);
    return new BalanceHistory(newReferenceBalance, newBackwardBalances, newOnwardBalances);
  }

  private BalanceHistory addBackwardTransaction(Transaction transaction) {
    var newBackwardBalances = this.addTransactionBackwards(transaction, backwardBalances, referenceBalance);
    return new BalanceHistory(referenceBalance, newBackwardBalances, onwardBalances);
  }

  private BalanceHistory addOnwardTransaction(Transaction transaction) {
    var newOnwardBalances = this.addTransactionOnwards(transaction, onwardBalances, referenceBalance);
    return new BalanceHistory(referenceBalance, backwardBalances, newOnwardBalances);
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

    return Match(balances.head()).of(
      Case($(balance -> transaction.getBookedDate().isEqual(balance.getDate())), balance -> {
        var newBalance = Balance.of(balance.getDate(), balance.getAmount().add(transaction.getAmount()));
        var updatedBalances = addAmountOnwards(transaction.getAmount(), balances.tail());
        return TreeSet.of(Balance.dateComparator(), newBalance).addAll(updatedBalances);
      }),
      Case($(balance -> transaction.getBookedDate().isBefore(balance.getDate())), balance -> {
        var newBalance = Balance.of(
          transaction.getBookedDate(),
          transaction.getAmount().add(referenceBalance.getAmount())
        );
        var updatedBalances = addAmountOnwards(transaction.getAmount(), balances);
        return TreeSet.of(Balance.dateComparator(), newBalance).addAll(updatedBalances);
      }),
      Case($(balance -> transaction.getBookedDate().isAfter(balance.getDate())), balance -> {
        var newOnwardBalances = addTransactionOnwards(transaction, balances.tail(), balance);
        return TreeSet.of(Balance.dateComparator(), balance)
          .addAll(newOnwardBalances);
      })
    );
  }

  private SortedSet<Balance> addAmountOnwards(BigDecimal amount, SortedSet<Balance> balances) {
    return balances.map(balance -> Balance.of(balance.getDate(), balance.getAmount().add(amount)));
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

    return Match(balances.head()).of(
      Case($(balance -> transaction.getBookedDate().isEqual(balance.getDate())), balance -> {
        var newBalance = Balance.of(
          transaction.getBookedDate(),
          balance.getAmount().subtract(transaction.getAmount())
        );
        var updatedBalances = addAmountBackwards(transaction.getAmount(), balances.tail());
        return TreeSet.of(Balance.dateComparator().reversed(), newBalance)
          .addAll(updatedBalances);
      }),
      Case($(balance -> transaction.getBookedDate().isAfter(balance.getDate())), () -> {
        var newBalance = Balance.of(
          transaction.getBookedDate(),
          transaction.getAmount().subtract(referenceBalance.getAmount())
        );
        var updatedBalances = addAmountBackwards(transaction.getAmount(), balances);
        return TreeSet.of(Balance.dateComparator().reversed(), newBalance)
          .addAll(updatedBalances);
      }),
      Case($(balance -> transaction.getBookedDate().isBefore(balance.getDate())), balance ->
        TreeSet.of(Balance.dateComparator().reversed(), balance)
          .addAll(addTransactionBackwards(transaction, balances.tail(), balance)))
    );
  }

  private SortedSet<Balance> addAmountBackwards(BigDecimal amount, SortedSet<Balance> balances) {
    return balances.map(balance -> Balance.of(balance.getDate(), balance.getAmount().subtract(amount)));
  }
}
