// Archivo build.gradle.kts (nivel de proyecto)

buildscript {
    extra.apply {
        set("compose_ui_version", "1.2.0")
        set("accompanist_version", "0.24.2-alpha")
    }
}

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.5.0" apply false
    id("com.android.library") version "8.5.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.23" apply false
    id("com.google.dagger.hilt.android") version "2.50" apply false
}
