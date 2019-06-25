package com.revolut.resources;

import com.revolut.App;
import lombok.Getter;
import lombok.Setter;
import org.junit.After;
import org.junit.Before;

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

    public abstract void beforeTest();

    @After
    public void clean() throws Exception {
        app.stop();
    }
}
