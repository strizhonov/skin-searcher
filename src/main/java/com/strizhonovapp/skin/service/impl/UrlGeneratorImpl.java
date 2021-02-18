package com.strizhonovapp.skin.service.impl;

import com.strizhonovapp.skin.dto.ListingsRequest;
import com.strizhonovapp.skin.service.UrlGenerator;
import com.strizhonovapp.skin.service.UrlHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UrlGeneratorImpl implements UrlGenerator {

    @Value(value = "${url.listing-steam}")
    private String listingUrlFromSteam;

    @Value("#{'${url.allSkins-hexa}' + '${hexa.key}'}")
    private String allSkinsUrlFromHexa;

    @Value(value = "${url.skin-steam}")
    private String skinUrlFromSteam;

    @Value(value = "${url.sticker-steam}")
    private String stickerUrlFromSteam;

    private final UrlHelper urlHelper;

    @Override
    public String getUrlForListingFromSteam(ListingsRequest request) {
        String skinName = request.getSkin().getName();
        String forUrlSkinName = urlHelper.replaceIllegalUrlSymbols(skinName);
        if (forUrlSkinName == null) {
            throw new IllegalStateException("Unable to create url without skin name");
        }
        return String.format(
                listingUrlFromSteam,
                forUrlSkinName,
                request.getStart(),
                request.getCount()
        );
    }

    @Override
    public String getUrlForSkinInfoFromSteam(String skinName) {
        String forUrlSkinName = urlHelper.replaceIllegalUrlSymbols(skinName);
        return String.format(skinUrlFromSteam, forUrlSkinName);
    }

    @Override
    public String getUrlForStickerInfoFromSteam(String stickerName) {
        String forUrlStickerName = urlHelper.replaceIllegalUrlSymbols(stickerName);
        return String.format(stickerUrlFromSteam, forUrlStickerName);
    }

    @Override
    public String getUrlForAllSkinsFromHexa() {
        return allSkinsUrlFromHexa;
    }

}
