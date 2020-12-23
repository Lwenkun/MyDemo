package me.liwenkun.demo.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import me.liwenkun.demo.R;

public class Indicator extends View {

    private int index;
    private float offset;
    private int requiredWidth;

    private final int radius;
    private final int margin;
    private int focusLength;

    private static final int DEF_RADIUS = 5;
    private static final int DEF_MARGIN = 15;
    private static final int DEF_FOCUS_LENGTH = 70;
    private static final int DEF_FOCUSED_DOT_COLOR = Color.GRAY;
    private static final int DEF_UNFOCUSED_DOT_COLOR = Color.LTGRAY;

    private final Paint focusedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint unFocusedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private int num;

    public Indicator(Context context) {
        this(context, null);
    }

    public Indicator(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Indicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Indicator, defStyleAttr, 0);
        radius = typedArray.getDimensionPixelSize(R.styleable.Indicator_dotRadius, DEF_RADIUS);
        margin = typedArray.getDimensionPixelSize(R.styleable.Indicator_dotMargin, DEF_MARGIN);
        focusLength = typedArray.getDimensionPixelSize(R.styleable.Indicator_focusedDotLength, DEF_FOCUS_LENGTH);
        focusLength = Math.max(radius * 2, focusLength);

        int focusedDotColor = typedArray.getColor(R.styleable.Indicator_focusedDotColor, DEF_FOCUSED_DOT_COLOR);
        int unfocusedDotColor = typedArray.getColor(R.styleable.Indicator_unfocusedDotColor, DEF_UNFOCUSED_DOT_COLOR);

        focusedPaint.setColor(focusedDotColor);
        focusedPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        unFocusedPaint.setColor(unfocusedDotColor);
        unFocusedPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        typedArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        // 焦点指示器超出的长度
        int deltaLength = focusLength - radius * 2;

        int startX = (getWidth() - requiredWidth) / 2;

        for (int i = 0; i < num; i++) {

            boolean focus = false;
            float l, t, r, b;
            int straightLength = 0;

            if (i == index) {
                straightLength = (int) (deltaLength * (1 - offset));
                focus = Math.abs(offset) < 0.5;
            }

            if (i == index +1) {
                straightLength = (int) (deltaLength * offset);
                focus = Math.abs(offset) >= 0.5;
            }

            l = startX;
            t = 0;
            r = l + radius * 2 + straightLength;
            b = radius * 2;

            canvas.drawRoundRect(l, t, r, b, radius, radius, focus ? focusedPaint : unFocusedPaint);
            startX = (int) (r + margin);
        }
    }

    private void setCurrent(int index, float offset) {
        this.index = index;
        this.offset = offset;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int finalW;
        int finalH;
        switch (widthMode) {
            case MeasureSpec.AT_MOST:
                finalW = Math.min(width, requiredWidth);
                break;
            case MeasureSpec.EXACTLY:
                finalW = width;
                break;
            case MeasureSpec.UNSPECIFIED:
            default:
                finalW = requiredWidth;
        }

        switch (heightMode) {
            case MeasureSpec.AT_MOST:
                finalH = Math.min(height, radius * 2);
                break;
            case MeasureSpec.EXACTLY:
                finalH = height;
                break;
            default:
            case MeasureSpec.UNSPECIFIED:
                finalH = radius * 2;
                break;
        }

        setMeasuredDimension(finalW, finalH);
    }

    public void setUpWithPager(ViewPager viewPager) {
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                setCurrent(position, positionOffset);
            }
        });

        if (viewPager.getAdapter() != null) {
            update(viewPager.getAdapter());
        }

        viewPager.addOnAdapterChangeListener((viewPager1, oldAdapter, newAdapter) -> {
            update(newAdapter);
        });
    }

    private void update(PagerAdapter pagerAdapter) {
        num = pagerAdapter == null ? 0 : pagerAdapter.getCount();
        requiredWidth = num == 0 ? 0 : (num - 1) * radius * 2 + focusLength + (num - 1) * margin;
    }
}
