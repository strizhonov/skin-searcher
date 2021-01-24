package com.strizhonovapp.skin.dto;

import com.strizhonovapp.skin.model.Skin;
import com.strizhonovapp.skin.model.Sticker;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class InitialMarketInfo {

    private Set<Skin> dirtilyPricedSkins = new HashSet<>();
    private Set<Sticker> dirtilyPricedStickers = new HashSet<>();

}
