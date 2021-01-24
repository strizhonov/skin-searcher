package com.strizhonovapp.skin.service.impl;

import com.strizhonovapp.skin.model.Sticker;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StickersPreparerImplTest {

    private final StickersPreparerImpl toTest = new StickersPreparerImpl();

    @Test
    void shouldReturnNullOnNullInput() {
        assertNull(toTest.prepare(null));
    }

    @Test
    void shouldReturnEmptyMapOnEmptySetInput() {
        assertTrue(toTest.prepare(new HashSet<>()).isEmpty());
    }

    @Test
    void shouldThrowRuntimeExceptionOnKeyDuplication() {
        Set<Sticker> stickers = getTwoStickersSetByNames("TEST_NAME", "TEST_NAME");
        assertThrows(RuntimeException.class, () -> toTest.prepare(stickers));
    }

    @Test
    void shouldProperlyMapTwoValuesSet() {
        Set<Sticker> stickers = getTwoStickersSetByNames("TEST_NAME_1", "TEST_NAME_2");
        Map<String, Sticker> result = toTest.prepare(stickers);
        assertEquals(2, result.size());
    }

    private Set<Sticker> getTwoStickersSetByNames(String name1, String name2) {
        Sticker sticker1 = Sticker.builder()
                .name(name1)
                .volume(99)
                .build();
        Sticker sticker2 = Sticker.builder()
                .name(name2)
                .volume(101)
                .build();
        Set<Sticker> stickers = new HashSet<>();
        stickers.add(sticker1);
        stickers.add(sticker2);
        return stickers;
    }

}