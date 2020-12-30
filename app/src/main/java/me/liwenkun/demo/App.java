package me.liwenkun.demo;

import android.app.Application;

public class App extends Application {

    private static App app;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        SourceInjector.init();
    }

    public static App get() {
        return app;
    }
}
