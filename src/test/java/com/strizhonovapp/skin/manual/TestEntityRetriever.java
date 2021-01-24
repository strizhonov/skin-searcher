package com.strizhonovapp.skin.manual;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.strizhonovapp.skin.dto.InitialMarketInfo;
import com.strizhonovapp.skin.service.impl.EntityRetrieverImpl;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class TestEntityRetriever extends EntityRetrieverImpl {

    @Autowired
    private ObjectMapper mapper;

    @Override
    @SneakyThrows
    public InitialMarketInfo getInitialInfo() {
        Path jsonPath = Paths.get("src", "test", "resources", "hexa-initial-items.json");
        String json = Files.lines(jsonPath, StandardCharsets.UTF_8)
                .collect(Collectors.joining());
        return mapper
                .readerFor(InitialMarketInfo.class)
                .readValue(json);
    }

}