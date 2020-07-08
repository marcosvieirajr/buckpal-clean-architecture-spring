package br.com.mvj.buckpal.application.usecase;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import br.com.mvj.buckpal.application.port.in.SendMoneyUseCase;
import br.com.mvj.buckpal.application.port.in.SendMoneyUseCase.SendMoneyCommand;
import br.com.mvj.buckpal.application.port.out.AccountLockPort;
import br.com.mvj.buckpal.application.port.out.LoadAccountPort;
import br.com.mvj.buckpal.application.port.out.UpdateAccountStatePort;
import br.com.mvj.buckpal.application.usecase.exceptions.ThresholdExceededException;
import br.com.mvj.buckpal.domain.Account;
import br.com.mvj.buckpal.domain.Account.AccountId;
import br.com.mvj.buckpal.domain.Activity;
import br.com.mvj.buckpal.domain.ActivityWindow;
import br.com.mvj.buckpal.domain.Money;

public class SendMoneyUseCaseImplTest {

    final LoadAccountPort loadAccountPort = mock(LoadAccountPort.class);
    final AccountLockPort accountLockPort = mock(AccountLockPort.class);
    final UpdateAccountStatePort updateAccountStatePort = mock(UpdateAccountStatePort.class);

    final SendMoneyUseCase sendMoneyUseCase =
        new SendMoneyUseCaseImpl(loadAccountPort, accountLockPort, updateAccountStatePort);

    final SendMoneyCommand sendMoneyCommand = mock(SendMoneyCommand.class);

    @BeforeEach
    void setUp() {
    }

    @Test
    void whenThresholdExceeded_thenLaunchThresholdExceededException() {

        when(sendMoneyCommand.getMoney()).thenReturn(Money.of(1_000_001L));

        assertThatExceptionOfType(ThresholdExceededException.class)
            .isThrownBy(() -> sendMoneyUseCase.execute(sendMoneyCommand)
            ).withMessage("Maximum threshold for transferring money exceeded: tried to transfer 1000001 but threshold is 1000000!");
    }

    @Test
    void whenAccountDoesNotExist_thenLaunchIllegalArgumentException() {

        when(loadAccountPort.loadAccount(any(), any()))
            .thenReturn(Optional.empty());

        var sendMoneyCommand = new SendMoneyCommand(
            new AccountId(1L),
            new AccountId(2L),
            Money.of(1_000L));

        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> sendMoneyUseCase.execute(sendMoneyCommand))
            .withMessage("expected source account exists");
    }

    @Test
    void whenWithdrawalFails_thenOnlySourceAccountIsLockedAndReleased() {
        Account sourceAccount = givenSourceAccount();
        Account targetAccount = givenTargetAccount();

        givenWithdrawalWillFail(sourceAccount);

        SendMoneyCommand sendMoneyCommand = new SendMoneyCommand(
            sourceAccount.getId().get(),
            targetAccount.getId().get(),
            Money.of(100L)
        );

        boolean success = sendMoneyUseCase.execute(sendMoneyCommand);

        assertThat(success).isFalse();
        verify(accountLockPort, times(1)).lockAccount(sourceAccount.getId().get());
        verify(accountLockPort, times(1)).releaseAccount(sourceAccount.getId().get());
        verify(accountLockPort, never()).lockAccount(targetAccount.getId().get());
    }

    @Test
    void whenDepositFails_thenSourceAndTargetAccountAreLockedAndReleased() {
        Account sourceAccount = givenSourceAccount();
        Account targetAccount = givenTargetAccount();

        givenWithdrawalWillSuccess(sourceAccount);
        givenDepositWillFail(targetAccount);

        SendMoneyCommand sendMoneyCommand = new SendMoneyCommand(
            sourceAccount.getId().get(),
            targetAccount.getId().get(),
            Money.of(100L)
        );

        boolean success = sendMoneyUseCase.execute(sendMoneyCommand);

        assertThat(success).isFalse();
        InOrder inOrder = inOrder(accountLockPort);
        inOrder.verify(accountLockPort, times(1)).lockAccount(sourceAccount.getId().get());
        inOrder.verify(accountLockPort, times(1)).lockAccount(targetAccount.getId().get());
        inOrder.verify(accountLockPort, times(1)).releaseAccount(sourceAccount.getId().get());
        inOrder.verify(accountLockPort, times(1)).releaseAccount(targetAccount.getId().get());
    }

    @Test
    void shouldSendMoneySuccess() {
        Account sourceAccount = givenSourceAccount();
        Account targetAccount = givenTargetAccount();

        givenWithdrawalWillSuccess(sourceAccount);
        givenDepositWillSucceed(targetAccount);

        SendMoneyCommand sendMoneyCommand = new SendMoneyCommand(
            sourceAccount.getId().get(),
            targetAccount.getId().get(),
            Money.of(100L)
        );

        boolean success = sendMoneyUseCase.execute(sendMoneyCommand);

        assertThat(success).isTrue();
        verify(accountLockPort, times(1)).lockAccount(sendMoneyCommand.getSourceAccountId());
        verify(sourceAccount, times(1)).withdraw(eq(sendMoneyCommand.getMoney()), eq(sendMoneyCommand.getTargetAccountId()));
        verify(updateAccountStatePort, times(1)).updateActivities(sourceAccount);
        verify(accountLockPort, times(1)).releaseAccount(sendMoneyCommand.getSourceAccountId());

        verify(accountLockPort, times(1)).lockAccount(sendMoneyCommand.getTargetAccountId());
        verify(targetAccount, times(1)).deposit(eq(sendMoneyCommand.getMoney()), eq(sendMoneyCommand.getSourceAccountId()));
        verify(updateAccountStatePort, times(1)).updateActivities(targetAccount);
        verify(accountLockPort, times(1)).releaseAccount(sendMoneyCommand.getTargetAccountId());
    }

    private void givenWithdrawalWillFail(Account sourceAccount) {
        when(sourceAccount.withdraw(any(Money.class), any(AccountId.class)))
            .thenReturn(false);
    }

    private void givenWithdrawalWillSuccess(Account sourceAccount) {
        when(sourceAccount.withdraw(any(Money.class), any(AccountId.class)))
            .thenReturn(true);
    }

    private void givenDepositWillSucceed(Account sourceAccount) {
        when(sourceAccount.deposit(any(Money.class), any(AccountId.class)))
            .thenReturn(true);
    }

    private void givenDepositWillFail(Account sourceAccount) {
        when(sourceAccount.deposit(any(Money.class), any(AccountId.class)))
            .thenReturn(false);
    }

    @Test
    void shouldReturnTrue() {

        var ownerAccountId = new AccountId(1L);
        var activityWindow = new ActivityWindow(
            new Activity(
                ownerAccountId,
                new AccountId(2L),
                ownerAccountId,
                LocalDateTime.now(),
                Money.of(100L)));
        Optional<Account> account = Optional.of(Account.withId(ownerAccountId, Money.of(1L), activityWindow));

        when(loadAccountPort.loadAccount(any(), any()))
            .thenReturn(account);

        var sendMoneyCommand = new SendMoneyCommand(
            new AccountId(1L),
            new AccountId(2L),
            Money.of(10L));

        var success = sendMoneyUseCase.execute(sendMoneyCommand);

        assertThat(success).isTrue();
    }



    private Account givenSourceAccount() {
        return givenAccountWithId(new AccountId(1L));
    }

    private Account givenTargetAccount() {
        return givenAccountWithId(new AccountId(2L));
    }

    private Account givenAccountWithId(AccountId accountId) {
        Account account = mock(Account.class);
        when(account.getId()).thenReturn(Optional.of(accountId));

        when(loadAccountPort.loadAccount(eq(accountId), any(LocalDateTime.class)))
            .thenReturn(Optional.of(account));

        return account;
    }
}
