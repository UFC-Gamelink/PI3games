package com.gamelink.gamelinkapp.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

abstract class ImageUtils {
    companion object {
        fun saveImage(context: Context, filename: String, bitmap: Bitmap): String? {
            val file = File(context.cacheDir, "$filename.jpg")

            return try {
                val stream = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                stream.flush()
                stream.close()

                file.absolutePath
            } catch(e: IOException) {
                e.printStackTrace()
                null
            }
        }

        fun getBitmap(absolutePath: String): Bitmap {
            return BitmapFactory.decodeFile(absolutePath)
        }

        fun deleteImage(path: String) {
            val imageFile = File(path)
            if(imageFile.exists()) {
                imageFile.delete()
            }

        }
    }

}