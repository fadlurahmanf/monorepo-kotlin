package com.fadlurahmanf.mapp_mlkit.domain.analyzer

import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import java.lang.Exception

class FaceDetectorAnalyzer(
    private val listener: Listener
) : ImageAnalysis.Analyzer {

    @ExperimentalGetImage
    override fun analyze(image: ImageProxy) {
        val mediaImage = image.image
        if (mediaImage != null) {
            val highAccuracyOpts = FaceDetectorOptions.Builder()
                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
                .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
                .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
                .build()
            val detector = FaceDetection.getClient(highAccuracyOpts)
            detector.process(
                InputImage.fromMediaImage(
                    mediaImage,
                    image.imageInfo.rotationDegrees
                )
            ).addOnSuccessListener { faces ->
                listener.onSuccessGetFaces(faces.toList(), image)
            }.addOnFailureListener {
                listener.onFailedGetFaces(it)
            }
        }
    }

    interface Listener {
        fun onSuccessGetFaces(faces: List<Face>, image: ImageProxy)
        fun onFailedGetFaces(e: Exception)
    }
}