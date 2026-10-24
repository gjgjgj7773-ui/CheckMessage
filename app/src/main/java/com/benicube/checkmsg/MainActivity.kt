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
    var text by remember { mutableStateOf("") }      // 내가 넣을 문자
    var checked by remember { mutableStateOf(false) } // 체크박스 상태
    var showDialog by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("메시지 문자 입력") },
            singleLine = true,
            // 키보드 엔터 키를 "완료"로 지정
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            // 엔터(완료)를 누르면 키보드를 내리고 메시지 창 표시
            keyboardActions = KeyboardActions(onDone = {
                keyboardController?.hide()
                showDialog = true
            }),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = checked,
                onCheckedChange = { isChecked ->
                    checked = isChecked
                    if (isChecked) showDialog = true   // 체크되는 순간 메시지 표시
                }
            )
            Text("체크하면 메시지 표시")
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("메시지") },
            text = { Text(text.ifBlank { "(입력한 문자가 없습니다)" }) },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) { Text("확인") }
            }
        )
    }
}
