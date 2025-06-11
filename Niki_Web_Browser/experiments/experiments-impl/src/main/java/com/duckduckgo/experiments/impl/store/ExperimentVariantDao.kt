

package com.duckduckgo.experiments.impl.store

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
abstract class ExperimentVariantDao {

    @Query("select * from experiment_variants")
    abstract fun variants(): List<ExperimentVariantEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAll(variants: List<ExperimentVariantEntity>)

    @Query("delete from experiment_variants")
    abstract fun delete()
}
