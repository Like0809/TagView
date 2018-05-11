package com.like.widget;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;

public class BaseTag extends Tag {

    private String text;
    private ColorStateList textColor;
    private int textSize;
    private int padding;
    private Drawable background;

    public BaseTag() {
    }

    public BaseTag(String text, ColorStateList textColor, int textSize, int padding, Drawable background, boolean selected) {
        this.text = text;
        this.textColor = textColor;
        this.textSize = textSize;
        this.padding = padding;
        this.background = background;
        this.setSelected(selected);
    }


    @Override
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ColorStateList getTextColor() {
        return textColor;
    }

    public void setTextColor(ColorStateList textColor) {
        this.textColor = textColor;
    }

    public void setTextColor(int color) {
        this.textColor = ColorStateList.valueOf(color);
    }

    @Override
    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    @Override
    public int getPadding() {
        return padding;
    }

    public void setPadding(int padding) {
        this.padding = padding;
    }

    @Override
    public Drawable getBackground() {
        return background;
    }

    public void setBackground(Drawable background) {
        this.background = background;
    }
}
