package br.com.mvj.buckpal.application.usecase;

import br.com.mvj.buckpal.domain.Money;
import lombok.Getter;

@Getter
public class MoneyTransferProperties {
    private Money maximumTransferThreshol = Money.of(1_000_000L);
}
