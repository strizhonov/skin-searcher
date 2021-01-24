package com.strizhonovapp.skin.jsonhelper;

import com.strizhonovapp.skin.model.AppliedSticker;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class StickerMapperImpl implements StickerMapper {

    @Override
    public List<AppliedSticker> map(List<String> stickerNames) {
        if (stickerNames == null) {
            return null;
        }
        return stickerNames.stream()
                .map(AppliedSticker::new)
                .collect(Collectors.toList());
    }

}
