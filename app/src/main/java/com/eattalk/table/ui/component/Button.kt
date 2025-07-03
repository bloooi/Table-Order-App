package com.eattalk.table.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.eattalk.table.R
import com.eattalk.table.ui.theme.AppTheme
import com.eattalk.table.ui.theme.Ref
import com.eattalk.table.ui.theme.body14Regular
import com.eattalk.table.ui.type.Btn
import com.eattalk.table.ui.type.ButtonSize
import com.eattalk.table.ui.type.ButtonType
import com.eattalk.table.ui.type.ButtonTypeProvider
import com.eattalk.table.ui.util.DialogManager


@Composable
fun NormalButton(
    onClick: () -> Unit,
    size: ButtonSize,
    type: ButtonType,
    text: String,
    icon: Painter? = null,
    isFillWidth: Boolean = false,
    isLoading: Boolean = false,
    modifier: Modifier = Modifier,
    shape: Shape? = null,
    border: BorderStroke? = null,
    textStyle: TextStyle? = null,
    contentPadding: PaddingValues? = null,
    borderColor: Color? = null,
    backgroundColor: Color? = null,
    disableBackgroundColor: Color? = null,
    textColor: Color? = null,
    iconColor: Color? = textColor,
    disableTextColor: Color? = null,
    height: Dp? = null,
    enabled: Boolean = true,
    lock: Boolean = false,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val state =
        (if (enabled) (if (isPressed) type.pressed else if (isLoading) type.loading else type.default) else type.disable)

    val typedEnableBackground = (if (isPressed) type.pressed else if (isLoading) type.loading else type.default).solidColor
    val typedDisableBackground =
        if (enabled) backgroundColor ?: typedEnableBackground else type.disable.solidColor

    val typedTextColor = (if (isPressed) type.pressed else type.default).labelColor
    val typedDisableTextColor =
        if (enabled) textColor ?: typedTextColor else type.disable.labelColor

    val typedAppTextStyle = size.labelFont

    val progressColor = type.loading.labelColor ?: Color.Transparent


    val fillModifier = if (isFillWidth) Modifier.fillMaxWidth() else Modifier


    Button(
        interactionSource = interactionSource,
        onClick = onClick,
        enabled = enabled && isLoading.not() && lock.not(),
        modifier = Modifier
            .then(modifier)
            .height(height ?: size.height)
            .then(fillModifier),
        border = border ?: BorderStroke(
            width = if (state.outlineColor != null) 1.dp else 0.dp,
            color = borderColor ?: (if (enabled) {
                if (isPressed)
                    type.pressed.outlineColor
                else
                    type.default.outlineColor
            } else {
                type.disable.outlineColor
            }) ?: Color.White,
        ),
        shape = shape ?: RoundedCornerShape(size.cornerRadius),
        contentPadding = contentPadding ?: PaddingValues(
            start = if (icon != null) 8.dp else size.minPadding,
            end = size.minPadding
        ),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor ?: typedEnableBackground,
            disabledContainerColor = disableBackgroundColor ?: typedDisableBackground,
            contentColor = textColor ?: typedTextColor ?: Color.Transparent,
            disabledContentColor = disableTextColor ?: typedDisableTextColor ?: Color.Transparent
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp,
            focusedElevation = 0.dp,
            hoveredElevation = 0.dp,
            disabledElevation = 0.dp,
        )
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color = progressColor,
                modifier = Modifier.size(20.dp),
                strokeWidth = 2.dp
            )
        } else {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                icon?.let {
                    Icon(
                        painter = icon,
                        contentDescription = "icon",
                        tint = iconColor ?: state.iconColor,
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .size(20.dp)
                    )
                }

                Text(
                    text = text,
                    style = textStyle ?: typedAppTextStyle,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFFFFFFFF
)
@Composable
fun PreviewTypedButton(@PreviewParameter(ButtonTypeProvider::class) data: Pair<ButtonSize, ButtonType>) {
    Column(modifier = Modifier.padding(8.dp)) {
        Row {
            NormalButton(
                onClick = {},
                size = data.first,
                type = data.second,
                text = "Button",
                icon = painterResource(id = R.drawable.add),
            )

            Spacer(modifier = Modifier.width(8.dp))

            NormalButton(
                onClick = {},
                size = data.first,
                type = data.second,
                text = "Button",
                icon = painterResource(id = R.drawable.add),
                enabled = false
            )

            Spacer(modifier = Modifier.width(8.dp))

            NormalButton(
                onClick = {},
                size = data.first,
                type = data.second,
                text = "Button",
                icon = painterResource(id = R.drawable.add),
                isLoading = true
            )

        }

        Row {
            NormalButton(
                onClick = {},
                size = data.first,
                type = data.second,
                text = "Button",
                icon = null,
            )

            Spacer(modifier = Modifier.width(8.dp))

            NormalButton(
                onClick = {},
                size = data.first,
                type = data.second,
                text = "Button",
                icon = null,
                enabled = false
            )

        }

        Spacer(modifier = Modifier.height(16.dp))

        NormalButton(
            onClick = {},
            size = data.first,
            type = data.second,
            text = "Button",
            icon = painterResource(id = R.drawable.add),
            isFillWidth = true,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        NormalButton(
            onClick = {},
            size = data.first,
            type = data.second,
            text = "Button",
            icon = painterResource(id = R.drawable.add),
            isFillWidth = true,
            enabled = false,
            modifier = Modifier.padding(horizontal = 16.dp)
        )


        Spacer(modifier = Modifier.height(16.dp))

        NormalButton(
            onClick = {},
            size = data.first,
            type = data.second,
            text = "Button",
            icon = null,
            isFillWidth = true,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        NormalButton(
            onClick = {},
            size = data.first,
            type = data.second,
            text = "Button",
            icon = null,
            isFillWidth = true,
            isLoading = true,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        NormalButton(
            onClick = {},
            size = data.first,
            type = data.second,
            text = "Button",
            icon = null,
            isFillWidth = true,
            enabled = false,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFFFFFFFF
)
@Composable
fun PreviewCustom() {
    AppTheme(DialogManager()) {
        Column(modifier = Modifier.padding(8.dp)) {
            Column {
                NormalButton(
                    onClick = {},
                    size = Btn.Large,
                    type = Btn.Solid.Primary,
                    text = "Button",
                    textStyle = body14Regular,
                    backgroundColor = Ref.Primary.c600,
                    textColor = Ref.Accent.c300,
                    contentPadding = PaddingValues(horizontal = 6.dp),
                )

                Spacer(modifier = Modifier.width(8.dp))

                NormalButton(
                    onClick = {},
                    text = "Normal Button",
                    size = Btn.Large,
                    type = Btn.Solid.Primary
                )

                NormalButton(
                    onClick = {},
                    text = "Normal Button",
                    size = Btn.Medium,
                    type = Btn.Solid.Primary,
                    modifier = Modifier.fillMaxWidth()
                )

                NormalButton(
                    onClick = {},
                    text = "Normal Button",
                    size = Btn.Small,
                    type = Btn.Solid.Primary
                )


                NormalButton(
                    onClick = { },
                    size = Btn.Small,
                    type = Btn.Outline.Neutral,
                    text = "Button Text",
                    borderColor = Ref.Error.c200,
                    textColor = Ref.Error.c400,
                    height = 22.dp
                )
            }
        }
    }
}