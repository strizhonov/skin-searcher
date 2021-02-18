package com.strizhonovapp.skin.service.impl;

import com.strizhonovapp.skin.model.SkinListing;
import com.strizhonovapp.skin.service.ListingPublisher;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class ListingPublisherImpl implements ListingPublisher {

    @Value("${misc.date-time-pattern}")
    private String dateTimePattern;

    @Value("${misc.result-path}")
    private String resultsPath;

    @SneakyThrows
    @Override
    public void publish(SkinListing listing) {
        byte[] bytes = getDataToWrite(listing);
        Path path = Paths.get(resultsPath);
        createIfNecessary(path);
        Files.write(path, bytes, StandardOpenOption.APPEND);
    }

    private byte[] getDataToWrite(SkinListing listing) {
        String result = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern(dateTimePattern))
                + "\n"
                + listing.toString()
                + "\n"
                + "------------------------------------------------------------"
                + "\n";
        return result.getBytes(Charset.defaultCharset());
    }

    private void createIfNecessary(Path path) throws IOException {
        if (Files.exists(path)) {
            return;
        }
        Path parent = path.getParent();
        if (parent != null && Files.notExists(parent)) {
            Files.createDirectories(parent);
        }
        Files.createFile(path);
    }

}
