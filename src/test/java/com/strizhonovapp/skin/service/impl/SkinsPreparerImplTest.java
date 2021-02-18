package com.strizhonovapp.skin.service.impl;

import com.strizhonovapp.skin.model.Skin;
import com.strizhonovapp.skin.service.SkinSuitabilityChecker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(MockitoExtension.class)
class SkinsPreparerImplTest {

    @Mock
    private SkinSuitabilityChecker skinSuitabilityChecker;

    @InjectMocks
    private SkinsPreparerImpl toTest;

    @BeforeEach
    void mock() {
        lenient().when(skinSuitabilityChecker.isSkinSuit(any(Skin.class))).thenReturn(true);
    }

    @Test
    void shouldReturnEmptyMapOnNullInput() {
        assertTrue(toTest.prepare(null).isEmpty());
    }

    @Test
    void shouldReturnEmptyMapOnEmptySetInput() {
        assertTrue(toTest.prepare(new HashSet<>()).isEmpty());
    }

    @Test
    void shouldThrowRuntimeExceptionOnKeyDuplication() {
        Set<Skin> skins = getTwoSkinsSetByNames("TEST_NAME", "TEST_NAME");
        assertThrows(RuntimeException.class, () -> toTest.prepare(skins));
    }

    @Test
    void shouldProperlyMapTwoValuesSet() {
        Set<Skin> skins = getTwoSkinsSetByNames("TEST_NAME_1", "TEST_NAME_2");
        Map<String, Skin> result = toTest.prepare(skins);
        assertEquals(2, result.size());
    }

    @Test
    void shouldCheckEverySkinWithChecker() {
        Set<Skin> skins = getNamedSkins();
        toTest.prepare(skins);
        verify(skinSuitabilityChecker, times(skins.size())).isSkinSuit(any(Skin.class));
    }

    private Set<Skin> getTwoSkinsSetByNames(String name1, String name2) {
        Skin skin1 = Skin.builder()
                .name(name1)
                .volume(99)
                .build();
        Skin skin2 = Skin.builder()
                .name(name2)
                .volume(101)
                .build();
        Set<Skin> skins = new HashSet<>();
        skins.add(skin1);
        skins.add(skin2);
        return skins;
    }

    private Set<Skin> getNamedSkins() {
        return Set.of(
                Skin.builder().name("1").build(),
                Skin.builder().name("2").build(),
                Skin.builder().name("3").build(),
                Skin.builder().name("4").build(),
                Skin.builder().name("5").build(),
                Skin.builder().name("6").build(),
                Skin.builder().name("7").build(),
                Skin.builder().name("8").build(),
                Skin.builder().name("9").build(),
                Skin.builder().name("10").build()
        );
    }

}