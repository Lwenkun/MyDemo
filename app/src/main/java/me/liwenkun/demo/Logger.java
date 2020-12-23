package me.liwenkun.demo;

import android.graphics.Color;

public interface Logger {
    int COLOR_ERROR = Color.RED;
    int COLOR_INFO = Color.WHITE;
    void log(String message, int color);
    void log(String message, int color, String promptChar);
    void deleteAllLogs();
}
