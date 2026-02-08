package com.example.smartfixer.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import java.io.ByteArrayOutputStream

object ImageUtil {

    private const val MAX_DIMENSION = 1024

    fun uriToBase64(context: Context, uri: Uri): String? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri) ?: return null
            val original = BitmapFactory.decodeStream(inputStream)
            inputStream.close()

            val scaled = scaleBitmap(original)

            val outputStream = ByteArrayOutputStream()
            scaled.compress(Bitmap.CompressFormat.JPEG, 85, outputStream)
            val bytes = outputStream.toByteArray()

            if (scaled !== original) scaled.recycle()
            original.recycle()

            Base64.encodeToString(bytes, Base64.NO_WRAP)
        } catch (e: Exception) {
            null
        }
    }

    private fun scaleBitmap(bitmap: Bitmap): Bitmap {
        val width = bitmap.width
        val height = bitmap.height

        if (width <= MAX_DIMENSION && height <= MAX_DIMENSION) return bitmap

        val ratio = minOf(
            MAX_DIMENSION.toFloat() / width,
            MAX_DIMENSION.toFloat() / height
        )
        val newWidth = (width * ratio).toInt()
        val newHeight = (height * ratio).toInt()

        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
    }
}
