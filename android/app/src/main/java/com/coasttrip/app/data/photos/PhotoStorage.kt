package com.coasttrip.app.data.photos

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

class PhotoStorage(private val context: Context) {
    private val photosDir: File
        get() = File(context.filesDir, "photos").apply { mkdirs() }

    fun fileFor(fileName: String): File = File(photosDir, fileName)

    fun saveFromUri(uri: Uri): String {
        context.contentResolver.openInputStream(uri).use { input ->
            requireNotNull(input) { "Could not open the selected photo." }
            val bitmap = BitmapFactory.decodeStream(input)
                ?: error("Could not decode the selected photo.")
            return saveBitmap(bitmap)
        }
    }

    fun saveBitmap(bitmap: Bitmap): String {
        val fileName = "${UUID.randomUUID()}.jpg"
        val file = fileFor(fileName)
        FileOutputStream(file).use { out ->
            if (!bitmap.compress(Bitmap.CompressFormat.JPEG, 85, out)) {
                throw IllegalStateException("Could not compress the photo for storage.")
            }
        }
        return fileName
    }

    fun delete(fileName: String) {
        fileFor(fileName).delete()
    }
}
