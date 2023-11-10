package com.gamelink.gamelinkapp.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.UUID

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
            } catch (e: IOException) {
                e.printStackTrace()
                null
            }
        }

        fun getBitmap(absolutePath: String): Bitmap {
            return BitmapFactory.decodeFile(absolutePath)
        }

        private fun isFileExists(context: Context, uri: Uri): Boolean {
            val contentResolver = context.contentResolver

            try {
                // Tente obter as informações do arquivo
                val inputStream = contentResolver.openInputStream(uri)
                inputStream?.close()
                return true
            } catch (e: IOException) {
                // O arquivo não pode ser aberto, então assume-se que não existe
                return false
            }
        }


        fun saveImageUri(context: Context, imageUri: Uri?): String? {
            if(imageUri == null) {
                return null
            }

            if(isFileExists(context, imageUri)) {
                return imageUri.toString()
            }

            // Obtenha um ContentResolver
            val contentResolver = context.contentResolver

            return try {
                // Abra um InputStream para o URI
                val inputStream: InputStream? = contentResolver.openInputStream(imageUri)

                // Verifique se o InputStream não é nulo
                inputStream?.use {
                    // Crie um arquivo no diretório de cache do aplicativo
                    val cacheDir: File = context.cacheDir
                    val cachedImage = File(cacheDir, "community-banner-${UUID.randomUUID()}.jpg")

                    // Crie um OutputStream para o arquivo no cache
                    val outputStream: OutputStream = cachedImage.outputStream()

                    // Copie os dados do InputStream para o OutputStream
                    inputStream.copyTo(outputStream)

                    // Feche o InputStream e o OutputStream
                    inputStream.close()
                    outputStream.close()

                    // O arquivo da imagem foi salvo no cache
                    cachedImage.absolutePath
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

        fun getUri(absolutePath: String): Uri {
            return Uri.fromFile(File(absolutePath))
        }

        fun deleteImage(path: String) {
            val imageFile = File(path)
            if (imageFile.exists()) {
                imageFile.delete()
            }

        }
    }

}