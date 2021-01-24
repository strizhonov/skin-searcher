package com.strizhonovapp.skin.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class Sticker extends MarketItem {

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private String name;
        private int volume;
        private int usdMedianPriceX100;
        private int usdLowestPriceX100;
        private int usdDirtyPriceX100;

        private Builder() {
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder volume(int volume) {
            this.volume = volume;
            return this;
        }

        public Builder usdMedianPriceX100(int usdMedianPriceX100) {
            this.usdMedianPriceX100 = usdMedianPriceX100;
            return this;
        }

        public Builder usdLowestPriceX100(int usdLowestPriceX100) {
            this.usdLowestPriceX100 = usdLowestPriceX100;
            return this;
        }

        public Builder usdDirtyPriceX100(int usdDirtyPriceX100) {
            this.usdDirtyPriceX100 = usdDirtyPriceX100;
            return this;
        }

        public Sticker build() {
            Sticker sticker = new Sticker();
            sticker.setName(name);
            sticker.setVolume(volume);
            sticker.setUsdMedianPriceX100(usdMedianPriceX100);
            sticker.setUsdLowestPriceX100(usdLowestPriceX100);
            sticker.setUsdDirtyPriceX100(usdDirtyPriceX100);
            return sticker;
        }
    }
}
