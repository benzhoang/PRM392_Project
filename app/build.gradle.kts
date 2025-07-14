plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.ksps)
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.example.prm392_project"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.prm392_project"
        minSdk = 33
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    buildFeatures{
        viewBinding = true
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

    implementation("com.tbuonomo:dotsindicator:4.2")
    implementation(libs.simpleratingbar)
    implementation("com.google.code.gson:gson:2.10.1")

    implementation (libs.easyvalidation.core)
    // Hilt
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    implementation(libs.navigation.fragment.ktx)
    implementation(libs.navigation.ui.ktx)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.work)
    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation (libs.logging.interceptor)
    // Glide
    implementation(libs.glide)
    annotationProcessor(libs.compiler)
    // Coroutines
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.core)
    //popup dialog
    implementation (libs.popup.dialog)
    // Bottom sheet
    implementation(libs.input)
    // Avatar Image View
    implementation(libs.avvylib)
    // Image Picker
    implementation (libs.imagepicker)
    // Ted Permission
    implementation ("io.github.ParkSangGwon:tedpermission-normal:3.3.0")
    implementation ("androidx.work:work-runtime:2.9.0")
    implementation ("androidx.core:core-ktx:1.13.1")

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}