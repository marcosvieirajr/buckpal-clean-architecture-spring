package br.com.mvj.buckpal.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.Optional;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Account {

    private AccountId id;
    private Money baselineBalance;
    private ActivityWindow activityWindow;

    public Money calculateBalance() {
        return Money.add(
            this.baselineBalance,
            this.activityWindow.calculateBalance(this.id));
    }

    public boolean deposit(Money money, AccountId souAccountId) {
        var deposit = new Activity(
            this.id, souAccountId, this.id,
            LocalDateTime.now(), money);
        this.activityWindow.addActivity(deposit);
        return true;
    }

    public boolean withdraw(Money money, AccountId targetAccountId) {
        if (!mayWithdraw(money)) return false;
        var withdraw = new Activity(
            this.id, this.id, targetAccountId,
            LocalDateTime.now(), money);
        this.activityWindow.addActivity(withdraw);
        return true;
    }

    private boolean mayWithdraw(Money money) {
        return Money.subtract(this.calculateBalance(), money)
            .isPositive();
    }

    public Optional<AccountId> getId() {
        return Optional.of(this.id);
    }

    public static Account withoutId(Money baselineBalance, ActivityWindow activityWindow) {
        return new Account(null, baselineBalance, activityWindow);
    }

    public static Account withId(AccountId accountId, Money baselineBalance, ActivityWindow activityWindow) {
        return new Account(accountId, baselineBalance, activityWindow);
    }

    @Value
    public static class AccountId {
        private Long value;
    }

}
