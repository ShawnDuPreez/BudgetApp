// settings.gradle.kts

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    plugins {
        // pin Kotlin & Android plugins
        id("org.jetbrains.kotlin.android") version "1.9.0"
        id("com.android.application")       version "8.1.4"
        // pin KSP to the matching Kotlin version
        id("com.google.devtools.ksp")      version "1.9.0-1.0.13"
    }
}

rootProject.name = "BudgetApp"
include(":app")
