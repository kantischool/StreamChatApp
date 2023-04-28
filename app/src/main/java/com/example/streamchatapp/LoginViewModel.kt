package com.example.streamchatapp

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.chat.android.client.ChatClient
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(val chatClient: ChatClient): ViewModel() {
}