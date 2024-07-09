// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    id("com.google.gms.google-services") version "4.4.2" apply false
    id("com.android.application")
    // Add the ID of the plugin
    id("com.google.gms.google-services")
    id("com.google.firebase.firebase-perf")
}