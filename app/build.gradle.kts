plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    kotlin("plugin.serialization") version "2.1.10"
}

android {
    namespace = "no.uio.ifi.in2000.sondrein.in2000_gruppe3"
    compileSdk = 35

    defaultConfig {
        applicationId = "no.uio.ifi.in2000.sondrein.in2000_gruppe3"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        buildConfigField("String", "MAPBOX_ACCESS_TOKEN", "\"${properties["MAPBOX_ACCESS_TOKEN"]}\"")
        buildConfigField("String", "MAPBOX_SECRET_TOKEN", "\"${properties["MAPBOX_SECRET_TOKEN"]}\"")

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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {

    //DataStore
    implementation(libs.androidx.datastore.preferences)

    // optional - RxJava2 support
    implementation(libs.androidx.datastore.preferences.rxjava2)

    // optional - RxJava3 support
    implementation(libs.androidx.datastore.preferences.rxjava3)

    // mapbox search
    val mapboxSearchVersion = "2.8.0-rc.1"
    implementation("com.mapbox.search:autofill:$mapboxSearchVersion")
    implementation("com.mapbox.search:discover:$mapboxSearchVersion")
    implementation("com.mapbox.search:place-autocomplete:$mapboxSearchVersion")
    implementation("com.mapbox.search:offline:$mapboxSearchVersion")
    implementation("com.mapbox.search:mapbox-search-android:$mapboxSearchVersion")
    implementation("com.mapbox.search:mapbox-search-android-ui:$mapboxSearchVersion")

    // Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.0")

    // Mapbox
    implementation("com.mapbox.maps:android:11.10.2")
    implementation("com.mapbox.extension:maps-compose:11.10.2")
    implementation("io.coil-kt:coil-compose:2.7.0")

    // Navcontroller
    implementation("androidx.compose.ui:ui:1.7.8")
    implementation("androidx.compose.material:material:1.7.8")
    implementation("androidx.compose.ui:ui-tooling-preview:1.7.8")
    implementation("androidx.navigation:navigation-compose:2.8.9")
    implementation("androidx.activity:activity-compose:1.8.2")

    // Ktor
    val ktorVersion = "3.0.3"
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-cio:$ktorVersion")
    implementation("io.ktor:ktor-client-android:$ktorVersion")
    implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-cbor:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-protobuf:$ktorVersion")

    // Standard libs
    implementation(libs.androidx.core.ktx)
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
}