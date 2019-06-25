package com.revolut.resources;

import com.revolut.App;
import com.revolut.api.resources.dto.SearchResultDto;
import com.revolut.model.Account;
import com.revolut.platform.ObjectMapperContextResolver;
import org.assertj.core.api.Assertions;
import org.glassfish.jersey.client.ClientConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

public class AccountsResourceIT {

    private App app;
    private String apiContext;

    @Before
    public void setUp() throws Exception {
        app = App.builder()
                .contextPath("/")
                .basePath("/api/v1")
                .port(8080)
                .build();
        apiContext = new StringBuilder("http://localhost:").append(app.getPort())
                .append(app.getBasePath())
                .toString();
        app.launch(true);
    }

    @Test
    public void findAllTest() {
        final Client client = ClientBuilder.newClient();
        final WebTarget path = client.target(apiContext)
                .path("/accounts");
        final SearchResultDto<Account> result = path
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get(SearchResultDto.class);

        Assertions.assertThat(result.isSuccess()).isTrue();
        Assertions.assertThat(result.getResult().isEmpty()).isFalse();
    }

    @Test
    public void findOneAccount() {

        final ClientConfig clientConfig = new ClientConfig();
        clientConfig.register(new ObjectMapperContextResolver());
        final Client client = ClientBuilder.newClient(clientConfig);
        WebTarget path = client.target(apiContext)
                .path("/accounts");
        final SearchResultDto<Account> result = path
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get(new GenericType<SearchResultDto<Account>>(){});

        path = client.target(apiContext)
                .path("/accounts/".concat(result.getResult().get(0).getId().toString()));
        final Account account = path.request(MediaType.APPLICATION_JSON_TYPE)
                .get(Account.class);

        Assertions.assertThat(account.getId()).isNotNull();
        Assertions.assertThat(account.getFirstName()).isNotNull();
        Assertions.assertThat(account.getLastName()).isNotNull();
        Assertions.assertThat(account.getBalance()).isNotNull();

    }

    @After
    public void clean() throws Exception {
        app.stop();
    }


}
