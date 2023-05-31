package me.liwenkun.demo.fragment

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import me.liwenkun.demo.demoframework.Logger

class TestFragment : Fragment() {

    private var tag: String? = null
    private lateinit var logger: Logger

    override fun onAttach(context: Context) {
        super.onAttach(context)
        logger = Logger.from(context)
        tag = getTag()
        logLifecycle("onAttach")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logLifecycle("onCreate")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        logLifecycle("onActivityCreated")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val showTag = TextView(context)
        showTag.setBackgroundColor(0xffeeeeee.toInt())
        showTag.text = tag
        showTag.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        showTag.gravity = Gravity.CENTER
        logLifecycle("onCreateView")
        return showTag
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logLifecycle("onViewCreated")
    }

    override fun onStart() {
        super.onStart()
        logLifecycle("onStart")
    }

    override fun onResume() {
        super.onResume()
        logLifecycle("onResume")
    }

    override fun onStop() {
        super.onStop()
        logLifecycle("onStop")
    }

    override fun onPause() {
        super.onPause()
        logLifecycle("onPause")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        logLifecycle("onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        logLifecycle("onDestroy")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        logLifecycle("onSaveInstanceState")
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        logLifecycle("onViewStateRestored")
    }

    private fun logLifecycle(name: String) {
        logger.log("fragment $tag $name called", Logger.COLOR_INFO)
    }

    companion object {
        @JvmStatic
        fun newInstance(): TestFragment {
            return TestFragment()
        }
    }
}