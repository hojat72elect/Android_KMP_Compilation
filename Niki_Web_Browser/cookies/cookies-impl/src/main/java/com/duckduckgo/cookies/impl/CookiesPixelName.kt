

package com.duckduckgo.cookies.impl

import com.duckduckgo.app.statistics.pixels.Pixel

enum class CookiesPixelName(override val pixelName: String) : Pixel.PixelName {
    COOKIE_DB_OPEN_ERROR("m_cookie_db_open_error"),
    COOKIE_EXPIRE_ERROR("m_cookie_db_expire_error"),
    COOKIE_DELETE_ERROR("m_cookie_db_delete_error"),
}
