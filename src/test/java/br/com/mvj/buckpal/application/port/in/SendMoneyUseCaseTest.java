package br.com.mvj.buckpal.application.port.in;

import br.com.mvj.buckpal.domain.Account;
import br.com.mvj.buckpal.domain.Money;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolationException;
import java.security.InvalidParameterException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class SendMoneyUseCaseTest {

    @Test
    void shouldCreateNewSendMoneyCommand() {
        var sendMoneyCommand = new SendMoneyUseCase.SendMoneyCommand(
            new Account.AccountId(1L),
            new Account.AccountId(2L),
            Money.of(10L));

        assertThat(sendMoneyCommand).isNotNull();
        assertThat(sendMoneyCommand.getSourceAccountId()).isEqualTo(new Account.AccountId(1L));
        assertThat(sendMoneyCommand.getTargetAccountId()).isEqualTo(new Account.AccountId(2L));
        assertThat(sendMoneyCommand.getMoney()).isEqualTo(Money.of(10L));
    }

    @Test
    void shouldLaunchException() {
        assertThatExceptionOfType(InvalidParameterException.class)
            .isThrownBy(() -> new SendMoneyUseCase.SendMoneyCommand(
                new Account.AccountId(1L),
                new Account.AccountId(2L),
                Money.of(0))
            ).withMessage("Invalid value");
    }

    @Test
    void shouldLaunchException2() {
        assertThatExceptionOfType(ConstraintViolationException.class)
            .isThrownBy(() -> new SendMoneyUseCase.SendMoneyCommand(
                null,
                new Account.AccountId(2L),
                Money.of(100L))
            ).withMessageStartingWith("sourceAccountId:");

        assertThatExceptionOfType(ConstraintViolationException.class)
            .isThrownBy(() -> new SendMoneyUseCase.SendMoneyCommand(
                new Account.AccountId(2L),
                null,
                Money.of(100L))
            ).withMessageStartingWith("targetAccountId:");
    }
}
