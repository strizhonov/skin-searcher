package com.strizhonovapp.skin.service.impl;

import com.strizhonovapp.skin.dto.ListingsRequest;
import com.strizhonovapp.skin.model.AppliedSticker;
import com.strizhonovapp.skin.model.Skin;
import com.strizhonovapp.skin.model.SkinListing;
import com.strizhonovapp.skin.service.CoreService;
import com.strizhonovapp.skin.service.EntityEnhancer;
import com.strizhonovapp.skin.service.EntityRetriever;
import com.strizhonovapp.skin.service.ListingEvaluator;
import com.strizhonovapp.skin.service.ListingPublisher;
import com.strizhonovapp.skin.testutil.SpringBootComponentTests;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootComponentTests(classes = CoreServiceImplTest.Configuration.class)
class CoreServiceImplTest {

    @MockBean
    private ListingPublisher listingPublisher;

    @Autowired
    private CoreService coreService;

    @Test
    void shouldCallForPublishingForAllValuableSkins() {
        coreService.processListingsOf(new Skin());
        verify(listingPublisher, times(4)).publish(any());
    }

    @TestConfiguration
    static class Configuration {

        @Bean
        EntityEnhancer entityEnhancer() {
            EntityEnhancer entityEnhancer = mock(EntityEnhancer.class);
            Skin tenVolumedSkin = Skin.builder().volume(10).build();
            when(entityEnhancer.enhancePriceAndVolumeInfo(nullable(Skin.class))).thenReturn(tenVolumedSkin);
            when(entityEnhancer.enhanceSkinAndStickersPrices(nullable(SkinListing.class)))
                    .thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));
            return entityEnhancer;
        }

        @Bean
        EntityRetriever entityRetriever() throws InterruptedException {
            EntityRetriever entityRetriever = mock(EntityRetriever.class);
            when(entityRetriever.findListingBatch(nullable(ListingsRequest.class))).thenReturn(getListings());
            return entityRetriever;
        }

        @Bean
        ListingEvaluator listingEvaluator() {
            ListingEvaluator listingEvaluator = mock(ListingEvaluator.class);
            when(listingEvaluator.isValuable(nullable(SkinListing.class))).thenAnswer(invocationOnMock -> {
                SkinListing listing = invocationOnMock.getArgument(0);
                return listing.getAppliedStickers() != null && listing.getAppliedStickers().size() == 4;
            });
            return listingEvaluator;
        }

        private Set<SkinListing> getListings() throws InterruptedException {
            return Set.of(
                    SkinListing.builder().appliedStickers(null).build(),
                    SkinListing.builder().appliedStickers(getStickers(0)).build(),
                    SkinListing.builder().appliedStickers(getStickers(1)).build(),
                    SkinListing.builder().appliedStickers(getStickers(2)).build(),
                    SkinListing.builder().appliedStickers(getStickers(3)).build(),
                    SkinListing.builder().appliedStickers(getStickers(5)).build(),
                    SkinListing.builder().appliedStickers(getStickers(6)).build(),
                    SkinListing.builder().appliedStickers(getStickers(7)).build(),

                    // Valuable skins
                    SkinListing.builder().appliedStickers(getStickers(4)).build(),
                    SkinListing.builder().appliedStickers(getStickers(4)).build(),
                    SkinListing.builder().appliedStickers(getStickers(4)).build(),
                    SkinListing.builder().appliedStickers(getStickers(4)).build()
            );
        }

        private List<AppliedSticker> getStickers(int count) throws InterruptedException {
            List<AppliedSticker> result = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                Thread.sleep(1);
                result.add(new AppliedSticker("" + i + "-" + LocalDateTime.now()));
            }
            return result;
        }

    }

}