package me.liwenkun.demo

import android.app.Application
import androidx.room.Room.databaseBuilder

class App : Application() {
    val appDatabase: AppDatabase by lazy {
        databaseBuilder(this, AppDatabase::class.java, "ap-db").build()
    }

    override fun onCreate() {
        super.onCreate()
        app = this
        SourceInjector.init()
    }

    companion object {
        private lateinit var app: App
        @JvmStatic
        fun get(): App {
            return app
        }
    }
}