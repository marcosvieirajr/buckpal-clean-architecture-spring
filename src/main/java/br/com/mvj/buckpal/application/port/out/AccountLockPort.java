package br.com.mvj.buckpal.application.port.out;

import br.com.mvj.buckpal.domain.Account;

public interface AccountLockPort {

    void lockAccount(Account.AccountId accountId);

    void releaseAccount(Account.AccountId accountId);
}
