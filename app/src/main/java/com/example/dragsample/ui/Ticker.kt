package com.example.dragsample.ui

import android.os.Handler

class Ticker(private val delay: Long = 1000L) {

    private val durationHandler: Handler = Handler()
    private var durationUpdater: Runnable? = null

    fun start(action: () -> Unit) {
        durationUpdater = object : Runnable {
            override fun run() {
                action()

                durationHandler.postDelayed(this, delay)
            }
        }.apply {
            run()
        }
    }

    fun stop() {
        durationUpdater?.let { durationHandler.removeCallbacks(it) }
    }
}