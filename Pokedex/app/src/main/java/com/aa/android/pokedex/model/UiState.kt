package com.aa.android.pokedex.model

/**
 * UiState is a data-holding helper class that represents the state of a piece of data that will be
 * presented in the UI.
 *
 * Example:
 * - Prior to fetching the data, a [Loading] is emitted to signify that the fetch is being performed.
 *   The UI can use this to show a loading spinner, placeholder UI, etc.
 * - When the response is received from the data source (network, DB, etc), a [Ready] or [Error]
 *   state is emitted depending on the success of the request.
 */
sealed class UiState<T> {
    class Loading<T>: UiState<T>()
    class Error<T>(val error: Throwable): UiState<T>()
    class Ready<T>(val data: T): UiState<T>()
}
