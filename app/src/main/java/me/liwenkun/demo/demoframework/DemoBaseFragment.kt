package me.liwenkun.demo.demoframework;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class DemoBaseFragment extends Fragment implements Logger {

    private Logger logger;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof Logger) {
            logger = ((Logger) context);
        }
    }

    protected void logInfo(String msg) {
        log(msg, Logger.COLOR_INFO);
    }

    protected void logError(String msg) {
        log(msg, Logger.COLOR_ERROR);
    }

    @Override
    public void log(String message, int color) {
        if (logger != null) {
            logger.log(message, color);
        }
    }

    @Override
    public void log(String message, int color, String promptChar) {
        if (logger != null) {
            logger.log(message, color, promptChar);
        }
    }

    @Override
    public void deleteAllLogs() {
        if (logger != null) {
            logger.deleteAllLogs();
        }
    }

    public DemoFragmentActivity getDemoFragmentActivity() {
        return ((DemoFragmentActivity) getActivity());
    }

    @Override
    public void log(String tag, String message, int color, String promptChar) {
        if (logger != null) {
            logger.log(tag, message, color, promptChar);
        }
    }
}
