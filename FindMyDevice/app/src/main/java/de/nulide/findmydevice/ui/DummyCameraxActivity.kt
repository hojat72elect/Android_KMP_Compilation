package de.nulide.findmydevice.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Size
import android.view.Surface
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.core.resolutionselector.ResolutionStrategy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.concurrent.futures.await
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import de.nulide.findmydevice.databinding.ActivityDummyCameraxBinding
import de.nulide.findmydevice.net.FMDServerApiRepoSpec
import de.nulide.findmydevice.net.FMDServerApiRepository
import de.nulide.findmydevice.utils.CypherUtils
import de.nulide.findmydevice.utils.imageToByteArray
import de.nulide.findmydevice.utils.log
import kotlinx.coroutines.launch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class DummyCameraxActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityDummyCameraxBinding
    private lateinit var cameraExecutor: ExecutorService
    private var cameraExtra: Int = CAMERA_BACK

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        if (!hasCameraPermission()) {
            this.log().w(TAG, "Camera permission is missing. Not taking picture.")
            finish()
        }
        viewBinding = ActivityDummyCameraxBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
            // On SDK >= 27 we have the flags in the AndroidManifest
            window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON)
        }

        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    override fun onResume() {
        super.onResume()
        if (!this::cameraExecutor.isInitialized) {
            // somehow it doesn't awlays initialise in onCreate
            cameraExecutor = Executors.newSingleThreadExecutor()
        }
        cameraExtra = intent.extras?.getInt(EXTRA_CAMERA) ?: CAMERA_BACK

        lifecycleScope.launch {
            takePhoto()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    private fun hasCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            baseContext, Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    private suspend fun takePhoto() {
        val cameraProvider = ProcessCameraProvider.getInstance(this).await()

        val imageCapture = ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            .setFlashMode(ImageCapture.FLASH_MODE_OFF)
            .setTargetRotation(Surface.ROTATION_0)
            // Set the resolution to 720 x 1280, aka "720p" (flipped because it is in portrait).
            // Or lower, if this resolution is not available.
            // This should be large enough for most use cases.
            // By default CameraX uses the highest resolution, but then the images are large, making the upload slow.
            .setResolutionSelector(
                ResolutionSelector.Builder().setResolutionStrategy(
                    ResolutionStrategy(
                        Size(720, 1280),
                        ResolutionStrategy.FALLBACK_RULE_CLOSEST_LOWER_THEN_HIGHER
                    )
                ).build()
            )
            .build()

        val cameraSelector =
            if (cameraExtra == CAMERA_FRONT) CameraSelector.DEFAULT_FRONT_CAMERA else CameraSelector.DEFAULT_BACK_CAMERA

        cameraProvider.unbindAll()
        try {
            cameraProvider.bindToLifecycle(this, cameraSelector, imageCapture)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            this.log().e(
                TAG,
                "Cannot take picture: bindToLifecycle failed, see the stacktrace. message=${e.message} cause=${e.cause}"
            )
            return
        }

        imageCapture.takePicture(
            cameraExecutor,
            object : ImageCapture.OnImageCapturedCallback() {
                @androidx.annotation.OptIn(androidx.camera.core.ExperimentalGetImage::class)
                override fun onCaptureSuccess(image: ImageProxy) {
                    super.onCaptureSuccess(image)
                    val img = image.image
                    if (img == null) {
                        applicationContext.log().w(TAG, "Captured image was null!")
                        finish()
                        return
                    }
                    val imgBytes = imageToByteArray(img)
                    uploadPhotoAndFinish(imgBytes)
                }

                override fun onError(exception: ImageCaptureException) {
                    super.onError(exception)
                    applicationContext.log()
                        .w(TAG, "Failed to take picture: ${exception.imageCaptureError}")
                }
            })
    }

    private fun uploadPhotoAndFinish(imgBytes: ByteArray) {
        val picture = CypherUtils.encodeBase64(imgBytes)

        // TODO: upload in a background job so that the activity can finish fast
        val repo = FMDServerApiRepository.getInstance(FMDServerApiRepoSpec(this))
        repo.sendPicture(picture)

        finish()
    }

    companion object {
        val TAG = DummyCameraxActivity::class.simpleName

        const val EXTRA_CAMERA = "camera"
        const val CAMERA_BACK = 0
        const val CAMERA_FRONT = 1
    }
}
