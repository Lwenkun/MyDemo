package me.liwenkun.demo.demoframework;

import android.graphics.Color;

public interface Logger {
    int COLOR_ERROR = Color.RED;
    int COLOR_INFO = Color.WHITE;
    void log(String message, int color);
    void log(String message, int color, String promptChar);
    void log(String tag, String message, int color, String promptChar);
    default void log(String tag, String message, int color) {
        log(tag, message, color, null);
    }
    void deleteAllLogs();
}
