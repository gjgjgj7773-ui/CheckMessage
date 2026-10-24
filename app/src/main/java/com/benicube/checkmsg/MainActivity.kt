package com.benicube.checkmsg

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    CheckMessageScreen()
                }
            }
        }
    }
}

@Composable
fun CheckMessageScreen() {
    // 지금 띄울 메시지 (null이면 창 닫힘)
    var dialogMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        CheckMessageItem(number = 1, onShow = { dialogMessage = it })
        Spacer(Modifier.height(32.dp))
        CheckMessageItem(number = 2, onShow = { dialogMessage = it })
    }

    dialogMessage?.let { msg ->
        AlertDialog(
            onDismissRequest = { dialogMessage = null },
            title = { Text("메시지") },
            text = { Text(msg) },
            confirmButton = {
                TextButton(onClick = { dialogMessage = null }) { Text("확인") }
            }
        )
    }
}

/** 입력칸 + 체크박스 한 세트 */
@Composable
fun CheckMessageItem(number: Int, onShow: (String) -> Unit) {
    var text by remember { mutableStateOf("") }
    var checked by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    fun show() = onShow(text.ifBlank { "(${number}번에 입력한 문자가 없습니다)" })

    OutlinedTextField(
        value = text,
        onValueChange = { text = it },
        label = { Text("${number}번 메시지 문자 입력") },
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = {
            keyboardController?.hide()
            show()
        }),
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(Modifier.height(8.dp))

    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(
            checked = checked,
            onCheckedChange = { isChecked ->
                checked = isChecked
                if (isChecked) show()
            }
        )
        Text("${number}번 체크하면 메시지 표시")
    }
}
