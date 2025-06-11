package ca.hojat.gamehub.core.data.api.igdb.common

import okhttp3.Response

val Response.responseCount: Int
    get() {
        var result = 1
        var response: Response? = priorResponse

        while (response != null) {
            result++

            response = response.priorResponse
        }

        return result
    }
