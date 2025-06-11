

package com.duckduckgo.app.cta.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter

enum class CtaId {
    SURVEY,
    ADD_WIDGET,
    DAX_INTRO,
    DAX_INTRO_VISIT_SITE,
    DAX_INTRO_PRIVACY_PRO,
    DAX_FIRE_BUTTON,
    DAX_DIALOG_SERP,
    DAX_DIALOG_TRACKERS_FOUND,
    DAX_DIALOG_NETWORK,
    DAX_DIALOG_OTHER,
    DAX_DIALOG_AUTOCONSENT,
    DAX_END,
    DAX_FIRE_BUTTON_PULSE,
    DEVICE_SHIELD_CTA,
    BROKEN_SITE_PROMPT,
    UNKNOWN,
}

@Entity(
    tableName = "dismissed_cta",
)
data class DismissedCta(
    @PrimaryKey
    var ctaId: CtaId,
) {

    class IdTypeConverter {

        @TypeConverter
        fun toId(value: String): CtaId {
            return try {
                CtaId.valueOf(value)
            } catch (ex: IllegalArgumentException) {
                CtaId.UNKNOWN
            }
        }

        @TypeConverter
        fun fromId(value: CtaId): String {
            return value.name
        }
    }
}
