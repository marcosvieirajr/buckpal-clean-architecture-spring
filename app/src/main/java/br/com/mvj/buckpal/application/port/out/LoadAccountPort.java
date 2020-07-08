package br.com.mvj.buckpal.application.port.out;

import br.com.mvj.buckpal.domain.Account;
import br.com.mvj.buckpal.domain.Account.AccountId;

import java.time.LocalDateTime;
import java.util.Optional;

public interface LoadAccountPort {

    Optional<Account> loadAccount(AccountId accountId, LocalDateTime baselineDate);
}
