package com.eattalk.table.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.eattalk.table.R
import com.eattalk.table.ui.component.StyleText
import com.eattalk.table.ui.theme.Ref

@Composable
fun PopupScreen(
    width: Dp = 610.dp,
    contentModifier: Modifier = Modifier.fillMaxHeight(),
    title: String,
    onClose: (() -> Unit)?,
    content: @Composable () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .wrapContentWidth()
                .width(width)
                .padding(vertical = 54.dp)
                .shadow(20.dp, androidx.compose.foundation.shape.RoundedCornerShape(12.dp))
                .background(
                    Ref.Neutral.c100,
                    androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
                )
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                StyleText(
                    text = title,
                    color = Ref.Neutral.c1000,
                    style = Ref.Head.s20
                )

                Spacer(modifier = Modifier.weight(1f))

                onClose?.apply {
                    IconButton(
                        onClick = onClose,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.close),
                            contentDescription = "Close Icon",
                            modifier = Modifier.size(24.dp),
                            tint = Ref.Neutral.c1000
                        )
                    }
                }
            }
            HorizontalDivider(
                color = Ref.Stroke.c100,
//                    modifier = Modifier.weight(1f)

            )

            Box(
                modifier = contentModifier
                    .fillMaxWidth()

            ) {
                content()
            }
        }
    }
}

@Preview(device = Devices.TABLET, widthDp = 1280, showBackground = true)
@Composable
private fun PreviewPopupScreen() {
    PopupScreen(
        title = "New Product",
        onClose = { }
    ) {
//
//        Box(modifier = Modifier
//            .background(Color.Gray)
//            .size(600.dp))
    }
}

