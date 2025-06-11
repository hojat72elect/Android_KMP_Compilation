

package com.duckduckgo.privacy.config.store.features.trackingparameters

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.duckduckgo.privacy.config.store.TrackingParameterEntity
import com.duckduckgo.privacy.config.store.TrackingParameterExceptionEntity

@Dao
abstract class TrackingParametersDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAllExceptions(domains: List<TrackingParameterExceptionEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAllParameters(parameters: List<TrackingParameterEntity>)

    @Transaction
    open fun updateAll(domains: List<TrackingParameterExceptionEntity>, parameters: List<TrackingParameterEntity>) {
        deleteAllExceptions()
        insertAllExceptions(domains)

        deleteAllTrackingParameters()
        insertAllParameters(parameters)
    }

    @Query("select * from tracking_parameter_exceptions")
    abstract fun getAllExceptions(): List<TrackingParameterExceptionEntity>

    @Query("select * from tracking_parameters")
    abstract fun getAllTrackingParameters(): List<TrackingParameterEntity>

    @Query("delete from tracking_parameter_exceptions")
    abstract fun deleteAllExceptions()

    @Query("delete from tracking_parameters")
    abstract fun deleteAllTrackingParameters()
}
