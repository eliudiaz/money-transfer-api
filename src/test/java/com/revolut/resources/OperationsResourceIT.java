package com.revolut.resources;

import com.revolut.api.resources.dto.requests.CreateAccountRequestDto;
import com.revolut.api.resources.dto.responses.AccountResponseDto;
import com.revolut.api.resources.dto.responses.SearchResultDto;
import com.revolut.api.resources.dto.requests.WireTransferenceRequestDto;
import com.revolut.api.resources.dto.responses.WireTransferenceResultDto;
import com.revolut.exception.ErrorMessage;
import com.revolut.model.Account;
import com.revolut.model.WireTransference;
import com.revolut.platform.ObjectMapperContextResolver;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.glassfish.jersey.client.ClientConfig;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.util.List;

@Slf4j
public class OperationsResourceIT extends BaseIT {

    private Client client;
    private WebTarget target;

    @Override
    public void beforeTest() {
        final ClientConfig clientConfig = new ClientConfig();
        clientConfig.register(new ObjectMapperContextResolver());
        client = ClientBuilder.newClient(clientConfig);
        target = client.target(getApiContext());
    }

    private AccountResponseDto registerAccount(BigDecimal amount){
        String fName = "test", lName = "test";
        final CreateAccountRequestDto requestDto = buildCreateAccountRequest(fName,lName,amount);
        return target
                .path("/accounts")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(requestDto, MediaType.APPLICATION_JSON_TYPE))
                .readEntity(AccountResponseDto.class);
    }

    @Test
    public void successTransferMoneyTest() {
        final AccountResponseDto originAccount = registerAccount(BigDecimal.valueOf(1000L));
        final AccountResponseDto targetAccount = registerAccount(BigDecimal.valueOf(1000L));

        final WireTransferenceRequestDto requestDto = WireTransferenceRequestDto.builder()
                .amount(BigDecimal.valueOf(500))
                .originAccountId(originAccount.getId())
                .targetAccountId(targetAccount.getId())
                .build();

        final WireTransferenceResultDto result = target
                .path("/operations/transfer")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(requestDto, MediaType.APPLICATION_JSON_TYPE))
                .readEntity(WireTransferenceResultDto.class);


        Assertions.assertThat(result.isSuccess()).isTrue();
        Assertions.assertThat(result.getWireTransferenceId()).isNotNull();
    }

    @Test
    public void sameAccountTransferMoneyTest() {
        final SearchResultDto<Account> accounts = target
                .path("/accounts")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get(new GenericType<SearchResultDto<Account>>() {
                });
        final WireTransferenceRequestDto requestDto = WireTransferenceRequestDto.builder()
                .amount(BigDecimal.valueOf(500))
                .originAccountId(accounts.getResult().get(1).getId())
                .targetAccountId(accounts.getResult().get(1).getId())
                .build();

        final ErrorMessage result = target
                .path("/operations/transfer")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(requestDto, MediaType.APPLICATION_JSON_TYPE))
                .readEntity(ErrorMessage.class);


        Assertions.assertThat(result.getMessage())
                .isNotNull()
                .isNotBlank();
        log.info("Transaction failed as expected: {}",result.getMessage());
    }

    @Test
    public void findAllTest() {
        successTransferMoneyTest();
        final List<WireTransference> tranferences = target
                .path("/operations/all")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get(new GenericType<List<WireTransference>>() {
                });


        Assertions.assertThat(tranferences.isEmpty())
                .isFalse();
    }



}
