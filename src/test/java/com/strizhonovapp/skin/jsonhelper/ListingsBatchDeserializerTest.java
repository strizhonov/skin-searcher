package com.strizhonovapp.skin.jsonhelper;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.strizhonovapp.skin.model.SkinListing;
import com.strizhonovapp.skin.testutil.SpringBeanComponentTests;
import com.strizhonovapp.skin.testutil.TestUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBeanComponentTests(classes = {ListingsBatchDeserializer.class, ObjectMapper.class,
        StickerMapperImpl.class, StickerNamesRetrieverImpl.class})
@TestPropertySource(properties = "misc.steam-fee-percentage=13")
class ListingsBatchDeserializerTest {

    public static final String STEAM_LISTINGS_JSON = "steam-listings.json";
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ListingsBatchDeserializer deserializer;

    @Test
    void shouldCorrectlyParseListings() throws IOException {
        Set<SkinListing> result = this.deserialize();
        assertEquals(40, result.size());
        result.forEach(l -> {
            assertNotNull(l.getSkinName());
            assertNotNull(l.getListingUsdPriceX100());
            assertNotNull(l.getSteamLink());
            assertNotNull(l.getAppliedStickers());
        });
    }

    private Set<SkinListing> deserialize() throws IOException {
        InputStream stream = TestUtil.getJsonStreamFromResourceName(STEAM_LISTINGS_JSON);
        JsonParser parser = objectMapper.getFactory().createParser(stream);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        return deserializer.deserialize(parser, ctxt).getSkinListings();
    }

}