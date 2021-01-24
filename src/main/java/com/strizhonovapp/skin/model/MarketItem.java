package com.strizhonovapp.skin.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class MarketItem {

    private String name;
    private Integer volume;
    private Integer usdMedianPriceX100;
    private Integer usdLowestPriceX100;
    private Integer usdDirtyPriceX100;

}
