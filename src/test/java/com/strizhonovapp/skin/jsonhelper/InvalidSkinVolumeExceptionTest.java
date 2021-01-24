package com.strizhonovapp.skin.jsonhelper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class InvalidSkinVolumeExceptionTest {

    @Test
    public void plainConstructorTest() {
        InvalidSkinVolumeException actualInvalidSkinVolumeException = new InvalidSkinVolumeException(1, 3);
        assertEquals(1, actualInvalidSkinVolumeException.getStart());
        assertEquals(3, actualInvalidSkinVolumeException.getTotalCount());
    }

}

