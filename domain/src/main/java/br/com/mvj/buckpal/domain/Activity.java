package br.com.mvj.buckpal.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@RequiredArgsConstructor
public class Activity {

    private ActivityId activityId;

    @NonNull private Account.AccountId ownerAccountId;
    @NonNull private Account.AccountId sourceAccountId;
    @NonNull private Account.AccountId targetAccountId;
    @NonNull private LocalDateTime timestamp;
    @NonNull private Money money;

    public Activity(
                    @NonNull Account.AccountId ownerAccountId,
                    @NonNull Account.AccountId sourceAccountId,
                    @NonNull Account.AccountId targetAccountId,
                    @NonNull LocalDateTime timestamp,
                    @NonNull Money money) {
        this.activityId = null;
        this.ownerAccountId = ownerAccountId;
        this.sourceAccountId = sourceAccountId;
        this.targetAccountId = targetAccountId;
        this.timestamp = timestamp;
        this.money = money;
    }

    @Value
    public static class ActivityId {
        private final Long value;
    }
}
