package br.com.mvj.buckpal.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class AccountTest {

    @Test
    void dopositSucceeds() {
        var accountId = new Account.AccountId(1L);
        var anotherAccount = new Account.AccountId(2L);

        var baselineBalance = Money.of(100L);

        var deposit = new Activity(accountId, anotherAccount, accountId, LocalDateTime.now(), Money.of(100L));
        var withdraw = new Activity(accountId, accountId, anotherAccount, LocalDateTime.now(), Money.of(50L));
        var activityWindow = new ActivityWindow(deposit, withdraw);

        Account account = Account.withId(accountId, baselineBalance, activityWindow);

        var success = account.deposit(Money.of(10L), anotherAccount);

        assertThat(success).isTrue();
        assertThat(account.getActivityWindow().getActivities()).hasSize(3);
        assertThat(account.calculateBalance()).isEqualTo(Money.of(160L));
    }

    @Test
    void withdrawSucceeds() {
        var accountId = new Account.AccountId(1L);
        var anotherAccount = new Account.AccountId(2L);

        var baselineBalance = Money.of(100L);

        var deposit = new Activity(accountId, anotherAccount, accountId, LocalDateTime.now(), Money.of(100L));
        var withdraw = new Activity(accountId, accountId, anotherAccount, LocalDateTime.now(), Money.of(50L));
        var activityWindow = new ActivityWindow(deposit, withdraw);

        Account account = Account.withId(accountId, baselineBalance, activityWindow);

        var success = account.withdraw(Money.of(10L), anotherAccount);

        assertThat(success).isTrue();
        assertThat(account.getActivityWindow().getActivities()).hasSize(3);
        assertThat(account.calculateBalance()).isEqualTo(Money.of(140L));
    }

    @Test
    void withdrawFail() {
        var accountId = new Account.AccountId(1L);
        var anotherAccount = new Account.AccountId(2L);

        var baselineBalance = Money.of(100L);

        var deposit = new Activity(accountId, anotherAccount, accountId, LocalDateTime.now(), Money.of(100L));
        var withdraw = new Activity(accountId, accountId, anotherAccount, LocalDateTime.now(), Money.of(50L));
        var activityWindow = new ActivityWindow(deposit, withdraw);

        Account account = Account.withId(accountId, baselineBalance, activityWindow);

        var success = account.withdraw(Money.of(200L), anotherAccount);

        assertThat(success).isFalse();
        assertThat(account.getActivityWindow().getActivities()).hasSize(2);
        assertThat(account.calculateBalance()).isEqualTo(Money.of(150L));
    }
}
