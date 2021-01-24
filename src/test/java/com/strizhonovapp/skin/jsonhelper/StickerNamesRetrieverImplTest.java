package com.strizhonovapp.skin.jsonhelper;

import com.strizhonovapp.skin.testutil.SpringBeanComponentTests;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBeanComponentTests(classes = StickerNamesRetrieverImpl.class)
@TestPropertySource(properties = "misc.sticker-names-with-comma=WITHCOMMA,STICKER;ANOTH,withCOmma")
class StickerNamesRetrieverImplTest {

    @Autowired
    private StickerNamesRetriever retriever;

    @ParameterizedTest
    @CsvSource(delimiter = '@', value = {
            "1   @   <br>Sticker: Terrorist-Tech</center>",
            "2   @   <br><div id=\"sticker_info\" name=\"sticker_info\" title=\"Sticker\" style=\"border: 2px solid " +
                    "rgb(102, 102, 102); border-radius: 6px; width=100; margin:4px; padding:8px;\"><center><img " +
                    "width=64 height=48><img height=48><br>Sticker: Terrorist-Tech, Terrorist-Tech</center></div>",
            "3   @   <br>Sticker: TEstSticker, Terrorist-Tech, And--Again</center>",
            "5   @   <br>Sticker: Ane, Test, Sticker, Again, Terrorist-Tech</center>",
    })
    void shouldParseValidStringsToStickerNames(int stickersCount, String withStickerString) {
        List<String> stickerNames = retriever.findStickerNames(withStickerString).orElseThrow();
        assertEquals(stickersCount, stickerNames.size());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "<br>Sticker: WITHCOMMA,STICKER,Terrorist-Tech</center>",
            "<br><div id=\"sticker_info\" name=\"sticker_info\" title=\"Sticker\" style=\"border: 2px solid " +
                    "rgb(102, 102, 102); border-radius: 6px; width=100; margin:4px; padding:8px;\"><center><img " +
                    "width=64 height=48><img height=48><br>Sticker: Terrorist-Tech, ANOTH,withCOmma</center></div>",
            "<br>Sticker: WITHCOMMA,STICKER, ANOTH,withCOmma</center>",
            "<br>Sticker: ANOTH,withCOmma,WITHCOMMA,STICKER</center>",
    })
    void shouldParseNamesWithCommaProperly(String withCommaStickerName) {
        List<String> stickerNames = retriever.findStickerNames(withCommaStickerName).orElseThrow();
        assertEquals(2, stickerNames.size());
    }

}