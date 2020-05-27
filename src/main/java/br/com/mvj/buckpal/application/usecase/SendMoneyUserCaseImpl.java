package br.com.mvj.buckpal.application.usecase;

import br.com.mvj.buckpal.application.port.in.SendMoneyUserCase;
import br.com.mvj.buckpal.application.port.out.AccountLockPort;
import br.com.mvj.buckpal.application.port.out.LoadAccountPort;
import br.com.mvj.buckpal.application.port.out.UpdateAccountStatePort;
import lombok.RequiredArgsConstructor;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

// TODO: create @UseCase annotation...
@Transactional
@RequiredArgsConstructor
public class SendMoneyUserCaseImpl implements SendMoneyUserCase {

    private final LoadAccountPort loadAccountPort;
    private final AccountLockPort accountLockPort;
    private final UpdateAccountStatePort updateAccountStatePort;
    private final MoneyTransferProperties moneyTransferProperties;

    @Override
    public boolean execute(SendMoneyCommand command) {

        checkThreshold(command);

        var baselineDate = LocalDateTime.now().minusDays(10);
        var sourceAccount = loadAccountPort.loadAccount(command.getSourceAccountId(), baselineDate)
            .orElseThrow(() -> new IllegalArgumentException("expected source account ID not to be empty"));

        var targetAccount = loadAccountPort.loadAccount(command.getTargetAccountId(), baselineDate)
            .orElseThrow(() -> new IllegalArgumentException("expected source account ID not to be empty"));

        var sourceAccountId = sourceAccount.getId().get();
        var targetAccountId = targetAccount.getId().get();

        accountLockPort.lockAccount(sourceAccountId);
        if(!sourceAccount.withdraw(command.getMoney(), targetAccountId)){
            accountLockPort.releaseAccount(sourceAccountId);
            return false;
        }

        accountLockPort.lockAccount(targetAccountId);
        if(!targetAccount.deposit(command.getMoney(), sourceAccountId)) {
            accountLockPort.releaseAccount(sourceAccountId);
            accountLockPort.releaseAccount(targetAccountId);
            return false;
        }

        updateAccountStatePort.updateActivities(sourceAccount);
        updateAccountStatePort.updateActivities(targetAccount);

        accountLockPort.releaseAccount(sourceAccountId);
        accountLockPort.releaseAccount(targetAccountId);
        return true;
    }

    private void checkThreshold(SendMoneyCommand command) {
        var threshold = moneyTransferProperties.getMaximumTransferThreshol();
        if(command.getMoney().isGreaterThan(threshold))
            throw new ThresholdExceededException(command.getMoney(), threshold);
    }
}
