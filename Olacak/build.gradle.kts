// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript{
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://jitpack.io")
        }

    }

    dependencies{
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.7.7")
        classpath("com.google.gms:google-services:4.4.1")
        classpath ("com.google.dagger:hilt-android-gradle-plugin:2.48.1")
    }

}

plugins {
    id("com.android.application") version "8.2.2" apply false
    id("com.android.library") version "7.3.1" apply false
    id("org.jetbrains.kotlin.android") version "1.9.22" apply false
    id("com.google.gms.google-services") version "4.4.1" apply false

    id("com.google.dagger.hilt.android") version "2.44" apply false
}

