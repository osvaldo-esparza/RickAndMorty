plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
}

android {
    namespace = "com.oseo27jul.rickandmorty"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.oseo27jul.rickandmorty"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures{
        viewBinding = true
        dataBinding = true
    }

}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation("com.google.android.material:material:1.4.0")

    // Retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    //Corrutinas
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.6")
    implementation("com.google.android.material:material:1.0.0")
    implementation("androidx.cardview:cardview:1.0.0")

    implementation("com.github.bumptech.glide:glide:4.16.0")

    //viewmodel
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    //livedata
    implementation ("androidx.lifecycle:lifecycle-livedata-core-ktx:2.7.0")
    //activity
    implementation("androidx.activity:activity-ktx:1.8.2")

    implementation ("androidx.fragment:fragment-ktx:1.5.4")
    //implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.0")
}