package com.phantom.smplayer.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.phantom.smplayer.MainActivity
import com.phantom.smplayer.components.DirectoryItem
import com.phantom.smplayer.components.Header
import com.phantom.smplayer.ui.theme.LocalColor
import com.phantom.smplayer.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DirectoryList(
    navController: NavHostController
) {

    val mainViewModel: MainViewModel =
        viewModel(viewModelStoreOwner = LocalContext.current as MainActivity)

    val videos = mainViewModel.getVideoDirectories().observeAsState().value ?: emptyMap()

    Scaffold(
        topBar = {
            Header(
                headerTitle = "Folders",
                contentColor = LocalColor.Monochrome.White,
                showBackButton = false
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0XFF010614))
                .padding(innerPadding)
        ) {

            LazyColumn {
                items(videos.keys.toList()) { directory ->
                    DirectoryItem(
                        directoryName = directory.name,
                        videoCount = videos[directory]?.size ?: 0
                    ) {
                        navController.navigate("Directory/${directory.name}")
                    }
                }
            }
        }
    }
}