import java.util.Properties

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    id("vkid.manifest.placeholders") version "1.1.0" apply true
}

vkidManifestPlaceholders {
    val clientId = "52875875"
    val clientSecret = "hteH1R6oRwb8jOU3fda6"
    init(
        clientId = clientId,
        clientSecret = clientSecret,
    )
    vkidRedirectHost = "vk.com" // Обычно vk.com.
    vkidRedirectScheme = "vk52871484" // Строго в формате vk{ID приложения}.

}