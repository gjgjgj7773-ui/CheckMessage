package com.benicube.checkmsg

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

// 체크박스(입력칸) 개수 — 이 숫자만 바꾸면 세트 수가 바뀝니다
private const val ITEM_COUNT = 3

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
    // 설치된 앱 버전을 자동으로 읽어서 화면에 표시
    val context = LocalContext.current
    val appVersion = remember {
        context.packageManager.getPackageInfo(context.packageName, 0).versionName ?: "?"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "CheckMessage v$appVersion",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.outline
        )
        Spacer(Modifier.height(16.dp))

        for (n in 1..ITEM_COUNT) {
            CheckMessageItem(number = n, onShow = { dialogMessage = it })
            if (n < ITEM_COUNT) Spacer(Modifier.height(28.dp))
        }
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

/** 입력칸 + 체크박스 + 결과값 한 세트 */
@Composable
fun CheckMessageItem(number: Int, onShow: (String) -> Unit) {
    var text by remember { mutableStateOf("") }
    var checked by remember { mutableStateOf(false) }
    var result by remember { mutableStateOf<String?>(null) }
    val keyboardController = LocalSoftwareKeyboardController.current

    // 결과값을 화면에 표시하고 메시지 창도 띄움
    fun show() {
        val msg = text.ifBlank { "(${number}번에 입력한 문자가 없습니다)" }
        result = msg
        onShow(msg)
    }

    OutlinedTextField(
        value = text,
        onValueChange = { text = it },
        label = { Text("${number}번 메시지 문자 입력") },
        singleLine = true,
        // 엔터 = 완료 → 바로 결과 표시
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = {
            keyboardController?.hide()
            show()
        }),
        modifier = Modifier.fillMaxWidth()
    )

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

    result?.let {
        Text(
            "결과: $it",
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(start = 12.dp)
        )
    }
}
