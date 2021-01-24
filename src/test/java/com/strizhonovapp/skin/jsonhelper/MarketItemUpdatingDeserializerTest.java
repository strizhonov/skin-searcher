package com.strizhonovapp.skin.jsonhelper;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.strizhonovapp.skin.model.MarketItem;
import com.strizhonovapp.skin.model.Skin;
import com.strizhonovapp.skin.model.Sticker;
import com.strizhonovapp.skin.testutil.SpringBeanComponentTests;
import com.strizhonovapp.skin.testutil.TestUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBeanComponentTests(classes = {MarketItemUpdatingDeserializer.class, ObjectMapper.class})
class MarketItemUpdatingDeserializerTest {

    public static final String STEAM_PRICE_OVERVIEW_JSON = "steam-price-overview.json";
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MarketItemUpdatingDeserializer<MarketItem> deserializer;

    @Test
    void shouldSerializeSkinVolumeAndLowestPriceAndMedianPrice() throws IOException {
        MarketItem result = this.deserialize(new Skin());
        assertTrue(result instanceof Skin);
        assertEquals(181, result.getUsdLowestPriceX100());
        assertEquals(1319, result.getVolume());
        assertEquals(187, result.getUsdMedianPriceX100());
    }

    @Test
    void shouldSerializeStickerVolumeAndLowestPriceAndMedianPrice() throws IOException {
        MarketItem result = this.deserialize(new Sticker());
        assertTrue(result instanceof Sticker);
        assertEquals(181, result.getUsdLowestPriceX100());
        assertEquals(1319, result.getVolume());
        assertEquals(187, result.getUsdMedianPriceX100());
    }

    @Test
    void shouldNotUpdateSkinNameAndDirtyPrice() throws IOException {
        Skin itemToUpdate = Skin.builder()
                .name("SKIN_NAME")
                .usdDirtyPriceX100(15)
                .build();
        MarketItem result = this.deserialize(itemToUpdate);
        assertTrue(result instanceof Skin);
        assertEquals("SKIN_NAME", result.getName());
        assertEquals(15, result.getUsdDirtyPriceX100());
    }

    @Test
    void shouldNotUpdateStickerNameAndDirtyPrice() throws IOException {
        Sticker itemToUpdate = Sticker.builder()
                .name("STICKER_NAME")
                .usdDirtyPriceX100(12)
                .build();
        MarketItem result = this.deserialize(itemToUpdate);
        assertTrue(result instanceof Sticker);
        assertEquals("STICKER_NAME", result.getName());
        assertEquals(12, result.getUsdDirtyPriceX100());
    }

    private MarketItem deserialize(MarketItem itemToUpdate) throws IOException {
        InputStream jsonStream = TestUtil.getJsonStreamFromResourceName(STEAM_PRICE_OVERVIEW_JSON);
        JsonParser parser = objectMapper.getFactory().createParser(jsonStream);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        return deserializer.deserialize(parser, ctxt, itemToUpdate);
    }

}