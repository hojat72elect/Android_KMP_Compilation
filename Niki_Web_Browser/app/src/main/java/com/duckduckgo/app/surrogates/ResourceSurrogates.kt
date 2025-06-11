

package com.duckduckgo.app.surrogates

interface ResourceSurrogates {
    fun loadSurrogates(urls: List<SurrogateResponse>)
    fun get(scriptId: String): SurrogateResponse
    fun getAll(): List<SurrogateResponse>
}

class ResourceSurrogatesImpl : ResourceSurrogates {

    private val surrogates = mutableListOf<SurrogateResponse>()

    override fun loadSurrogates(urls: List<SurrogateResponse>) {
        surrogates.clear()
        surrogates.addAll(urls)
    }

    override fun get(scriptId: String): SurrogateResponse {
        return surrogates.find { it.scriptId == scriptId }
            ?: return SurrogateResponse(responseAvailable = false)
    }

    override fun getAll(): List<SurrogateResponse> {
        return surrogates
    }
}
