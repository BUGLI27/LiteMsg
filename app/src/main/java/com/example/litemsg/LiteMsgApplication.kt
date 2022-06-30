package com.example.litemsg

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class LiteMsgApplication : Application() {

    // companion object {} 伴生对象，只能存在一个，类似静态变量
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
        const val uid = 5
    }
    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }

}