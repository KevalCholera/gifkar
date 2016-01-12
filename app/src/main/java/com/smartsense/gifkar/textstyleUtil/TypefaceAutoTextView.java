package com.smartsense.gifkar.textstyleUtil;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

/**
 * A subclass of {@link TextView} which has a custom {@link TextStyle} applied.
 */
public class TypefaceAutoTextView extends AutoCompleteTextView {

    public TypefaceAutoTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypefaceManager.getInstance().applyTypeface(this, context, attrs);
    }

    /**
     * Convenience method in case I need to change the font from code as well.
     * @param textStyle
     */
    public void setTextStyle(TextStyle textStyle) {
        TypefaceManager.getInstance().applyTypeface(this, textStyle);
    }
}
