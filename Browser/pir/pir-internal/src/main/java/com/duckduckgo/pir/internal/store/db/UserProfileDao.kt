

package com.duckduckgo.pir.internal.store.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserProfileDao {
    @Query("SELECT * FROM pir_user_profile")
    fun getUserProfiles(): List<UserProfile>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUserProfile(userProfile: UserProfile)

    @Query("DELETE from pir_user_profile")
    fun deleteAllProfiles()
}
