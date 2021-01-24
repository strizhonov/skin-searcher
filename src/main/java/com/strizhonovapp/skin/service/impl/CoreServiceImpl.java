package com.strizhonovapp.skin.service.impl;

import com.strizhonovapp.skin.dto.ListingsRequest;
import com.strizhonovapp.skin.jsonhelper.InvalidSkinVolumeException;
import com.strizhonovapp.skin.model.Skin;
import com.strizhonovapp.skin.model.SkinListing;
import com.strizhonovapp.skin.service.CoreService;
import com.strizhonovapp.skin.service.EntityEnhancer;
import com.strizhonovapp.skin.service.EntityRetriever;
import com.strizhonovapp.skin.service.ListingEvaluator;
import com.strizhonovapp.skin.service.ListingPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CoreServiceImpl implements CoreService {

    private static final int MAX_LISTINGS_PER_REQUEST = 100;

    private final EntityEnhancer entityEnhancer;
    private final EntityRetriever entityRetriever;
    private final ListingEvaluator listingEvaluator;
    private final ListingPublisher publisher;

    @Override
    public void processListingsOf(Skin skin) {
        Skin updatedSkin = entityEnhancer.enhancePriceAndVolumeInfo(skin);
        int volume = updatedSkin.getVolume();
        for (int start = 0; start < 1; start += MAX_LISTINGS_PER_REQUEST) {
            Set<SkinListing> batch = getBatch(updatedSkin, start);
            processBatch(batch, updatedSkin, volume);
        }
    }

    private Set<SkinListing> getBatch(Skin clarified, int start) {
        ListingsRequest listingsRequest = ListingsRequest.builder()
                .skin(clarified)
                .count(MAX_LISTINGS_PER_REQUEST)
                .start(start)
                .build();
        return entityRetriever.findListingBatch(listingsRequest).stream()
                .map(entityEnhancer::enhanceSkinAndStickersPrices)
                .collect(Collectors.toSet());
    }

    private void processBatch(Set<SkinListing> batch, Skin clarified,
                              int volume) {
        try {
            batch.stream()
                    .filter(listingEvaluator::isValuable)
                    .forEach(publisher::publish);
        } catch (InvalidSkinVolumeException e) {
            String messageTemplate = "Deprecated skin volume data (supposed volume: {}, real volume: {})";
            log.warn(messageTemplate, volume, e.getTotalCount());
            clarified.setVolume(e.getTotalCount());
        }
    }

}
