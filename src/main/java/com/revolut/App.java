package com.revolut;

public class App {

    public static void main(String[] args) throws Exception {
        BaseApp.builder()
                .contextPath("/")
                .basePath("/api/v1/*")
                .port(8080)
                .build()
                .launch(false);
    }

}
