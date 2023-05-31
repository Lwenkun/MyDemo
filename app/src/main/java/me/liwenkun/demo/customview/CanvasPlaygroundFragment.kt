package me.liwenkun.demo.customview

import android.content.Context
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.liwenkun.demo.R
import me.liwenkun.demo.demoframework.DemoBaseFragment
import me.liwenkun.demo.libannotation.Demo

@Demo(title = "Canvas Playground")
class CanvasPlaygroundFragment : DemoBaseFragment() {
    override fun onAttach(context: Context) {
        super.onAttach(context)
        demoFragmentActivity.setSourceCode(CanvasView.sourceCode)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_canvas_playground, container, false)
        view.setLayerType(View.LAYER_TYPE_SOFTWARE, Paint())
        view.setWillNotCacheDrawing(false)
        return view
    }
}