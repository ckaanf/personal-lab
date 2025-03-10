package com.library.feign;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest(classes = NaverClientTest.TestConfiguration.class)
@ActiveProfiles("test")
class NaverClientTest {

    @EnableAutoConfiguration
    @EnableFeignClients(clients = NaverClient.class)
    static class TestConfiguration {}

    @Autowired
    NaverClient naverClient;

    @Test
    void callNaver() {
        String sut = naverClient.searchBook("바보", 1, 1);

        System.out.println(sut);
        assertFalse(sut.isEmpty());
    }
}