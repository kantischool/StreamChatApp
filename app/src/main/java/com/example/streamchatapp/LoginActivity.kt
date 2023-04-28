package com.example.streamchatapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
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
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.streamchatapp.ui.theme.StreamChatAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : ComponentActivity() {

    private val loginViewModel: LoginViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        readTheEvents()
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

    private fun readTheEvents() {
        lifecycleScope.launchWhenStarted {
            loginViewModel.loginEvent.collect { event ->
                when (event) {
                    is LoginViewModel.LogInEvent.ErrorInputTooShort -> {
                        Toast.makeText(
                            this@LoginActivity,
                            "user id is too short",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    is LoginViewModel.LogInEvent.ErrorLogIn -> {
                        Toast.makeText(this@LoginActivity, event.error, Toast.LENGTH_SHORT).show()
                    }

                    is LoginViewModel.LogInEvent.Success -> {
                        Toast.makeText(this@LoginActivity, "Successful", Toast.LENGTH_SHORT).show()
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

        loginViewModel.loadingState.observe(this, Observer {loadingStatus ->

        when(loadingStatus){
            is LoginViewModel.LoadingState.Loading -> {
                showProgress.value = true
            }
            is LoginViewModel.LoadingState.NotLoading -> {
                showProgress.value = false
            }
        }

        })
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
                onClick = {
                    loginViewModel.loginUser(
                        username.value.text,
                        getString(R.string.jwt_token)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .padding(15.dp)

            ) {
                Text(text = "Login as user")
            }

            Button(
                onClick = { loginViewModel.loginUser(username.value.text) },
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
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    StreamChatAppTheme {
//        LoginScreen()
    }
}