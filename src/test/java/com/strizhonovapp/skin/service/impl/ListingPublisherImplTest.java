package com.strizhonovapp.skin.service.impl;

import com.strizhonovapp.skin.model.SkinListing;
import com.strizhonovapp.skin.service.ListingPublisher;
import com.strizhonovapp.skin.testutil.SpringBeanComponentTests;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBeanComponentTests(classes = ListingPublisherImpl.class)
@TestPropertySource(properties = {
        "misc.date-time-pattern=yyyy-MM-dd HH:mm:ss",
        "misc.result-path=" + ListingPublisherImplTest.RESULT_PATH
})
public class ListingPublisherImplTest {

    static final String RESULT_PATH = "test.txt";

    @Autowired
    private ListingPublisher listingPublisher;

    @Test
    void shouldWriteFileWithListingData() throws IOException {
        SkinListing listing = SkinListing.builder()
                .skinName("NAme")
                .listingUsdPriceX100(100)
                .build();
        listingPublisher.publish(listing);

        Path path = Paths.get(RESULT_PATH);
        byte[] written = Files.readAllBytes(path);

        assertTrue(new String(written, Charset.defaultCharset()).contains(listing.toString()));
        assertTrue(Files.deleteIfExists(path));
    }

}

