package com.strizhonovapp.skin.service;

import com.strizhonovapp.skin.model.Sticker;

import java.util.Map;
import java.util.Set;

public interface StickersPreparer {

    Map<String, Sticker> prepare(Set<Sticker> stickers);

}
