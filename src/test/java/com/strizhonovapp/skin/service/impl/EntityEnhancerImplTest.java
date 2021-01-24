package com.strizhonovapp.skin.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.strizhonovapp.skin.model.AppliedSticker;
import com.strizhonovapp.skin.model.Skin;
import com.strizhonovapp.skin.model.SkinListing;
import com.strizhonovapp.skin.model.Sticker;
import com.strizhonovapp.skin.service.MarketItemsHolder;
import com.strizhonovapp.skin.service.RequestSender;
import com.strizhonovapp.skin.service.UrlGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EntityEnhancerImplTest {

    @Mock
    private RequestSender steamRequestSender;

    @Mock
    private ObjectMapper mapper;

    @Mock
    private UrlGenerator urlGenerator;

    @Mock
    private MarketItemsHolder marketItemsHolder;

    @InjectMocks
    private EntityEnhancerImpl entityEnhancer;

    @Test
    void shouldCallUrlGeneratorForSticker() {
        when(mapper.readerForUpdating(any())).thenReturn(mock(ObjectReader.class));
        Sticker sticker = new Sticker();
        entityEnhancer.enhancePriceAndVolumeInfo(sticker);
        verify(urlGenerator).getUrlForStickerInfoFromSteam(nullable(String.class));
    }

    @Test
    void shouldCallUrlGeneratorForSkin() {
        when(mapper.readerForUpdating(any())).thenReturn(mock(ObjectReader.class));
        Skin sticker = new Skin();
        entityEnhancer.enhancePriceAndVolumeInfo(sticker);
        verify(urlGenerator).getUrlForSkinInfoFromSteam(nullable(String.class));
    }

    @Test
    void shouldThrowRuntimeExceptionWhenSkinIsNotFoundByName() {
        String skinName = "SKIN_NAME";
        SkinListing listing = SkinListing.builder()
                .skinName(skinName)
                .appliedStickers(new ArrayList<>())
                .build();
        when(marketItemsHolder.findSkin(skinName)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> entityEnhancer.enhanceSkinAndStickersPrices(listing));
    }

    @Test
    void shouldThrowRuntimeExceptionWhenStickerIsNotFoundByName() {
        String someStickerName = "STICKER_NAME";
        SkinListing listing = SkinListing.builder()
                .skinName("SKIN_NAME")
                .appliedStickers(List.of(new AppliedSticker(someStickerName)))
                .build();
        when(marketItemsHolder.findSkin(nullable(String.class))).thenReturn(Optional.of(new Skin()));
        when(marketItemsHolder.findSticker(someStickerName)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> entityEnhancer.enhanceSkinAndStickersPrices(listing));
    }

    @Test
    void shouldReplaceSkinPriceFromLowestPrice() {
        int oldPrice = 100;
        int newPrice = 55;
        String skinName = "SKIN_NAME";

        Skin newSkin = getSkinForMock(newPrice, skinName);
        when(marketItemsHolder.findSkin(skinName)).thenReturn(Optional.of(newSkin));

        SkinListing listing = getTestListing(oldPrice, skinName, new ArrayList<>());
        entityEnhancer.enhanceSkinAndStickersPrices(listing);
        assertEquals(newPrice, listing.getSkinUsdPriceX100());
    }

    @Test
    void shouldReplaceStickersPricesFromDirtyPrice() {
        int oldPrice = 100;
        int newPrice = 55;

        String skinName = "SKIN_NAME";
        String stickerName = "STICKER_NAME";

        when(marketItemsHolder.findSkin(skinName)).thenReturn(Optional.of(new Skin()));
        Sticker sticker = getStickerForMock(newPrice, stickerName);
        when(marketItemsHolder.findSticker(stickerName)).thenReturn(Optional.of(sticker));

        List<AppliedSticker> stickers = getFourSameStickers(oldPrice, stickerName);
        SkinListing listing = getTestListing(oldPrice, skinName, stickers);
        entityEnhancer.enhanceSkinAndStickersPrices(listing);
        listing.getAppliedStickers().forEach(s -> assertEquals(newPrice, s.getUsdPriceX100()));
    }

    private List<AppliedSticker> getFourSameStickers(int oldPrice, String stickerName) {
        return List.of(
                new AppliedSticker(stickerName, oldPrice),
                new AppliedSticker(stickerName, oldPrice),
                new AppliedSticker(stickerName, oldPrice),
                new AppliedSticker(stickerName, oldPrice)
        );
    }

    private SkinListing getTestListing(int oldPrice, String skinName, List<AppliedSticker> stickers) {
        return SkinListing.builder()
                .skinName(skinName)
                .skinUsdPriceX100(oldPrice)
                .appliedStickers(stickers)
                .build();
    }

    private Sticker getStickerForMock(int newPrice, String stickerName) {
        return Sticker.builder()
                .name(stickerName)
                .usdDirtyPriceX100(newPrice)
                .build();
    }

    private Skin getSkinForMock(int newPrice, String skinName) {
        return Skin.builder()
                .name(skinName)
                .usdLowestPriceX100(newPrice)
                .build();
    }

}