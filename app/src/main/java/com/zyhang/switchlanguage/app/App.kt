package com.zyhang.switchlanguage.app

import android.app.Application
import android.support.v7.app.AppCompatDelegate

/**
 * Created by zyhang on 2018/9/10.16:24
 */

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }
}
