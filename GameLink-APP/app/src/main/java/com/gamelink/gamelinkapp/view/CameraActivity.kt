package com.gamelink.gamelinkapp.view

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.gamelink.gamelinkapp.R
import com.gamelink.gamelinkapp.databinding.ActivityCameraBinding
import com.gamelink.gamelinkapp.service.constants.GameLinkConstants
import com.gamelink.gamelinkapp.service.constants.GameLinkConstants.CAMERA.TAG
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

class CameraActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCameraBinding
    private var imageCapture: ImageCapture? = null
    private lateinit var outputDirectory: File
    private var lensFacing = CameraSelector.LENS_FACING_BACK
    private lateinit var cameraProvider: ProcessCameraProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        outputDirectory = getOutputDirectory()

        if (allPermissionGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this, GameLinkConstants.CAMERA.REQUIRED_PERMISSIONS,
                GameLinkConstants.CAMERA.REQUEST_CODE_PERMISSIONS
            )
        }

        binding.buttonTakePhoto.setOnClickListener {
            takePhoto()
        }

        binding.buttonFlip.setOnClickListener {
            lensFacing = if (lensFacing == CameraSelector.LENS_FACING_FRONT) {
                CameraSelector.LENS_FACING_BACK
            } else {
                CameraSelector.LENS_FACING_FRONT
            }
            bindCameraUserCases()
        }
    }

    private fun getOutputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let { mFile ->
            File(mFile, resources.getString(R.string.app_name)).apply {
                mkdirs()
            }


        }
        return if (mediaDir != null && mediaDir.exists()) mediaDir else filesDir
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return

        val photoFile = File(
            applicationContext.cacheDir,
            SimpleDateFormat(
                GameLinkConstants.CAMERA.FILE_NAME_FORMAT,
                Locale.getDefault()
            ).format(System.currentTimeMillis()) + ".jpg"
        )

        val outputOption = ImageCapture.OutputFileOptions.Builder(photoFile).build()


        imageCapture.takePicture(
            outputOption, ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val savedUri = Uri.fromFile(photoFile)
                    //val msg = "Photo Saved"
                    val intent = Intent()
                    val bundle = Bundle()

                    bundle.putString("photo_uri", savedUri.toString())
                    intent.putExtras(bundle)

                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e(TAG, "onError: ${exception.message}", exception)
                }

            }
        )

    }


    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            cameraProvider = cameraProviderFuture.get()
            bindCameraUserCases()
        }, ContextCompat.getMainExecutor(this))
    }

    private fun bindCameraUserCases() {
        val preview = Preview.Builder()
            .build()
            .also { mPreview ->
                mPreview.setSurfaceProvider(
                    binding.previewCamera.surfaceProvider
                )
            }

        imageCapture = ImageCapture.Builder()
            .build()

        val cameraSelector = CameraSelector.Builder()
            .requireLensFacing(lensFacing)
            .build()

        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                this, cameraSelector, preview, imageCapture
            )
        } catch (e: Exception) {
            Log.d(TAG, "startCamera FAil", e)
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == GameLinkConstants.CAMERA.REQUEST_CODE_PERMISSIONS) {
            if (allPermissionGranted()) {
                startCamera()
            } else {
                Toast.makeText(this, "Permissions not granted by the user", Toast.LENGTH_SHORT)
                    .show()

                finish()
            }
        }


    }

    private fun allPermissionGranted() =
        GameLinkConstants.CAMERA.REQUIRED_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(
                baseContext, it
            ) == PackageManager.PERMISSION_GRANTED
        }
}