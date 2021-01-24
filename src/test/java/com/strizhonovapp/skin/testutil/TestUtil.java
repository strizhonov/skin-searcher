package com.strizhonovapp.skin.testutil;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class TestUtil {

    public static InputStream getJsonStreamFromResourceName(String resourceName) throws IOException {
        Path jsonPath = Paths.get("src", "test", "resources", resourceName);
        String json = Files.lines(jsonPath, StandardCharsets.UTF_8)
                .collect(Collectors.joining());
        return new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
    }

}
