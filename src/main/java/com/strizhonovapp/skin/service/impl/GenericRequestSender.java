package com.strizhonovapp.skin.service.impl;

import com.strizhonovapp.skin.config.GenericRequestSenderQualifier;
import com.strizhonovapp.skin.service.RequestSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
@GenericRequestSenderQualifier
@RequiredArgsConstructor
public class GenericRequestSender implements RequestSender {

    private final RestTemplate restTemplate;

    @Override
    public String send(String url) {
        log.info("Sending request, url: {}", url);

        return restTemplate
                .getForEntity(url, String.class)
                .getBody();
    }

}
