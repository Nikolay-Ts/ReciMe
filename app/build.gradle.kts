import java.util.Properties
import java.io.FileInputStream // Import FileInputStream for loading the properties file

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
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
                "proguard-rules.pro"
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

dependencies {

    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-cio:$ktorVersion")

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
