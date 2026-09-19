plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
}

import java.util.Properties

val keystorePropertiesFile = rootProject.file("keystore.properties")
val keystoreProperties = Properties()
val hasKeystore = keystorePropertiesFile.exists()
if (hasKeystore) {
    keystorePropertiesFile.inputStream().use { keystoreProperties.load(it) }
}

// The version lives in gradle.properties, where the release workflow and F-Droid's
// checkupdates both read it. See the comment there before bumping it.
val appVersionName: String = providers.gradleProperty("VERSION_NAME").get()
val appVersionCode: Int = providers.gradleProperty("VERSION_CODE").get().toInt()

android {
    namespace = "com.cocode.tmsmeasurement"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.cocode.tmsmeasurement"
        minSdk = 24
        targetSdk = 36
        versionCode = appVersionCode
        versionName = appVersionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        if (hasKeystore) {
            create("release") {
                storeFile = rootProject.file(keystoreProperties["storeFile"] as String)
                storePassword = keystoreProperties["storePassword"] as String
                keyAlias = keystoreProperties["keyAlias"] as String
                keyPassword = keystoreProperties["keyPassword"] as String
            }
        }
    }
    buildTypes {
        release {
            // F-Droid rejects a release build with minification off for no reason
            // (fdroiddata !49432); the app has no reflection/JSON-model surface that
            // needs keep rules, so shrinking is safe as-is.
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            if (hasKeystore) {
                signingConfig = signingConfigs.getByName("release")
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }

    packaging {
        jniLibs {
            // The only .so file in the APK is a prebuilt from AndroidX (graphics-path). AGP
            // strips it with whatever NDK it finds, so a rebuild without that exact NDK
            // produces different bytes — F-Droid's builder has none unless its recipe pins one.
            // Keeping the symbols leaves the library exactly as its AAR ships it, which
            // rebuilds identically anywhere, and costs a few kB.
            keepDebugSymbols += "**/*.so"
        }
    }

    // AGP otherwise adds a Google-encrypted dependency list to the APK signing block,
    // and F-Droid rejects any release APK that carries it.
    dependenciesInfo {
        includeInApk = false
        includeInBundle = false
    }
}

ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    ksp(libs.androidx.room.compiler)
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
