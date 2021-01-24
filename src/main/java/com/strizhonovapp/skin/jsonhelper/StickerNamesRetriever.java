package com.strizhonovapp.skin.jsonhelper;

import java.util.List;
import java.util.Optional;

public interface StickerNamesRetriever {

    Optional<List<String>> findStickerNames(String nodeValue);

}
