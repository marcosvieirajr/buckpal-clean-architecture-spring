package br.com.mvj.buckpal.adapter.persistence;

import br.com.mvj.buckpal.domain.Account;
import br.com.mvj.buckpal.domain.Account.AccountId;
import br.com.mvj.buckpal.domain.Activity;
import br.com.mvj.buckpal.domain.ActivityWindow;
import br.com.mvj.buckpal.domain.Money;

import java.util.ArrayList;
import java.util.List;

class AccountMapper {

    static Account mapToAccount(
        AccountJpaEntity account,
        List<ActivityJpaEntity> activities,
        Long withdrawalBalance,
        Long depositBalance) {

        Money baselineBalance = Money.subtract(
            Money.of(depositBalance),
            Money.of(withdrawalBalance));

        return Account.withId(
            new AccountId(account.getId()),
            baselineBalance,
            mapToActivityWindow(activities));

    }

    static ActivityWindow mapToActivityWindow(List<ActivityJpaEntity> activities) {
        List<Activity> mappedActivities = new ArrayList<>();

        for (ActivityJpaEntity activity : activities) {
            mappedActivities.add(new Activity(
                new Activity.ActivityId(activity.getId()),
                new Account.AccountId(activity.getOwnerAccountId()),
                new Account.AccountId(activity.getSourceAccountId()),
                new Account.AccountId(activity.getTargetAccountId()),
                activity.getTimestamp(),
                Money.of(activity.getAmount())));
        }

        return new ActivityWindow(mappedActivities);
    }

    static ActivityJpaEntity mapToJpaEntity(Activity activity) {
        return new ActivityJpaEntity(
            activity.getActivityId() == null ? null : activity.getActivityId().getValue(),
            activity.getTimestamp(),
            activity.getOwnerAccountId().getValue(),
            activity.getSourceAccountId().getValue(),
            activity.getTargetAccountId().getValue(),
            activity.getMoney().getAmount().longValue());
    }
}
