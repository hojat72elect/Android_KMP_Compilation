package com.amaze.fileutilities.home_page.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SimilarImagesAnalysisMetadataDao {

    @Query("SELECT * FROM similarimagesanalysismetadata WHERE file_path=:path")
    fun findByPath(path: String): SimilarImagesAnalysisMetadata?

    @Query(
        "SELECT * FROM similarimagesanalysismetadata " +
                "WHERE file_path=:path and parent_path=:parentPath"
    )
    fun findByParentAndPath(path: String, parentPath: String): SimilarImagesAnalysisMetadata?

    @Query("SELECT * FROM similarimagesanalysismetadata WHERE parent_path=:parentPath")
    fun findAllByParentPath(parentPath: String): List<SimilarImagesAnalysisMetadata>

    @Query("SELECT * FROM similarimagesanalysismetadata")
    fun getAll(): List<SimilarImagesAnalysisMetadata>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(imagesAnalysis: SimilarImagesAnalysisMetadata)

    @Delete
    fun delete(imagesAnalysis: SimilarImagesAnalysisMetadata)

    @Query("DELETE FROM similarimagesanalysismetadata WHERE file_path like '%' || :path || '%'")
    fun deleteByPathContains(path: String)

    @Query("SELECT count(*) from similarimagesanalysismetadata")
    fun getAllCount(): Int
}
