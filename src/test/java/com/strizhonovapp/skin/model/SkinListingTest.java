package com.strizhonovapp.skin.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SkinListingTest {

    @Test
    void shouldNotFailOnEmptyObjectToStringMethod() {
        assertNotNull(new SkinListing().toString());
    }

    @Test
    void shouldNotFailOnAllFieldsNullToStringMethod() {
        assertNotNull(SkinListing.builder()
                .appliedStickers(null)
                .build()
                .toString());
    }

}