package br.com.mvj.buckpal.application.port.out;

import br.com.mvj.buckpal.domain.Account;

public interface UpdateAccountStatePort {

    void updateActivities(Account account);
}
