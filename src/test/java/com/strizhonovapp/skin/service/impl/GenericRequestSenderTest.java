package com.strizhonovapp.skin.service.impl;

import com.strizhonovapp.skin.config.GenericRequestSenderQualifier;
import com.strizhonovapp.skin.service.RequestSender;
import com.strizhonovapp.skin.testutil.SpringBootComponentTests;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootComponentTests(classes = GenericRequestSenderTest.SteamRequestSenderTestConfig.class)
class GenericRequestSenderTest {

    @Autowired
    @GenericRequestSenderQualifier
    private RequestSender requestSender;

    @Autowired
    private RestTemplate restTemplate;

    @ParameterizedTest
    @ValueSource(strings = {
            "notsteamcommunity.com",
            "http://anysite.by",
            "test"})
    void shouldCallRestTemplateForAnyUrl(String url) {
        String result = requestSender.send(url);
        assertEquals("FROM_MOCK", result);
        verify(restTemplate).getForEntity(anyString(), any());
        clearInvocations(restTemplate);
    }

    @ParameterizedTest
    @Timeout(15)
    @ValueSource(strings = {
            "notsteamcommunity.com",
            "http://anysite.by",
            "test"})
    void shouldExecuteInitialSendingWithinTimeout(String url) {
        requestSender.send(url);
    }

    @TestConfiguration
    static class SteamRequestSenderTestConfig {

        @Bean
        RestTemplate restTemplate() {
            RestTemplate mockedTemplate = Mockito.mock(RestTemplate.class);
            when(mockedTemplate.getForEntity(anyString(), any()))
                    .thenReturn(ResponseEntity.ok("FROM_MOCK"));
            return mockedTemplate;
        }

    }

}