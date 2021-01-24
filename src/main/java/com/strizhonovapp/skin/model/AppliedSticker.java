package com.strizhonovapp.skin.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class AppliedSticker {

    private String name;
    private Integer usdPriceX100;

    public AppliedSticker(String name) {
        this.name = name;
    }

}
