import org.jetbrains.kotlin.gradle.plugin.KaptExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id(PLUGIN_GRADLE_VERSIONS) version Tooling.gradleVersionsPlugin
}

buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:${Tooling.gradlePluginVersion}")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Tooling.kotlin}")
        classpath("com.google.dagger:hilt-android-gradle-plugin:${Hilt.coreHiltVersion}")
        classpath("com.google.protobuf:protobuf-gradle-plugin:${Tooling.protobufPluginVersion}")
        classpath("com.github.ben-manes:gradle-versions-plugin:${Tooling.gradleVersionsPlugin}")
    }
}

allprojects {

    repositories {
        google()
        mavenCentral()
        maven { setUrl("https://jitpack.io") }
    }

    // Without the below block, a build failure was happening when
    // running ./gradlew connectedAndroidTest.
    // See: https://github.com/mockito/mockito/issues/2007#issuecomment-689365556
    configurations.all {
        resolutionStrategy.force("org.objenesis:objenesis:2.6")
    }
}

subprojects {

    tasks.withType<KotlinCompile>().all {
        kotlinOptions {
            freeCompilerArgs = freeCompilerArgs + listOf(
                "-opt-in=kotlinx.coroutines.FlowPreview",
                "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                "-opt-in=kotlinx.serialization.ExperimentalSerializationApi",
                "-opt-in=androidx.compose.material.ExperimentalMaterialApi",
                "-opt-in=androidx.compose.animation.ExperimentalAnimationApi",
                "-opt-in=androidx.compose.ui.ExperimentalComposeUiApi",
                "-opt-in=androidx.compose.foundation.ExperimentalFoundationApi",
                "-opt-in=androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi",
                "-opt-in=com.google.accompanist.pager.ExperimentalPagerApi",
            )

            jvmTarget = Tooling.kotlinCompatibilityVersion.toString()
        }
    }

    plugins.withId(PLUGIN_KOTLIN_KAPT) {
        extensions.findByType<KaptExtension>()?.run {
            correctErrorTypes = true
        }
    }

    // https://stackoverflow.com/a/70348822/7015881
    // https://issuetracker.google.com/issues/238425626
    configurations.all {
        resolutionStrategy.eachDependency {
            if (requested.group == "androidx.lifecycle" && requested.name == "lifecycle-viewmodel-ktx") {
                useVersion(AndroidX.viewModel)
            }
        }
    }
}
