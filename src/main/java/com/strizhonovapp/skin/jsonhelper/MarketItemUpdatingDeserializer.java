package com.strizhonovapp.skin.jsonhelper;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.strizhonovapp.skin.model.MarketItem;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.ParseException;

@Component
public class MarketItemUpdatingDeserializer<T extends MarketItem> extends JsonDeserializer<T> {

    @SneakyThrows
    @Override
    public T deserialize(JsonParser parser, DeserializationContext ctxt, T toUpdate) {
        JsonNode node = parser.getCodec().readTree(parser);
        processMedianPrice(toUpdate, node);
        processLowestPrice(toUpdate, node);
        processVolume(toUpdate, node);
        return toUpdate;
    }

    @Override
    public T deserialize(JsonParser parser, DeserializationContext ctxt) {
        throw new UnsupportedOperationException("Market item is abstract so can't be instantiated in deserializer.");
    }

    private void processMedianPrice(T toUpdate, JsonNode node)
            throws ParseException {
        JsonNode medianPriceNode = node.get("median_price");
        if (medianPriceNode != null) {
            int medianPrice = getUsdPriceX100(medianPriceNode);
            toUpdate.setUsdMedianPriceX100(medianPrice);
        }
    }

    private void processLowestPrice(T toUpdate, JsonNode node)
            throws ParseException {
        JsonNode lowestPriceNode = node.get("lowest_price");
        if (lowestPriceNode != null) {
            int lowestPrice = getUsdPriceX100(lowestPriceNode);
            toUpdate.setUsdLowestPriceX100(lowestPrice);
        }
    }

    private void processVolume(T toUpdate, JsonNode node)
            throws ParseException {
        JsonNode volumeNode = node.get("volume");
        if (volumeNode != null) {
            int volume = getVolume(volumeNode);
            toUpdate.setVolume(volume);
        }
    }

    private int getVolume(JsonNode volumeNode) throws ParseException {
        return (int) parse(volumeNode.asText());
    }

    private int getUsdPriceX100(JsonNode priceNode) throws ParseException {
        double usdPrice = getUsdPrice(priceNode);
        return (int) Math.round(usdPrice * 100d);
    }

    private double getUsdPrice(JsonNode priceNode) throws ParseException {
        String stringPrice = getStringPrice(priceNode);
        return parse(stringPrice);
    }

    private String getStringPrice(JsonNode priceNode) {
        String priceWithCurrencySign = priceNode.asText();
        return trimCurrencySign(priceWithCurrencySign);
    }

    private double parse(String toParse) throws ParseException {
        String doubleWithoutCommas = toParse.replace(",", "");
        return new BigDecimal(doubleWithoutCommas).doubleValue();
    }

    private String trimCurrencySign(String lowestPriceWithCurrencySign) {
        return lowestPriceWithCurrencySign.substring(1);
    }

}
