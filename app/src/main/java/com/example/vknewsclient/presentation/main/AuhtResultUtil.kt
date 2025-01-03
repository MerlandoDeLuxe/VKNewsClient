package com.example.vknewsclient.presentation.main

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.vk.id.AccessToken
import com.vk.id.OAuth
import com.vk.id.VKIDAuthFail
import com.vk.id.onetap.common.OneTapOAuth

private const val TOKEN_VISIBLE_CHARACTERS = 10

private var currentToast: Toast? = null

public fun showToast(context: Context, text: String) {
    currentToast?.cancel()
    currentToast = Toast.makeText(context, text, Toast.LENGTH_SHORT)
    currentToast?.show()
}

private fun formatToken(token: String): String = token.hideLastCharacters(TOKEN_VISIBLE_CHARACTERS)

private fun String.hideLastCharacters(firstCharactersToKeepVisible: Int): String {
    return if (this.length <= firstCharactersToKeepVisible) {
        this
    } else {
        this.substring(0, firstCharactersToKeepVisible) + "..."
    }
}

public fun copyToClipboard(
    context: Context,
    label: String,
    text: String,
) {
    val clipboard = ContextCompat.getSystemService(context, ClipboardManager::class.java)!!
    val clip = ClipData.newPlainText(label, text)
    clipboard.setPrimaryClip(clip)
}

public fun onVKIDAuthSuccess(
    context: Context,
    oAuth: OAuth?,
    accessToken: AccessToken,
    viewModel: MainViewModel
) {

    val oAuthLabel = oAuth?.name ?: "VK ID"
    showToast(
        context,
        "Auth from $oAuthLabel with token ${formatToken(accessToken.token)}"
    )

    viewModel.onSuccess()
}

public fun getMultibrandingSuccessCallback(
    context: Context,
    viewModel: MainViewModel,
    onToken: (AccessToken) -> Unit,

    ): (OAuth, AccessToken) -> Unit = { oAuth, token ->
    onToken(token)
    onVKIDAuthSuccess(context, oAuth, token, viewModel)
}

public fun getOneTapSuccessCallback(
    context: Context,
    viewModel: MainViewModel,
    onToken: (AccessToken) -> Unit,

    ): (OneTapOAuth?, AccessToken) -> Unit = { oAuth, token ->
    onToken(token)
    onVKIDAuthSuccess(context, oAuth?.toOAuth(), token, viewModel)
}

public fun onVKIDAuthFail(
    context: Context,
    oAuth: OAuth?,
    fail: VKIDAuthFail,
) {
    val oAuthLabel = oAuth?.name ?: "VK ID"
    when (fail) {
        is VKIDAuthFail.Canceled -> {
            showToast(context, "Auth with $oAuthLabel was canceled")
        }

        else -> {
            showToast(context, "Auth with $oAuthLabel failed with: ${fail.description}")
        }
    }
}

public fun getOneTapFailCallback(
    context: Context
): (OneTapOAuth?, VKIDAuthFail) -> Unit = { oAuth, fail ->
    onVKIDAuthFail(context, oAuth?.toOAuth(), fail)
}

public fun getMultibrandingFailCallback(
    context: Context
): (OAuth, VKIDAuthFail) -> Unit = { oAuth, fail ->
    onVKIDAuthFail(context, oAuth, fail)
}
