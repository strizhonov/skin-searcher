package com.strizhonovapp.skin.jsonhelper;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.strizhonovapp.skin.dto.InitialMarketInfo;
import com.strizhonovapp.skin.model.Skin;
import com.strizhonovapp.skin.model.Sticker;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

@Component
public class InitialMarketInfoDeserializer extends JsonDeserializer<InitialMarketInfo> {

    public static final String STICKER_NAME_PART = "Sticker | ";

    @Override
    public InitialMarketInfo deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException {
        InitialMarketInfo parsed = new InitialMarketInfo();
        Iterator<Map.Entry<String, JsonNode>> iter = getFieldsIterator(parser);
        while (iter.hasNext()) {
            Map.Entry<String, JsonNode> entry = iter.next();
            processEntry(parsed, entry);
        }
        return parsed;
    }

    private Iterator<Map.Entry<String, JsonNode>> getFieldsIterator(JsonParser parser) throws IOException {
        return parser.getCodec().<JsonNode>readTree(parser)
                .get("result")
                .get("listings")
                .fields();
    }

    private void processEntry(InitialMarketInfo parsed, Map.Entry<String, JsonNode> entry) {
        if (isSticker(entry)) {
            Sticker sticker = parseSticker(entry);
            parsed.getDirtilyPricedStickers().add(sticker);
        } else {
            Skin parseSkin = parseSkin(entry);
            parsed.getDirtilyPricedSkins().add(parseSkin);
        }
    }

    private boolean isSticker(Map.Entry<String, JsonNode> entry) {
        return entry.getKey().contains(STICKER_NAME_PART);
    }

    private Skin parseSkin(Map.Entry<String, JsonNode> entry) {
        int usdDirtyPriceX100 = getPriceFromEntry(entry);
        String name = getNameFromEntry(entry);
        return Skin.builder()
                .name(name)
                .usdDirtyPriceX100(usdDirtyPriceX100)
                .build();
    }

    private Sticker parseSticker(Map.Entry<String, JsonNode> entry) {
        int usdDirtyPriceX100 = getPriceFromEntry(entry);
        String name = getNameOfSticker(entry);
        return Sticker.builder()
                .name(name)
                .usdDirtyPriceX100(usdDirtyPriceX100)
                .build();
    }

    private String getNameOfSticker(Map.Entry<String, JsonNode> entry) {
        return getNameFromEntry(entry)
                .replace(STICKER_NAME_PART, "");
    }

    private String getNameFromEntry(Map.Entry<String, JsonNode> entry) {
        return entry.getKey();
    }

    private int getPriceFromEntry(Map.Entry<String, JsonNode> entry) {
        JsonNode node = entry.getValue();
        JsonNode price = node.findValue("price");
        return getIntPriceX100(price);
    }

    private int getIntPriceX100(JsonNode price) {
        return (int) (price.asDouble() * 100);
    }

}
