package com.eattalk.table.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eattalk.table.R
import com.eattalk.table.ui.theme.AppTheme
import com.eattalk.table.ui.theme.Ref
import com.eattalk.table.ui.type.InputFieldType
import com.eattalk.table.ui.util.DialogManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputField(
    type: InputFieldType,
    text: String,
    onTextChange: (String) -> Unit,
    subText: String? = null,
    subIcon: Painter? = null,
    modifier: Modifier = Modifier,
    modifierTextField: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    focusRequester: FocusRequester? = null,
    placeholder: String? = null,
    isError: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
    keyboardActions: KeyboardActions = KeyboardActions(),
    visualTransformation: VisualTransformation = VisualTransformation.None,
    singleLine: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    colors: TextFieldColors = defaultInputFieldColors()
) {
    val shape = RoundedCornerShape(8.dp)
    val focusRequest by remember { mutableStateOf(focusRequester ?: FocusRequester()) }
    var isFocused by remember { mutableStateOf(false) }
    Row(
        modifier = modifier
            .border(BorderStroke(1.dp, Ref.Stroke.c100), shape)
            .background(
                color = if (enabled) colors.focusedContainerColor else colors.disabledContainerColor,
                shape = shape
            )
            .clip(shape = shape),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (type == InputFieldType.Search) {
            Icon(
                painter = painterResource(id = R.drawable.search),
                contentDescription = "Search Icon",
                modifier = Modifier
                    .padding(start = 12.dp)
                    .size(20.dp),
                tint = Ref.Neutral.c1000
            )
        }


        BasicTextField(
            value = text,
            modifier = modifierTextField
                .weight(1f)
//                .defaultMinSize(
//                    minWidth = TextFieldDefaults.MinWidth,
//                )
                .focusRequester(focusRequest)
                .paddingFromBaseline(
                    top = Ref.Body.s16Regular.topBaselinePadding,
                    bottom = Ref.Body.s16Regular.bottomBaselinePadding
                )
                .onFocusChanged {
                    isFocused = it.isFocused
                },
            onValueChange = onTextChange,
            enabled = enabled,
            readOnly = readOnly,
            textStyle = Ref.Body.s16Regular.textStyle.copy(
                textAlign = if (type == InputFieldType.Number) TextAlign.End else TextAlign.Start,
                color =  if (enabled) colors.focusedTextColor else colors.disabledTextColor
            ),
            cursorBrush = SolidColor(Ref.Primary.c300),
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = if (type == InputFieldType.Default) singleLine else true,
            maxLines = maxLines,
            visualTransformation = visualTransformation,
            decorationBox = @Composable { innerTextField ->
                // places leading icon, text field with label and placeholder, trailing icon
                TextFieldDefaults.DecorationBox(
                    value = text,
                    visualTransformation = visualTransformation,
                    innerTextField = innerTextField,
                    placeholder = {
                        Row {
                            placeholder?.also {
//                                if (type == InputFieldType.Number) {
//                                    Spacer(modifier = Modifier.weight(1f))
//                                }
                                StyleText(
                                    text = it,
                                    style = Ref.Body.s16Regular,
                                    textAlign = if (type == InputFieldType.Number) TextAlign.End else TextAlign.Start,
                                    color = Ref.Neutral.c500,
                                    modifier = Modifier
//                                modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    },
                    singleLine = true,
                    enabled = enabled,
                    isError = isError,
                    interactionSource = interactionSource,
                    colors = colors,
                    contentPadding = PaddingValues(
                        if (type == InputFieldType.Search) 4.dp else 12.dp,
                        9.dp,
                        if (type == InputFieldType.Search) 8.dp else 12.dp,
                        9.dp
                    )
                )
            }
        )

        subText?.let {
            StyleText(
                text = subText,
                style = Ref.Body.s16Regular,
                color = Ref.Neutral.c500,
                modifier = Modifier.padding(end = 12.dp)
            )
        }

        subIcon?.let {
            Image(
                painter = subIcon,
                contentDescription = "Sub Icon",
                modifier = Modifier
                    .padding(end = 16.dp)
                    .size(20.dp)
            )
        }
    }
}

@Composable
fun defaultInputFieldColors(): TextFieldColors = TextFieldDefaults.colors().copy(
    focusedContainerColor = Ref.Neutral.c100,
    errorContainerColor = Ref.Neutral.c100,
    disabledContainerColor = Ref.Neutral.c100,
    unfocusedContainerColor = Ref.Neutral.c100,
    focusedIndicatorColor = Color.Transparent,
    errorIndicatorColor = Color.Transparent,
    disabledIndicatorColor = Color.Transparent,
    unfocusedIndicatorColor = Color.Transparent,
    disabledTextColor = Ref.Neutral.c1000,
    focusedTextColor = Ref.Neutral.c1000,
    unfocusedTextColor = Ref.Neutral.c1000,
    errorTextColor = Ref.Neutral.c1000,
    cursorColor = Ref.Primary.c300,
)

@Composable
fun disabledInputFieldColors(): TextFieldColors = TextFieldDefaults.colors().copy(
    focusedContainerColor = Ref.Neutral.c100,
    errorContainerColor = Ref.Neutral.c100,
    disabledContainerColor = Ref.Neutral.c200,
    unfocusedContainerColor = Ref.Neutral.c100,
    focusedIndicatorColor = Color.Transparent,
    errorIndicatorColor = Color.Transparent,
    disabledIndicatorColor = Color.Transparent,
    unfocusedIndicatorColor = Color.Transparent,
    disabledTextColor = Ref.Neutral.c500,
    focusedTextColor = Ref.Neutral.c1000,
    unfocusedTextColor = Ref.Neutral.c1000,
    errorTextColor = Ref.Neutral.c1000,
    cursorColor = Ref.Primary.c300,
)

@Preview
@Composable
private fun PreviewInputField() {
    var text by remember { mutableStateOf("") }
    val placeholder = "Input Something"

    AppTheme(manager = DialogManager()) {
        Surface(
            color = Ref.Neutral.c900
        ) {
            Column {
                InputField(
                    type = InputFieldType.Default,
                    text = text,
                    onTextChange = { text = it },
                    modifier = Modifier.padding(16.dp),
                    placeholder = placeholder
                )

                InputField(
                    type = InputFieldType.Default,
                    text = text,
                    onTextChange = { text = it },
                    subText = "USD",
                    modifier = Modifier.padding(16.dp),
                    placeholder = placeholder
                )

                InputField(
                    type = InputFieldType.Search,
                    text = text,
                    onTextChange = { text = it },
                    modifier = Modifier.padding(16.dp),
                    placeholder = placeholder
                )

                InputField(
                    type = InputFieldType.Number,
                    text = text,
                    onTextChange = { text = it },
                    modifier = Modifier.padding(16.dp),
                    placeholder = placeholder
                )

                InputField(
                    type = InputFieldType.Default,
                    text = text,
                    subIcon = painterResource(id = R.drawable.error_empty),
                    onTextChange = { text = it },
                    modifier = Modifier.padding(16.dp),
                    placeholder = placeholder
                )

                InputField(
                    type = InputFieldType.Default,
                    text = text,
                    subIcon = painterResource(id = R.drawable.error_empty),
                    onTextChange = { text = it },
                    modifier = Modifier.padding(16.dp),
                    placeholder = placeholder,
                    enabled = false,
                    colors = disabledInputFieldColors()
                )
            }
        }
    }
}
