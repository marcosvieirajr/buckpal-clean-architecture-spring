package br.com.mvj.buckpal.application.port.in;

import br.com.mvj.buckpal.domain.Account;
import br.com.mvj.buckpal.domain.Money;

public interface GetAccountBalanceQuery {

    Money execute(Account.AccountId accountId);
}
