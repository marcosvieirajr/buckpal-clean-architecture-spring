package br.com.mvj.buckpal.application.port.in;

import br.com.mvj.buckpal.domain.Account.AccountId;
import br.com.mvj.buckpal.domain.Money;
import br.com.mvj.shared.SelfValidating;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.security.InvalidParameterException;

public interface SendMoneyUseCase {

    boolean execute(SendMoneyCommand command);

    @Getter
    @EqualsAndHashCode
    class SendMoneyCommand extends SelfValidating<SendMoneyCommand> {

        @NotNull private final AccountId sourceAccountId;
        @NotNull private final AccountId targetAccountId;
        @NotNull private final Money money;

        public SendMoneyCommand(AccountId sourceAccountId, AccountId targetAccountId, Money money) {
            this.sourceAccountId = sourceAccountId;
            this.targetAccountId = targetAccountId;
            this.money = money;
            this.requireGreaterThan(0, money);
            this.validateSelf();
        }

        private void requireGreaterThan(int value, Money money) {
            // TODO: messagem...
            if(!money.isGreaterThan(Money.of(value))) throw new InvalidParameterException("Invalid value");
        }
    }
}
