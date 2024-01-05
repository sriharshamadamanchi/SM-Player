package com.phantom.smplayer.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.phantom.smplayer.R
import com.phantom.smplayer.ui.theme.LocalColor

@Composable
fun DirectoryItem(
    directoryName: String,
    videoCount: Int,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 5.dp)
            .clickable(enabled = true) {
                onClick()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.folder),
            contentDescription = "",
            modifier = Modifier
                .size(80.dp),
            tint = LocalColor.Secondary.Regular
        )

        Column {
            Label(title = directoryName, contentColor = LocalColor.Monochrome.Light)
            Spacer(modifier = Modifier.height(5.dp))
            Label(
                title = "$videoCount Video" + if (videoCount > 1) "s" else "",
                contentColor = LocalColor.Monochrome.Medium,
                s = true
            )
        }
    }
}