package com.strizhonovapp.skin.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SkinListing {

    private String skinName;
    private Integer skinUsdPriceX100;
    private Integer listingUsdPriceX100;
    private String steamLink;
    private String nameTag;
    private List<AppliedSticker> appliedStickers = new ArrayList<>();

    @Override
    public String toString() {
        return "Skin: " + skinName + ", " + skinUsdPriceX100 + ";\n"
                + "Actual price: " + listingUsdPriceX100 + ";\n"
                + "Stickers: " + evaluateStickersString()
                + (nameTag == null ? "" : "\n" + "Nametag: " + nameTag);
    }

    private String evaluateStickersString() {
        return appliedStickers == null || appliedStickers.isEmpty()
                ? "-"
                : createStickersString();
    }

    private String createStickersString() {
        String beforeSticker = "\n\t";
        String afterSticker = ";";
        String stickersString = appliedStickers.stream()
                .map(AppliedSticker::toString)
                .collect(Collectors.joining(afterSticker + beforeSticker));
        return beforeSticker + stickersString + afterSticker;
    }

}
