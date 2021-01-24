package com.strizhonovapp.skin.service;

import com.strizhonovapp.skin.dto.ListingsRequest;

public interface UrlGenerator {

    String getUrlForListingFromSteam(ListingsRequest request);

    String getUrlForSkinInfoFromSteam(String skinName);

    String getUrlForStickerInfoFromSteam(String stickerName);

    String getUrlForAllSkinsFromHexa();

}
