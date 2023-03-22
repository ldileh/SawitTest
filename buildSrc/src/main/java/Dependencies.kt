@Suppress("unused")
object BuildPlugins{
    const val android = "com.android.tools.build:gradle:${Versions.gradlePlugin}"
    const val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.Kotlin.gradle}"
    const val hilt = "com.google.dagger:hilt-android-gradle-plugin:${Versions.hilt}"
}

@Suppress("unused")
object Dependencies {
    const val appCompat = "androidx.appcompat:appcompat:${Versions.appCompat}"
    const val material = "com.google.android.material:material:${Versions.material}"
    const val constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
    const val androidSupport = "com.android.support:support-v4:${Versions.androidSupport}"
    const val lifeCycleJava = "androidx.lifecycle:lifecycle-common-java8:${Versions.lifeCycleJava}"

    const val KotlinCore = "androidx.core:core-ktx:${Versions.Kotlin.core}"
    const val KotlinStdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.Kotlin.gradle}"
    const val KotlinCoroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.Kotlin.coroutines}"
    const val KotlinCoroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.Kotlin.coroutines}"

    const val RoomRuntime = "androidx.room:room-runtime:${Versions.room}"
    const val RoomCompiler = "androidx.room:room-compiler:${Versions.room}"

    const val RetrofitOkhttp = "com.squareup.okhttp3:okhttp:${Versions.Retrofit.okhttp}"
    const val RetrofitCore = "com.squareup.retrofit2:retrofit:${Versions.Retrofit.core}"
    const val RetrofitConverterGson = "com.squareup.retrofit2:converter-gson:${Versions.Retrofit.core}"
    const val RetrofitLoggingInterceptor = "com.squareup.okhttp3:logging-interceptor:${Versions.Retrofit.loggingInterceptor}"
    const val RetrofitConverterScalars = "com.squareup.retrofit2:converter-scalars:${Versions.Retrofit.converterScalars}"

    const val DaggerCore = "com.google.dagger:dagger:${Versions.dagger2}"
    const val DaggerCompiler = "com.google.dagger:dagger-compiler:${Versions.dagger2}"
    const val DaggerAndroid = "com.google.dagger:dagger-android:${Versions.dagger2}"
    const val DaggerAndroidSupport = "com.google.dagger:dagger-android-support:${Versions.dagger2}"
    const val DaggerAndroidProcessor = "com.google.dagger:dagger-android-processor:${Versions.dagger2}"

    const val GlideCore = "com.github.bumptech.glide:glide:${Versions.glide}"
    const val GlideCompiler = "com.github.bumptech.glide:compiler:${Versions.glide}"

    const val LifecycleExt = "androidx.lifecycle:lifecycle-extensions:${Versions.lifecycle}"
    const val LifecycleLiveData = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycle}"
    const val LifecycleViewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}"

    const val Hilt = "com.google.dagger:hilt-android:${Versions.hilt}"
    const val HiltCompiler = "com.google.dagger:hilt-compiler:${Versions.hilt}"
    const val HiltViewModel = "androidx.hilt:hilt-lifecycle-viewmodel:${Versions.HiltAndroidX.viewModel}"
    const val HiltViewModelCompiler = "androidx.hilt:hilt-compiler:${Versions.HiltAndroidX.compiler}"
    const val HiltActivity = "androidx.activity:activity-ktx:${Versions.HiltAndroidX.activity}"
    const val HiltFragment = "androidx.fragment:fragment-ktx:${Versions.HiltAndroidX.fragment}"

    const val Timber = "com.jakewharton.timber:timber:${Versions.timber}"

    const val Parceler = "org.parceler:parceler:${Versions.parceler}"
    const val ParcelerApi = "org.parceler:parceler-api:${Versions.parceler}"

    const val jUnit = "androidx.test.ext:junit:${Versions.jUnit}"

    const val Tesseract = "cz.adaptech.tesseract4android:tesseract4android-openmp:${Versions.tesseract}"
}