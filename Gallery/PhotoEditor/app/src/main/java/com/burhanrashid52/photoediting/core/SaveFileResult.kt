package com.burhanrashid52.photoediting.core

import java.io.IOException

sealed interface SaveFileResult {

    object Success : SaveFileResult
    class Failure(val exception: IOException) : SaveFileResult

}