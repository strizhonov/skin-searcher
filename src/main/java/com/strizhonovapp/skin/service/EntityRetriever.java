package com.strizhonovapp.skin.service;

import com.strizhonovapp.skin.dto.InitialMarketInfo;
import com.strizhonovapp.skin.dto.ListingsRequest;
import com.strizhonovapp.skin.model.SkinListing;

import java.util.Set;

public interface EntityRetriever {

    InitialMarketInfo getInitialInfo();

    Set<SkinListing> findListingBatch(ListingsRequest request);

}
