package com.example.olacak.util

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

class VibratorHelper private constructor(private val context: Context) {

    @Suppress("DEPRECATION")
    fun vibrate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager =
                context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator.run {
                cancel()
                vibrate(
                    VibrationEffect.createWaveform(
                        longArrayOf(0, 400, 200, 400),
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
            }
        } else {
            val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            vibrator.cancel()
            if (Build.VERSION.SDK_INT >= 26) {
                vibrator.vibrate(
                    VibrationEffect.createWaveform(
                        longArrayOf(0, 400, 200, 400),
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
            } else {
                vibrator.vibrate(500)
            }
        }
    }

    companion object {
        @JvmStatic
        fun from(context: Context): VibratorHelper? {
            val hasVibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager =
                    context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                vibratorManager.defaultVibrator.hasVibrator()
            } else {
                @Suppress("DEPRECATION")
                val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                vibrator.hasVibrator()
            }
            return if (hasVibrator) VibratorHelper(context.applicationContext) else null
        }
    }
}