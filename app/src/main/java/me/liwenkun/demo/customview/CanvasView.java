package me.liwenkun.demo.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RecordingCanvas;
import android.graphics.Rect;
import android.graphics.RenderNode;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import me.liwenkun.demo.libannotation.Source;

public class CanvasView extends View {

    Paint paint;
    RenderNode renderNode;
    Rect rect = new Rect();

    @Source
    public static String sourceCode;

    public CanvasView(Context context) {
        super(context);
    }

    public CanvasView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            renderNode = new RenderNode("d");
            renderNode.setPosition(0, 0, 100, 100);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.RED);
        paint.setColor(Color.BLUE);
        canvas.drawCircle(getWidth() / 2f, getHeight() / 2f, getWidth() / 2f, paint);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            rect.set(0, 0, 50, 50);
            renderNode.setClipRect(rect);
            int count = canvas.save();
            RecordingCanvas recordingCanvas = renderNode.beginRecording();
            recordingCanvas.drawColor(Color.YELLOW);
            renderNode.endRecording();
            canvas.drawRenderNode(renderNode);
            canvas.restoreToCount(count);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }
}
