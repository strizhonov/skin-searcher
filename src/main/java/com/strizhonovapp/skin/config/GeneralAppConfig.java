package com.strizhonovapp.skin.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.strizhonovapp.skin.dto.InitialMarketInfo;
import com.strizhonovapp.skin.dto.SkinListingWrapper;
import com.strizhonovapp.skin.jsonhelper.InitialMarketInfoDeserializer;
import com.strizhonovapp.skin.jsonhelper.ListingsBatchDeserializer;
import com.strizhonovapp.skin.jsonhelper.MarketItemUpdatingDeserializer;
import com.strizhonovapp.skin.model.MarketItem;
import com.strizhonovapp.skin.model.Skin;
import com.strizhonovapp.skin.model.Sticker;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class GeneralAppConfig {

    private final MarketItemUpdatingDeserializer<MarketItem> marketItemUpdatingDeserializer;
    private final MarketItemUpdatingDeserializer<Skin> skinDeserializer;
    private final MarketItemUpdatingDeserializer<Sticker> stickerDeserializer;
    private final ListingsBatchDeserializer listingsBatchDeserializer;
    private final InitialMarketInfoDeserializer initialMarketInfoDeserializer;
    private final ListingsBatchDeserializer listingBatchDeserializer;

    @Bean
    RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        StringHttpMessageConverter utf8Converter = new StringHttpMessageConverter(StandardCharsets.UTF_8);
        restTemplate.getMessageConverters().add(0, utf8Converter);
        return restTemplate;
    }

    @Bean
    ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        setDeserializers(mapper);
        return mapper;
    }

    @Bean
    @SneakyThrows
    Map<String, String>
    illegalUrlCharacters(@Value("${misc.illegal-url-characters-and-replacement}") String charactersAndReplacements) {
        TypeReference<Map<String, String>> mapType = new TypeReference<>() {
        };
        return new ObjectMapper().readValue(charactersAndReplacements, mapType);
    }

    private void setDeserializers(ObjectMapper mapper) {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(InitialMarketInfo.class, initialMarketInfoDeserializer);
        module.addDeserializer(SkinListingWrapper.class, listingBatchDeserializer);
        module.addDeserializer(MarketItem.class, marketItemUpdatingDeserializer);
        module.addDeserializer(Skin.class, skinDeserializer);
        module.addDeserializer(Sticker.class, stickerDeserializer);
        module.addDeserializer(SkinListingWrapper.class, listingsBatchDeserializer);
        mapper.registerModule(module);
    }

}
