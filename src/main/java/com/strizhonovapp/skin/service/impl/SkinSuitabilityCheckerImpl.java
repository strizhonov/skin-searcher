package com.strizhonovapp.skin.service.impl;

import com.strizhonovapp.skin.model.Skin;
import com.strizhonovapp.skin.service.SkinSuitabilityChecker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Pattern;

@Service
public class SkinSuitabilityCheckerImpl implements SkinSuitabilityChecker {

    private static final String SKIN_NAME_PATTERN_STRING
            = "^.+\\s\\|\\s(.+\\s)+\\((Factory New|Minimal Wear|Field-Tested|Well-Worn|Battle-Scarred)\\)$";
    private static final Pattern SKIN_NAME_PATTERN = Pattern.compile(SKIN_NAME_PATTERN_STRING);

    @Value("#{'${misc.forbidden-skin-name-parts}'.split(',')}")
    private Set<String> forbiddenNameParts;

    @Value(value = "${business.bottom-price.min}")
    private long minBottomPrice;

    @Value(value = "${business.bottom-price.max}")
    private long maxBottomPrice;

    @Override
    public boolean isSkinSuit(@NonNull Skin skin) {
        if (skin.getName() == null) {
            throw new IllegalStateException("Unable to process skin without name");
        }
        return skin.getUsdDirtyPriceX100() > minBottomPrice
                && skin.getUsdDirtyPriceX100() < maxBottomPrice
                && forbiddenNameParts.stream().noneMatch(nameContains(skin))
                && SKIN_NAME_PATTERN.matcher(skin.getName()).matches();
    }

    private Predicate<String> nameContains(Skin toTest) {
        return s -> toTest.getName().contains(s);
    }

}
