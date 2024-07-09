plugins {
    alias(libs.plugins.androidApplication)
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.studytimerappcode"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.studytimerappcode"
        minSdk = 34
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
    }
    buildscript{
        repositories {
            // Make sure that you have the following two repositories
            google()  // Google's Maven repository
            mavenCentral()  // Maven Central repository
        }
        dependencies {
            // Add the Maven coordinates and latest version of the plugin
            classpath ("PLUGIN_MAVEN_COORDINATES:PLUGIN_VERSION")
        }
    }
    allprojects {
        repositories {
            // Make sure that you have the following two repositories
            google()  // Google's Maven repository
            mavenCentral()  // Maven Central repository
        }
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.activity)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(platform("com.google.firebase:firebase-bom:33.1.1"))
    implementation("com.google.firebase:firebase-analytics")
}