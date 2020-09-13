import Versions.archServices_version
import Versions.appcompat_version
import Versions.constraint_layout_version
import Versions.dagger_version
import Versions.dependency_updates_version
import Versions.design_version
import Versions.gradle_version
import Versions.jUnit_version
import Versions.kotlin_version
import Versions.ktLint_version
import Versions.lifecycle_extensions_version
import Versions.lifecycle_version
import Versions.test_runner_version
import Versions.localBroadcastManager_version
import org.gradle.kotlin.dsl.DependencyHandlerScope

object Dependencies {
    val gradle = "com.android.tools.build:gradle:$gradle_version"

    val kotlin_gradle_plugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version"
    val kotlin_android_extensions = "org.jetbrains.kotlin:kotlin-android-extensions:$kotlin_version"
    val kotlin_reflect = "org.jetbrains.kotlin:kotlin-reflect:$kotlin_version"
    val kotlin_stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlin_version"

    val androidX_app_compat = "androidx.appcompat:appcompat:$appcompat_version"
    val android_material_design = "com.google.android.material:material:$design_version"
    val androidX_constraint_layout =
        "androidx.constraintlayout:constraintlayout:$constraint_layout_version"

    val androidX_lifecycle_extensions = "androidx.lifecycle:lifecycle-extensions:$lifecycle_extensions_version"
    val androidX_lifecycle_viewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version"
    val androidX_lifecycle_compiler = "androidx.lifecycle:lifecycle-compiler:$lifecycle_version"
    val androidX_localBroadcastManager = "androidx.localbroadcastmanager:localbroadcastmanager:$localBroadcastManager_version"

    val jUnit = "junit:junit:$jUnit_version"
    val androidX_test_runner = "androidx.test:runner:$test_runner_version"

    val dagger_core = "com.google.dagger:dagger:$dagger_version"
    val dagger_android = "com.google.dagger:dagger-android:$dagger_version"
    val dagger_android_support = "com.google.dagger:dagger-android-support:$dagger_version"
    val dagger_compiler = "com.google.dagger:dagger-compiler:$dagger_version"
    val dagger_android_processor = "com.google.dagger:dagger-android-processor:$dagger_version"

    val dependency_updates =
        "com.github.ben-manes:gradle-versions-plugin:$dependency_updates_version"
    val ktLint = "com.pinterest:ktlint:$ktLint_version"

    val archServices = "com.github.DanielKnauf:archservices:$archServices_version"
    val jitPackUrl = "https://jitpack.io"
}

fun DependencyHandlerScope.implementDependencies(){
    "implementation"(Dependencies.androidX_app_compat)
    "implementation"(Dependencies.android_material_design)
    "implementation"(Dependencies.androidX_constraint_layout)
    "implementation"(Dependencies.androidX_localBroadcastManager)

    "implementation"(Dependencies.androidX_lifecycle_viewModel)
    "implementation"(Dependencies.androidX_lifecycle_extensions)
    "kapt"(Dependencies.androidX_lifecycle_compiler)

    //testing
    "testImplementation"(Dependencies.jUnit)
    "androidTestImplementation"(Dependencies.androidX_test_runner)

    //dagger2
    "implementation"(Dependencies.dagger_core)
    "implementation"(Dependencies.dagger_android)
    "implementation"(Dependencies.dagger_android_support)
    "kapt"(Dependencies.dagger_compiler)
    "kapt"(Dependencies.dagger_android_processor)

    //kotlin
    "implementation"(Dependencies.kotlin_reflect)
    "implementation"(Dependencies.kotlin_stdlib)

    //archServices
    "implementation"(Dependencies.archServices)
}
