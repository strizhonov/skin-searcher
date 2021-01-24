package com.strizhonovapp.skin.service.impl;

import com.strizhonovapp.skin.dto.InitialMarketInfo;
import com.strizhonovapp.skin.model.Skin;
import com.strizhonovapp.skin.model.Sticker;
import com.strizhonovapp.skin.service.EntityRetriever;
import com.strizhonovapp.skin.service.SkinsPreparer;
import com.strizhonovapp.skin.service.StickersPreparer;
import com.strizhonovapp.skin.testutil.SpringBootComponentTests;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootComponentTests(classes = DecentMarketItemsHolderTest.Configuration.class)
class DecentMarketItemsHolderTest {

    @Autowired
    private StickersPreparer stickersPreparer;

    @Autowired
    private SkinsPreparer skinsPreparer;

    @Autowired
    private DecentMarketItemsHolder decentMarketItemsHolder;

    @ParameterizedTest
    @ValueSource(strings = {"FIRST_SKIN", "SECOND_SKIN", "THIRD_SKIN"})
    void shouldFindAllPreparedSkins(String name) {
        assertTrue(decentMarketItemsHolder.findSkin(name).isPresent());
    }

    @ParameterizedTest
    @ValueSource(strings = {"FIRST_STICKER", "SECOND_STICKER", "THIRD_STICKER"})
    void shouldFindAllPreparedStickers(String name) {
        assertTrue(decentMarketItemsHolder.findSticker(name).isPresent());
    }

    @Test
    void shouldEvenlyLoopThroughSkins() {
        Map<Skin, Integer> skinsAndItsRetrievalCount = new HashMap<>();
        for (int i = 0; i < 99; i++) {
            Skin skin = decentMarketItemsHolder.nextSkin();
            int previousCount = skinsAndItsRetrievalCount.getOrDefault(skin, 0);
            skinsAndItsRetrievalCount.put(skin, previousCount + 1);
        }

        assertEquals(3, skinsAndItsRetrievalCount.size());
        skinsAndItsRetrievalCount.values()
                .stream()
                .reduce((a, b) -> {
                    assertEquals(a, b);
                    return a;
                }).orElseThrow();
    }

    @Test
    void shouldHaveInitTime() {
        assertNotNull(decentMarketItemsHolder.getItemsInitializationTime());
    }

    @Test
    void shouldUpdateInitTimeAfterReload() {
        LocalDateTime oldInitializationTime = decentMarketItemsHolder.getItemsInitializationTime();
        decentMarketItemsHolder.reload();
        LocalDateTime newInitializationTime = decentMarketItemsHolder.getItemsInitializationTime();
        assertTrue(newInitializationTime.isAfter(oldInitializationTime));
    }

    @Test
    void shouldCallPreparersAfterAfterReload() {
        clearInvocations(skinsPreparer);
        clearInvocations(stickersPreparer);
        decentMarketItemsHolder.reload();
        verify(skinsPreparer).prepare(anySet());
        verify(stickersPreparer).prepare(anySet());
    }

    @TestConfiguration
    static class Configuration {

        @Bean
        EntityRetriever entityRetriever() {
            EntityRetriever entityRetriever = Mockito.mock(EntityRetriever.class);
            when(entityRetriever.getInitialInfo()).thenReturn(new InitialMarketInfo());
            return entityRetriever;
        }

        @Bean
        StickersPreparer stickersPreparer() {
            StickersPreparer stickersPreparer = Mockito.mock(StickersPreparer.class);
            when(stickersPreparer.prepare(anySet())).thenReturn(getPreparedStickers());
            return stickersPreparer;
        }

        @Bean
        SkinsPreparer skinsPreparer() {
            SkinsPreparer skinsPreparer = Mockito.mock(SkinsPreparer.class);
            when(skinsPreparer.prepare(anySet())).thenReturn(getPreparedSkins());
            return skinsPreparer;
        }

        private Map<String, Sticker> getPreparedStickers() {
            Map<String, Sticker> result = new HashMap<>();
            result.put("FIRST_STICKER", Sticker.builder()
                    .name("FIRST_STICKER")
                    .volume(11)
                    .usdDirtyPriceX100(122)
                    .usdLowestPriceX100(322)
                    .usdMedianPriceX100(1)
                    .build());
            result.put("SECOND_STICKER", Sticker.builder()
                    .name("SECOND_STICKER")
                    .volume(22)
                    .usdDirtyPriceX100(122)
                    .usdLowestPriceX100(22)
                    .usdMedianPriceX100(22)
                    .build());
            result.put("THIRD_STICKER", Sticker.builder()
                    .name("THIRD_STICKER")
                    .volume(2)
                    .usdDirtyPriceX100(14)
                    .usdLowestPriceX100(4)
                    .usdMedianPriceX100(9)
                    .build());
            return result;
        }

        private Map<String, Skin> getPreparedSkins() {
            Map<String, Skin> result = new HashMap<>();
            result.put("FIRST_SKIN", Skin.builder()
                    .name("FIRST_SKIN")
                    .volume(911)
                    .usdDirtyPriceX100(9122)
                    .usdLowestPriceX100(9322)
                    .usdMedianPriceX100(91)
                    .build());
            result.put("SECOND_SKIN", Skin.builder()
                    .name("SECOND_SKIN")
                    .volume(922)
                    .usdDirtyPriceX100(9122)
                    .usdLowestPriceX100(922)
                    .usdMedianPriceX100(922)
                    .build());
            result.put("THIRD_SKIN", Skin.builder()
                    .name("THIRD_SKIN")
                    .volume(92)
                    .usdDirtyPriceX100(914)
                    .usdLowestPriceX100(94)
                    .usdMedianPriceX100(89)
                    .build());
            return result;
        }

    }

}