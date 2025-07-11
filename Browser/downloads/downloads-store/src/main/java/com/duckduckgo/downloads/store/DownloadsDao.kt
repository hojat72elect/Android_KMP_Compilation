

package com.duckduckgo.downloads.store

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DownloadsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(downloadItem: DownloadEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(downloadItems: List<DownloadEntity>)

    @Query("update downloads set downloadStatus = :downloadStatus, contentLength = :contentLength where downloadId =:downloadId")
    suspend fun update(downloadId: Long, downloadStatus: Int, contentLength: Long)

    @Query(
        """update downloads set downloadStatus = :downloadStatus, contentLength = :contentLength where id =
        (select id from downloads where downloadId = 0 and fileName = :fileName order by createdAt desc limit 1)""",
    )
    suspend fun update(fileName: String, downloadStatus: Int, contentLength: Long)

    @Query("delete from downloads where downloadId = :downloadId")
    suspend fun delete(downloadId: Long)

    @Query("delete from downloads where downloadId in (:downloadIdList)")
    suspend fun delete(downloadIdList: List<Long>)

    @Query("delete from downloads")
    suspend fun delete()

    @Query("select * from downloads order by createdAt desc")
    fun getDownloadsAsFlow(): Flow<List<DownloadEntity>>

    @Query("select * from downloads order by createdAt desc")
    suspend fun getDownloads(): List<DownloadEntity>

    @Query("select * from downloads where downloadId = :downloadId")
    suspend fun getDownloadItem(downloadId: Long): DownloadEntity?
}
