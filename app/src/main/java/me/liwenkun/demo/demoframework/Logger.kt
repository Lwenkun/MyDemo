package me.liwenkun.demo.demoframework

import android.content.Context
import android.graphics.Color

interface Logger {
    fun log(message: String?, color: Int, promptChar: String? = "") {
        log("", message, color, promptChar)
    }

    fun log(tag: String?, message: String?, color: Int, promptChar: String?)

    fun log(tag: String?, message: String?, color: Int) {
        log(tag, message, color, "")
    }

    fun logInfo(message: String?) {
        logInfo("", message)
    }

    fun logInfo(tag: String?, message: String?) {
        log(tag, message, COLOR_INFO)
    }

    fun logError(message: String?) {
        logError("", message)
    }

    fun logError(tag: String?, message: String?) {
        log(tag, message, COLOR_ERROR)
    }

    fun deleteAllLogs()

    companion object {
        @JvmStatic
        fun from(context: Context?): Logger {
            return context as? Logger ?: DUMMY
        }

        private val DUMMY: Logger = object : Logger {
            override fun log(tag: String?, message: String?, color: Int, promptChar: String?) {}
            override fun deleteAllLogs() {}
        }
        const val COLOR_ERROR = Color.RED
        const val COLOR_INFO = Color.WHITE
    }
}