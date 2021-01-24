package com.strizhonovapp.skin.service;

import com.strizhonovapp.skin.model.Skin;
import com.strizhonovapp.skin.model.Sticker;

import java.time.LocalDateTime;
import java.util.Optional;

public interface MarketItemsHolder {

    Optional<Skin> findSkin(String skinName);

    Optional<Sticker> findSticker(String stickerName);

    Skin nextSkin();

    LocalDateTime getItemsInitializationTime();

    void reload();

}
