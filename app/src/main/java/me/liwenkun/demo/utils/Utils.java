package me.liwenkun.demo.utils;


import me.liwenkun.demo.App;

public class Utils {

    public static int px(int dp) {
        float density = App.get().getResources().getDisplayMetrics().density;
        return (int)(dp * density);
    }

    private Utils() {}
}
