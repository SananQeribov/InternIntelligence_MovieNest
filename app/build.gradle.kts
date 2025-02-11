plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
    id("androidx.navigation.safeargs.kotlin") version "2.8.6" apply false
}

android {
    namespace = "com.legalist.movienest"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.legalist.movienest"
        minSdk = 24
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures{
        viewBinding = true

    }
    buildToolsVersion = "34.0.0"

}

dependencies {
    implementation(project(":data"))
        implementation ("com.pierfrancescosoffritti.androidyoutubeplayer:core:12.1.0")
    // For Preferences DataStore
    implementation ("androidx.datastore:datastore-preferences:1.0.0")

    // For Proto DataStore (if you're using it)
    implementation ("androidx.datastore:datastore:1.0.0")

    implementation ("me.relex:circleindicator:2.1.6")
    implementation ("androidx.navigation:navigation-fragment-ktx:2.8.5")
    implementation ("androidx.navigation:navigation-ui-ktx:2.8.5")
    //animation
    implementation ("com.airbnb.android:lottie:5.0.3")
    implementation ("androidx.viewpager2:viewpager2:1.1.0")
    //retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.picasso:picasso:2.71828")
//circleimage
    implementation ("de.hdodenhof:circleimageview:3.1.0")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(project(":common"))
    implementation(libs.firebase.database)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    implementation(libs.androidx.recyclerview)
    implementation(libs.glide)
    implementation(libs.firebase.storage.ktx)
    implementation(libs.activity)
    implementation(libs.play.services.base)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}