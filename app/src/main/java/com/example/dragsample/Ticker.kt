package com.example.dragsample

import android.os.Handler
import android.os.Looper

class Ticker(private val delay: Long = 1000L) {

    private val durationHandler: Handler = Handler(Looper.getMainLooper())
    private var durationUpdater: Runnable? = null

    fun start(action: () -> Unit) {
        object : Runnable {
            override fun run() {
                action()

                durationHandler.postDelayed(this, delay)
            }
        }.apply {
            run()
        }.also { durationUpdater = it }
    }
}