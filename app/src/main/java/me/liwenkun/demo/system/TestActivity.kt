package me.liwenkun.demo.system

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.widget.TextView
import androidx.annotation.RequiresApi
import me.liwenkun.demo.demoframework.DemoBaseActivity
import me.liwenkun.demo.libannotation.Demo

@Demo(title = "同步屏障")
class TestActivity : DemoBaseActivity() {
    private lateinit var textView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        textView = TextView(this).also {
            setContentView(it)
        }
    }

    override fun showLogOnStart(): Boolean {
        return true
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        val handler = Handler(mainLooper)
        val startTime = System.currentTimeMillis()
        val message = Message.obtain(handler) {
            logInfo("延迟时间：" + (System.currentTimeMillis() - startTime))
        }
        message.isAsynchronous = true
        handler.sendMessage(message)
    }
}