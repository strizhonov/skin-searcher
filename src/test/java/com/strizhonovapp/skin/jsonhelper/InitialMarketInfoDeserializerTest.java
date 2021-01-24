package com.strizhonovapp.skin.jsonhelper;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.strizhonovapp.skin.dto.InitialMarketInfo;
import com.strizhonovapp.skin.model.Skin;
import com.strizhonovapp.skin.model.Sticker;
import com.strizhonovapp.skin.testutil.SpringBeanComponentTests;
import com.strizhonovapp.skin.testutil.TestUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBeanComponentTests(classes = {InitialMarketInfoDeserializer.class, ObjectMapper.class})
class InitialMarketInfoDeserializerTest {

    public static final String HEXA_INITIAL_ITEMS_JSON = "hexa-initial-items.json";
    @Autowired
    private InitialMarketInfoDeserializer deserializer;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldSerializeItemsWithNameAndDirtyPriceFromHexaJson() throws IOException {
        InitialMarketInfo result = this.deserialize();

        Set<Skin> dirtilyPricedSkins = result.getDirtilyPricedSkins();
        assertEquals(13169, dirtilyPricedSkins.size());
        dirtilyPricedSkins.forEach(s -> {
            assertNotNull(s.getName());
            assertNotNull(s.getUsdDirtyPriceX100());
        });
        Set<Sticker> dirtilyPricedStickers = result.getDirtilyPricedStickers();
        assertEquals(3643, dirtilyPricedStickers.size());
        dirtilyPricedStickers.forEach(s -> {
            assertNotNull(s.getName());
            assertNotNull(s.getUsdDirtyPriceX100());
        });
    }

    private InitialMarketInfo deserialize() throws IOException {
        InputStream stream = TestUtil.getJsonStreamFromResourceName(HEXA_INITIAL_ITEMS_JSON);
        JsonParser parser = objectMapper.getFactory().createParser(stream);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        return deserializer.deserialize(parser, ctxt);
    }

}