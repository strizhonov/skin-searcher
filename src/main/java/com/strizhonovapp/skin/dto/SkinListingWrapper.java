package com.strizhonovapp.skin.dto;

import com.strizhonovapp.skin.model.SkinListing;
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
public class SkinListingWrapper {

    private Set<SkinListing> skinListings = new HashSet<>();

}
