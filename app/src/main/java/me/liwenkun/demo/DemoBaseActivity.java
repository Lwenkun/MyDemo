package me.liwenkun.demo;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import me.liwenkun.demo.utils.Utils;
import thereisnospon.codeview.CodeView;
import thereisnospon.codeview.CodeViewTheme;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class DemoBaseActivity extends AppCompatActivity implements Logger {

    private static final int MENU_ID_OPEN_LOG = View.generateViewId();

    public static final String EXTRA_DEMO_TITLE = "demo_title";

    private LogView logView;

    private ViewGroup contentView;
    private CodeView codeView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logView = new LogView(this);
        String demoTitle = getIntent().getStringExtra(EXTRA_DEMO_TITLE);
        setTitle(demoTitle);
        super.setContentView(R.layout.activity_demo_base_activity);
        contentView = findViewById(R.id.content);
        codeView = findViewById(R.id.code_view);
        codeView.setTheme(CodeViewTheme.ATELIER_FOREST_DARK).fillColor();
        codeView.setEncode("utf-8");
        codeView.setVisibility(View.GONE);
    }

    protected void setSourceCode(String sourceCode) {
        codeView.showCode(sourceCode);
        codeView.setVisibility(View.VISIBLE);
    }

    @Override
    public void setContentView(View view) {
        setContentView(view, new ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT));
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        contentView.removeAllViews();
        contentView.addView(view, params);
    }

    @Override
    public void setContentView(int layoutResID) {
        contentView.removeAllViews();
        LayoutInflater.from(this).inflate(layoutResID, contentView, true);
    }

    @Override
    public void addContentView(View view, ViewGroup.LayoutParams params) {
        contentView.addView(view, params);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(View.generateViewId(), MENU_ID_OPEN_LOG, Menu.NONE, "打开日志");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == MENU_ID_OPEN_LOG) {
            if (logView.getParent() == null) {
                FrameLayout.LayoutParams lp
                        = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        Utils.px(350));
                lp.gravity = Gravity.BOTTOM;
                super.addContentView(logView, lp);
                item.setTitle("关闭日志");
            } else {
                if (logView.getVisibility() == View.VISIBLE) {
                    logView.setVisibility(View.GONE);
                    item.setTitle("打开日志");
                } else {
                    logView.setVisibility(View.VISIBLE);
                    item.setTitle("关闭日志");
                }
            }
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    protected void logInfo(String msg) {
        log(msg, Logger.COLOR_INFO);
    }

    protected void logError(String msg) {
        log(msg, Logger.COLOR_ERROR);
    }

    @Override
    public void deleteAllLogs() {
        logView.deleteAllLogs();
    }

    @Override
    public void log(String message, int color) {
        log(message, color, "");
    }

    @Override
    public void log(String message, int color, String promptChar) {
        logView.print(message, color, promptChar);
    }
}
