package me.liwenkun.demo.view

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import me.liwenkun.demo.R
import me.liwenkun.demo.demoframework.DemoBaseFragment
import me.liwenkun.demo.demoframework.Logger
import me.liwenkun.demo.libannotation.Demo

@Demo(title = "ViewPager")
class ViewPagerDemoFragment : DemoBaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_view_pager_demo, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewPager: ViewPager = view.findViewById(R.id.view_pager)
        viewPager.adapter = object : PagerAdapter() {
            override fun getCount(): Int {
                return 5
            }

            override fun isViewFromObject(view: View, `object`: Any): Boolean {
                return view === `object`
            }

            override fun instantiateItem(container: ViewGroup, position: Int): Any {
                val textView = TextView(container.context)
                textView.gravity = Gravity.CENTER
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30f)
                textView.setBackgroundColor(Color.CYAN)
                textView.text = position.toString()
                textView.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                container.addView(textView)
                textView.tag = position
                log("instantiateItem","position: $position", Logger.COLOR_INFO)
                return textView
            }

            override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
                container.removeView(`object` as View)
                log("destroyItem","position: $position", Logger.COLOR_INFO)
            }
        }
        viewPager.setPageMarginDrawable(ColorDrawable(Color.WHITE))
        viewPager.pageMargin = 10
        viewPager.offscreenPageLimit = 2

        viewPager.setPageTransformer(true) { page, position ->
            val index = page.tag
            page.pivotX = if (position < 0) page.width.toFloat() else 0f
            page.pivotY = 0f
            log("view: ${index}, position: $position", Logger.COLOR_INFO)
            page.rotationY = 10 * (-position)
        }
    }
}