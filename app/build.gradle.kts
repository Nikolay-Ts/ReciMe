import org.jetbrains.dokka.DokkaDefaults.includeNonPublic
import org.jetbrains.dokka.DokkaDefaults.outputDir
import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("org.jetbrains.kotlin.plugin.serialization")
    id("org.jetbrains.kotlin.kapt")
    id("org.jetbrains.dokka") version "1.9.20"
}

android {
    namespace = "com.sonnenstahl.recime"
    compileSdk = 35

    val apikeyPropertiesFile = rootProject.file("local.properties")
    val apikeyProperties = Properties()

    if (apikeyPropertiesFile.exists()) {
        apikeyProperties.load(FileInputStream(apikeyPropertiesFile))
    } else {
        println("WARNING: local.properties file not found. API_KEY might be missing or default.")
    }

    val apiKey = apikeyProperties.getProperty("SPOON_API", "SPOON_API")

    defaultConfig {
        applicationId = "com.sonnenstahl.recime"
        minSdk = 28
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "SPOON", "\"$apiKey\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }
}

val ktorVersion: String by project
val navVersion: String by project
val coilVersion: String by project

dependencies {
    implementation("androidx.navigation:navigation-compose:$navVersion")
//    // Views/Fragments integration
//    implementation("androidx.navigation:navigation-fragment:$navVersion")
//    implementation("androidx.navigation:navigation-ui:$navVersion")

    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("io.coil-kt.coil3:coil-compose:$coilVersion")

    implementation("com.google.accompanist:accompanist-swiperefresh:0.24.13-rc")

    implementation("androidx.hilt:hilt-navigation-compose:1.1.0") // or latest

    implementation("com.google.dagger:hilt-android:2.50") // or latest
    kapt("com.google.dagger:hilt-compiler:2.50")

    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-cio:$ktorVersion")
    implementation("io.ktor:ktor-client-okhttp:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
    implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.1")

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
