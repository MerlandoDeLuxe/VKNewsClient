package com.example.vknewsclient.presentation.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.vknewsclient.presentation.news.CreatingVideoPlayer
import com.vk.id.AccessToken
import com.vk.id.RefreshToken
import com.vk.id.VKID
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Preview
@Composable
fun LoginScreen(
//    onLoginClickListener: () -> Unit
) {

    val TAG = "LoginScreen"

//    CreatingVideoPlayer()
    val viewModel : MainViewModel = hiltViewModel()
    val authState = viewModel.authState.observeAsState(AuthState.Initial)

    when (authState.value) {
        AuthState.Authorized -> {

            MainScreen()
        }

        AuthState.Initial -> {

        }

        AuthState.NotAuthorized -> {

        }
    }

}
//@Composable
//fun LoginScreen(
//    onLoginClick: () -> Unit
//) {
//    val context = LocalContext.current
//    // for demo purpose only, use secured place to keep token
//    var token: AccessToken? by remember { mutableStateOf(null) }
//    val viewModel: MainViewModel = viewModel()
//    val authState = viewModel.authState.observeAsState(AuthState.Initial)
//    val TAG = "LoginScreen"
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
////            .verticalScroll(rememberScrollState())
//            .padding(horizontal = 16.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Spacer(modifier = Modifier.height(32.dp))
//        com.vk.id.onetap.compose.onetap.OneTap(
//            onAuth = getOneTapSuccessCallback(context, viewModel = viewModel) { token = it },
//            onFail = getOneTapFailCallback(context),
//            signInAnotherAccountButtonEnabled = true,
//            oAuths = setOf(OneTapOAuth.MAIL, OneTapOAuth.OK),
//        )
//        token?.let {
//            Spacer(modifier = Modifier.height(32.dp))
////            UseToken(it)
//            onLoginClick()
//            when (authState.value) {
//                AuthState.Authorized -> {
//                    MainScreen()
//                    Log.d(TAG, "LoginScreen: мы тут AuthState.Authorized")
//                }
//
//                AuthState.Initial -> {
//                    Log.d(TAG, "LoginScreen: мы тут AuthState.Initial")
//                }
//                AuthState.NotAuthorized -> {
//                    LoginScreen {
//                        Log.d(TAG, "LoginScreen: мы тут AuthState.NotAuthorized")
//                    }
//                }
//            }
//        }
//    }
//}

@Composable
internal fun UseToken(accessToken: AccessToken) {
    Column(horizontalAlignment = Alignment.Start) {
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            color = MaterialTheme.colorScheme.onBackground,
            text = """
                |Token: ${accessToken.token.take(@Suppress("MagicNumber") 10)}...
                |Current user: ${accessToken.userID}
                |First name: ${accessToken.userData.firstName}
                |Last name: ${accessToken.userData.lastName}
                |Phone: ${accessToken.userData.phone}
                |Email: ${accessToken.userData.email}
                |Scopes: ${accessToken.scopes?.joinToString()}
            """.trimMargin()
        )
        Spacer(modifier = Modifier.height(12.dp))
        var refreshToken: RefreshToken? by remember { mutableStateOf(null) }
        LaunchedEffect(Unit) {
            refreshToken = withContext(Dispatchers.IO) { VKID.instance.refreshToken }
        }
        refreshToken?.let {
            Text(
                color = MaterialTheme.colorScheme.onBackground,
                text = """
                |Refresh token: ${it.token.take(@Suppress("MagicNumber") 5)}...
                |Scopes: ${it.scopes?.joinToString()}
                """.trimMargin()
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}