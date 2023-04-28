package com.example.streamchatapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.models.User
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val chatClient: ChatClient) : ViewModel() {

    private val _loginEvent = MutableSharedFlow<LogInEvent>()
    val loginEvent = _loginEvent.asSharedFlow()

    private val _loadingState = MutableLiveData<LoadingState>()
    val loadingState: LiveData<LoadingState> get() = _loadingState
    private fun isUserValid(userName: String): Boolean {
        return userName.length > 3
    }

    fun loginUser(userName: String, token: String? = null) {
        val trimUser = userName.trim()

        viewModelScope.launch {
            if (isUserValid(userName) && token != null) {
                loginRegisterUser(userName, token)
            } else if (isUserValid(userName) && token == null) {
                loginGuestUser(userName)
            } else {
                _loginEvent.emit(LogInEvent.ErrorInputTooShort)
            }
        }

    }

    private fun loginGuestUser(userName: String) {

        _loadingState.value = LoadingState.Loading
        chatClient.connectGuestUser(userId = userName, username = userName).enqueue {
            _loadingState.value = LoadingState.NotLoading

            if (it.isSuccess) {
                viewModelScope.launch {
                    _loginEvent.emit(LogInEvent.Success)
                }

            } else {
                viewModelScope.launch {
                    _loginEvent.emit(
                        LogInEvent.ErrorLogIn(
                            it.error().message ?: "Something went wrong"
                        )
                    )
                }
            }
        }
    }

    private fun loginRegisterUser(userName: String, token: String) {

        val user = User(id = userName, name = userName)
        _loadingState.value = LoadingState.Loading
        chatClient.connectUser(user, token = token).enqueue {
            _loadingState.value = LoadingState.NotLoading
            if (it.isSuccess) {
                viewModelScope.launch {
                    _loginEvent.emit(LogInEvent.Success)
                }

            } else {
                viewModelScope.launch {
                    _loginEvent.emit(
                        LogInEvent.ErrorLogIn(
                            it.error().message ?: "Something went wrong"
                        )
                    )
                }
            }
        }

    }

    sealed class LogInEvent {
        object ErrorInputTooShort : LogInEvent()
        data class ErrorLogIn(val error: String) : LogInEvent()
        object Success : LogInEvent()
    }

    sealed class LoadingState {
        object Loading : LoadingState()
        object NotLoading : LoadingState()
    }
}