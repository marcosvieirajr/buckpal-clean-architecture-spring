package br.com.mvj.buckpal.adapter.out.persistence;

import br.com.mvj.buckpal.application.port.out.AccountLockPort;
import br.com.mvj.buckpal.application.port.out.LoadAccountPort;
import br.com.mvj.buckpal.application.port.out.UpdateAccountStatePort;
import br.com.mvj.buckpal.domain.Account;
import br.com.mvj.buckpal.domain.Account.AccountId;
import br.com.mvj.buckpal.domain.Activity;
import jdk.jshell.spi.ExecutionControl;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
class AccountPersistenceAdapter implements LoadAccountPort, UpdateAccountStatePort, AccountLockPort {

    private final AccountRepository accountRepository;
    private final ActivityRepository activityRepository;
//    private final

    @Override
    public Optional<Account> loadAccount(AccountId accountId, LocalDateTime baselineDate) {

        var account = accountRepository.findById(accountId.getValue())
            .orElseThrow(EntityNotFoundException::new);

        var activities = activityRepository.findByOwnerSince(accountId.getValue(), baselineDate);

        var withdrawalBalance = orZero(activityRepository.getWithdrawalBalanceUntil(accountId.getValue(), baselineDate));

        var depositBalance = orZero(activityRepository.getDepositBalanceUntil(accountId.getValue(), baselineDate));

        return Optional.of(AccountMapper.mapToAccount(account, activities, withdrawalBalance, depositBalance));
    }

    private Long orZero(Long value) {
        return value == null ? 0 : value;
    }

    @Override
    public void updateActivities(Account account) {

        account.getActivityWindow().getActivities().stream()
            .filter(activity -> activity.getActivityId() == null)
            .map(AccountMapper::mapToJpaEntity)
            .forEach(activityRepository::save);
    }

    @Override
    @SneakyThrows
    public void lockAccount(AccountId accountId) {
        // TODO: not implemented yet
    }

    @Override
    @SneakyThrows
    public void releaseAccount(AccountId accountId) {
        // TODO: not implemented yet
    }
}
