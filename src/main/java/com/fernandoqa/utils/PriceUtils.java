package com.fernandoqa.utils;

import java.math.BigDecimal;

public final class PriceUtils {
//dhsicuh
    private PriceUtils() {
    }

    public static BigDecimal parsePrice(String priceText) {
        String normalized = priceText
                .replace("$", "")
                .replace('\u00A0', ' ')
                .trim();

        return new BigDecimal(normalized);
    }
}
