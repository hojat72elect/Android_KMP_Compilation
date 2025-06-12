package com.amaze.fileutilities.home_page.database

import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
import com.amaze.fileutilities.utilis.PreferencesConstants
import java.lang.ref.WeakReference

/**
 * While fetching and processing, be sure to validate that file exists
 */
@Keep
@Entity(indices = [Index(value = ["path", "feature"], unique = true)])
data class PathPreferences(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    val uid: Int,
    @ColumnInfo(name = "path") val path: String,
    @ColumnInfo(name = "feature") val feature: Int,
    @ColumnInfo(name = "excludes") val excludes: Boolean,
) {
    @Ignore
    constructor(path: String, feature: Int, excludes: Boolean = false) :
            this(0, path, feature, excludes)

    companion object {
        const val FEATURE_AUDIO_PLAYER = 0
        const val FEATURE_ANALYSIS_MEME = 1
        const val FEATURE_ANALYSIS_BLUR = 2
        const val FEATURE_ANALYSIS_IMAGE_FEATURES = 3
        const val FEATURE_ANALYSIS_DOWNLOADS = 4
        const val FEATURE_ANALYSIS_RECORDING = 5
        const val FEATURE_ANALYSIS_SCREENSHOTS = 6
        const val FEATURE_ANALYSIS_TELEGRAM = 7
        const val FEATURE_ANALYSIS_LOW_LIGHT = 8
        const val FEATURE_ANALYSIS_WHATSAPP = 9
        const val FEATURE_ANALYSIS_SIMILAR_IMAGES = 11

        val ANALYSE_FEATURES_LIST = arrayListOf(
            FEATURE_ANALYSIS_MEME, FEATURE_ANALYSIS_BLUR,
            FEATURE_ANALYSIS_IMAGE_FEATURES, FEATURE_ANALYSIS_LOW_LIGHT,
            FEATURE_ANALYSIS_SIMILAR_IMAGES
        )

        val MIGRATION_PREF_MAP = mapOf(
            Pair(FEATURE_ANALYSIS_MEME, PreferencesConstants.VAL_MIGRATION_FEATURE_ANALYSIS_MEME),
            Pair(FEATURE_ANALYSIS_BLUR, PreferencesConstants.VAL_MIGRATION_FEATURE_ANALYSIS_BLUR),
            Pair(
                FEATURE_ANALYSIS_IMAGE_FEATURES,
                PreferencesConstants.VAL_MIGRATION_FEATURE_ANALYSIS_IMAGE_FEATURES
            ),
            Pair(
                FEATURE_ANALYSIS_LOW_LIGHT,
                PreferencesConstants.VAL_MIGRATION_FEATURE_ANALYSIS_LOW_LIGHT
            ),
            Pair(
                FEATURE_ANALYSIS_SIMILAR_IMAGES,
                PreferencesConstants.VAL_MIGRATION_FEATURE_ANALYSIS_SIMILAR_IMAGES
            )
        )

        fun getEnablePreferenceKey(feature: Int): String {
            return "${feature}_enabled"
        }

        fun getAnalysisMigrationPreferenceKey(feature: Int): String {
            return "${feature}_migration_version"
        }

        fun isEnabled(sharedPreferences: SharedPreferences, feature: Int): Boolean {
            return sharedPreferences.getBoolean(
                getEnablePreferenceKey(feature),
                PreferencesConstants.DEFAULT_ENABLED_ANALYSIS
            )
        }

        fun deleteAnalysisData(
            pathPreferences: List<PathPreferences>,
            contextRef: WeakReference<Context>
        ) {
            contextRef.get()?.let { context ->
                val db = AppDatabase.getInstance(context)
                pathPreferences.forEach {
                    when (it.feature) {
                        FEATURE_ANALYSIS_IMAGE_FEATURES -> {
                            val analysisDao = db.analysisDao()
                            analysisDao.deleteByPathContains(it.path)
                        }

                        FEATURE_ANALYSIS_BLUR -> {
                            val blurDao = db.blurAnalysisDao()
                            blurDao.deleteByPathContains(it.path)
                        }

                        FEATURE_ANALYSIS_MEME -> {
                            val memesDao = db.memesAnalysisDao()
                            memesDao.deleteByPathContains(it.path)
                        }

                        FEATURE_ANALYSIS_LOW_LIGHT -> {
                            val lowLightDao = db.lowLightAnalysisDao()
                            lowLightDao.deleteByPathContains(it.path)
                        }

                        FEATURE_ANALYSIS_SIMILAR_IMAGES -> {
                            val similarImagesAnalysisDao = db.similarImagesAnalysisDao()
                            val similarImagesAnalysisMetadataDao =
                                db.similarImagesAnalysisMetadataDao()
                            similarImagesAnalysisMetadataDao.deleteByPathContains(it.path)
                            similarImagesAnalysisDao.deleteAll()
                        }
                    }
                }
            }
        }
    }
}
