package com.fadlurahmanf.bebas_onboarding.domain.analyzer

import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
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
            val detection = ObjectDetection.getClient(options)
            detection.process(inputImage).addOnSuccessListener {
                listener.onSuccessGetLabels(it.toList(), image)
            }.addOnFailureListener {
                listener.onFailedGetLabels(it)
            }
        }
    }

    interface Listener {
        fun onSuccessGetLabels(objects: List<DetectedObject>, image: ImageProxy)
        fun onFailedGetLabels(e: Exception)
    }
}