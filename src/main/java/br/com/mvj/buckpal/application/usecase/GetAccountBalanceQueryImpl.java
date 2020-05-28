package br.com.mvj.buckpal.application.usecase;

import br.com.mvj.buckpal.application.port.in.GetAccountBalanceQuery;
import br.com.mvj.buckpal.application.port.out.LoadAccountPort;
import br.com.mvj.buckpal.domain.Account;
import br.com.mvj.buckpal.domain.Money;
import lombok.RequiredArgsConstructor;

import javax.inject.Named;
import java.time.LocalDateTime;

@Named // TODO: create @UseCase annotation...
@RequiredArgsConstructor
public class GetAccountBalanceQueryImpl implements GetAccountBalanceQuery {

    private final LoadAccountPort loadAccount;

    @Override
    public Money execute(Account.AccountId accountId) {
        return loadAccount.loadAccount(accountId, LocalDateTime.now())
            .orElseThrow(() -> new RuntimeException("xxx")) // TODO: change exception
            .calculateBalance();
    }
}
