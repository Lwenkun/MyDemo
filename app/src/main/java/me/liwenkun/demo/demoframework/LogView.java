package me.liwenkun.demo.demoframework;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import me.liwenkun.demo.R;

import static me.liwenkun.demo.utils.Utils.px;

public class LogView extends FrameLayout {

    private final StringBuilder temp = new StringBuilder();

    private ListView lvLogs;

    private final SimpleDateFormat simpleDateFormat
            = new SimpleDateFormat("[HH:mm:ss.SSS] ", Locale.getDefault());

    private boolean enableAutoScroll;

    private final List<LogItem> logs = new ArrayList<>();
    private ArrayAdapter<LogItem> logAdapter;

    private static class LogItem {
        String log;
        @ColorInt
        int color;

        @NotNull
        @Override
        public String toString() {
            return log;
        }
    }

    public LogView(@NonNull Context context) {
        super(context);
        inflate(context, R.layout.layout_log_view, this);
        initView();
    }

    private void initView() {
        lvLogs = findViewById(R.id.logs);
        View handle = findViewById(R.id.handle);
        handle.setOnTouchListener(new OnTouchListener() {
            float lastY = 0;
            float offsetLoss = 0;
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getActionMasked() == MotionEvent.ACTION_MOVE) {
                    offset(lastY - event.getRawY());
                } else if (event.getActionMasked() == MotionEvent.ACTION_UP) {
                    offsetLoss = 0;
                }
                lastY = event.getRawY();
                return false;
            }

            private void offset(float y) {
                y = Math.max(-(getHeight() - px(40)), Math.min(getTop(), y));
                ViewGroup.LayoutParams lp = getLayoutParams();
                offsetLoss += y - (int) y;
                if (offsetLoss > 1) {
                    y = y +1;
                    offsetLoss = offsetLoss - 1;
                }
                lp.height += y;
                requestLayout();
            }
        });
        logAdapter = new ArrayAdapter<LogItem>(getContext(),
                R.layout.layout_log_item, R.id.log, logs) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                LogItem logItem = getItem(position);
                if (logItem != null) {
                    TextView tv = view.findViewById(R.id.log);
                    tv.setTextColor(logItem.color);
                }
                return view;
            }
        };
        lvLogs.setAdapter(logAdapter);

        lvLogs.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
                    View lastVisibleItemView = view.getChildAt(view.getChildCount() - 1);
                    enableAutoScroll = lastVisibleItemView != null
                            && lastVisibleItemView.getBottom() + view.getPaddingBottom() == view.getHeight();
                } else {
                    enableAutoScroll = false;
                }
            }
        });
    }

    public void deleteAllLogs() {
        logAdapter.clear();
    }

    public void print(String msg, int color, String promptChar) {
        temp.delete(0, temp.length());
        if (!TextUtils.isEmpty(promptChar)) {
            temp.append(promptChar);
        }
        temp.append(simpleDateFormat.format(new Date())).append(msg);
        LogItem log = new LogItem();
        log.log = temp.toString();
        log.color = color;
        logAdapter.add(log);
        if (enableAutoScroll) {
            post(this::scrollToBottom);
        }
    }

    public void scrollToTop() {
        lvLogs.setSelection(0);
    }

    public void scrollToBottom() {
        lvLogs.setSelection(logAdapter.getCount() - 1);
    }
}
