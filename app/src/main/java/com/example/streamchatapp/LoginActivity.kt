package com.example.streamchatapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.streamchatapp.ui.theme.StreamChatAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StreamChatAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LoginScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen() {
    var username = remember {
        mutableStateOf(TextFieldValue(""))
    }

    val showProgress = remember {
        mutableStateOf(false)
    }

    Column {

        OutlinedTextField(
            value = username.value,
            onValueChange = {
                username.value = it
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
                .wrapContentHeight(),
            label = {
                Text(text = "User name")
            }
        )

        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .padding(15.dp)

        ) {
            Text(text = "Login as user")
        }

        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .padding(15.dp)

        ) {
            Text(text = "Login as guest")
        }

        if (showProgress.value)
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .width(50.dp)
                    .height(50.dp)
                    .padding(8.dp),
                strokeWidth = 3.dp
            )


    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    StreamChatAppTheme {
        LoginScreen()
    }
}