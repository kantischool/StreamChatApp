package com.example.streamchatapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.streamchatapp.ui.theme.StreamChatAppTheme
import io.getstream.chat.android.compose.ui.messages.MessagesScreen
import io.getstream.chat.android.compose.ui.theme.ChatTheme

class MessageActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val channelId = intent.getStringExtra(CHANNEL_KEY)
        if (channelId == null) {
            finish()
            return
        }
        setContent {
            ChatTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MessagesScreen(
                        channelId = channelId,
                        messageLimit = 30,
                        onBackPressed = { finish() }
                    )
                }
            }
        }
    }

    companion object {
        const val CHANNEL_KEY = "channel"

        fun getIntent(ctx: Context, channelId: String): Intent {
            return Intent(ctx, MessageActivity::class.java).apply {
                putExtra(CHANNEL_KEY, channelId)
            }
        }
    }
}

@Composable
fun Greeting2(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview3() {
    StreamChatAppTheme {
        Greeting2("Android")
    }
}