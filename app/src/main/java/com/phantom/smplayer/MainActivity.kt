package com.phantom.smplayer

import android.Manifest
import android.animation.ObjectAnimator
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.animation.AnticipateInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.Modifier
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
import com.phantom.smplayer.home.Home
import com.phantom.smplayer.home.NoPermissions
import com.phantom.smplayer.home.VideoDirectoryScanner
import com.phantom.smplayer.ui.theme.LocalColor
import com.phantom.smplayer.ui.theme.SMPlayerTheme

class MainActivity : ComponentActivity() {

    private val permissionsGiven = mutableIntStateOf(PackageManager.PERMISSION_DENIED)

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun requestPermissions(
        onResult: (granted: Int) -> Unit
    ) {
        val requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    onResult(PackageManager.PERMISSION_GRANTED)
                } else {
                    onResult(PackageManager.PERMISSION_DENIED)
                }
            }

        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_MEDIA_VIDEO
            ) == PackageManager.PERMISSION_GRANTED -> {
                onResult(PackageManager.PERMISSION_GRANTED)
            }

            else -> {
                requestPermissionLauncher.launch(
                    Manifest.permission.READ_MEDIA_VIDEO
                )
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestPermissions(onResult = { permissionsGiven.intValue = it })

        splashScreen.setOnExitAnimationListener { splashScreenView ->
            val slideUp = ObjectAnimator.ofFloat(
                splashScreenView,
                View.TRANSLATION_Y,
                0F,
                -splashScreenView.height.toFloat()
            )
            slideUp.interpolator = AnticipateInterpolator()
            slideUp.duration = 500L

            slideUp.doOnEnd { splashScreenView.remove() }

            slideUp.start()
        }

        Log.i("Harsha", "Load Videos")
        VideoDirectoryScanner(this).loadMedia()

        setContent {
            SMPlayerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = LocalColor.Base
                ) {
                    if (permissionsGiven.intValue == PackageManager.PERMISSION_GRANTED) {
                        Home()
                    } else {
                        NoPermissions {
                            startActivity(
                                Intent(
                                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                    Uri.fromParts("package", packageName, null)
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onResume() {
        super.onResume()
        if (
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_MEDIA_VIDEO
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            permissionsGiven.intValue = PackageManager.PERMISSION_GRANTED
        }
    }
}