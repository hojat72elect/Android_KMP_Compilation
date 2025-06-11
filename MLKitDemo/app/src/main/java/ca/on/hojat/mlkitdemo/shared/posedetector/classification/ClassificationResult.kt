package ca.on.hojat.mlkitdemo.shared.posedetector.classification

import java.util.Collections

/**
 * Represents Pose classification result as outputted by [PoseClassifier]. Can be manipulated.
 */
class ClassificationResult {
    // For an entry in this map, the key is the class name,
    // and the value is how many times this class appears in the
    // top K nearest neighbors. The value is in range [0, K] and
    // could be a float after EMA smoothing. We use this number to
    // represent the confidence of a pose being in this class.
    private val classConfidences = HashMap<String, Float>()


    val allClasses = classConfidences.keys

    fun getClassConfidence(className: String) =
        if (classConfidences.containsKey(className)) classConfidences[className] ?: 0f
        else
            0f


    val maxConfidenceClass: String
        get() = Collections.max<Map.Entry<String, Float>>(
            classConfidences.entries
        ) { (_, value): Map.Entry<String, Float>, (_, value1): Map.Entry<String, Float> -> (value - value1).toInt() }.key

    fun incrementClassConfidence(className: String) {
        classConfidences[className] =
            if (classConfidences.containsKey(className)) classConfidences[className]!! + 1f else 1f
    }

    fun putClassConfidence(className: String, confidence: Float) {
        classConfidences[className] = confidence
    }
}