import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-parcelize")
    id("vkid.manifest.placeholders") version "1.1.0" apply true
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp") version "2.1.20-Beta1-1.0.29"
}

android {
    namespace = "com.example.vknewsclient"
    compileSdk = 35

    defaultConfig {
        addManifestPlaceholders(
            mapOf(
                "VKIDRedirectHost" to "vk.com", // Обычно vk.com.
                "VKIDRedirectScheme" to "vk52875875", // Строго в формате vk{ID приложения}.
                "VKIDClientID" to "52875875", // com_vk_sdk_AppId.
                "VKIDClientSecret" to "hteH1R6oRwb8jOU3fda6" // vk_client_secret.
            )
        )
//        setProperty("archivesBaseName", "vkid-${stringProperty("build.type")}-${stringProperty("build.number")}")
        applicationId = "com.example.vknewsclient"
        minSdk = 30
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"


    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        isCoreLibraryDesugaringEnabled = true
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    kapt {
        correctErrorTypes = true
    }
}

dependencies {
    implementation("com.google.accompanist:accompanist-pager:0.16.0")
    implementation("com.google.dagger:hilt-android:2.51.1")
    kapt("com.google.dagger:hilt-android-compiler:2.51.1")
    implementation("androidx.hilt:hilt-navigation-fragment:1.0.0")
    implementation ("androidx.hilt:hilt-navigation-compose:1.2.0")
    implementation ("com.squareup.retrofit2:retrofit:2.11.0")
    implementation ("com.google.code.gson:gson:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.3.0")
    implementation("com.vk.id:vkid:2.2.2")
    implementation("com.vk.id:onetap-compose:2.2.2")
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.1.3")
    implementation("androidx.compose.runtime:runtime-livedata:1.7.6")
    val lifecycle_version = "2.8.7"
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycle_version")
    implementation("androidx.media3:media3-exoplayer:1.3.1")
    implementation("androidx.media3:media3-ui:1.3.1")
    implementation ("androidx.compose.material:material-icons-extended:1.7.6")

    implementation ("androidx.compose.ui:ui:1.2.0-beta02")
    implementation ("androidx.compose.material:material:1.2.0-beta02")
    val room_version = "2.6.1"
    implementation("androidx.room:room-runtime:$room_version")
    ksp("androidx.room:room-compiler:$room_version")
    kapt ("androidx.room:room-ktx:2.6.1")
    implementation ("androidx.room:room-ktx:$rootProject.roomVersion")
//    annotationProcessor("androidx.room:room-compiler:$room_version")


    implementation(libs.androidx.navigation.compose)
    implementation("com.google.code.gson:gson:2.11.0")
    implementation(platform("androidx.compose:compose-bom:2024.12.01"))

    implementation("io.coil-kt.coil3:coil-compose:3.0.4")

    implementation(platform("androidx.compose:compose-bom:2024.12.01"))
    implementation("com.github.bumptech.glide:compose:1.0.0-beta01")

    implementation("com.squareup.okhttp3:logging-interceptor")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}

fun Project.stringProperty(key: String, default: String = ""): String {
    var prop = (properties[key] as? String)?.takeIf { it.isNotEmpty() }
    if (prop == null) {
        prop = (rootProject.properties[key] as? String)?.takeIf { it.isNotEmpty() }
    }
    return prop ?: default
}
