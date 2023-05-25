package me.liwenkun.demo.demoframework;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import me.liwenkun.demo.R;
import thereisnospon.codeview.CodeView;
import thereisnospon.codeview.CodeViewTheme;

public class DemoBaseActivity extends AppCompatActivity implements Logger {
    private static final int MENU_ID_OPEN_LOG = View.generateViewId();

    public static final String EXTRA_DEMO_TITLE = "demo_title";

    private LogView logView;

    private ViewGroup contentView;
    private CodeView codeView;

    private String openLogText;
    private String closeLogText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String demoTitle = getIntent().getStringExtra(EXTRA_DEMO_TITLE);
        setTitle(demoTitle);
        super.setContentView(R.layout.activity_demo_base_activity);
        contentView = findViewById(R.id.content);
        initCodeView();
        logView = findViewById(R.id.log_view);
        if (showLogOnStart()) {
            logView.setVisibility(View.VISIBLE);
        }
        openLogText = getString(R.string.open_log);
        closeLogText = getString(R.string.close_log);
    }

    protected boolean showLogOnStart() {
        return false;
    }

    private void initCodeView() {
        codeView = findViewById(R.id.code_view);
        codeView.setTheme(CodeViewTheme.ATELIER_FOREST_DARK).fillColor();
        codeView.setEncode("utf-8");
    }

    public void setSourceCode(String sourceCode) {
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
        MenuItem openLogView = menu.add(View.generateViewId(), MENU_ID_OPEN_LOG, Menu.NONE,
                logView.getVisibility() == View.VISIBLE ? closeLogText : openLogText);
        openLogView.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == MENU_ID_OPEN_LOG) {
            if (logView.getVisibility() == View.VISIBLE) {
                logView.setVisibility(View.GONE);
                item.setTitle("打开日志");
            } else {
                logView.setVisibility(View.VISIBLE);
                item.setTitle("关闭日志");
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
        log("", message, color, promptChar);
    }

    @Override
    public void log(String tag, String message, int color, String promptChar) {
        logView.print(tag, message, color, promptChar);
    }
}
