package com.example.vknewsclient

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.vknewsclient.ui.theme.VKNewsClientTheme
import com.vk.id.AccessToken
import com.vk.id.RefreshToken
import com.vk.id.VKID
import com.vk.id.VKIDAuthFail
import com.vk.id.VKIDUser
import com.vk.id.auth.AuthCodeData
import com.vk.id.onetap.common.OneTapOAuth
import com.vk.id.onetap.compose.onetap.OneTap
import com.vk.id.onetap.compose.onetap.OneTapTitleScenario
import com.vk.id.refresh.VKIDRefreshTokenCallback
import com.vk.id.refresh.VKIDRefreshTokenFail
import com.vk.id.sample.xml.uikit.common.getOneTapFailCallback
import com.vk.id.sample.xml.uikit.common.getOneTapSuccessCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    val TAG = "ScreenWithVKIDButton"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        VKID.init(this)

        setContent {
            VKNewsClientTheme {
//                ActivityResultTest()
//                MainScreen()
                ScreenWithVKIDButton()
//                CustomApplication()
            }
        }
    }
}

@Composable
fun ScreenWithVKIDButton() {
    val TAG = "ScreenWithVKIDButton"

    val context = LocalContext.current
    Log.d(TAG, "ScreenWithVKIDButton: context: $context")
    var token: AccessToken? by remember { mutableStateOf(null) }
    Scaffold {
        OneTap(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            onAuth = getOneTapSuccessCallback(context) { token = it },
            onFail = getOneTapFailCallback(context),
            fastAuthEnabled = false,
            signInAnotherAccountButtonEnabled = true,
            oAuths = setOf(OneTapOAuth.MAIL, OneTapOAuth.OK)
        )
    }
    token?.let {
        Spacer(modifier = Modifier.height(32.dp))
        UseToken(it)
    }
}

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

