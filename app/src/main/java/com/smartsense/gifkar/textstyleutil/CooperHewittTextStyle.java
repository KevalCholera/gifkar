package com.smartsense.gifkar.textstyleutil;

/**
 * Implementation of {@link TextStyle} defining the possible values for the 'textStyle' attribute
 * using the Roboto font.
 * Created by evelina on 16/01/2014.
 */
public enum CooperHewittTextStyle implements TextStyle {

    NORMAL("normal", "CooperHewitt-Book.ttf"),
    BOLD("bold", "CooperHewitt-Bold.ttf");

    private String mName;
    private String mFontName;

    CooperHewittTextStyle(String name, String fontName) {
        mName = name;
        mFontName = fontName;
    }

    @Override
    public String getFontName() {
        return mFontName;
    }

    @Override
    public String getName() {
        return mName;
    }
}
