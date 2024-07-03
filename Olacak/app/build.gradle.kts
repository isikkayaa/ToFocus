plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.gms.google-services")
    id("androidx.navigation.safeargs.kotlin")
    id("dagger.hilt.android.plugin")
    id("kotlin-parcelize")
}

kapt {
    correctErrorTypes = true
    arguments {
        arg("room.schemaLocation", "$rootDir/schemas")
    }
}
android {
    namespace = "com.example.olacak"
    compileSdk = 34

    buildFeatures{
        viewBinding = true
        dataBinding  = true
    }



    defaultConfig {
        applicationId = "com.example.olacak"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        javaCompileOptions {
            annotationProcessorOptions {
                arguments.plusAssign("room.schemaLocation" to "$projectDir/schemas".toString())
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
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")
    implementation("androidx.preference:preference:1.2.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation("androidx.lifecycle:lifecycle-viewmodel:2.7.0")
    implementation("androidx.activity:activity-ktx:1.9.0")

    implementation("com.google.dagger:hilt-android:2.48.1")
    kapt("com.google.dagger:hilt-android-compiler:2.48.1")

    implementation ("androidx.room:room-runtime:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")


    implementation(platform("com.google.firebase:firebase-bom:32.8.0"))
    implementation("com.google.firebase:firebase-auth:23.0.0")
    implementation("com.google.android.gms:play-services-auth:21.1.1")
    implementation ("com.firebaseui:firebase-ui-auth:7.2.0")

    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
    implementation("com.google.android.material:material:1.12.0")

//    implementation("com.airbnb.android:lottie:3.4.0")


  //  implementation ("com.airbnb.android:lottie:6.0.0")


    implementation ("at.favre.lib:bcrypt:0.9.0")


}