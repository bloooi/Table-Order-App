package com.eattalk.table.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eattalk.table.ui.theme.Ref

@Composable
fun ScreenContent(
    modifier: Modifier = Modifier,
    content : @Composable () -> Unit
) {
    Box(
        modifier.fillMaxSize()
            .background(Ref.Neutral.c1000)
    ){
        Box(
            modifier = modifier.fillMaxSize()
                .padding(vertical = 2.dp)
                .padding(end = 2.dp)
                .clip(RoundedCornerShape(7.dp))
                .background(Ref.Neutral.c100)
        ){
            content()
        }
    }
}

@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
private fun PreviewScreenContent() {
    ScreenContent{

    }
}