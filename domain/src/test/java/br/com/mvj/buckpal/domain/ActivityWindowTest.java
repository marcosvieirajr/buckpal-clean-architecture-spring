package br.com.mvj.buckpal.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class ActivityWindowTest {

    @Test
    void shouldReturnStartTimestamp(){

        LocalDateTime startDateTime = LocalDateTime.of(2020, 05, 01, 12, 00);
        LocalDateTime endDateTime = LocalDateTime.of(2020, 05, 01, 12, 30);

        var accountId = new Account.AccountId(1L);
        var anotherAccount = new Account.AccountId(2L);
        var activityWindow = new ActivityWindow(
            new Activity(accountId, anotherAccount, accountId, endDateTime, Money.of(100L)),
            new Activity(accountId, accountId, anotherAccount, startDateTime, Money.of(50L)),
            new Activity(accountId, accountId, anotherAccount, LocalDateTime.of(2020, 05, 01, 12, 25), Money.of(10L)));

        var startTimestamp = activityWindow.getStartTimestamp();

        assertThat(startTimestamp).isEqualTo(startDateTime);
    }

    @Test
    void shouldReturnEndTimestamp(){

        LocalDateTime startDateTime = LocalDateTime.of(2020, 05, 01, 12, 00);
        LocalDateTime endDateTime = LocalDateTime.of(2020, 05, 01, 12, 30);

        var accountId = new Account.AccountId(1L);
        var anotherAccount = new Account.AccountId(2L);
        var activityWindow = new ActivityWindow(
            new Activity(accountId, anotherAccount, accountId, endDateTime, Money.of(100L)),
            new Activity(accountId, accountId, anotherAccount, startDateTime, Money.of(50L)),
            new Activity(accountId, accountId, anotherAccount, LocalDateTime.of(2020, 05, 01, 12, 25), Money.of(10L)));

        var endTimestamp = activityWindow.getEndTimestamp();

        assertThat(endTimestamp).isEqualTo(endDateTime);
    }

}
