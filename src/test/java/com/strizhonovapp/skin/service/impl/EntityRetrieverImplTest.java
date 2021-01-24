package com.strizhonovapp.skin.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.strizhonovapp.skin.dto.InitialMarketInfo;
import com.strizhonovapp.skin.dto.ListingsRequest;
import com.strizhonovapp.skin.dto.SkinListingWrapper;
import com.strizhonovapp.skin.model.Skin;
import com.strizhonovapp.skin.service.RequestSender;
import com.strizhonovapp.skin.service.UrlGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EntityRetrieverImplTest {

    @Mock
    private UrlGenerator urlGenerator;

    @Mock
    private ObjectReader readerMock;

    @Mock
    private RequestSender genericRequestSender;

    @Mock
    private RequestSender steamRequestSender;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private EntityRetrieverImpl entityRetriever;

    @Test
    void shouldCallSteamForListingBatch() throws JsonProcessingException {
        String stringForSkinListingWrapper = "WRAPPER";
        when(readerMock.readValue(stringForSkinListingWrapper)).thenReturn(new SkinListingWrapper());
        when(objectMapper.readerFor(SkinListingWrapper.class)).thenReturn(readerMock);
        when(steamRequestSender.send(nullable(String.class))).thenReturn(stringForSkinListingWrapper);

        ListingsRequest request = ListingsRequest.builder()
                .skin(new Skin())
                .build();
        entityRetriever.findListingBatch(request);
        verify(steamRequestSender).send(nullable(String.class));
    }

    @Test
    void shouldCallGenericRequestSenderForInitialInfo() throws JsonProcessingException {
        String stringForInitialMarketInfo = "INFO";
        when(readerMock.readValue(stringForInitialMarketInfo)).thenReturn(new InitialMarketInfo());
        when(objectMapper.readerFor(InitialMarketInfo.class)).thenReturn(readerMock);
        when(genericRequestSender.send(nullable(String.class))).thenReturn(stringForInitialMarketInfo);

        entityRetriever.getInitialInfo();
        verify(genericRequestSender).send(nullable(String.class));
    }

}