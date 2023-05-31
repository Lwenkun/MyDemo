package me.liwenkun.demo.customview;

import android.content.Context;
import android.graphics.Canvas
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RecordingCanvas;
import android.graphics.Rect;
import android.graphics.RenderNode;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import me.liwenkun.demo.demoframework.Logger;
import me.liwenkun.demo.libannotation.Source;

class CanvasView(context: Context, attributeSet: AttributeSet?):  View(context, attributeSet) {

    private var paint: Paint = Paint()
    private lateinit var renderNode: RenderNode
    private var rect: Rect = Rect()

    private var logger: Logger

    companion object {
        @Source
        @JvmField
        var sourceCode: String? = null
    }

    constructor(context: Context): this(context, null)

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            renderNode = RenderNode("d")
            renderNode.setPosition(0, 0, 200, 200)
        }
        logger = Logger.from(context)
        postDelayed({
                    invalidate(3, 3, 5, 6)
        }, 1000)
    }

    /**
     * 如果父 View 设置了 clipChildren = false，那么父 View 会调用所有子 View 的 mRenderNode 的 clipToBounds
     * 方法，告诉 RenderNode 将绘制范围限制在边界内，这个边界就是 setPosition 设置的值。
     * 等到绘制的时候，如果父 View 提供的画布支持硬件加速，子 View 就使用
     * RenderNode 进行绘制，因为 RenderNode 已经设置了 clipToBounds，所以不用针对父 View 设置的 clipChildren 属性
     * 做任何处理，RenderNode 会处理好这一切。但是如果父 View 的画布不支持硬件加速，那么：假如使用软件层或者硬件层
     * （此时硬件层也会被当做软件层）绘制，clipChildren 其实 true false 都无所谓，因为在新图层绘制的东西肯定是超不过边界的，
     * 但如果是 NONE，那么就需要画布自己做裁剪，就是调用 clipRect
     *
     * layerType 也是如此，如果使用 RenderNode 进行绘制，RenderNode 会自己处理好 layerType，
     * 处理 layerType 的细节隐藏到了 RenderNode 中了。
     */

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.reset()
        canvas.drawColor(Color.RED)
        paint.color = Color.BLUE;
        canvas.drawCircle(width / 2f, height / 2f, width / 2f, paint);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && canvas.isHardwareAccelerated()) {
            rect.set(0, 0, 100, 100);
            renderNode.setClipRect(rect);
            val count = canvas.save()
            val recordingCanvas = renderNode.beginRecording()
            logger.logInfo("width: ${recordingCanvas.width}, height: ${recordingCanvas.height}")
            recordingCanvas.drawColor(Color.YELLOW)
            renderNode.endRecording()
            paint.color = Color.BLUE
            recordingCanvas.drawCircle(50f, 50f, 50f, paint);
            canvas.drawRenderNode(renderNode)
            canvas.restoreToCount(count)
        }
    }
}
