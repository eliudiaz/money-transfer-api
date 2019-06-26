package com.revolut.resources;

import com.revolut.App;
import com.revolut.api.resources.dto.requests.CreateAccountRequestDto;
import lombok.Getter;
import lombok.Setter;
import org.junit.After;
import org.junit.Before;

import java.math.BigDecimal;

@Getter
@Setter
public abstract class BaseIT {

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
        beforeTest();
    }

    public static CreateAccountRequestDto buildCreateAccountRequest(
            final String fName, final String lName, final BigDecimal amount
    ) {
        return CreateAccountRequestDto.builder()
                .initialBalance(amount)
                .firstName(fName)
                .lastName(lName)
                .build();
    }

    public abstract void beforeTest();

    @After
    public void clean() throws Exception {
        app.stop();
    }
}
