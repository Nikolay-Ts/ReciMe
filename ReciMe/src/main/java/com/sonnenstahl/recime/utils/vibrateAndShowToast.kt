package com.sonnenstahl.recime.utils

import android.Manifest
import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresPermission
import androidx.core.content.ContextCompat.getSystemService

/**
 * vibrate the device and display the error message to the user
 *
 * @param context of the view
 * @param message to be displayed the user
 * @param durationMilli the duration of the vibration
 */
fun vibrateAndShowToast(
    context: Context,
    message: String,
    durationMilli: Long
) {
    val vibrator = context.getSystemService(Vibrator::class.java)
    vibrator.vibrate(VibrationEffect.createOneShot(durationMilli, VibrationEffect.DEFAULT_AMPLITUDE))
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}