

package com.duckduckgo.autofill.impl.securestorage

import java.lang.Exception

sealed class SecureStorageException : Exception() {
    /**
     * Public data class exception that is thrown when a method that requires user authentication is accessed by a
     * non authenticated user.
     */
    data class UserNotAuthenticatedException(override val message: String) : SecureStorageException()
    data class InternalSecureStorageException(
        override val message: String,
        override val cause: Throwable? = null,
    ) : SecureStorageException()
}
