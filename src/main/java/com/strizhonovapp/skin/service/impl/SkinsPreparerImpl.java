package com.strizhonovapp.skin.service.impl;

import com.strizhonovapp.skin.model.Skin;
import com.strizhonovapp.skin.service.SkinSuitabilityChecker;
import com.strizhonovapp.skin.service.SkinsPreparer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SkinsPreparerImpl implements SkinsPreparer {

    private final SkinSuitabilityChecker skinSuitabilityChecker;

    @Override
    public Map<String, Skin> prepare(Set<Skin> skins) {
        if (skins == null) {
            return null;
        }
        return skins.stream()
                .filter(skinSuitabilityChecker::isSkinSuit)
                .collect(Collectors.toMap(Skin::getName, Function.identity()));
    }

}
