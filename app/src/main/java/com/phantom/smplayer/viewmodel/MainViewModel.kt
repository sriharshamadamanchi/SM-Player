package com.phantom.smplayer.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.phantom.smplayer.data.Video
import java.io.File

class MainViewModel : ViewModel() {

    private val _videoDirectories = MutableLiveData<Map<File, List<Video>>>()
    private val _selectedVideo = MutableLiveData<Uri>()

    fun updateVideoDirectories(videoDirectories: Map<File, List<Video>>) {
        _videoDirectories.postValue(videoDirectories)
    }

    fun getVideoDirectories(): LiveData<Map<File, List<Video>>> {
        return _videoDirectories
    }

    fun setSelectedVideo(uri: Uri) {
        _selectedVideo.value = uri
    }

    fun getSelectedVideo(): Uri? {
        return _selectedVideo.value
    }
}