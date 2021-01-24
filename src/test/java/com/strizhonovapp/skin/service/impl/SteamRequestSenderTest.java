package com.strizhonovapp.skin.service.impl;

import com.strizhonovapp.skin.config.SteamRequestSenderQualifier;
import com.strizhonovapp.skin.service.RequestSender;
import com.strizhonovapp.skin.testutil.SpringBootComponentTests;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootComponentTests(classes = SteamRequestSenderTest.SteamRequestSenderTestConfig.class)
@TestPropertySource(properties = {
        "misc.steam-request-freeze-interval-ms=100",
        "misc.too-many-requests-default-freeze-interval-ms=100",
})
class SteamRequestSenderTest {

    private static final String STEAM_URL = "https://steamcommunity.com/market/";
    private static final String URL_FOR_TOO_MANY_REQUESTS_EXCEPTION = STEAM_URL + "TOO_MANY_REQS";

    @Autowired
    @SteamRequestSenderQualifier
    private RequestSender requestSender;

    @Autowired
    private RestTemplate restTemplate;

    @ParameterizedTest
    @ValueSource(strings = {
            "notsteamcommunity.com",
            "http://anysite.by",
            "test"})
    void shouldThrowRuntimeExceptionIfUrlIsNotForSteam(String url) {
        assertThrows(RuntimeException.class, () -> requestSender.send(url));
    }

    @Test
    void shouldCallRestTemplateAfterUrlValidation() {
        String result = requestSender.send(STEAM_URL);
        assertEquals("FROM_MOCK", result);
        verify(restTemplate).getForEntity(STEAM_URL, String.class);
    }

    @Test
    void shouldContinueToPerformAfterTooMuchRequestsException() throws InterruptedException {
        new Thread(() -> requestSender.send(URL_FOR_TOO_MANY_REQUESTS_EXCEPTION)).start();
        Thread.sleep(1000);
        verify(restTemplate, atLeast(2)).getForEntity(URL_FOR_TOO_MANY_REQUESTS_EXCEPTION, String.class);
    }

    @Test
    @Timeout(15)
    void shouldExecuteInitialSendingWithinTimeout() {
        requestSender.send(STEAM_URL);
    }

    @TestConfiguration
    static class SteamRequestSenderTestConfig {

        @Bean
        RestTemplate restTemplate() {
            RestTemplate mockedTemplate = Mockito.mock(RestTemplate.class);
            when(mockedTemplate.getForEntity(eq(STEAM_URL), any()))
                    .thenReturn(ResponseEntity.ok("FROM_MOCK"));
            when(mockedTemplate.getForEntity(eq(URL_FOR_TOO_MANY_REQUESTS_EXCEPTION), any()))
                    .thenThrow(new HttpClientErrorException(HttpStatus.TOO_MANY_REQUESTS));
            return mockedTemplate;
        }

    }

}