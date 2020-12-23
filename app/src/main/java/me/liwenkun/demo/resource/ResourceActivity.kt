package me.liwenkun.demo.resource

import android.os.Bundle
import android.util.TypedValue
import me.liwenkun.demo.DemoBaseActivity
import me.liwenkun.demo.DemoFragmentActivity
import me.liwenkun.demo.R
import me.liwenkun.demo.libannotation.Demo

@Demo("/安卓/资源", "资源获取api")
class ResourceActivity : DemoBaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)

        val typedValue = TypedValue()
        //1
        var ta = obtainStyledAttributes(intArrayOf(R.attr.colorPrimary))
        ta.getValue(0, typedValue)
        ta.recycle()
        //2 resourceId == 0，data == color 资源 id
        theme.resolveAttribute(R.attr.colorPrimary, typedValue, false)
        //3 等效于 1，resourceId == color 资源 id，data == 色值
        theme.resolveAttribute(R.attr.colorPrimary, typedValue, true)


        //7 resourceId == style 资源 id, data == style 资源 id
        ta = obtainStyledAttributes(intArrayOf(R.attr.textInputStyle))
        ta.getValue(0, typedValue)
        ta.recycle()
        //8 resourceId == 0, data == style 资源 id
        theme.resolveAttribute(R.attr.textInputStyle, typedValue, false)
        //9 等价于 7 但是 sourceResourceId 不为空
        theme.resolveAttribute(R.attr.textInputStyle, typedValue, true)

        ta = obtainStyledAttributes(android.R.style.TextAppearance, intArrayOf(android.R.attr.textSize))
        ta.getValue(0, typedValue)
        ta.recycle()

        //4 resourceId == 资源 id，data 值存放用来加载字符串的值（如果引用的是资源文件而不是value值，
        // 那么 resolveRefs == true 就会解析出文件，但是文件无法用 TypedValue 表示，因此只能用路径来表示，
        // 因此 TypedValue 的值就是 String 类型的了）
        ta = obtainStyledAttributes(intArrayOf(R.attr.selectableItemBackground))
        ta.getValue(0, typedValue)
        ta.recycle()
        //6 resourceId == 0, data == drawable 资源 id
        theme.resolveAttribute(R.attr.selectableItemBackground, typedValue, false)
        //5 等价于 4
        theme.resolveAttribute(R.attr.selectableItemBackground, typedValue, true)
    }
}