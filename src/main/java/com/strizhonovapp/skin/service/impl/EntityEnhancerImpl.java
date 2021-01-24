package com.strizhonovapp.skin.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.strizhonovapp.skin.config.SteamRequestSenderQualifier;
import com.strizhonovapp.skin.model.AppliedSticker;
import com.strizhonovapp.skin.model.MarketItem;
import com.strizhonovapp.skin.model.Skin;
import com.strizhonovapp.skin.model.SkinListing;
import com.strizhonovapp.skin.model.Sticker;
import com.strizhonovapp.skin.service.EntityEnhancer;
import com.strizhonovapp.skin.service.MarketItemsHolder;
import com.strizhonovapp.skin.service.RequestSender;
import com.strizhonovapp.skin.service.UrlGenerator;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class EntityEnhancerImpl implements EntityEnhancer {

    @Autowired
    @SteamRequestSenderQualifier
    private RequestSender steamRequestSender;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private UrlGenerator urlGenerator;

    @Autowired
    private MarketItemsHolder marketItemsHolder;

    @Override
    @SneakyThrows
    @Cacheable(value = "itemPrices", key = "{#item.name}")
    public <T extends MarketItem> T enhancePriceAndVolumeInfo(@NonNull T item) {
        String url = getUrlForMarketItemEnhancing(item);
        String json = steamRequestSender.send(url);
        return mapper
                .readerForUpdating(item)
                .readValue(json);
    }

    @Override
    public SkinListing enhanceSkinAndStickersPrices(@NonNull SkinListing skinListing) {
        enhanceSkinPrice(skinListing);
        enhanceStickersPrices(skinListing);
        return skinListing;
    }

    private <T extends MarketItem> String getUrlForMarketItemEnhancing(T item) {
        if (item instanceof Skin) {
            return urlGenerator.getUrlForSkinInfoFromSteam(item.getName());
        }
        if (item instanceof Sticker) {
            return urlGenerator.getUrlForStickerInfoFromSteam(item.getName());
        }
        throw new IllegalStateException("Unable to define item type.");
    }

    private void enhanceStickersPrices(SkinListing skinListing) {
        List<AppliedSticker> appliedStickers = skinListing.getAppliedStickers();
        if (appliedStickers == null) {
            return;
        }
        appliedStickers.forEach(this::enhanceStickerPrice);
    }

    private void enhanceStickerPrice(AppliedSticker appliedSticker) {
        String stickerName = appliedSticker.getName();
        try {
            Integer price = marketItemsHolder.findSticker(stickerName)
                    .orElseThrow()
                    .getUsdDirtyPriceX100();
            appliedSticker.setUsdPriceX100(price);
        } catch (Exception e) {
            log.info(stickerName);
            throw e;
        }
    }

    private void enhanceSkinPrice(SkinListing skinListing) {
        String skinName = skinListing.getSkinName();
        try {
            Integer skinPrice = marketItemsHolder.findSkin(skinName)
                    .orElseThrow()
                    .getUsdLowestPriceX100();
            skinListing.setSkinUsdPriceX100(skinPrice);
        } catch (Exception e) {
            log.info(skinName);
            throw e;
        }
    }

}
