package me.liwenkun.demo.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ClipLayout extends FrameLayout {

   private float clip;

   private final Path path;

   public ClipLayout(@NonNull Context context) {
      this(context, null);
   }

   public ClipLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
      this(context, attrs, 0);
   }

   public ClipLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
      this(context, attrs, defStyleAttr, 0);
   }

   public ClipLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
      super(context, attrs, defStyleAttr, defStyleRes);
      path = new Path();
   }

   void setClip(float clip) {
      this.clip = clip;
      invalidate();
   }

   @Override
   protected void dispatchDraw(Canvas canvas) {
      path.reset();
      float x = clip * getWidth();
      path.moveTo(0, 0);
      path.lineTo(x, 0);
      path.quadTo(x + getHeight(), getHeight() / 2f, x, getHeight());
      path.lineTo(0, getHeight());
      path.lineTo(0, 0);
      int count = canvas.save();
      canvas.clipPath(path);
      super.dispatchDraw(canvas);
      canvas.restoreToCount(count);
   }
}
