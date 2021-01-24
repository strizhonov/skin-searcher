package com.strizhonovapp.skin.service.impl;

import com.strizhonovapp.skin.model.Skin;
import com.strizhonovapp.skin.service.SkinSuitabilityChecker;
import com.strizhonovapp.skin.testutil.SpringBeanComponentTests;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBeanComponentTests(classes = SkinSuitabilityCheckerImpl.class)
@TestPropertySource(properties = {
        "misc.forbidden-skin-name-parts=" + SkinSuitabilityCheckerImplTest.FORBIDDEN_NAME_PARTS,
        "business.bottom-price.min=" + SkinSuitabilityCheckerImplTest.BOTTOM_PRICE_MIN,
        "business.bottom-price.max=" + SkinSuitabilityCheckerImplTest.BOTTOM_PRICE_MAX
})
class SkinSuitabilityCheckerImplTest {

    static final String FORBIDDEN_NAME_PARTS = "skinpart1,skinpart2,skinpart3";
    static final int BOTTOM_PRICE_MIN = 10;
    static final int BOTTOM_PRICE_MAX = 100000;

    private static final String LEGAL_SKIN_NAME = "AK-47 | Elite Build (Well-Worn)";

    @Autowired
    private SkinSuitabilityChecker checker;

    @Test
    void shouldThrowRuntimeExceptionIfSkinNameIsNull() {
        Skin nullNameSkin = Skin.builder()
                .name(null)
                .build();
        assertThrows(RuntimeException.class, () -> checker.isSkinSuit(nullNameSkin));
    }

    @ParameterizedTest
    @MethodSource("invalidSkins")
    void shouldReturnFalseWithInvalidSkins(Skin skin) {
        assertFalse(checker.isSkinSuit(skin));
    }

    @ParameterizedTest
    @MethodSource("validSkins")
    void shouldReturnFalseWithValidSkins(Skin skin) {
        assertTrue(checker.isSkinSuit(skin));
    }

    static Stream<Skin> invalidSkins() {
        String[] forbiddenParts = FORBIDDEN_NAME_PARTS.split(",");
        assertTrue(forbiddenParts.length >= 3);

        return Stream.of(
                Skin.builder().usdDirtyPriceX100(BOTTOM_PRICE_MIN - 1).name(LEGAL_SKIN_NAME).build(),
                Skin.builder().usdDirtyPriceX100(BOTTOM_PRICE_MIN - 100).name(LEGAL_SKIN_NAME).build(),
                Skin.builder().usdDirtyPriceX100(BOTTOM_PRICE_MIN - 1000).name(LEGAL_SKIN_NAME).build(),

                Skin.builder().usdDirtyPriceX100(BOTTOM_PRICE_MAX + 1).name(LEGAL_SKIN_NAME).build(),
                Skin.builder().usdDirtyPriceX100(BOTTOM_PRICE_MAX + 100).name(LEGAL_SKIN_NAME).build(),
                Skin.builder().usdDirtyPriceX100(BOTTOM_PRICE_MAX + 1000).name(LEGAL_SKIN_NAME).build(),

                Skin.builder().name(forbiddenParts[0] + "any").build(),
                Skin.builder().name(forbiddenParts[1]).build(),
                Skin.builder().name("any" + forbiddenParts[2]).build(),

                Skin.builder().name("Name (Field-TesteeeEEEEd)").build(),
                Skin.builder().name("ILLEGAL_VALUE1").build(),
                Skin.builder().name("ILLEGAL_VALUE2").build()
        );
    }

    static Stream<Skin> validSkins() {
        String[] forbiddenParts = FORBIDDEN_NAME_PARTS.split(",");
        assertTrue(forbiddenParts.length >= 3);

        return Stream.of(
                Skin.builder()
                        .usdDirtyPriceX100(BOTTOM_PRICE_MIN + 1)
                        .name(LEGAL_SKIN_NAME)
                        .build(),
                Skin.builder()
                        .usdDirtyPriceX100(BOTTOM_PRICE_MIN + 100)
                        .name("Name | Name name name (Battle-Scarred)")
                        .build(),
                Skin.builder()
                        .usdDirtyPriceX100(BOTTOM_PRICE_MIN + 1000)
                        .name("Any | LEGALVALUE (Well-Worn)")
                        .build(),

                Skin.builder()
                        .usdDirtyPriceX100(BOTTOM_PRICE_MAX - 1)
                        .name("Gun | Name (Field-Tested)")
                        .build(),
                Skin.builder()
                        .usdDirtyPriceX100(BOTTOM_PRICE_MAX - 100)
                        .name("Name | Name name name (Battle-Scarred)")
                        .build(),
                Skin.builder()
                        .usdDirtyPriceX100(BOTTOM_PRICE_MAX - 1000)
                        .name("Any | LEGALVALUE (Well-Worn)")
                        .build()
        );
    }

}