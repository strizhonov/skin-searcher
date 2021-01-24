package com.strizhonovapp.skin.service.impl;

import com.strizhonovapp.skin.service.UrlHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class UrlHelperImpl implements UrlHelper {

    private final Map<String, String> illegalUrlCharactersAndReplacement;

    @Override
    public String replaceIllegalUrlSymbols(String toFix) {
        if (toFix == null) {
            return null;
        }
        String fixedValue = toFix;
        for (Map.Entry<String, String> entry : illegalUrlCharactersAndReplacement.entrySet()) {
            String key = entry.getKey();
            while (fixedValue.contains(key)) {
                String value = entry.getValue();
                fixedValue = fixedValue.replace(key, value);
            }
        }
        return fixedValue;
    }

}
