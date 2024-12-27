import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.serialization)
}

val signingProperties = File(rootDir, "signing.properties")
    .takeIf { it.exists() }
    ?.inputStream()
    ?.use {
        Properties().apply { load(it) }
    }

android {
    namespace = "dev.ryanandrew.supernotelinker"
    compileSdk = 35

    defaultConfig {
        applicationId = "dev.ryanandrew.supernotelinker"
        minSdk = 27
        targetSdk = 35
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    signingConfigs {
        create("release") {
            val storeFilePath = signingProperties?.getProperty("storeFile")
            storeFile = storeFilePath?.let { File(it) }
            storePassword = signingProperties?.getProperty("storePassword")
            keyAlias = signingProperties?.getProperty("keyAlias")
            keyPassword = signingProperties?.getProperty("keyPassword")
        }
    }
    buildTypes {
        release {
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            isMinifyEnabled = true
            isDebuggable = false
            isShrinkResources = true
            signingConfig = signingConfigs.getByName("release")
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
    splits {
        abi {
            isEnable = true
            reset()
            // Supernote Android will only ever be 64-bit ARM, so release versions can just
            //  include arm64-v8a
            val isRelease =
                project.gradle.startParameter.taskNames.any { it.contains("Release", true) }
            when (isRelease) {
                true -> include("arm64-v8a")
                false -> include("armeabi-v7a", "arm64-v8a", "x86", "x86_64")
            }
            isUniversalApk = false
        }
    }
    applicationVariants.all {
        val variant = this
        variant.outputs.all {
            val output = this as com.android.build.gradle.internal.api.BaseVariantOutputImpl
            output.outputFileName = "SupernoteLinker_${variant.versionName}_${variant.buildType.name}.apk"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
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
    implementation(libs.hilt.android)
    implementation(libs.compose.navigation)
    implementation(libs.hilt.navigation.compose)
    implementation(libs.compose.lifecycle)
    implementation(libs.kotlinx.serialization.json)
    ksp(libs.hilt.android.compiler)
    implementation(libs.coil.compose)
}