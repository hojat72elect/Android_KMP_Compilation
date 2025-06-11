

package com.duckduckgo.duckplayer.impl

import com.duckduckgo.app.statistics.pixels.Pixel

enum class DuckPlayerPixelName(override val pixelName: String) : Pixel.PixelName {
    DUCK_PLAYER_OVERLAY_YOUTUBE_IMPRESSIONS("duckplayer_overlay_youtube_impressions"),
    DUCK_PLAYER_VIEW_FROM_YOUTUBE_MAIN_OVERLAY("duckplayer_view-from_youtube_main-overlay"),
    DUCK_PLAYER_OVERLAY_YOUTUBE_WATCH_HERE("duckplayer_overlay_youtube_watch_here"),
    DUCK_PLAYER_WATCH_ON_YOUTUBE("duckplayer_watch_on_youtube"),
    DUCK_PLAYER_DAILY_UNIQUE_VIEW("duckplayer_daily-unique-view"),
    DUCK_PLAYER_VIEW_FROM_YOUTUBE_AUTOMATIC("duckplayer_view-from_youtube_automatic"),
    DUCK_PLAYER_VIEW_FROM_OTHER("duckplayer_view-from_other"),
    DUCK_PLAYER_VIEW_FROM_SERP("duckplayer_view-from_serp"),
    DUCK_PLAYER_SETTINGS_ALWAYS_SETTINGS("duckplayer_setting_always_settings"),
    DUCK_PLAYER_SETTINGS_BACK_TO_DEFAULT("duckplayer_setting_back-to-default"),
    DUCK_PLAYER_SETTINGS_NEVER_SETTINGS("duckplayer_setting_never_settings"),
    DUCK_PLAYER_SETTINGS_PRESSED("duckplayer_setting_pressed"),
    DUCK_PLAYER_NEWTAB_SETTING_ON("duckplayer_newtab_setting-on"),
    DUCK_PLAYER_NEWTAB_SETTING_OFF("duckplayer_newtab_setting-off"),
}
