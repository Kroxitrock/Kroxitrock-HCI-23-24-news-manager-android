package es.upm.reader.news.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64

object ImageUtils {
    fun base64ToBitmap(base64Image: String): Bitmap {
        val imageBytes = Base64.decode(base64Image, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }
}