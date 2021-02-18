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
            throw new NullPointerException("Unable to fix null url");
        }
        StringBuilder fixedValue = new StringBuilder(toFix);
        for (Map.Entry<String, String> entry : illegalUrlCharactersAndReplacement.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            int keyIndex;
            while ((keyIndex = fixedValue.indexOf(key)) > -1) {
                fixedValue.replace(keyIndex, keyIndex + key.length(), value);
            }
        }
        return fixedValue.toString();
    }

}
