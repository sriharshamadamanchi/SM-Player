package com.phantom.smplayer.home

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.phantom.smplayer.MainActivity
import com.phantom.smplayer.data.Video
import com.phantom.smplayer.viewmodel.MainViewModel
import java.io.File

class VideoDirectoryScanner(private val context: Context) {

    private val mainViewModel =
        ViewModelProvider(context as MainActivity)[MainViewModel::class.java]

    fun loadMedia() {
        val videoMap = mutableMapOf<File, MutableList<Video>>()

        val contentResolver = context.contentResolver
        val videoUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Video.Media.DATA,
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.TITLE,
            MediaStore.Video.Media.DURATION,
        )

        contentResolver.query(
            videoUri,
            projection,
            null,
            null
        )?.use { cursor ->
            val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
            val nameColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE)
            val durationColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)

            while (cursor.moveToNext()) {
                val absoluteFilePath: String = cursor.getString(dataColumn)
                val videoFile = File(absoluteFilePath)
                val videoDirectory = videoFile.parentFile
                if (videoDirectory != null) {
                    if (videoMap[videoDirectory] == null) {
                        videoMap[videoDirectory] = mutableListOf()
                    }

                    val id = cursor.getLong(idColumn)
                    val name = cursor.getString(nameColumn)
                    val duration = cursor.getInt(durationColumn)

                    val contentUri: Uri = ContentUris.withAppendedId(
                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                        id
                    )

                    videoMap[videoDirectory]?.add(Video(contentUri, name, duration))
                }
            }

            Log.i("Harsha", "" + videoMap)

            mainViewModel.updateVideoDirectories(videoMap)

        }
    }
}