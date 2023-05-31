package me.liwenkun.demo.utils

import me.liwenkun.demo.App.Companion.get

object DimensionUtils {
    @JvmStatic
    fun px(dp: Int): Int {
        val density = get().resources.displayMetrics.density
        return (dp * density).toInt()
    }
}