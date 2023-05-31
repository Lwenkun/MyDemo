package me.liwenkun.demo.system;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import me.liwenkun.demo.demoframework.DemoBaseActivity;
import me.liwenkun.demo.libannotation.Demo;

@Demo(title = "同步屏障")
public class TestActivity extends DemoBaseActivity {
    TextView textView;
    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        textView = new TextView(this);
        setContentView(textView);
    }

    @Override
    protected boolean showLogOnStart() {
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Handler handler = new Handler();
        long startTime = System.currentTimeMillis();
        Runnable runnable = () -> logInfo("延迟时间：" + (System.currentTimeMillis() - startTime));
        Message message = Message.obtain(handler, runnable);
        message.setAsynchronous(true);
        handler.sendMessage(message);
    }
}
