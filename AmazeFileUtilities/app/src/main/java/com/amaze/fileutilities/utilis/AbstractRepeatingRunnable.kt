package com.amaze.fileutilities.utilis

import android.os.CountDownTimer

abstract class AbstractRepeatingRunnable(
    startImmediately: Boolean
) :
    Runnable {

    private val countDownTimer: CountDownTimer


    fun cancel() {
        countDownTimer.cancel()
    }

    init {
        if (!startImmediately) {
            throw UnsupportedOperationException("RepeatingRunnables are immediately executed!")
        }
        countDownTimer = object : CountDownTimer(Long.MAX_VALUE, 1000) {
            var firstTime = true
            override fun onTick(millisUntilFinished: Long) {
                if (firstTime) {
                    firstTime = false
                    return
                }
                run()
            }

            override fun onFinish() {
                // do nothing
            }
        }.start()
    }
}
