package com.phantom.smplayer.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.phantom.smplayer.data.Video
import java.io.File

class MainViewModel : ViewModel() {

    private val _videoDirectories = MutableLiveData<Map<File, List<Video>>>()

    fun updateVideoDirectories(videoDirectories: Map<File, List<Video>>) {
        _videoDirectories.postValue(videoDirectories)
    }

    fun getVideoDirectories(): LiveData<Map<File, List<Video>>> {
        return _videoDirectories
    }
}