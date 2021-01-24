package com.strizhonovapp.skin.service.impl;

import com.strizhonovapp.skin.dto.InitialMarketInfo;
import com.strizhonovapp.skin.model.Skin;
import com.strizhonovapp.skin.model.Sticker;
import com.strizhonovapp.skin.service.EntityRetriever;
import com.strizhonovapp.skin.service.MarketItemsHolder;
import com.strizhonovapp.skin.service.SkinsPreparer;
import com.strizhonovapp.skin.service.StickersPreparer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class DecentMarketItemsHolder implements MarketItemsHolder {

    private final EntityRetriever entityRetriever;
    private final StickersPreparer stickersPreparer;
    private final SkinsPreparer skinsPreparer;

    private Map<String, Sticker> stickerCache;
    private Map<String, Skin> skinCache;
    private Iterator<Skin> skinIterator;
    private LocalDateTime initializationTime;

    @Override
    public Optional<Skin> findSkin(String skinName) {
        return Optional.ofNullable(skinCache.get(skinName));
    }

    @Override
    public Optional<Sticker> findSticker(String stickerName) {
        return Optional.ofNullable(stickerCache.get(stickerName));
    }

    @Override
    public Skin nextSkin() {
        if (!skinIterator.hasNext()) {
            skinIterator = skinCache.values().iterator();
        }
        return skinIterator.next();
    }

    @Override
    public LocalDateTime getItemsInitializationTime() {
        return initializationTime;
    }

    @Override
    public void reload() {
        init();
    }

    @PostConstruct
    private void init() {
        initializationTime = LocalDateTime.now();
        InitialMarketInfo marketInfo = entityRetriever.getInitialInfo();
        stickerCache = getDecentStickerCache(marketInfo);
        skinCache = getDecentSkinCache(marketInfo);
        skinIterator = skinCache.values().iterator();
    }

    private Map<String, Sticker> getDecentStickerCache(InitialMarketInfo marketInfo) {
        Set<Sticker> stickers = marketInfo.getDirtilyPricedStickers();
        return stickersPreparer.prepare(stickers);
    }

    private Map<String, Skin> getDecentSkinCache(InitialMarketInfo marketInfo) {
        Set<Skin> skins = marketInfo.getDirtilyPricedSkins();
        return skinsPreparer.prepare(skins);
    }

}
