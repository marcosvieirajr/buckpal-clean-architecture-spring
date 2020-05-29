package br.com.mvj.buckpal.adapter.in.web;

import br.com.mvj.buckpal.application.port.in.SendMoneyUseCase;
import br.com.mvj.buckpal.application.port.in.SendMoneyUseCase.SendMoneyCommand;
import br.com.mvj.buckpal.domain.Account;
import br.com.mvj.buckpal.domain.Money;
import br.com.mvj.shared.stereotypes.WebAdapter;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;


@WebAdapter
@RestController
@RequestMapping("/v1/accounts")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SendMoneyController {

    private final SendMoneyUseCase sendMoneyUseCase;

    @Transactional
    @PostMapping("/{sourceAccountId}/send-money/{targetAccountId}")
    public ResponseEntity<?> sendMoney(
        @PathVariable Long sourceAccountId,
        @PathVariable Long targetAccountId,
        @RequestBody Long amount) {

        log.info("requesting transfer {} from account {} to account {}", amount, sourceAccountId, targetAccountId);

        var command = new SendMoneyCommand(
            new Account.AccountId(sourceAccountId),
            new Account.AccountId(targetAccountId),
            Money.of(amount));

        this.sendMoneyUseCase.execute(command);

        return ResponseEntity.ok().build();
    }

}
