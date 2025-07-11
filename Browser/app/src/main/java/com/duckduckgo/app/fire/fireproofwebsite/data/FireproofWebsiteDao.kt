

package com.duckduckgo.app.fire.fireproofwebsite.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FireproofWebsiteDao {

    @Query("select * from fireproofWebsites")
    fun fireproofWebsitesSync(): List<FireproofWebsiteEntity>

    @Query("select * from fireproofWebsites")
    fun fireproofWebsitesEntities(): LiveData<List<FireproofWebsiteEntity>>

    @Query("select * from fireproofWebsites WHERE domain = :domain")
    fun getFireproofWebsiteSync(domain: String): FireproofWebsiteEntity?

    @Query("select count(*) from fireproofWebsites WHERE domain LIKE :domain")
    fun fireproofWebsitesCountByDomain(domain: String): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(fireproofWebsiteEntity: FireproofWebsiteEntity): Long

    @Delete
    fun delete(fireproofWebsiteEntity: FireproofWebsiteEntity): Int

    @Query("delete from fireproofWebsites")
    fun deleteAll()
}
