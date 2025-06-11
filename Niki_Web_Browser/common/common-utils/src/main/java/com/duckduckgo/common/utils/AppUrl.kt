

package com.duckduckgo.common.utils

class AppUrl {

    object Url {
        const val HOST = "duckduckgo.com"
        const val API = "https://$HOST"
        const val HOME = "https://$HOST"
        const val COOKIES = "https://$HOST"
        const val SURVEY_COOKIES = "https://surveys.$HOST"
        const val ABOUT = "https://$HOST/about"
        const val PIXEL = "https://improving.duckduckgo.com"
        const val EMAIL_SEGMENT = "email"
    }

    object ParamKey {
        const val QUERY = "q"
        const val SOURCE = "t"
        const val ATB = "atb"
        const val RETENTION_ATB = "set_atb"
        const val DEV_MODE = "test"
        const val LANGUAGE = "lg"
        const val EMAIL = "email"
        const val COUNTRY = "co"
        const val HIDE_SERP = "ko"
        const val VERTICAL = "ia"
        const val VERTICAL_REWRITE = "iar"
    }

    object ParamValue {
        const val SOURCE = "ddg_android"
        const val SOURCE_EU_AUCTION = "ddg_androideu"
        const val HIDE_SERP = "-1"
        const val CHAT_VERTICAL = "chat"
    }

    object StaticUrl {
        const val SETTINGS = "/settings"
        const val PARAMS = "/params"
    }
}
