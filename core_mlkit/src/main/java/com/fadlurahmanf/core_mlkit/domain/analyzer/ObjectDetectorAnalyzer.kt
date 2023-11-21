package com.fadlurahmanf.core_mlkit.domain.analyzer

import android.graphics.Bitmap
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.common.internal.ImageConvertUtils
import com.google.mlkit.vision.objects.DetectedObject
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions

class ObjectDetectorAnalyzer(
    private val listener: Listener
) : ImageAnalysis.Analyzer {

    private val options = ObjectDetectorOptions.Builder()
        .setDetectorMode(ObjectDetectorOptions.SINGLE_IMAGE_MODE)
        .enableClassification()  // Optional
        .build()

    @ExperimentalGetImage
    override fun analyze(image: ImageProxy) {
        val mediaImage = image.image
        if (mediaImage != null) {
            val inputImage = InputImage.fromMediaImage(mediaImage, image.imageInfo.rotationDegrees)
            val bitmap = ImageConvertUtils.getInstance().getUpRightBitmap(inputImage)
            val detection = ObjectDetection.getClient(options)
            detection.process(inputImage).addOnSuccessListener {
                listener.onSuccessGetLabels(it.toList(), image, bitmap)
            }.addOnFailureListener {
                listener.onFailedGetLabels(it)
            }
        }
    }

    interface Listener {
        fun onSuccessGetLabels(
            objects: List<DetectedObject>,
            image: ImageProxy,
            bitmapImage: Bitmap
        )

        fun onFailedGetLabels(e: Exception)
    }
}