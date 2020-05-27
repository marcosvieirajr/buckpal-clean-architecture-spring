package br.com.mvj.buckpal.domain;

import lombok.NonNull;

import java.time.LocalDateTime;
import java.util.*;

public class ActivityWindow {

    private List<Activity> activities;

    public LocalDateTime getStartTimestamp(){
        return this.activities.stream()
            .min(Comparator.comparing(Activity::getTimestamp))
            .orElseThrow(IllegalStateException::new)
            .getTimestamp();
    }

    public LocalDateTime getEndTimestamp(){
        return this.activities.stream()
            .max(Comparator.comparing(Activity::getTimestamp))
            .orElseThrow(IllegalStateException::new)
            .getTimestamp();
    }

    public Money calculateBalance(Account.AccountId accountId){
        var depositBalance = this.activities.stream()
            .filter(activity -> activity.getTargetAccountId().equals(accountId))
            .map(Activity::getMoney)
            .reduce(Money.ZERO, Money::add);

        var withdrawBalance = this.activities.stream()
            .filter(activity -> activity.getSourceAccountId().equals(accountId))
            .map(Activity::getMoney)
            .reduce(Money.ZERO, Money::add);

        return Money.subtract(depositBalance, withdrawBalance);
    }

    public ActivityWindow (@NonNull List<Activity> activities) {
        this.activities = activities;
    }

    public ActivityWindow (@NonNull Activity... activities) {
        this.activities = new ArrayList<>(Arrays.asList(activities));
    }

    public List<Activity> getActivities() {
        return Collections.unmodifiableList(this.activities);
    }

    public void addActivity(Activity activity) {
        this.activities.add(activity);
    }
}
