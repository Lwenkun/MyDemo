package me.liwenkun.demo.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

public class MyFrameLayout extends android.widget.FrameLayout {

   public MyFrameLayout(Context context, @Nullable AttributeSet attrs) {
      super(context, attrs);
   }

   public MyFrameLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
      super(context, attrs, defStyleAttr);
   }

   public MyFrameLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
      super(context, attrs, defStyleAttr, defStyleRes);
   }

   @Override
   protected void dispatchDraw(Canvas canvas) {
      super.dispatchDraw(canvas);
   }

   @Override
   protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
      super.onMeasure(widthMeasureSpec, heightMeasureSpec);
   }
}
