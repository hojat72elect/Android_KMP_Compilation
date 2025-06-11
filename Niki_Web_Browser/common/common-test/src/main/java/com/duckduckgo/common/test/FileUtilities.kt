

package com.duckduckgo.common.test

import java.io.BufferedReader
import java.io.InputStream
import org.json.JSONObject

object FileUtilities {

    fun loadText(
        classLoader: ClassLoader,
        resourceName: String,
    ): String = readResource(classLoader, resourceName).use { it.readText() }

    private fun readResource(
        classLoader: ClassLoader,
        resourceName: String,
    ): BufferedReader {
        return classLoader.getResource(resourceName).openStream().bufferedReader()
    }

    fun readBytes(
        classLoader: ClassLoader,
        resourceName: String,
    ): ByteArray {
        return loadResource(classLoader, resourceName).use { it.readBytes() }
    }

    fun loadResource(
        classLoader: ClassLoader,
        resourceName: String,
    ): InputStream {
        return classLoader.getResource(resourceName).openStream()
    }

    fun getJsonObjectFromFile(
        classLoader: ClassLoader,
        filename: String,
    ): JSONObject {
        val json = loadText(classLoader, filename)
        return JSONObject(json)
    }
}
