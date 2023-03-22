package com.example.sawit.ui.view.main

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.activity.viewModels
import com.example.core.base.BaseActivityVM
import com.example.core.utils.PageMessageUtil
import com.example.core.utils.ext.showToast
import com.example.sawit.R
import com.example.sawit.databinding.ActivityMainBinding
import com.example.sawit.ui.model.MainModel
import com.example.sawit.ui.viewmodel.MainViewModel
import com.example.sawit.utils.*
import dagger.hilt.android.AndroidEntryPoint
import java.io.File


@AndroidEntryPoint
class MainActivity:
    BaseActivityVM<ActivityMainBinding, MainViewModel>(ActivityMainBinding::inflate),
    LocationListener{

    private var resultPhotoPath: String? = null
    private val textTakeImage: String by lazy{ getString(R.string.text_take_picture) }
    private val textStop: String by lazy{ getString(R.string.text_stop) }

    private lateinit var locationManager: LocationManager
    private var currentLocation: Location? = null

    private var resultData: MainModel? = null

    override val viewModel: MainViewModel by viewModels()

    override var messageType: PageMessageUtil.Type = PageMessageUtil.Type.SNACK_BAR

    override fun ActivityMainBinding.onViewCreated(savedInstanceState: Bundle?) {
        initTesseract()
        checkPermissionLocation(this@MainActivity, REQUEST_LOCATION){
            getLocation()
        }

        btnTake.setOnClickListener {
            if(btnTake.text.toString() == textTakeImage)
                onActionTakePicture()
            else
                viewModel.stopProcessImage()
        }

        btnSubmit.setOnClickListener {
            MainResultActivity.showPage(this@MainActivity, resultData)
        }
    }

    private fun initTesseract() {
        extractAssets(this@MainActivity)

        viewModel.initTesseract(filesDir.absolutePath)
    }

    override fun observeViewModel(viewModel: MainViewModel) {
        super.observeViewModel(viewModel.apply {

            progress.observe(this@MainActivity){ text ->
                binding.tvProgress.text = text
            }

            processing.observe(this@MainActivity){ isProcess ->
                setViewButtons(isProcess)
            }

            resultProcessImage.observe(this@MainActivity){ result ->
                resultData = result.first

                binding.apply {
                    tvResult.text = result.second
                    btnSubmit.isEnabled = result.second.isNotEmpty()
                }
            }

            thumbnail.observe(this@MainActivity){ bitmap ->
                binding.imgSample.setImageBitmap(bitmap)
            }
        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            REQUEST_CAMERA -> {
                if(grantResults.isGrantResultPassed()){
                    launchCamera()
                }
                return
            }

            REQUEST_LOCATION -> {
                if(grantResults.isGrantResultPassed()){
                    getLocation()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == TAKE_PICTURE && resultCode == Activity.RESULT_OK){
            val filePath = resultPhotoPath
            if(filePath == null) {
                showToast("Failed to capture image!")
                return
            }

            val imageView = binding.imgSample
            viewModel.processImage(File(filePath), imageView.width, imageView.height)
        }
    }

    override fun onLocationChanged(location: Location) {
        currentLocation = location

        viewModel.setLocation(currentLocation)
    }

    override fun onDestroy() {
        locationManager.removeUpdates(this)
        super.onDestroy()
    }

    private fun onActionTakePicture(){
        checkPermissionCamera(this@MainActivity, REQUEST_CAMERA){
            launchCamera()
        }
    }

    private fun launchCamera(){
        dispatchTakePictureIntent(this@MainActivity, TAKE_PICTURE){ currentPhotoPath ->
            resultPhotoPath = currentPhotoPath
        }
    }

    private fun setViewButtons(isProcess: Boolean){
        binding.apply {
            btnTake.text = if(isProcess) textStop else textTakeImage

            if(isProcess)
                btnSubmit.isEnabled = false
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLocation(){
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        currentLocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER)

        if (currentLocation == null)
            currentLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

        if (currentLocation == null)
            currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

        // try to request current location
        if(currentLocation == null){
            locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                10 * 1000,
                10F,
                this@MainActivity
            )
        }

        viewModel.setLocation(currentLocation)
    }

    companion object{

        private const val REQUEST_CAMERA = 201
        private const val TAKE_PICTURE = 202
        private const val REQUEST_LOCATION = 203
    }
}