package com.strizhonovapp.skin.service.impl;

import com.strizhonovapp.skin.model.AppliedSticker;
import com.strizhonovapp.skin.model.SkinListing;
import com.strizhonovapp.skin.service.ListingEvaluator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListingEvaluatorImpl implements ListingEvaluator {

    @Value("${business.max-listing-price}")
    private int maxListingPrice;

    @Value("${business.max-listing-overprice-coef}")
    private double maxListingOverprice;

    @Override
    public boolean isValuable(SkinListing listing) {
        return priceIsInAllowedBounds(listing)
                && hasEnoughUpgrades(listing)
                && isNotOverpriced(listing);
    }

    private boolean priceIsInAllowedBounds(SkinListing skinListing) {
        return skinListing.getListingUsdPriceX100() < maxListingPrice;
    }

    private boolean hasEnoughUpgrades(SkinListing skinListing) {
        List<AppliedSticker> appliedStickers = skinListing.getAppliedStickers();
        return appliedStickers != null
                && appliedStickers.size() == 4
                && skinListing.getNameTag() != null;
    }

    private boolean isNotOverpriced(SkinListing listing) {
        double listingUsdPriceX100 = listing.getListingUsdPriceX100();
        double skinUsdPriceX100 = listing.getSkinUsdPriceX100();

        return listingUsdPriceX100 / skinUsdPriceX100 <= maxListingOverprice;
    }

}
