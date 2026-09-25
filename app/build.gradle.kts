plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.compose)
  alias(libs.plugins.kotlin.serialization)
  alias(libs.plugins.kotlin.parcelize)
  alias(libs.plugins.ksp)
  alias(libs.plugins.hilt)
  alias(libs.plugins.detekt)
}

detekt {
  buildUponDefaultConfig = true
  config.setFrom(files("$rootDir/config/detekt/detekt.yml"))
}

android {
  namespace = "com.jayvijay.growtherapy"
  compileSdk {
    version =
      release(37) {
        minorApiLevel = 1
      }
  }

  defaultConfig {
    applicationId = "com.jayvijay.growtherapy"
    minSdk = 26
    targetSdk = 37
    versionCode = 1
    versionName = "1.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  buildTypes {
    release {
      optimization {
        enable = false
      }
    }
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }

  buildFeatures {
    compose = true
  }

  testOptions {
    unitTests {
      isReturnDefaultValues = true
      isIncludeAndroidResources = true
      all {
        it.jvmArgs("--add-exports=java.base/jdk.internal.access=ALL-UNNAMED")
      }
    }
  }
}

ksp {
  arg("circuit.codegen.mode", "hilt")
}

dependencies {
  detektPlugins(libs.compose.rules.detekt)

  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.lifecycle.runtime.ktx)

  implementation(libs.androidx.activity.compose)
  implementation(libs.androidx.compose.material3)
  implementation(libs.compose.icons.core)
  implementation(libs.compose.icons.extended)
  implementation(libs.androidx.compose.ui)
  implementation(libs.androidx.compose.ui.graphics)
  implementation(libs.androidx.compose.ui.tooling.preview)

  implementation(libs.bundles.circuit)
  implementation(libs.bundles.circuitx)
  implementation(libs.circuit.codegen.annotations)
  ksp(libs.circuit.codegen)

  implementation(libs.hilt.android)
  ksp(libs.hilt.compiler)

  testImplementation(libs.junit)
  testImplementation(libs.circuit.test)
  testImplementation(libs.turbine)
  testImplementation(libs.kotlinx.coroutines.test)
  testImplementation(libs.mockwebserver)
  testImplementation(libs.robolectric)
  testImplementation(libs.androidx.test.core)
  @Suppress("AvoidDuplicateDependencies") testImplementation(libs.androidx.compose.ui.test.junit4)

  androidTestImplementation(libs.androidx.junit)
  androidTestImplementation(libs.androidx.espresso.core)
  @Suppress("AvoidDuplicateDependencies")
  androidTestImplementation(libs.androidx.compose.ui.test.junit4)

  debugImplementation(libs.androidx.compose.ui.tooling)
  debugImplementation(libs.androidx.compose.ui.test.manifest)
}
