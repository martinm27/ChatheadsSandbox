package com.acutisbits.chatheadssandbox

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.util.*

object IncomingCallTimeUtility {

    private var timer = Timer()

    private var incomingCallTime = MutableLiveData(formatSeconds(0.0))
    var time = 0

    fun startTimer() {
        if (!isCallActive()) {
            timer = Timer().apply {
                scheduleAtFixedRate(object : TimerTask() {
                    override fun run() {
                        time++
                        incomingCallTime.postValue(formatSeconds(time.toDouble()))
                    }
                }, ONE_SECOND_IN_MILLISECONDS, ONE_SECOND_IN_MILLISECONDS)
            }
        }
    }

    fun incomingCallTime(): LiveData<String> = incomingCallTime

    fun formatSeconds(seconds: Double): String {
        val secondsLeft = (seconds % 3600 % 60).toInt()
        val minutes = Math.floor(seconds % 3600 / 60).toInt()

        val MM = if (minutes < 10) "0$minutes" else minutes
        val SS = if (secondsLeft < 10) "0$secondsLeft" else secondsLeft

        return "$MM:$SS"
    }

    fun reset() {
        timer.cancel()
        time = 0
        incomingCallTime.postValue(formatSeconds(time.toDouble()))
    }

    fun isCallActive(): Boolean = time != 0
}
