package com.revolut.resources;

import com.revolut.BaseApp;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AccountsResourceIT {

    private BaseApp baseApp;

    @Before
    public void setUp() throws Exception {
        baseApp = BaseApp.builder()
                .contextPath("/")
                .basePath("/api/v1/*")
                .port(8080)
                .build();

        baseApp.launch(true);
    }

    @Test
    public void singleTest() {
        System.out.println("tested!");
    }

    @After
    public void clean() throws Exception{
        baseApp.stop();
    }


}
