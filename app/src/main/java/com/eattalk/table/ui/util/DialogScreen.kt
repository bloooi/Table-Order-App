package com.eattalk.table.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.eattalk.table.ui.component.DefaultDialog
import com.eattalk.table.ui.component.ETDialog

@Composable
fun DialogScreen(
    manager: DialogManager,
    content: @Composable () -> Unit
) {

    val dialogQueue by manager.dialogQueue.collectAsState()
    val currentDialog = dialogQueue.firstOrNull()
    content()
    currentDialog?.also {
        val onDismissRequest = { manager.dismissDialog()  }
        ETDialog(
            onDismissRequest = onDismissRequest
        ) {
            DefaultDialog(
                title = it.title,
                body = it.body,
                confirm = it.confirm,
                negative = it.negative,
            )
        }
    }
}