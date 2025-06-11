package ca.hojat.gamehub.android

import com.android.build.gradle.BaseExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import PLUGIN_ANDROID_APPLICATION
import PLUGIN_KOTLIN_ANDROID
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.kotlin.dsl.findByType
import java.util.Properties

class GameNewsAndroidPlugin : Plugin<Project> {

    private companion object {
        const val BUILD_TYPE_DEBUG = "debug"
        const val BUILD_TYPE_RELEASE = "release"
        const val SIGNING_CONFIG_RELEASE = "release"
        const val KEYSTORE_FILE_NAME = "keystore.properties"
    }

    override fun apply(project: Project) = with(project) {
        setupPlugins()
        configurePlugins()
    }

    private fun Project.setupPlugins() {
        plugins.apply(PLUGIN_KOTLIN_ANDROID)
    }

    private fun Project.configurePlugins() {
        configureAndroidCommonInfo()
        configureAndroidApplicationId()
    }

    private fun Project.configureAndroidCommonInfo() {
        extensions.findByType<BaseExtension>()?.run {
            compileSdkVersion(AppConfig.compileSdkVersion)

            defaultConfig {
                minSdk = AppConfig.minSdkVersion
                targetSdk = AppConfig.targetSdkVersion
                versionCode = AppConfig.versionCode
                versionName = AppConfig.versionName

                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            }

            buildTypes {
                getByName(BUILD_TYPE_DEBUG) {
                    sourceSets {
                        getByName(BUILD_TYPE_DEBUG) {
                            java.srcDir(file("build/generated/ksp/$BUILD_TYPE_DEBUG/java"))
                            java.srcDir(file("build/generated/ksp/$BUILD_TYPE_DEBUG/kotlin"))
                        }
                    }

                    // Enabling accessing sites with http schemas for testing (especially
                    // instrumented tests using MockWebServer) and disabling it in the
                    // production to avoid security issues
                    manifestPlaceholders["usesCleartextTraffic"] = true
                }

                getByName(BUILD_TYPE_RELEASE) {
                    sourceSets {
                        getByName(BUILD_TYPE_RELEASE) {
                            java.srcDir(file("build/generated/ksp/$BUILD_TYPE_RELEASE/java"))
                            java.srcDir(file("build/generated/ksp/$BUILD_TYPE_RELEASE/kotlin"))
                        }
                    }

                    debuggable(true)
                    manifestPlaceholders["usesCleartextTraffic"] = false

                    isMinifyEnabled = true
                    proguardFiles(
                        getDefaultProguardFile("proguard-android-optimize.txt"),
                        "proguard-rules.pro"
                    )
                }
            }

            compileOptions {
                sourceCompatibility = Tooling.javaCompatibilityVersion
                targetCompatibility = Tooling.javaCompatibilityVersion
            }

            lintOptions {
                checkOnly("NewApi", "HandlerLeak", "MissingTranslation")
                baseline(file("lint-baseline.xml"))
            }

        }
    }

    private fun Project.configureAndroidApplicationId() {
        plugins.withId(PLUGIN_ANDROID_APPLICATION) {
            extensions.findByType<BaseAppModuleExtension>()?.run {
                defaultConfig {
                    applicationId = AppConfig.applicationId
                }

                signingConfigs {
                    create(SIGNING_CONFIG_RELEASE) {
                        if (rootProject.file(KEYSTORE_FILE_NAME).canRead()) {
                            val properties = readProperties(KEYSTORE_FILE_NAME)

                            storeFile = file(properties.getValue("storeFile"))
                            storePassword = properties.getValue("storePassword")
                            keyAlias = properties.getValue("keyAlias")
                            keyPassword = properties.getValue("keyPassword")
                        } else {
                            println(
                                """
                                Cannot create a release signing config. The file,
                                $KEYSTORE_FILE_NAME, either does not exist or
                                cannot be read from.
                            """.trimIndent()
                            )
                        }
                    }
                }

                buildTypes {
                    getByName(BUILD_TYPE_RELEASE) {
                        signingConfig = signingConfigs.getByName(SIGNING_CONFIG_RELEASE)
                    }
                }
            }
        }
    }

    private fun Project.readProperties(fileName: String): Properties {
        return Properties().apply {
            load(rootProject.file(fileName).inputStream())
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> Properties.getValue(key: String): T {
        return (get(key) as T)
    }
}
