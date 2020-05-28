package br.com.mvj.buckpal.application.usecase;

import br.com.mvj.buckpal.application.port.in.SendMoneyUseCase;
import br.com.mvj.buckpal.application.port.out.AccountLockPort;
import br.com.mvj.buckpal.application.port.out.LoadAccountPort;
import br.com.mvj.buckpal.application.port.out.UpdateAccountStatePort;
import lombok.RequiredArgsConstructor;

import javax.inject.Named;
import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Named // TODO: create @UseCase annotation...
@Transactional
@RequiredArgsConstructor
public class SendMoneyUserCaseImpl implements SendMoneyUseCase {

    private final LoadAccountPort loadAccount;
    private final AccountLockPort accountLock;
    private final UpdateAccountStatePort updateAccountState;
    private final MoneyTransferProperties moneyTransferProperties;

    @Override
    public boolean execute(SendMoneyCommand command) {

        checkThreshold(command);

        var baselineDate = LocalDateTime.now().minusDays(10);
        var sourceAccount = loadAccount.loadAccount(command.getSourceAccountId(), baselineDate)
            .orElseThrow(() -> new IllegalArgumentException("expected source account exists"));

        var targetAccount = loadAccount.loadAccount(command.getTargetAccountId(), baselineDate)
            .orElseThrow(() -> new IllegalArgumentException("expected source account exists"));

        var sourceAccountId = sourceAccount.getId().get();
        var targetAccountId = targetAccount.getId().get();

        accountLock.lockAccount(sourceAccountId);
        if(!sourceAccount.withdraw(command.getMoney(), targetAccountId)){
            accountLock.releaseAccount(sourceAccountId);
            return false;
        }

        accountLock.lockAccount(targetAccountId);
        if(!targetAccount.deposit(command.getMoney(), sourceAccountId)) {
            accountLock.releaseAccount(sourceAccountId);
            accountLock.releaseAccount(targetAccountId);
            return false;
        }

        updateAccountState.updateActivities(sourceAccount);
        updateAccountState.updateActivities(targetAccount);

        accountLock.releaseAccount(sourceAccountId);
        accountLock.releaseAccount(targetAccountId);
        return true;
    }

    private void checkThreshold(SendMoneyCommand command) {
        var threshold = moneyTransferProperties.getMaximumTransferThreshol();
        if(command.getMoney().isGreaterThan(threshold))
            throw new ThresholdExceededException(threshold, command.getMoney());
    }
}
