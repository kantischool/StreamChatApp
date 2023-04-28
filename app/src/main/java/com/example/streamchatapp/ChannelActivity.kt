package com.example.streamchatapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.example.streamchatapp.ui.theme.StreamChatAppTheme
import dagger.hilt.android.AndroidEntryPoint
import io.getstream.chat.android.client.models.Filters
import io.getstream.chat.android.compose.ui.channels.ChannelsScreen
import io.getstream.chat.android.compose.ui.theme.ChatTheme
import io.getstream.chat.android.compose.viewmodel.channels.ChannelListViewModel

@AndroidEntryPoint
class ChannelActivity : ComponentActivity() {

    val channelViewModel: ChannelViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        subscribeToEvents()

        setContent {
            ChatTheme() {

                var showDialog = remember {
                    mutableStateOf(false)
                }

                if (showDialog.value) {
                    ChannelDialog(
                        dismiss = { channelName ->
                            channelViewModel.createChannel(channelName)
                            showDialog.value = false
                        }
                    )
                }
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                   ChannelsScreen(
                       filters = Filters.`in`("type", values = listOf("team", "messaging")),
                       title = "Channels",
                       isShowingSearch = true,
                       onBackPressed = {finish()},
                       onItemClick = {
                           startActivity(MessageActivity.getIntent(this, it.cid))
                       },
                       onHeaderActionClick = {
                           showDialog.value = true
                       },
                       onHeaderAvatarClick = {
                           channelViewModel.logout()
                           finish()
                           startActivity(Intent(this, LoginActivity::class.java))
                       }
                   )
                }
            }
        }
    }

    private fun subscribeToEvents() {

        lifecycleScope.launchWhenStarted {

            channelViewModel.createChannelEvent.collect { event ->

                when (event) {

                    is ChannelViewModel.CreateChannelEvent.Error -> {
                        val errorMessage = event.error
                        showToast(errorMessage)
                    }

                    is ChannelViewModel.CreateChannelEvent.Success -> {
                        showToast("Channel Created!")
                    }
                }
            }
        }
    }

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun ChannelDialog(dismiss: (String) -> Unit) {

        val channelName = remember {
            mutableStateOf("")
        }

        AlertDialog(
            onDismissRequest = { dismiss(channelName.value) },
            title = {
                Text(text = "Enter Channel Name")
            },
            text = {
                TextField(
                    value = channelName.value,
                    onValueChange = {
                        channelName.value = it
                    }
                )
            },
            confirmButton = {
                Row(
                    modifier = Modifier.padding(all = 8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { dismiss(channelName.value) }
                    ) {
                        Text(text = "Create Channel")
                    }
                }
            }
        )
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    StreamChatAppTheme {
        Greeting("Android")
    }
}