package com.fadlurahmanf.core_mlkit.domain.analyzer

import android.graphics.Bitmap
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabel
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import java.lang.Exception
import com.google.mlkit.vision.common.internal.ImageConvertUtils

class ImageLabelerAnalyzer(
    private val listener: Listener
) : ImageAnalysis.Analyzer {


    @ExperimentalGetImage
    override fun analyze(image: ImageProxy) {
        val mediaImage = image.image
        if (mediaImage != null) {
            val labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)
            val inputImage = InputImage.fromMediaImage(
                mediaImage,
                image.imageInfo.rotationDegrees
            )
            val bitmap = ImageConvertUtils.getInstance().getUpRightBitmap(inputImage)
            labeler.process(inputImage).addOnSuccessListener { labels ->
                listener.onSuccessGetLabels(labels.toList(), image, bitmap)
            }.addOnFailureListener {
                listener.onFailedGetLabels(it)
            }
        }
    }

    interface Listener {
        fun onSuccessGetLabels(labels: List<ImageLabel>, image: ImageProxy, bitmapImage: Bitmap)
        fun onFailedGetLabels(e: Exception)
    }
}