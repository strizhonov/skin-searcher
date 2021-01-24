package com.strizhonovapp.skin.jsonhelper;

import com.strizhonovapp.skin.model.AppliedSticker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StickerMapperImplTest {

    private final StickerMapperImpl mapper = new StickerMapperImpl();

    @Test
    void shouldReturnNullOnNullPassing() {
        assertNull(mapper.map(null));
    }

    @ParameterizedTest
    @MethodSource("names")
    void shouldCreateListOfAppliedStickersWithSameNamesAndNothingMore(List<String> names) {
        List<AppliedSticker> result = mapper.map(names);
        assertEquals(names.size(), result.size());
        result.forEach(s -> assertTrue(names.contains(s.getName())));
    }

    private static Stream<List<String>> names() {
        return Stream.of(
                List.of("TEST_1_1", "TEST_1_2", "TEST_1_3"),
                List.of("TEST_2_1", "TEST_2_2", "TEST_2_3"),
                List.of("TEST_3_1", "TEST_3_2", "TEST_3_3"),
                List.of("TEST_4_1", "TEST_4_2", "TEST_4_3"),
                List.of("TEST_5_1", "TEST_5_2", "TEST_5_3"),
                List.of("TEST_6_1", "TEST_6_2", "TEST_6_3")
        );
    }

}