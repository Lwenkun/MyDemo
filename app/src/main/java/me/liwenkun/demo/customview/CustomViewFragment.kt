package me.liwenkun.demo.customview

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import me.liwenkun.demo.DemoBaseFragment
import me.liwenkun.demo.R
import me.liwenkun.demo.libannotation.Demo

@Demo("/安卓/自定义View/", "仿搜狗输入法指示器")
class CustomViewFragment : DemoBaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.activity_custom_view, container, false)
        return view;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val viewPager: ViewPager = view.findViewById(R.id.view_pager)
        val indicator: Indicator = view.findViewById(R.id.indicator)
        viewPager.adapter = object : PagerAdapter() {
            override fun getCount(): Int {
                return 14
            }

            override fun isViewFromObject(view: View, `object`: Any): Boolean {
                return view == `object`
            }

            override fun instantiateItem(container: ViewGroup, position: Int): Any {
                val textView = TextView(context)
                textView.gravity = Gravity.CENTER
                // noinspection all
                textView.text = "仿照搜狗输入法指示器$position"
                container.addView(textView)
                return textView
            }

            override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
                container.removeView(`object` as View)
            }
        }
        indicator.setUpWithPager(viewPager)
    }
}