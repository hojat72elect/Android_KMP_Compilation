plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    google()
}

dependencies {
    implementation("com.android.tools.build:gradle:7.2.2")
    // https://github.com/google/dagger/issues/3068#issuecomment-999118496
    // Needs to be checked whether JavaPoet is still needed after AGP is updated
    // because currently it forces 1.10 JavaPoet version, for some odd reason.
    implementation("com.squareup:javapoet:1.13.0")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.0")
    implementation("com.google.protobuf:protobuf-gradle-plugin:0.8.19")
    implementation(gradleApi()) // for custom plugins
}
