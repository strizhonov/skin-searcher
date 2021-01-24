package com.strizhonovapp.skin.service.impl;

import com.strizhonovapp.skin.model.Sticker;
import com.strizhonovapp.skin.service.StickersPreparer;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class StickersPreparerImpl implements StickersPreparer {

    @Override
    public Map<String, Sticker> prepare(Set<Sticker> stickers) {
        if (stickers == null) {
            return null;
        }
        return stickers.stream()
                .collect(Collectors.toMap(Sticker::getName, Function.identity()));
    }

}
