package com.amaze.fileutilities.home_page.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SimilarImagesAnalysisDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(imagesAnalysis: SimilarImagesAnalysis)

    @Query("SELECT * FROM similarimagesanalysis WHERE histogram_checksum=:histogramChecksum")
    fun findByHistogramChecksum(histogramChecksum: String): SimilarImagesAnalysis?

    @Query("SELECT * FROM similarimagesanalysis")
    fun getAll(): List<SimilarImagesAnalysis>

    @Delete
    fun delete(imagesAnalysis: SimilarImagesAnalysis)

    @Query("DELETE FROM similarimagesanalysis")
    fun deleteAll()
}
