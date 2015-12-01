package com.smartsense.gifkar.textstyleutil;

/**
 * Implementation of {@TextStyleExtractor} for the Roboto font.
 */
public class CooperHewittExtractor extends TextStyleExtractor {

    private static final CooperHewittExtractor INSTANCE = new CooperHewittExtractor();

    public static TextStyleExtractor getInstance() {
        return INSTANCE;
    }

    @Override
    public TextStyle[] getTextStyles() {
        return CooperHewittTextStyle.values();
    }
}
