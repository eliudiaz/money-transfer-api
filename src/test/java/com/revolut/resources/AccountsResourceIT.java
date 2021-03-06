package com.revolut.resources;

import com.revolut.api.resources.dto.requests.CreateAccountRequestDto;
import com.revolut.api.resources.dto.responses.AccountResponseDto;
import com.revolut.api.resources.dto.responses.SearchResultDto;
import com.revolut.model.Account;
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
import javax.ws.rs.core.Response;
import java.math.BigDecimal;

@Slf4j
public class AccountsResourceIT extends BaseIT {

    private Client client;
    private WebTarget target;

    @Override
    public void beforeTest() {
        final ClientConfig clientConfig = new ClientConfig();
        clientConfig.register(new ObjectMapperContextResolver());
        client = ClientBuilder.newClient(clientConfig);
        target = client.target(getApiContext());
    }

    @Test
    public void findAllAccountsTest() {
        final SearchResultDto<Account> result = target
                .path("/accounts")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get(SearchResultDto.class);

        Assertions.assertThat(result.isSuccess()).isTrue();
        Assertions.assertThat(result.getResult().isEmpty()).isFalse();
    }

    @Test
    public void findOneAccountTest() {
        final SearchResultDto<Account> result = target
                .path("/accounts")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get(new GenericType<SearchResultDto<Account>>() {
                });

        final Account account = target
                .path("/accounts/".concat(result.getResult().get(0).getId().toString()))
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get(Account.class);

        Assertions.assertThat(account.getId()).isNotNull();
        Assertions.assertThat(account.getFirstName()).isNotNull();
        Assertions.assertThat(account.getLastName()).isNotNull();
        Assertions.assertThat(account.getBalance()).isNotNull();
    }

    @Test
    public void notFoundAccountTest() {
        Response response = target
                .path("/accounts/".concat(Integer.MAX_VALUE + ""))
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();

        Assertions.assertThat(response.getStatus())
                .isEqualTo(404);
    }

    @Test
    public void createAccountTest() {
        String fName = "test", lName = "test";
        BigDecimal initialBalance = BigDecimal.valueOf(500.00).setScale(2);
        final CreateAccountRequestDto requestDto = buildCreateAccountRequest(fName,lName,initialBalance);
        final AccountResponseDto result = target
                .path("/accounts")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(requestDto, MediaType.APPLICATION_JSON_TYPE))
                .readEntity(AccountResponseDto.class);

        Assertions.assertThat(result.getId())
                .isNotNull();
        Assertions.assertThat(result.getFirstName())
                .isEqualTo(fName);
        Assertions.assertThat(result.getLastName())
                .isEqualTo(lName);
        Assertions.assertThat(result.isEnabled())
                .isTrue();
        Assertions.assertThat(result.getCreatedAt())
                .isNotNull();
        Assertions.assertThat(result.getLastUpdate())
                .isNotNull();

        Assertions.assertThat(result.getBalance().setScale(2).equals(initialBalance)).isTrue();
    }

    @Test
    public void createAccountWithInvalidBalanceTest() {
        String fName = "test", lName = "test";
        BigDecimal initialBalance = BigDecimal.valueOf(-1).setScale(2);
        final CreateAccountRequestDto requestDto = CreateAccountRequestDto.builder()
                .initialBalance(initialBalance)
                .firstName(fName)
                .lastName(lName)
                .build();

        final Response result = target
                .path("/accounts")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(requestDto, MediaType.APPLICATION_JSON_TYPE));


        Assertions.assertThat(result.getStatus())
                .isEqualTo(422)
                .isNotNull();
    }

    @Test
    public void disableAccountTest() {
        final SearchResultDto<Account> accounts = target
                .path("/accounts")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get(new GenericType<SearchResultDto<Account>>() {
                });



        final Response result = target
                .path("/accounts/".concat(accounts.getResult().get(0).getId().toString()).concat("/disable"))
                .request(MediaType.APPLICATION_JSON_TYPE)
                .put(Entity.entity(String.class,MediaType.APPLICATION_JSON_TYPE));


        Assertions.assertThat(result.getStatus())
                .isEqualTo(204);
    }

    @Test
    public void createAccountWithoutMandatoryFieldsBalanceTest() {
        BigDecimal initialBalance = BigDecimal.ZERO.setScale(2);
        final CreateAccountRequestDto requestDto = CreateAccountRequestDto.builder()
                .initialBalance(initialBalance)
                .build();

        final Response result = target
                .path("/accounts")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(requestDto, MediaType.APPLICATION_JSON_TYPE));


        Assertions.assertThat(result.getStatus())
                .isEqualTo(422)
                .isNotNull();
    }


}
