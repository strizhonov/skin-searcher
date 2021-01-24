package com.strizhonovapp.skin.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.strizhonovapp.skin.config.GenericRequestSenderQualifier;
import com.strizhonovapp.skin.config.SteamRequestSenderQualifier;
import com.strizhonovapp.skin.dto.InitialMarketInfo;
import com.strizhonovapp.skin.dto.ListingsRequest;
import com.strizhonovapp.skin.dto.SkinListingWrapper;
import com.strizhonovapp.skin.model.SkinListing;
import com.strizhonovapp.skin.service.EntityRetriever;
import com.strizhonovapp.skin.service.RequestSender;
import com.strizhonovapp.skin.service.UrlGenerator;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class EntityRetrieverImpl implements EntityRetriever {

    @Autowired
    @GenericRequestSenderQualifier
    private RequestSender genericRequestSender;

    @Autowired
    @SteamRequestSenderQualifier
    private RequestSender steamRequestSender;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private UrlGenerator urlGenerator;

    @Override
    @SneakyThrows
    public Set<SkinListing> findListingBatch(@NonNull ListingsRequest request) {
        String url = urlGenerator.getUrlForListingFromSteam(request);
        String json = steamRequestSender.send(url);
        return mapper
                .readerFor(SkinListingWrapper.class)
                .<SkinListingWrapper>readValue(json)
                .getSkinListings();
    }

    @Override
    @SneakyThrows
    public InitialMarketInfo getInitialInfo() {
        String url = urlGenerator.getUrlForAllSkinsFromHexa();
        String json = genericRequestSender.send(url);
        return mapper
                .readerFor(InitialMarketInfo.class)
                .readValue(json);
    }

}
