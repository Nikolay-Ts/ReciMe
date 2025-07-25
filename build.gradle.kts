// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    id("org.jetbrains.kotlin.plugin.serialization")
    id("org.jetbrains.dokka") version "1.9.20"
}

tasks.dokkaHtmlMultiModule {
    includes.from("README.md")
}
