package id.tugasakhir.sistempendeteksiwadaikhasbanjar.data

import android.graphics.Bitmap

data class ClassificationResult(
    val classificationResultId: String,
    val bitmap: Bitmap,
    val detectedClassName: String,
    val similarItems: List<String>,
    val confidences: FloatArray
)
