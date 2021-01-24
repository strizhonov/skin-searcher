package com.strizhonovapp.skin.service;

import com.strizhonovapp.skin.model.MarketItem;
import com.strizhonovapp.skin.model.SkinListing;

public interface EntityEnhancer {

    <T extends MarketItem> T enhancePriceAndVolumeInfo(T marketItem);

    SkinListing enhanceSkinAndStickersPrices(SkinListing skinListing);

}
