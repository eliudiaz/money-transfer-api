package com.revolut.resources;

import com.revolut.App;
import com.revolut.api.resources.dto.SearchResultDto;
import com.revolut.model.Account;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
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
        System.out.println(">>>>>>>>>>>> calling!");
        final Client client = ClientBuilder.newClient();
        WebTarget path = client.target(apiContext)
                .path("/accounts");
        SearchResultDto<Account> result = path
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get(SearchResultDto.class);

    }

    @After
    public void clean() throws Exception {
        app.stop();
    }


}
