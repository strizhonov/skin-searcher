package com.strizhonovapp.skin.jsonhelper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
public class StickerNamesRetrieverImpl implements StickerNamesRetriever {

    @Value("#{'${misc.sticker-names-with-comma}'.split(';')}")
    private Set<String> withCommaStickerNames;

    @Override
    public Optional<List<String>> findStickerNames(String nodeValue) {
        if (nodeValue != null && nodeValue.contains("<br>Sticker: ")) {
            List<String> stickerNames = getStickerNames(nodeValue);
            return Optional.of(stickerNames);
        }
        return Optional.empty();
    }

    private List<String> getStickerNames(String value) {
        String stickersRawString = value
                .split("<br>Sticker: ")[1]
                .split("</center>")[0];
        return getStickerNamesFromRawString(stickersRawString);
    }

    private List<String> getStickerNamesFromRawString(String raw) {
        List<String> result = new ArrayList<>();
        StringBuilder rawBuilder = new StringBuilder(raw);
        moveWithCommaStickersFromRawStringToList(result, rawBuilder);
        moveAllLeftStickersFromRawStringToList(result, rawBuilder);
        return result;
    }

    private void moveWithCommaStickersFromRawStringToList(List<String> result, StringBuilder rawBuilder) {
        for (String name : withCommaStickerNames) {
            if (name == null || name.isEmpty()) {
                continue;
            }
            int nameIndex;
            while ((nameIndex = rawBuilder.indexOf(name)) > -1) {
                result.add(name);
                rawBuilder.delete(nameIndex, nameIndex + name.length());
            }
        }
    }

    private void moveAllLeftStickersFromRawStringToList(List<String> result, StringBuilder rawBuilder) {
        String[] stickerNamesCandidates = rawBuilder.toString().split(",");
        Arrays.stream(stickerNamesCandidates)
                .map(String::strip)
                .filter(s -> !s.isEmpty())
                .forEach(result::add);
    }

}
