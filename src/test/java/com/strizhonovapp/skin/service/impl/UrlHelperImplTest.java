package com.strizhonovapp.skin.service.impl;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class UrlHelperImplTest {

    @ParameterizedTest
    @NullAndEmptySource
    void shouldReturnSameIfValueIsEmpty(String valueToFix) {
        UrlHelperImpl toTest = new UrlHelperImpl(new HashMap<>());
        String result = toTest.replaceIllegalUrlSymbols(valueToFix);
        assertEquals(valueToFix, result);
    }

    @ParameterizedTest
    @ValueSource(strings = {"ANY", "SECOnd", "@@2212", "'%%'\""})
    void shouldReturnSameValueIfUriHelperContainsEmptyMap(String valueToFix) {
        UrlHelperImpl toTest = new UrlHelperImpl(new HashMap<>());

        String result = toTest.replaceIllegalUrlSymbols(valueToFix);
        assertEquals(valueToFix, result);
    }

    @ParameterizedTest
    @ValueSource(strings = {"ANY2", "SECOnd12", "@@2212", "1'%2%'\""})
    void shouldReplaceNeededValues(String valueToFix) {
        HashMap<String, String> illegalUrlCharactersAndReplacement = new HashMap<>();
        illegalUrlCharactersAndReplacement.put("1", "2");
        illegalUrlCharactersAndReplacement.put("@", "x");
        UrlHelperImpl toTest = new UrlHelperImpl(illegalUrlCharactersAndReplacement);

        String result = toTest.replaceIllegalUrlSymbols(valueToFix);
        assertFalse(result.contains("1"));
        assertFalse(result.contains("@"));
    }

}