package com.strizhonovapp.skin.service.impl;

import com.strizhonovapp.skin.model.AppliedSticker;
import com.strizhonovapp.skin.model.SkinListing;
import com.strizhonovapp.skin.service.ListingEvaluator;
import com.strizhonovapp.skin.testutil.SpringBeanComponentTests;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBeanComponentTests(classes = ListingEvaluatorImpl.class)
@TestPropertySource(properties = {
        "business.max-listing-price=" + ListingEvaluatorImplTest.MAX_LISTING_PRICE,
        "business.max-listing-overprice-coef=" + ListingEvaluatorImplTest.MAX_LISTING_OVERPRICE
})
class ListingEvaluatorImplTest {

    static final double MAX_LISTING_OVERPRICE = 2.5;
    static final int MAX_LISTING_PRICE = 200;

    @Autowired
    private ListingEvaluator listingEvaluator;

    @ParameterizedTest
    @MethodSource("valuableListing")
    void shouldReturnTrueForValuableListing(SkinListing listing) {
        assertTrue(listingEvaluator.isValuable(listing));
    }

    @ParameterizedTest
    @MethodSource("withLessThanFourStickers")
    void shouldReturnFalseForListingWithLessThanFourStickers(SkinListing listing) {
        assertFalse(listingEvaluator.isValuable(listing));
    }

    @ParameterizedTest
    @MethodSource("withoutNametag")
    void shouldReturnFalseForListingWithoutNametag(SkinListing listing) {
        assertFalse(listingEvaluator.isValuable(listing));
    }

    static Stream<SkinListing> valuableListing() {
        return Stream.of(
                SkinListing.builder()
                        .skinName("ANY")
                        .nameTag("NAMETAG")
                        .skinUsdPriceX100(100)
                        .listingUsdPriceX100(150)
                        .appliedStickers(List.of(new AppliedSticker(), new AppliedSticker(),
                                new AppliedSticker(), new AppliedSticker()))
                        .build()
        );
    }

    static Stream<SkinListing> withLessThanFourStickers() {
        return Stream.of(
                SkinListing.builder()
                        .skinName("ANY")
                        .nameTag("NAMETAG")
                        .skinUsdPriceX100(100)
                        .listingUsdPriceX100(150)
                        .appliedStickers(null)
                        .build(),
                SkinListing.builder()
                        .skinName("ANY")
                        .nameTag("NAMETAG")
                        .skinUsdPriceX100(100)
                        .listingUsdPriceX100(150)
                        .appliedStickers(new ArrayList<>())
                        .build(),
                SkinListing.builder()
                        .skinName("ANY")
                        .nameTag("NAMETAG")
                        .skinUsdPriceX100(100)
                        .listingUsdPriceX100(150)
                        .appliedStickers(List.of(new AppliedSticker()))
                        .build(),
                SkinListing.builder()
                        .skinName("ANY")
                        .nameTag("NAMETAG")
                        .skinUsdPriceX100(100)
                        .listingUsdPriceX100(150)
                        .appliedStickers(List.of(new AppliedSticker(), new AppliedSticker()))
                        .build(),
                SkinListing.builder()
                        .skinName("ANY")
                        .nameTag("NAMETAG")
                        .skinUsdPriceX100(100)
                        .listingUsdPriceX100(150)
                        .appliedStickers(List.of(new AppliedSticker(), new AppliedSticker(), new AppliedSticker()))
                        .build()
        );
    }

    static Stream<SkinListing> withoutNametag() {
        List<AppliedSticker> fourStickers = List.of(new AppliedSticker(), new AppliedSticker(),
                new AppliedSticker(), new AppliedSticker());
        return Stream.of(
                SkinListing.builder()
                        .skinName("ANY")
                        .nameTag(null)
                        .skinUsdPriceX100(100)
                        .listingUsdPriceX100(150)
                        .appliedStickers(fourStickers)
                        .build()
        );
    }

}