package com.strizhonovapp.skin.service;

import com.strizhonovapp.skin.model.Skin;

import java.util.Map;
import java.util.Set;

public interface SkinsPreparer {

    Map<String, Skin> prepare(Set<Skin> skins);

}
