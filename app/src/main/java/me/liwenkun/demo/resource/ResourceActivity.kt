package me.liwenkun.demo.resource

import android.app.Notification
import android.os.Bundle
import android.util.TypedValue
import me.liwenkun.demo.DemoBaseActivity
import me.liwenkun.demo.DemoFragmentActivity
import me.liwenkun.demo.R
import me.liwenkun.demo.libannotation.Demo
import me.liwenkun.demo.libannotation.Source

@Demo("/安卓/资源", "资源获取api")
class ResourceActivity : DemoBaseActivity() {

     companion object {
         @Source
         lateinit var source: String
     }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSourceCode(source)

        val typedValue = TypedValue()
        //1 resourceId == color 资源 id，data == 色值
        var ta = obtainStyledAttributes(intArrayOf(R.attr.colorPrimary))
        ta.getValue(0, typedValue)
        ta.recycle()
        logInfo("typedValue 1-> $typedValue")
        //2 resourceId == 0，data == color 资源 id
        theme.resolveAttribute(R.attr.colorPrimary, typedValue, false)
        logInfo("typedValue 2 -> $typedValue")
        //3 等价于 1
        theme.resolveAttribute(R.attr.colorPrimary, typedValue, true)
        logInfo("typedValue 3 -> $typedValue")

        //4 resourceId == 资源 id，data 值存放用来加载字符串的值（如果引用的是资源文件而不是value值，
        // 那么 resolveRefs == true 就会解析出文件，但是文件无法用 TypedValue 表示，因此只能用路径来表示，
        // 因此 TypedValue 的值就是 String 类型的了）
        ta = obtainStyledAttributes(intArrayOf(R.attr.selectableItemBackground))
        ta.getValue(0, typedValue)
        ta.recycle()
        logInfo("typedValue 4 -> $typedValue")
        //5 resourceId == 0, data == drawable 资源 id
        theme.resolveAttribute(R.attr.selectableItemBackground, typedValue, false)
        logInfo("typedValue 5 -> $typedValue")
        //6 等价于 4
        theme.resolveAttribute(R.attr.selectableItemBackground, typedValue, true)
        logInfo("typedValue 6 -> $typedValue")


        //7 resourceId == style 资源 id, data == style 资源 id
        ta = obtainStyledAttributes(intArrayOf(R.attr.textInputStyle))
        ta.getValue(0, typedValue)
        ta.recycle()
        logInfo("typedValue 7 -> $typedValue")
        //8 resourceId == 0, data == style 资源 id
        theme.resolveAttribute(R.attr.textInputStyle, typedValue, false)
        logInfo("typedValue 8 -> $typedValue")
        //9 等价于 7 但是 sourceResourceId 不为空
        theme.resolveAttribute(R.attr.textInputStyle, typedValue, true)
        logInfo("typedValue 9 -> $typedValue")

        ta = obtainStyledAttributes(android.R.style.TextAppearance, intArrayOf(android.R.attr.textSize))
        ta.getValue(0, typedValue)
        ta.recycle()
    }
}