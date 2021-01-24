package com.strizhonovapp.skin.service.impl;

import com.strizhonovapp.skin.dto.ListingsRequest;
import com.strizhonovapp.skin.model.Skin;
import com.strizhonovapp.skin.service.UrlGenerator;
import com.strizhonovapp.skin.testutil.SpringBootComponentTests;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootComponentTests
class UrlGeneratorImplTest {

    @Autowired
    private UrlGenerator urlGenerator;

    @Test
    void shouldCheckHexaUrlForNull() {
        assertThat(urlGenerator.getUrlForAllSkinsFromHexa(), CoreMatchers.not(emptyOrNullString()));
    }

    @Test
    void shouldThrowRuntimeExceptionWhenCreatingListingUrlFromNullNamedSkin() {
        ListingsRequest request = getListingRequest(null);
        assertThrows(RuntimeException.class, () -> urlGenerator.getUrlForListingFromSteam(request));
    }

    @ParameterizedTest
    @ValueSource(strings = {"11", "22123423", "Third", "FFFF%ourth"})
    void shouldCheckListingUrlCreatingOnNull(String skinName) {
        ListingsRequest request = getListingRequest(skinName);
        assertThat(urlGenerator.getUrlForListingFromSteam(request), CoreMatchers.not(emptyOrNullString()));
    }

    @ParameterizedTest
    @ValueSource(strings = {"11", "22123423", "Third", "FFFF%ourth"})
    void shouldCheckSkinFromSteamUrlForNull(String skinName) {
        assertThat(urlGenerator.getUrlForSkinInfoFromSteam(skinName), CoreMatchers.not(emptyOrNullString()));
    }

    @ParameterizedTest
    @ValueSource(strings = {"11", "22123423", "Third", "FFFF%ourth"})
    void shouldCheckStickerFromSteamUrlForNull(String stickerName) {
        assertThat(urlGenerator.getUrlForStickerInfoFromSteam(stickerName), CoreMatchers.not(emptyOrNullString()));
    }

    private ListingsRequest getListingRequest(String skinName) {
        Skin skin = Skin.builder()
                .name(skinName)
                .build();
        return ListingsRequest.builder()
                .skin(skin)
                .build();
    }

}