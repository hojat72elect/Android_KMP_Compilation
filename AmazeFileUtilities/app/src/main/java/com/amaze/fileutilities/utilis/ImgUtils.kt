package com.amaze.fileutilities.utilis

import android.graphics.Bitmap
import java.util.PriorityQueue
import kotlin.math.pow
import kotlin.math.roundToInt
import org.opencv.android.Utils
import org.opencv.core.Core
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.core.MatOfDouble
import org.opencv.core.MatOfFloat
import org.opencv.core.MatOfInt
import org.opencv.core.Scalar
import org.opencv.core.Size
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.imgproc.Imgproc
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class ImgUtils {

    companion object {

        var log: Logger = LoggerFactory.getLogger(ImgUtils::class.java)

        const val DATAPOINTS = 8
        const val THRESHOLD = 100
        const val ASSERT_DATAPOINTS = 6
        const val PIXEL_POSITION_NORMALIZE_FACTOR = 20
        const val PIXEL_INTENSITY_NORMALIZE_FACTOR = 50
        val wordRegex = "^[A-Za-z]*$".toRegex()

        fun convertMatToBitmap(input: Mat): Bitmap? {
            var bmp: Bitmap? = null
            val rgb = Mat()
            try {
                Imgproc.cvtColor(input, rgb, Imgproc.COLOR_BGR2RGB)
                bmp = Bitmap.createBitmap(rgb.cols(), rgb.rows(), Bitmap.Config.ARGB_8888)
                Utils.matToBitmap(rgb, bmp)
            } catch (e: Exception) {
                log.warn("failed to convert mat to bitmap", e)
            }
            return bmp
        }

        fun readImage(path: String): Mat? {
            if (!path.doesFileExist()) {
                log.warn("failed to read matrix from path as file not found")
                return null
            }
            val mat = Imgcodecs.imread(path)
            if (mat.empty() || mat.height() == 0 || mat.width() == 0) {
                log.warn("failed to read matrix from path as image parameters empty")
                return null
            }
            return mat
        }

        fun isImageBlur(
            path: String
        ): Boolean? {
            return try {
                val matrix = readImage(path)
                if (matrix == null) {
                    log.warn("failure to find blur for input")
                    return false
                }
                val factor = laplace(matrix)
                if (factor == Double.MAX_VALUE) {
                    return null
                }
                if (factor < 50 && factor != 0.0) {
                    return true
                }
                return false
            } catch (e: Exception) {
                log.warn("Failed to check for blurry image", e)
                null
            } catch (oom: OutOfMemoryError) {
                log.warn("Failed to check for low light image", oom)
                null
            }
        }

        fun isImageLowLight(
            path: String
        ): Boolean? {
            return try {
                val matrix = readImage(path)
                if (matrix == null) {
                    log.warn("failure to find low light for input")
                    return false
                }
                processForLowLight(matrix)
            } catch (e: Exception) {
                log.warn("Failed to check for low light image", e)
                null
            } catch (oom: OutOfMemoryError) {
                log.warn("Failed to check for low light image", oom)
                null
            }
        }

        fun getHistogram(inputPath: String, widthPx: Double, heightPx: Double): Bitmap? {
            return try {
                val matrix = readImage(inputPath)
                if (matrix == null) {
                    log.warn("failure to find input for histogram for path {}", inputPath)
                    return null
                }
                val histograms = processHistogram(matrix, heightPx)
                val histMatBitmap = Mat(
                    Size(256.0, heightPx), CvType.CV_8UC4,
                    Scalar(
                        0.0, 0.0,
                        0.0, 0.0
                    )
                )
                /*val colorsBgr =
                    arrayOf(Scalar(0.0, 0.0, 200.0, 255.0),
                        Scalar(0.0, 200.0, 0.0, 255.0),
                        Scalar(200.0, 0.0, 0.0, 255.0))*/
                histograms.forEachIndexed { index, mat ->
                    for (j in 0 until 256) {
                        /*val p1 = Point(
                            (binWidth * (j - 1)).toDouble(), (heightPx - Math.round(
                                mat.get(j - 1, 0)[0]
                            ))
                        )
                        val p2 =
                            Point(
                                (binWidth * j).toDouble(),
                                (heightPx - Math.round(mat.get(j, 0)[0]))
                            )
                        Imgproc.line(histMatBitmap, p1, p2, colorsBgr.get(index), 4, 16, 0)*/
                        val heightCalc = heightPx - Math.round(mat.get(j, 0)[0])
                        for (pt in heightPx.toInt() - 1 downTo heightCalc.toInt()) {
                            val existingChannel1 = histMatBitmap.get(pt, j)[0]
                            val existingChannel2 = histMatBitmap.get(pt, j)[1]
                            val existingChannel3 = histMatBitmap.get(pt, j)[2]
                            when (index) {
                                0 ->
                                    histMatBitmap.put(
                                        pt, j,
                                        existingChannel1, existingChannel2, 200.0, 0.0
                                    )

                                1 ->
                                    histMatBitmap.put(
                                        pt, j,
                                        existingChannel1, 200.0, existingChannel3, 0.0
                                    )

                                2 ->
                                    histMatBitmap.put(
                                        pt, j,
                                        200.0, existingChannel2, existingChannel3, 0.0
                                    )
                            }
                        }
                    }
                }
                val resultBitmap = Mat()
                Imgproc.resize(histMatBitmap, resultBitmap, Size(widthPx, heightPx))

                val resultBitmapFiltered = Mat(
                    resultBitmap.rows(), resultBitmap.cols(),
                    resultBitmap.type()
                )
                Imgproc.medianBlur(resultBitmap, resultBitmapFiltered, 19)

                val histBitmap = Bitmap.createBitmap(
                    resultBitmapFiltered.cols(),
                    resultBitmapFiltered.rows(),
                    Bitmap.Config.ARGB_8888
                )
                Utils.matToBitmap(resultBitmapFiltered, histBitmap)
                resultBitmapFiltered.release()
                resultBitmap.release()
                histMatBitmap.release()
                histograms.forEach { it.release() }
                matrix.release()
                return histBitmap
            } catch (e: Exception) {
                log.warn("Failed to get histogram for {}", inputPath, e)
                null
            } catch (oom: OutOfMemoryError) {
                log.warn("Failed to get histogram for {}", inputPath, oom)
                null
            }
        }

        fun getHistogramChannelsWithPeaks(inputPath: String): List<List<Pair<Int, Int>>>? {
            return try {
                val matrix = readImage(inputPath)
                if (matrix == null) {
                    log.warn("failure to find input for histogram for path {}", inputPath)
                    return null
                }
                val histograms = processHistogram(
                    matrix,
                    THRESHOLD.toDouble()
                )

                val windowWidth = 256 / DATAPOINTS
                val priorityQueueBlue = PriorityQueue<Pair<Int, Int>>(
                    windowWidth + 1
                ) { o1, o2 -> o1.second.compareTo(o2.second) }
                val priorityQueueGreen = PriorityQueue<Pair<Int, Int>>(
                    windowWidth + 1
                ) { o1, o2 -> o1.second.compareTo(o2.second) }
                val priorityQueueRed = PriorityQueue<Pair<Int, Int>>(
                    windowWidth + 1
                ) { o1, o2 -> o1.second.compareTo(o2.second) }

                val blueTopValues: MutableList<Pair<Int, Int>> = mutableListOf()
                val greenTopValues: MutableList<Pair<Int, Int>> = mutableListOf()
                val redTopValues: MutableList<Pair<Int, Int>> = mutableListOf()
                histograms.forEachIndexed { index, mat ->
                    for (j in 0 until 256) {
                        val channelCurrentLevel = mat.get(j, 0)[0].roundToInt()
                        when (index) {
                            0 -> {
                                if (j % windowWidth == 0) {
                                    if (!priorityQueueBlue.isEmpty()) {
                                        blueTopValues.add(priorityQueueBlue.remove())
                                        priorityQueueBlue.clear()
                                    }
                                } else {
                                    priorityQueueBlue.add(Pair(j, channelCurrentLevel))
                                }
                            }

                            1 -> {
                                if (j % windowWidth == 0) {
                                    if (!priorityQueueGreen.isEmpty()) {
                                        greenTopValues.add(priorityQueueGreen.remove())
                                        priorityQueueGreen.clear()
                                    }
                                } else {
                                    priorityQueueGreen.add(Pair(j, channelCurrentLevel))
                                }
                            }

                            2 -> {
                                if (j % windowWidth == 0) {
                                    if (!priorityQueueRed.isEmpty()) {
                                        redTopValues.add(priorityQueueRed.remove())
                                        priorityQueueRed.clear()
                                    }
                                } else {
                                    priorityQueueRed.add(Pair(j, channelCurrentLevel))
                                }
                            }
                        }
                    }
                }
                if (!priorityQueueBlue.isEmpty()) {
                    blueTopValues.add(priorityQueueBlue.remove())
                    priorityQueueBlue.clear()
                }
                if (!priorityQueueGreen.isEmpty()) {
                    greenTopValues.add(priorityQueueGreen.remove())
                    priorityQueueGreen.clear()
                }
                if (!priorityQueueRed.isEmpty()) {
                    redTopValues.add(priorityQueueRed.remove())
                    priorityQueueRed.clear()
                }
                histograms.forEach { it.release() }
                matrix.release()
                return mutableListOf(blueTopValues, greenTopValues, redTopValues)
            } catch (e: Exception) {
                log.warn("Failed to process similar images histogram for {}", inputPath, e)
                null
            } catch (oom: OutOfMemoryError) {
                log.warn("Failed to process similar images histogram for {}", inputPath, oom)
                null
            }
        }

        private fun processHistogram(inputMat: Mat, heightPx: Double): List<Mat> {
            val bHist = Mat()
            val gHist = Mat()
            val rHist = Mat()
            val bgrPlane = ArrayList<Mat>(3)
            Core.split(inputMat, bgrPlane)
            Imgproc.calcHist(
                arrayListOf(bgrPlane[0]), MatOfInt(0), Mat(), bHist,
                MatOfInt(256), MatOfFloat(0f, 256f), false
            )
            Imgproc.calcHist(
                arrayListOf(bgrPlane[1]), MatOfInt(0), Mat(), gHist,
                MatOfInt(256), MatOfFloat(0f, 256f), false
            )
            Imgproc.calcHist(
                arrayListOf(bgrPlane[2]), MatOfInt(0), Mat(), rHist,
                MatOfInt(256), MatOfFloat(0f, 256f), false
            )
            Core.normalize(
                bHist, bHist, heightPx,
                0.0, Core.NORM_INF
            )
            Core.normalize(
                gHist, gHist, heightPx,
                0.0, Core.NORM_INF
            )
            Core.normalize(
                rHist, rHist, heightPx,
                0.0, Core.NORM_INF
            )
            return arrayListOf(bHist, gHist, rHist)
        }

        private fun processForLowLight(matrix: Mat): Boolean? {
            return try {
                val zerosPair = getTotalAndZeros(matrix)
                val ratio = (zerosPair.second.toDouble() / zerosPair.first.toDouble())
                matrix.release()
                return ratio >= 0.8
            } catch (e: Exception) {
                log.warn("Failed to check for low light image", e)
                null
            }
        }

        private fun laplace(image: Mat): Double {
            val destination = Mat()
            val matGray = Mat()
            return try {
                val resizedImage = resize(image, getGenericWidth(image), getGenericHeight(image))

                Imgproc.cvtColor(resizedImage, matGray, Imgproc.COLOR_BGR2GRAY)
                Imgproc.Laplacian(matGray, destination, 3)
                val median = MatOfDouble()
                val std = MatOfDouble()
                Core.meanStdDev(destination, median, std)

                resizedImage.release()
                matGray.release()
                destination.release()
                image.release()

                std[0, 0][0].pow(2.0)

            } catch (e: Exception) {
                log.warn("Failed to check for blurry image", e)
                Double.MAX_VALUE
            }
        }

        private fun getTotalAndZeros(matrix: Mat): Pair<Int, Int> {
            try {
                val resizedImage = resize(
                    matrix, getGenericWidth(matrix),
                    getGenericHeight(matrix)
                )
                val matGray = gray(resizedImage)
                val threshold = thresholdInvert(matGray, 100.0)
                val nonZeros = Core.countNonZero(threshold)
                val total = resizedImage.width() * resizedImage.height()

                resizedImage.release()
                matGray.release()
                threshold.release()
                matrix.release()

                return Pair(total, total - nonZeros)
            } catch (e: Exception) {
                log.warn("cannot get zeros and total count from img", e)
            }
            return Pair(1, 0)
        }

        private fun thresholdInvert(matrix: Mat, intensity: Double): Mat {
            val blur = blur(matrix)
            val threshold = Mat()
            Imgproc.threshold(
                blur, threshold, intensity, 255.0,
                Imgproc.THRESH_BINARY
            )
            return threshold
        }

        fun getHistogramChecksum(
            blueChannelMap: Map<Int, Int>,
            greenChannelMap: Map<Int, Int>,
            redChannelMap: Map<Int, Int>,
            parentPath: String
        ): String {
            var blueChannelPosSum = 0
            var blueChannelIntensitySum = 0
            var greenChannelPosSum = 0
            var greenChannelIntensitySum = 0
            var redChannelPosSum = 0
            var redChannelIntensitySum = 0
            blueChannelMap.forEach {
                blueChannelPosSum += it.key
                blueChannelIntensitySum += it.value
            }
            greenChannelMap.forEach {
                greenChannelPosSum += it.key
                greenChannelIntensitySum += it.value
            }
            redChannelMap.forEach {
                redChannelPosSum += it.key
                redChannelIntensitySum += it.value
            }
            val checksumRaw = "$parentPath/$blueChannelPosSum:${blueChannelIntensitySum}_" +
                    "$greenChannelPosSum:${greenChannelIntensitySum}_" +
                    "$redChannelPosSum:$redChannelIntensitySum"
            return com.amaze.fileutilities.utilis.Utils.getMd5ForString(checksumRaw)
        }

        fun getGenericWidth(matrix: Mat): Double {
            return if (matrix.width() > matrix.height()) 620.0 else 480.0
        }

        fun getGenericHeight(matrix: Mat): Double {
            return if (matrix.height() > matrix.width()) 620.0 else 480.0
        }

        private fun blur(matrix: Mat): Mat {
            val result = Mat()
            Imgproc.blur(matrix, result, Size(9.0, 9.0))
            return result
        }

        fun resize(matrix: Mat, width: Double, height: Double): Mat {
            val resizeMat = Mat()
            val sz = Size(width, height)
            Imgproc.resize(
                matrix, resizeMat, sz, 0.0, 0.0,
                if (matrix.cols() > width || matrix.rows() > height) Imgproc.INTER_AREA
                else Imgproc.INTER_CUBIC
            )
            return resizeMat
        }

        private fun gray(matrix: Mat): Mat {
            val matGray = Mat()
            Imgproc.cvtColor(matrix, matGray, Imgproc.COLOR_BGR2GRAY)
            return matGray
        }
    }

    data class ImageFeatures(
        val isSad: Boolean = false,
        val isSleeping: Boolean = false,
        val isDistracted: Boolean = false,
        val facesCount: Int = 0
    )
}
