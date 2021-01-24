package com.strizhonovapp.skin.jsonhelper;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.strizhonovapp.skin.dto.SkinListingWrapper;
import com.strizhonovapp.skin.model.AppliedSticker;
import com.strizhonovapp.skin.model.SkinListing;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class ListingsBatchDeserializer extends JsonDeserializer<SkinListingWrapper> {

    @Value("${misc.steam-fee-percentage}")
    private double feePercentage;

    private final StickerMapper stickerMapper;
    private final StickerNamesRetriever stickerNamesRetriever;

    @Override
    public SkinListingWrapper deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException {
        JsonNode root = parser.getCodec().readTree(parser);
        checkListingsVolume(root);
        Set<SkinListing> skinListings = getSkinListings(root);
        return new SkinListingWrapper(skinListings);
    }

    private void checkListingsVolume(JsonNode root) {
        int start = root.get("start").asInt();
        int totalCount = root.get("total_count").asInt();
        if (start > totalCount) {
            throw new InvalidSkinVolumeException(start, totalCount);
        }
    }

    private Set<SkinListing> getSkinListings(JsonNode root) {
        Set<SkinListing> skinListings = new HashSet<>();
        root.get("listinginfo")
                .fields()
                .forEachRemaining(entry -> findListing(entry, root).ifPresent(skinListings::add));
        return skinListings;
    }

    private Optional<SkinListing> findListing(Map.Entry<String, JsonNode> entry, JsonNode root) {
        JsonNode listingNode = entry.getValue();
        if (doesNotContainListingInfo(listingNode)) {
            return Optional.empty();
        }
        JsonNode assetNode = root.get("assets").get("730").get("2");

        List<AppliedSticker> stickers = getStickers(listingNode, assetNode);
        String nameTag = getNameTag(listingNode, assetNode);
        int price = getPrice(listingNode);
        String link = getLink(listingNode);
        String name = getName(root);

        SkinListing result = SkinListing.builder()
                .appliedStickers(stickers)
                .nameTag(nameTag)
                .listingUsdPriceX100(price)
                .skinName(name)
                .steamLink(link)
                .build();
        return Optional.of(result);
    }

    private boolean doesNotContainListingInfo(JsonNode listingNode) {
        return listingNode.findValue("converted_price") == null;
    }

    private int getPrice(JsonNode listingNode) {
        int priceWithoutFee = listingNode.findValue("converted_price").asInt();
        double price = priceWithoutFee / (1 - feePercentage / 100d);
        return (int) Math.round(price);
    }

    private String getLink(JsonNode listingNode) {
        String listingId = listingNode.get("listingid").asText();
        String assetId = listingNode.get("asset").get("id").asText();
        String link = listingNode.get("asset")
                .get("market_actions")
                .get(0)
                .get("link").asText();

        return link.replace("%listingid%", listingId)
                .replace("%assetid%", assetId)
                .replace("%20", " ");
    }

    private List<AppliedSticker> getStickers(JsonNode listingNode, JsonNode assetNode) {
        String listingId = listingNode.get("asset").get("id").asText();
        JsonNode descriptionsNode = assetNode.get(listingId).findValue("descriptions");
        for (JsonNode descriptionNode : descriptionsNode) {
            Optional<List<String>> names = findStickerNames(descriptionNode);
            if (names.isPresent()) {
                return stickerMapper.map(names.get());
            }
        }
        return new ArrayList<>();
    }

    private String getNameTag(JsonNode listingNode, JsonNode assetNode) {
        String listingId = listingNode.get("asset").get("id").asText();
        JsonNode nameTagNode = assetNode.get(listingId).findValue("fraudwarnings");
        return Optional.ofNullable(nameTagNode)
                .map(node -> node.get(0))
                .map(JsonNode::asText)
                .map(value -> value.split("Name Tag: ")[1])
                .map(value -> value.split("''")[1])
                .orElse(null);
    }

    private String getName(JsonNode root) {
        return root.findValue("market_hash_name").asText();
    }

    private Optional<List<String>> findStickerNames(JsonNode descriptionNode) {
        JsonNode valueNode = descriptionNode.findValue("value");
        if (valueNode == null) {
            return Optional.empty();
        }
        String value = valueNode.asText();
        return stickerNamesRetriever.findStickerNames(value);
    }

}
