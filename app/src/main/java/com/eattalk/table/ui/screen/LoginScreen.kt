package com.eattalk.table.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.eattalk.table.R
import com.eattalk.table.presentation.start.LoginViewModel
import com.eattalk.table.ui.component.InputField
import com.eattalk.table.ui.component.NormalButton
import com.eattalk.table.ui.component.StyleText
import com.eattalk.table.ui.theme.AppTheme
import com.eattalk.table.ui.theme.Ref
import com.eattalk.table.ui.type.Btn
import com.eattalk.table.ui.type.InputFieldType

@Composable
fun StartLayout(
    viewModel: LoginViewModel = hiltViewModel()
) {
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    val uiStep by viewModel.uiStep.collectAsState()

    val stores by viewModel.stores.collectAsState()

    val isLoading by viewModel.isLoading.collectAsState()

    AppTheme(viewModel.dialogManager) {
        Box {
            when (uiStep) {
                0 -> {
                    LoginScreen(
                        onEmailChange = { viewModel.onEmailChange(it) },
                        onPasswordChange = { viewModel.onPasswordChange(it) },
                        onLoginClick = { viewModel.login() },
                        email = email,
                        password = password
                    )
                }
                1 -> {
                    SelectStoreScreen(
                        onLogout = { viewModel.logout() },
                        onStoreClick = { viewModel.selectStore(it.id) },
                        stores = stores
                    )
                }
            }

            LoadingLayout(isLoading)
        }
    }
}

@Composable
fun LoginScreen(
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    email: String,
    password: String,
) {
    Column(
        modifier = Modifier
            .background(Ref.Background.c200)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(
            modifier = Modifier.width(400.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .padding(bottom = 32.dp)
                    .size(132.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Ref.Primary.a10)
            )

            StyleText(
                text = stringResource(R.string.title_login),
                style = Ref.Head.s24,
                color = Ref.Neutral.c1000,
                modifier = Modifier.padding(bottom = 28.dp)
            )

            StyleText(
                text = stringResource(R.string.email),
                style = Ref.Body.s14Regular,
                color = Ref.Neutral.c800,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 6.dp)
            )

            InputField(
                type = InputFieldType.Default,
                text = email,
                onTextChange = onEmailChange,
                placeholder = stringResource(R.string.login_email_placeholder),
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            )

            StyleText(
                text = stringResource(R.string.password),
                style = Ref.Body.s14Regular,
                color = Ref.Neutral.c800,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 6.dp)
            )

            InputField(
                type = InputFieldType.Default,
                text = password,
                onTextChange = onPasswordChange,
                placeholder = stringResource(R.string.login_password_placeholder),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Go
                ),
                keyboardActions = KeyboardActions { onLoginClick() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
            )

            NormalButton(
                size = Btn.Medium,
                type = Btn.Solid.Primary,
                onClick = onLoginClick,
                enabled = email.isNotEmpty() && password.isNotEmpty(),
                text = stringResource(R.string.login),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
private fun PreviewLoginScreen() {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }
    Box {
        LoginScreen(
            onEmailChange = {
                email = it
            },
            onPasswordChange = {
                password = it
            },
            onLoginClick = {
                loading = true
            },
            email = email,
            password = password
        )
        LoadingLayout(loading)
    }
}