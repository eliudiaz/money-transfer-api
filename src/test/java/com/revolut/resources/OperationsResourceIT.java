package com.revolut.resources;

import com.revolut.api.resources.dto.SearchResultDto;
import com.revolut.model.Account;
import com.revolut.platform.ObjectMapperContextResolver;
import org.assertj.core.api.Assertions;
import org.glassfish.jersey.client.ClientConfig;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

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

    @Test
    public void transferMoneyTest() {
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


}
