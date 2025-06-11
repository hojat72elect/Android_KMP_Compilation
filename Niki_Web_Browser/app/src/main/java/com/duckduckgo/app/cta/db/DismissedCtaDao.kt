

package com.duckduckgo.app.cta.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.duckduckgo.app.cta.model.CtaId
import com.duckduckgo.app.cta.model.DismissedCta
import kotlinx.coroutines.flow.Flow

@Dao
abstract class DismissedCtaDao {

    @Query("select * from dismissed_cta")
    abstract fun dismissedCtas(): Flow<List<DismissedCta>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(dismissedCta: DismissedCta)

    @Query("select count(1) > 0 from dismissed_cta where ctaId = :ctaId")
    abstract fun exists(ctaId: CtaId): Boolean
}
