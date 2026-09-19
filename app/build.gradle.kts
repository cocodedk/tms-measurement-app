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

// The release workflow computes the version from the latest git tag and passes it
// in; F-Droid passes it as a Gradle property (gradleprops in the build recipe).
// A local build with neither just gets 0.0.0 and never pretends otherwise.
val appVersionName: String = providers.gradleProperty("VERSION_NAME").orNull?.takeIf { it.isNotBlank() }
    ?: System.getenv("VERSION_NAME")?.takeIf { it.isNotBlank() }
    ?: "0.0.0"
val semver = appVersionName.split(".")
val vMajor = semver.getOrNull(0)?.toIntOrNull() ?: 0
val vMinor = semver.getOrNull(1)?.toIntOrNull() ?: 0
val vPatch = semver.getOrNull(2)?.toIntOrNull() ?: 0
// Ceiling: 999 minor and 999 patch releases per level before this overflows. Floored
// at 1 (only ever engages for the 0.0.0 fallback): AGP rejects versionCode 0 outright,
// and buildSmoke/CI build with no VERSION_NAME set.
val appVersionCode: Int = maxOf(1, vMajor * 1_000_000 + vMinor * 1_000 + vPatch)

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
            isMinifyEnabled = false
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
