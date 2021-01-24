package com.strizhonovapp.skin.jsonhelper;

import com.strizhonovapp.skin.model.AppliedSticker;

import java.util.List;

public interface StickerMapper {

    List<AppliedSticker> map(List<String> stickerNames);

}
