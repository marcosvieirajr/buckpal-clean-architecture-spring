package br.com.mvj.buckpal.adapter.in.web;

import br.com.mvj.buckpal.application.port.in.SendMoneyUseCase;
import br.com.mvj.buckpal.domain.Account;
import br.com.mvj.buckpal.domain.Money;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static br.com.mvj.buckpal.adapter.in.web.SendMoneyController.*;
import static br.com.mvj.buckpal.application.port.in.SendMoneyUseCase.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = SendMoneyController.class)
class SendMoneyControllerTest extends BaseControllerTest {

    @MockBean SendMoneyUseCase sendMoneyUseCase;

    @Test
    void testSendMoney() throws Exception {

        var sourceAccountId = 1L;
        var targetAccountId = 2L;
        var amount = 500L;

        System.out.println("**************> " + toJson(amount));

        this.mockMvc.perform(

//            post("/v1/accounts/send-money/{sourceAccountId}/{targetAccountId}/{amount}", 1L, 2L, 500)
//                .header("Content-Type", "application/json"))
//            .andExpect(status().isOk());

            post("/v1/accounts/{sourceAccountId}/send-money/{targetAccountId}", sourceAccountId, targetAccountId)
                .content(toJson(amount))
                .header("Content-Type", "application/json"))
            .andExpect(status().isOk());

        var command = new SendMoneyCommand(
            new Account.AccountId(sourceAccountId),
            new Account.AccountId(targetAccountId),
            Money.of(amount));

        verify(sendMoneyUseCase, times(1)).execute(eq(command));
    }

}
