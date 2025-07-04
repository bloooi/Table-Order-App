package com.eattalk.table.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.text.isDigitsOnly
import com.eattalk.table.R
import com.eattalk.table.ui.module.PopupSection
import com.eattalk.table.ui.theme.Ref
import com.eattalk.table.ui.type.Btn
import com.eattalk.table.ui.type.InputFieldType
import com.eattalk.table.ui.type.Selector

@Composable
fun ETDialog(
    onDismissRequest: () -> Unit,
    dismissOnBackPress: Boolean = false,
    dismissOnClickOutside: Boolean = false,
    content: @Composable () -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            dismissOnBackPress = dismissOnBackPress,
            dismissOnClickOutside = dismissOnClickOutside
        )
    ) {
        content()
    }
}

@Composable
fun DefaultDialogSurface(content: @Composable () -> Unit) {
    Surface(
        color = Ref.Neutral.c100,
        shape = RoundedCornerShape(12.dp)
    ) {
        content()
    }
}

@Composable
fun DefaultDialog(
    title: String,
    body: String,
    confirm: Pair<String, () -> Unit>,
    negative: Pair<String, () -> Unit>? = null,
) {
    DefaultDialogSurface {
        Column(
            modifier = Modifier
                .widthIn(max = 580.dp)
                .padding(24.dp)
        ) {
            StyleText(
                text = title,
                color = Ref.Neutral.c1000,
                style = Ref.Head.s20,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            StyleText(
                text = body,
                color = Ref.Neutral.c1000,
                style = Ref.Body.s14Regular,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
            )

            Row(
                modifier = Modifier.padding(top = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                negative?.let {
                    NormalButton(
                        onClick = negative.second,
                        size = Btn.Medium,
                        type = Btn.Outline.Primary,
                        text = negative.first,
                        modifier = Modifier.weight(1f)
                    )
                }

                NormalButton(
                    onClick = confirm.second,
                    size = Btn.Medium,
                    type = Btn.Solid.Primary,
                    text = confirm.first,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Composable
fun RegisterMemberDialog(
    onAdd: (String, String, String, String, (Boolean) -> Unit) -> Unit,
) {
    var isLoading by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("+") }
    var instagram by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    DefaultDialogSurface {
        Column(
            modifier = Modifier
                .widthIn(max = 400.dp)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            StyleText(
                text = stringResource(R.string.register_customer),
                color = Ref.Neutral.c1000,
                style = Ref.Head.s20,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                textAlign = TextAlign.Center
            )
            PopupSection(
                title = buildAnnotatedString {
                    append(stringResource(R.string.register_customer_name))
                    withStyle(style = SpanStyle(color = Ref.Error.c300)) {
                        append(" *")
                    }
                }
            ) {
                InputField(
                    type = InputFieldType.Default,
                    text = name,
                    onTextChange = { if (it.length <= 32) name = it },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next
                    )
                )
            }

            PopupSection(
                title = buildAnnotatedString {
                    append(stringResource(R.string.register_customer_phone))
                    withStyle(style = SpanStyle(color = Ref.Error.c300)) {
                        append(" *")
                    }
                }
            ) {
                InputField(
                    type = InputFieldType.Default,
                    text = phone,
                    onTextChange = { if (it.length <= 32) phone = it },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Phone,
                        imeAction = ImeAction.Next
                    )
                )
            }

            PopupSection(
                title = stringResource(R.string.register_customer_instagram)
            ) {
                InputField(
                    type = InputFieldType.Default,
                    text = instagram,
                    onTextChange = { if (it.length <= 32) instagram = it },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next
                    )
                )
            }
            PopupSection(
                title = stringResource(R.string.register_customer_email)
            ) {
                InputField(
                    type = InputFieldType.Default,
                    text = email,
                    onTextChange = { if (it.length <= 64) email = it },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next
                    )
                )
            }

            NormalButton(
                onClick = {
                    isLoading = true // 작업 시작 전에 true로 설정
                    onAdd(name, phone, instagram, email) { isSuccess ->
                        // onAdd 작업 완료 후 결과에 따라 isLoading 상태 변경
                        isLoading = false
                        if (isSuccess) {

                        }
                    }
                },
                size = Btn.Medium,
                type = Btn.Ghost.Neutral,
                isLoading = isLoading,
                text = stringResource(R.string.register),
                modifier = Modifier.fillMaxWidth(),
                enabled = name.isNotEmpty() && phone.isNotEmpty()
            )
        }
    }

}


@Preview
@Composable
private fun PreviewAppDialog() {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        DefaultDialog(
            title = "Title Text is Very Big",
            body = "This action cannot be undone. This will permanently delete your account and remove your data from our servers.",
            confirm = Pair("Confirm", {}),
            negative = Pair("dismiss", {}),

            )

        DefaultDialog(
            title = "Title Text is Very Big",
            body = "This action cannot be undone. This will permanently delete your account and remove your data from our servers.",
            confirm = Pair("Confirm", {}),

            )
    }
}

@Preview
@Composable
fun PreviewRegisterMembership() {
    RegisterMemberDialog(
        onAdd = { _, _, _, _, _->

        },
    )
}
