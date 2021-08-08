package com.github.gilbertotcc.playground.balance;

import io.vavr.collection.List;
import io.vavr.collection.SortedSet;
import io.vavr.collection.TreeSet;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.With;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BalanceHistory {

  @With(AccessLevel.PRIVATE)
  final Balance referenceBalance;
  @With(AccessLevel.PRIVATE)
  final SortedSet<Balance> backwardBalances;
  @With(AccessLevel.PRIVATE)
  final SortedSet<Balance> onwardBalances;

  public static BalanceHistory from(Balance initialBalance) {
    return new BalanceHistory(initialBalance, SetBuilder.backwardEmptySet(), SetBuilder.onwardEmptySet());
  }

  public BalanceHistory addTransaction(Transaction transaction) {
    return Match(transaction).of(
      Case($(t -> t.getBookedDate().isEqual(referenceBalance.getDate())),
        t -> new BalanceHistory(
          referenceBalance.withAmount(referenceBalance.getAmount().add(t.getAmount())),
          addAmountBackwards(t.getAmount(), backwardBalances),
          addAmountOnwards(t.getAmount(), onwardBalances)
        )
      ),
      Case($(t -> t.getBookedDate().isBefore(referenceBalance.getDate())),
        t -> this.withBackwardBalances(addTransactionBackwards(t, backwardBalances, referenceBalance))
      ),
      Case($(t -> t.getBookedDate().isAfter(referenceBalance.getDate())),
        t -> this.withOnwardBalances(addTransactionOnwards(t, onwardBalances, referenceBalance))
      )
    );
  }

  public List<Balance> getBalances() {
    return SetBuilder.onwardEmptySet()
      .addAll(backwardBalances)
      .add(referenceBalance)
      .addAll(onwardBalances)
      .toList();
  }

  private SortedSet<Balance> addTransactionOnwards(Transaction transaction,
                                                   SortedSet<Balance> balances,
                                                   Balance referenceBalance) {
    if (balances.isEmpty()) {
      return SetBuilder
        .onwardSetOf(Balance.of(transaction.getBookedDate(), transaction.getAmount().add(referenceBalance.getAmount())));
    }

    return Match(balances.head()).of(
      Case($(balance -> transaction.getBookedDate().isEqual(balance.getDate())),
        balance -> SetBuilder
          .onwardSetOf(balance.withAmount(balance.getAmount().add(transaction.getAmount())))
          .addAll(addAmountOnwards(transaction.getAmount(), balances.tail()))
      ),
      Case($(balance -> transaction.getBookedDate().isAfter(balance.getDate())),
        balance -> SetBuilder
          .onwardSetOf(balance)
          .addAll(addTransactionOnwards(transaction, balances.tail(), balance))
      ),
      Case($(balance -> transaction.getBookedDate().isBefore(balance.getDate())),
        () -> SetBuilder
          .onwardSetOf(Balance.of(transaction.getBookedDate(), transaction.getAmount().add(referenceBalance.getAmount())))
          .addAll(addAmountOnwards(transaction.getAmount(), balances))
      )
    );
  }

  private SortedSet<Balance> addAmountOnwards(BigDecimal amount, SortedSet<Balance> balances) {
    return balances.map(balance -> balance.withAmount(balance.getAmount().add(amount)));
  }

  private SortedSet<Balance> addTransactionBackwards(Transaction transaction,
                                                     SortedSet<Balance> balances,
                                                     Balance referenceBalance) {
    if (balances.isEmpty()) {
      return SetBuilder
        .backwardSetOf(Balance.of(transaction.getBookedDate(), referenceBalance.getAmount().subtract(transaction.getAmount())));
    }

    return Match(balances.head()).of(
      Case($(balance -> transaction.getBookedDate().isEqual(balance.getDate())),
        balance -> SetBuilder
          .backwardSetOf(balance.withAmount(balance.getAmount().subtract(transaction.getAmount())))
          .addAll(addAmountBackwards(transaction.getAmount(), balances.tail()))
      ),
      Case($(balance -> transaction.getBookedDate().isBefore(balance.getDate())),
        balance -> SetBuilder
          .backwardSetOf(balance)
          .addAll(addTransactionBackwards(transaction, balances.tail(), balance))
      ),
      Case($(balance -> transaction.getBookedDate().isAfter(balance.getDate())),
        () -> SetBuilder
          .backwardSetOf(Balance.of(transaction.getBookedDate(), transaction.getAmount().subtract(referenceBalance.getAmount())))
          .addAll(addAmountBackwards(transaction.getAmount(), balances))
      )
    );
  }

  private SortedSet<Balance> addAmountBackwards(BigDecimal amount, SortedSet<Balance> balances) {
    return balances.map(balance -> balance.withAmount(balance.getAmount().subtract(amount)));
  }

  @UtilityClass
  private static class SetBuilder {

    public static SortedSet<Balance> onwardEmptySet() {
      return TreeSet.empty(Balance.dateComparator());
    }

    private static SortedSet<Balance> onwardSetOf(Balance balance) {
      return onwardEmptySet().add(balance);
    }

    public static SortedSet<Balance> backwardEmptySet() {
      return TreeSet.empty(Balance.dateComparator().reversed());
    }

    private static SortedSet<Balance> backwardSetOf(Balance balance) {
      return backwardEmptySet().add(balance);
    }
  }
}
