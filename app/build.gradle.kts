plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.googleDevtoolsKsp)
    id("kotlin-parcelize")
}

android {
    namespace = "com.kisahcode.androidintermediate"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.kisahcode.androidintermediate"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
        buildConfigField("String", "API_KEY", "\"db874e166f4c473e9132d19a45135274\"")
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
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
    packaging {
        resources {
            excludes += listOf("META-INF/AL2.0", "META-INF/LGPL2.1")
        }
    }
    testOptions {
        animationsDisabled = true
    }
}

dependencies {
    // Desugaring
    coreLibraryDesugaring(libs.desugar.jdk.libs)

    // UI
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    implementation(libs.glide)
    implementation(libs.androidx.viewpager2)
    implementation(libs.androidx.fragment.ktx)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Mockito
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.inline)

    // Special Testing
    testImplementation(libs.androidx.core.testing) // InstantTaskExecutorRule
    testImplementation(libs.kotlinx.coroutines.test) //TestDispatcher

    // Special Instrumentation Testing
    androidTestImplementation(libs.androidx.core.testing) // InstantTaskExecutorRule
    androidTestImplementation(libs.kotlinx.coroutines.test) //TestDispatcher
    debugImplementation(libs.androidx.fragment.testing) //launchFragmentInContainer
    androidTestImplementation(libs.espresso.contrib) //RecyclerViewActions

    // Mock Web Server
    androidTestImplementation(libs.mockwebserver)
    androidTestImplementation(libs.okhttp3.okhttp.tls)
    implementation(libs.espresso.idling.resource)
    androidTestImplementation(libs.kotlinx.coroutines.test.v173) //TestDispatcher

    // Room
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)

    // Coroutine Support
    implementation(libs.androidx.lifecycle.viewmodel.ktx) //viewModelScope
    implementation(libs.androidx.lifecycle.livedata.ktx) //liveData
}