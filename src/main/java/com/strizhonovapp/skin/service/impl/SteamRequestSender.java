package com.strizhonovapp.skin.service.impl;

import com.strizhonovapp.skin.config.SteamRequestSenderQualifier;
import com.strizhonovapp.skin.service.RequestSender;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@SteamRequestSenderQualifier
@RequiredArgsConstructor
public class SteamRequestSender implements RequestSender {

    private final RestTemplate restTemplate;

    private int tooManyRequestsExceptionsCount = 0;

    @Value("${misc.steam-request-freeze-interval-ms}")
    private int steamPerRequestFreezeInterval;

    @Value("${misc.too-many-requests-default-freeze-interval-ms}")
    private int defaultFreezeInterval;

    @Value("${url.community-market-domain-name}")
    private String steamCommunityCoreDomainName;

    @Override
    @SneakyThrows
    public String send(String url) {
        if (!url.startsWith(steamCommunityCoreDomainName)) {
            throw new IllegalStateException("Request url should contain steam market path.");
        }
        log.info("Sending request, url: {}", url);

        while (true) {
            try {
                String response = getResponse(url);
                sleepDefaultTime();
                return response;
            } catch (HttpClientErrorException e) {
                processClientException(e, url);
            }
        }
    }

    private void sleepDefaultTime() throws InterruptedException {
        Thread.sleep(steamPerRequestFreezeInterval);
    }

    private void processClientException(HttpClientErrorException e, String url)
            throws InterruptedException {
        if (e.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS) {
            processTooManyRequests(url);
        } else {
            throw e;
        }
    }

    private String getResponse(String url) {
        try {
            ResponseEntity<String> httpResponse = restTemplate.getForEntity(url, String.class);
            resetTooManyRequestsExceptionCount();
            return httpResponse.getBody();
        } catch (Exception e) {
            log.warn(url);
            log.warn(e.getMessage());
            throw e;
        }
    }

    private void resetTooManyRequestsExceptionCount() {
        tooManyRequestsExceptionsCount = 0;
    }

    private void processTooManyRequests(String url)
            throws InterruptedException {
        ++tooManyRequestsExceptionsCount;

        log.warn("Too many requests, freezing. Url: {}", url);
        int actualFreezeInterval = defaultFreezeInterval * tooManyRequestsExceptionsCount;

        log.info("actualFreezeInterval: {}", actualFreezeInterval);
        Thread.sleep(actualFreezeInterval);

        log.info("Waking up.");
    }

}
