package com.like.widget;

import android.content.res.ColorStateList;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

@SuppressWarnings("unused")
public abstract class Tag {

    private Rect mRect = new Rect();
    protected boolean selected = false;

    public abstract String getText();

    public abstract int getTextSize();

    public abstract int getPadding();

    public abstract Drawable getBackground();

    public abstract ColorStateList getTextColor();

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void toggleSelected() {
        this.selected = !this.selected;
    }

    public final Rect getRect() {
        return mRect;
    }

    public final void setRect(Rect rect) {
        mRect = rect;
    }

}
