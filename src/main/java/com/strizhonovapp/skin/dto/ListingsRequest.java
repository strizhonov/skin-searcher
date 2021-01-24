package com.strizhonovapp.skin.dto;

import com.strizhonovapp.skin.model.Skin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ListingsRequest {

    @NonNull
    private Skin skin;
    private int start;
    private int count;

}
