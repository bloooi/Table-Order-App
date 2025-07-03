package com.eattalk.table.ui.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.eattalk.table.ui.theme.AppTextStyle
import com.eattalk.table.ui.theme.AppTheme
import com.eattalk.table.ui.theme.Ref
import com.eattalk.table.ui.theme.TypoProvider
import com.eattalk.table.ui.util.DialogManager

// Style과 Color 강제의 의미가 있음.
@Composable
fun StyleText(
    text: String,
    color: Color,
    style: AppTextStyle,
    textAlign: TextAlign? = null,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
    modifier: Modifier = Modifier,
) {
    StyleText(
        text = AnnotatedString(text),
        color = color,
        style = style,
        textAlign = textAlign,
        maxLines = maxLines,
        overflow = overflow,
        modifier = modifier
    )
}

@Composable
fun StyleText(
    text: AnnotatedString,
    color: Color,
    style: AppTextStyle,
    textAlign: TextAlign? = null,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        color = color,
        style = style.textStyle,
        textAlign = textAlign,
        maxLines = maxLines,
        overflow = overflow,
        modifier = modifier
            .paddingFromBaseline(style.topBaselinePadding, style.bottomBaselinePadding)
    )
}


@Preview(showBackground = true)
@Composable
private fun PreviewStyleText(@PreviewParameter(TypoProvider::class) typos: List<Pair<String, AppTextStyle>>) {
    AppTheme(DialogManager()) {
        Column {
            typos.forEach { typo ->
                StyleText(
                    text = typo.first,
                    color = Ref.Neutral.c1000,
                    style = typo.second
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewBorderStyleText(@PreviewParameter(TypoProvider::class) typos: List<Pair<String, AppTextStyle>>) {
    AppTheme(DialogManager()) {
        Column {
            typos.forEach { typo ->
                Box(modifier = Modifier.border(1.dp, color = Ref.Error.c300)) {
                    StyleText(
                        text = typo.first,
                        color = Ref.Neutral.c1000,
                        style = typo.second
                    )
                }
            }
        }
    }
}