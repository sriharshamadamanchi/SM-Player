package com.phantom.smplayer.home

import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.phantom.smplayer.MainActivity
import com.phantom.smplayer.components.Header
import com.phantom.smplayer.components.VideoItem
import com.phantom.smplayer.components.player.video.VideoActivity
import com.phantom.smplayer.data.Video
import com.phantom.smplayer.ui.theme.LocalColor
import com.phantom.smplayer.viewmodel.MainViewModel

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun Directory(
    navController: NavHostController,
    directory: String
) {

    val context = LocalContext.current

    val mainViewModel: MainViewModel =
        viewModel(viewModelStoreOwner = LocalContext.current as MainActivity)

    val videosList = mainViewModel.getVideoDirectories().observeAsState().value ?: emptyMap()

    val videos = remember {
        mutableListOf<Video>()
    }

    videos.clear()

    videosList.map {
        if (it.key.name == directory) {
            videos.addAll(it.value.sortedBy { video -> video.name })
            return@map
        }
    }

    Scaffold(
        topBar = {
            Header(
                headerTitle = directory,
                contentColor = LocalColor.Monochrome.White,
            ) {
                navController.navigateUp()
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(LocalColor.Base)
        ) {
            LazyColumn() {
                items(videos) {
                    VideoItem(
                        video = it
                    ) {
                        val intent = Intent(context, VideoActivity::class.java)
                        intent.putExtra("video", it)
                        context.startActivity(intent)
                    }
                }
            }
        }
    }
}