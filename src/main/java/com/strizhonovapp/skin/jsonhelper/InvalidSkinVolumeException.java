package com.strizhonovapp.skin.jsonhelper;

import lombok.Getter;

@Getter
public class InvalidSkinVolumeException extends RuntimeException {

    private final int totalCount;
    private final int start;

    public InvalidSkinVolumeException(int start, int totalCount) {
        super("Invalid volume. Total count is " + totalCount + ", while start is " + start);
        this.totalCount = totalCount;
        this.start = start;
    }

}
