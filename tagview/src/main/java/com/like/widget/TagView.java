package com.like.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
public class TagView extends View {

    private float density;

    private int mHorizontalSpacing;
    private int mVerticalSpacing;
    private ColorStateList mTextColor;
    private Drawable mTagBackground;
    private int mMaxLines;
    private int mTextSize;
    private List<Tag> mTags = new ArrayList<>();

    private int mPadding;
    private String mTagString;
    private Paint mPaint;
    private Rect mTextRect = new Rect();

    private int mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    private float mTouchX;
    private float mTouchY;
    private Tag mTouchTag;
    private int mBreakItem;

    public TagView(Context context) {
        this(context, null);
    }

    public TagView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TagView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        density = context.getResources().getDisplayMetrics().density;

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TagView);
        mHorizontalSpacing = typedArray.getDimensionPixelOffset(R.styleable.TagView_tv_horizontal_spacing, getPx(4));
        mVerticalSpacing = typedArray.getDimensionPixelOffset(R.styleable.TagView_tv_vertical_spacing, getPx(4));
        mMaxLines = typedArray.getInt(R.styleable.TagView_tv_max_lines, Integer.MAX_VALUE);
        mPadding = typedArray.getDimensionPixelOffset(R.styleable.TagView_tv_padding, getPx(4));

        mTextColor = typedArray.getColorStateList(R.styleable.TagView_tv_text_color);
        if (mTextColor == null) {
            mTextColor = ColorStateList.valueOf(Color.GRAY);
        }

        mTextSize = typedArray.getDimensionPixelOffset(R.styleable.TagView_tv_text_size, getPx(12));

        mTagBackground = typedArray.getDrawable(R.styleable.TagView_tv_tag_background);
        if (mTagBackground == null) {
            mTagBackground = new ColorDrawable(Color.TRANSPARENT);
        }

        mTagString = typedArray.getString(R.styleable.TagView_tv_tags);
        typedArray.recycle();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(mTextSize);
        updateTags();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int totalWidth = 0;
        int totalHeight = getPaddingTop();

        int lineWidth = getPaddingLeft();
        int lineHeight = 0;

        int lines = 0;
        for (int i = 0, childNum = mTags.size(); i < childNum; i++) {
            Tag tag = mTags.get(i);

            mPaint.setTextSize(tag.getTextSize());
            mPaint.getTextBounds(tag.getText(), 0, tag.getText().length(), mTextRect);
            //经过测试，两种获取文字宽度的方式中，这种画出来要好看很多
            int textWidth = (int) mPaint.measureText(tag.getText());
            int tagWidth = Math.min(mHorizontalSpacing + 2 * tag.getPadding() + textWidth, widthSize - getPaddingRight() - getPaddingLeft());

            Rect rect = tag.getRect();

            if ((lineWidth + tagWidth) <= widthSize - getPaddingRight()) {
                rect.top = totalHeight;
                if (lineWidth == getPaddingLeft()) {
                    rect.left = lineWidth;
                    lineWidth += tagWidth - mHorizontalSpacing;
                } else {
                    rect.left = lineWidth + mHorizontalSpacing;
                    lineWidth += tagWidth;
                }
                lineHeight = Math.max(lineHeight, mTextRect.height() + 2 * tag.getPadding());
            } else {
                lines++;
                if (lines >= mMaxLines) {
                    break;
                }
                totalHeight += lineHeight + mVerticalSpacing;
                rect.top = totalHeight;
                rect.left = getPaddingLeft();
                totalWidth = Math.max(lineWidth, totalWidth);
                lineWidth = getPaddingLeft() + tagWidth - mHorizontalSpacing;
                lineHeight = mTextRect.height() + 2 * tag.getPadding();
            }
            mBreakItem = i;
            rect.bottom = rect.top + mTextRect.height() + 2 * tag.getPadding();
            rect.right = Math.min(rect.left + 2 * tag.getPadding() + textWidth, widthSize - getPaddingRight());
        }
        totalWidth = Math.max(lineWidth, totalWidth);
        totalHeight += lineHeight;

        if (widthMode == MeasureSpec.EXACTLY) {
            totalWidth = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            totalWidth = Math.min(totalWidth + getPaddingRight(), widthSize);
        } else {
            totalWidth = totalHeight + getPaddingRight();
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            totalHeight = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            totalHeight = Math.min(totalHeight + getPaddingBottom(), heightSize);
        } else {
            totalHeight = totalWidth + getPaddingBottom();
        }
        setMeasuredDimension(totalWidth, totalHeight);
    }

    private int[] selected = new int[]{android.R.attr.state_selected};
    private int[] normal = new int[]{};

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0, childNum = mTags.size(); i < childNum; i++) {
            if (i > mBreakItem) break;
            Tag tag = mTags.get(i);
            Rect rect = tag.getRect();
            Drawable drawable = tag.getBackground();
            if (drawable == null) {
                drawable = mTagBackground;
            }
            drawable.setBounds(rect);
            if (drawable.isStateful()) {
                drawable.setState(tag.isSelected() ? selected : normal);
            }
            drawable.draw(canvas);
            ColorStateList colorStateList = tag.getTextColor();
            if (colorStateList == null) {
                colorStateList = mTextColor;
            }
            mPaint.setColor(colorStateList.getColorForState(tag.isSelected() ? selected : normal, colorStateList.getDefaultColor()));
            mPaint.setTextSize(tag.getTextSize());
            Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
            float top = fontMetrics.top;
            float bottom = fontMetrics.bottom;
            int baseLineY = (int) (rect.centerY() - top / 2 - bottom / 2);
            canvas.drawText(tag.getText(), rect.left + tag.getPadding(), baseLineY, mPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mTouchX = event.getX();
                mTouchY = event.getY();
                mTouchTag = getTagByPosition((int) mTouchX, (int) mTouchY);
                return true;

            case MotionEvent.ACTION_UP:
                float x = event.getX();
                float y = event.getY();
                //滑动距离小于点击阈值并且点击时的索引值不是非法值，并且up时的索引值和down时的索引值相等时，才触发选中操作
                if (Math.abs(x - mTouchX) < mTouchSlop && Math.abs(y - mTouchY) < mTouchSlop
                        && mTouchTag != null && getTagByPosition((int) x, (int) y) == mTouchTag) {
                    mTouchTag.toggleSelected();
                    invalidate();
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    private Tag getTagByPosition(int x, int y) {
        for (int i = 0; i < mTags.size(); i++) {
            if (mTags.get(i).getRect().contains(x, y)) {
                return mTags.get(i);
            }
        }
        return null;
    }

    private int getPx(int dp) {
        return (int) (dp * density + 0.5f);
    }

    private void updateTags() {
        mTags.clear();
        if (!TextUtils.isEmpty(mTagString)) {
            String tags[] = mTagString.split(",");
            for (String tag : tags) {
                newTag(tag).add();
            }
            requestLayout();
            invalidate();
        }
    }

    public void addTag(Tag newTag) {
        if (newTag == null) return;
        mTags.add(newTag);
        requestLayout();
        invalidate();
    }


    public void addTag(String newTagString) {
        if (newTagString == null) return;
        mTags.add(newTag(newTagString).build());
    }

    public void setTags(String newTagString) {
        if (newTagString == null) return;
        mTagString = newTagString;
        updateTags();
    }

    public void setTags(List<Tag> tags) {
        if (tags != null && mTags != tags) {
            mTags.clear();
            mTags.addAll(tags);
            requestLayout();
            invalidate();
        }
    }

    public void addTags(List<Tag> tags) {
        mTags.addAll(tags);
        requestLayout();
        invalidate();
    }

    public TagBuilder newTag() {
        return newTag("");
    }

    public TagBuilder newTag(String newTagString) {
        return new TagBuilder(this, newTagString, mTextColor, mTextSize, mPadding, mTagBackground);
    }

    public void removeAll() {
        mTags.clear();
        requestLayout();
    }

    public static class TagBuilder {
        private String text;
        private ColorStateList textColor;
        private int textSize;
        private int padding;
        private Drawable background;
        private boolean selected = false;

        private TagView instance;

        TagBuilder() {
        }

        TagBuilder(TagView instance, String text, ColorStateList textColor, int textSize, int padding, Drawable background) {
            this.instance = instance;
            this.text = text;
            this.textColor = textColor;
            this.textSize = textSize;
            this.padding = padding;
            this.background = background;
        }

        public BaseTag build() {
            return new BaseTag(text, textColor, textSize, padding, background, selected);
        }

        public void add() {
            instance.addTag(build());
        }

        public TagBuilder text(String text) {
            this.text = text;
            return this;
        }

        public TagBuilder textColor(int textColor) {
            this.textColor = ColorStateList.valueOf(textColor);
            return this;
        }

        public TagBuilder textColor(ColorStateList textColor) {
            this.textColor = textColor;
            return this;
        }

        public TagBuilder textSize(int textSize) {
            this.textSize = textSize;
            return this;
        }

        public TagBuilder padding(int padding) {
            this.padding = padding;
            return this;
        }

        public TagBuilder background(Drawable background) {
            this.background = background;
            return this;
        }

        public TagBuilder selected(boolean selected) {
            this.selected = selected;
            return this;
        }

    }

}
