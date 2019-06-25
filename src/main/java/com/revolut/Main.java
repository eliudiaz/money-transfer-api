package com.revolut;

public class Main {

    public static void main(String[] args) throws Exception {
        App.builder()
                .contextPath("/")
                .basePath("/api/v1")
                .port(8080)
                .build()
                .launch(false);
    }

}
