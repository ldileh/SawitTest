package com.example.sawit.ui.viewmodel

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.core.base.BaseViewModel
import com.example.sawit.domain.usecase.MainUseCase
import com.example.sawit.ui.model.MainModel
import com.googlecode.tesseract.android.TessBaseAPI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import java.text.DecimalFormat
import javax.inject.Inject
import kotlin.math.exp
import kotlin.math.max
import kotlin.math.min


@HiltViewModel
class MainViewModel @Inject constructor(
    private val useCase: MainUseCase
) : BaseViewModel(){

    private var tesseract: TessBaseAPI

    private val _processing = MutableLiveData(false)
    val processing: LiveData<Boolean> = _processing

    private val _progress = MutableLiveData<String>()
    val progress: LiveData<String> = _progress

    private val _resultProcessImage = MutableLiveData<Pair<MainModel?, String>>()
    val resultProcessImage: LiveData<Pair<MainModel?, String>> = _resultProcessImage

    private val _thumbnail = MutableLiveData<Bitmap>()
    val thumbnail: LiveData<Bitmap> = _thumbnail

    private var isTesseractInitialize: Boolean = false
    private var stopped: Boolean = true
    private var latitude: Double? = null
    private var longitude: Double? = null
    private var destLocation: Location

    init {
        tesseract = TessBaseAPI{ progress ->
            _progress.postValue("Progress: ${progress.percent}%")
        }
        destLocation = Location("Plaza Indonesia Jakarta").apply {
            latitude = -6.1899966
            longitude = 106.8177066
        }

        _progress.value = "Tesseract version ${tesseract.version} (${tesseract.libraryFlavor})"
    }

    override fun onCleared() {
        super.onCleared()

        if(isProcessing()) tesseract.stop()
        tesseract.recycle()
    }

    fun initTesseract(path: String){
        try {
            isTesseractInitialize = tesseract.init(path, "eng", TessBaseAPI.OEM_TESSERACT_LSTM_COMBINED)
        }catch (e: Exception){
            isTesseractInitialize = false
            Timber.e(e)
        }
    }

    fun processImage(file: File, targetW: Int, targetH: Int){
        if (!isTesseractInitialize){
            Timber.e("Tesseract is not initialized")
            return
        }

        if(isProcessing()){
            Timber.e("Processing is in progress")
            return
        }

        processThumbnail(file.absolutePath, targetW, targetH)

        _resultProcessImage.value = Pair(null, "")
        _processing.value = true
        _progress.value = "Processing..."
        stopped = false

        CoroutineScope(coroutineContext).launch {
            tesseract.setImage(file)

            val startTime = SystemClock.uptimeMillis()

            // Use getHOCRText(0) method to trigger recognition with progress notifications and
            // ability to cancel ongoing processing.
            tesseract.getHOCRText(0)

            // Then get just normal UTF8 text as result. Using only this method would also trigger
            // recognition, but would just block until it is completed.
            val result = processResultText(tesseract.utF8Text)

            _resultProcessImage.postValue(result)
            _processing.postValue(false)

            if (stopped) {
                _progress.postValue("Stopped.")
            } else {
                val duration = SystemClock.uptimeMillis() - startTime
                _progress.postValue("Completed in ${duration / 1000f}.")
            }
        }
    }

    fun stopProcessImage(){
        if (!isProcessing()) return

        tesseract.stop()
        _progress.value = "Stopping..."
        stopped = true
    }

    fun setLocation(location: Location?){
        latitude = location?.latitude
        longitude = location?.longitude

        Timber.e("latitude: $latitude, longitude: $longitude")
    }

    private fun processThumbnail(
        filePath: String,
        targetW: Int,
        targetH: Int
    ) = launch(Dispatchers.IO) {
        // Get the dimensions of the bitmap
        val bmOptions = BitmapFactory.Options()
        bmOptions.inJustDecodeBounds = true

        BitmapFactory.decodeFile(filePath, bmOptions)

        val photoW = bmOptions.outWidth
        val photoH = bmOptions.outHeight

        // Determine how much to scale down the image
        val scaleFactor = max(1, min(photoW / targetW, photoH / targetH))

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false
        bmOptions.inSampleSize = scaleFactor
        bmOptions.inPurgeable = true

        val bitmap = BitmapFactory.decodeFile(filePath, bmOptions)
        _thumbnail.postValue(bitmap)
    }

    private fun isProcessing(): Boolean = processing.value == true

    private fun processResultText(textImage: String): Pair<MainModel, String>{
        val destLocation = Location("Plaza Indonesia Jakarta").apply {
            latitude = -6.1899966
            longitude = 106.8177066
        }

        return if(latitude != null && longitude != null){
            val distanceResult = calculateDistanceLocation(latitude!!, longitude!!)
            val distanceNumber = distanceResult.first
            val distanceFormatted = distanceResult.second

            /**
             * Experimental math calculation.
             * Basically using google maps api for more accurate data
             */
            val speedIs10MetersPerMinute = 10
            val estimatedDriveTimeInMinutes: Float = distanceNumber / speedIs10MetersPerMinute
            val estimatedTimeText = convertTime(estimatedDriveTimeInMinutes)

            val formattedText = "Location Taken Picture: $latitude,$longitude" +
                "\nDistance to ${destLocation.provider}: $distanceFormatted Km" +
                "\nTime Arrival in $estimatedTimeText" +
                "\n\nScanned Text From Image: \n$textImage"

            Pair(
                MainModel(
                    writtenText = textImage,
                    distance = distanceFormatted,
                    estimatedTime = estimatedTimeText
                ),
                formattedText
            )
        }else{
            val formattedText = "Scanned Text From Image: \n$textImage"

            Pair(
                MainModel(
                    writtenText = textImage,
                    distance = "-",
                    estimatedTime = "-"
                ),
                formattedText
            )
        }
    }

    /**
     * This method using aerial distance (reference: https://lambdageeks.com/measure-distance-in-air/),
     * probably not accurate :)
     * Result in this method is Km
     */
    private fun calculateDistanceLocation(currentLat: Double, currentLong: Double): Pair<Float, String>{
        val currentLocation = Location("Current Location").apply {
            latitude = currentLat
            longitude = currentLong
        }

        val distance = currentLocation.distanceTo(destLocation)
        val result = (distance/1000).toDouble()
        val distanceText = DecimalFormat("##.##").format(result)
        return Pair<Float, String>(distance, distanceText)
    }

    private fun convertTime(totalMinutes: Float): String{
        var minutes: String = (totalMinutes % 60).toInt().toString()
        minutes = if (minutes.length == 1) "0$minutes" else minutes
        return "${(totalMinutes / 60).toInt()} Hours : $minutes Minutes"
    }
}