package com.example.playlistmaker

import android.content.Context
import android.util.DisplayMetrics

class Utils {
    fun convertPixelsToDp(px: Float, context: Context): Float {
        return px / (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }
}