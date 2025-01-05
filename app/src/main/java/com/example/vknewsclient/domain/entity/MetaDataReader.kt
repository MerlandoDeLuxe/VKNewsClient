package com.example.vknewsclient.domain.entity

import android.app.Application
import android.net.Uri
import android.provider.MediaStore
import android.util.Log

data class MetaData(
    val fileName: String
)

interface MetaDataReader {

    fun getMetaDataFromUri(contentUri: Uri): MetaData?
}

class MetaDataReaderImpl(
    private val app: Application
) : MetaDataReader {

    private val TAG = "MetaDataReaderImpl"


    override fun getMetaDataFromUri(contentUri: Uri): MetaData? {

        Log.d(TAG, "getMetaDataFromUri: мы тут 1")
        if (contentUri.scheme != "content") {
            Log.d(TAG, "getMetaDataFromUri: мы тут 2")
            return null
        }
        Log.d(TAG, "getMetaDataFromUri: мы тут 3")
        val fileName = app.contentResolver
            .query(
                contentUri,
                arrayOf(MediaStore.Video.VideoColumns.DISPLAY_NAME),
                null,
                null,
                null
            )
            ?.use { cursor ->
                val index = cursor.getColumnIndex(MediaStore.Video.VideoColumns.DISPLAY_NAME)
                cursor.moveToFirst()
                cursor.getShort(index)
            }

        Log.d(TAG, "getMetaDataFromUri: fileName = $fileName")
        return fileName?.let { fullFileName ->
            MetaData(
                fileName = Uri.parse(fullFileName.toString()).lastPathSegment ?: return null
            )
        }
    }
}