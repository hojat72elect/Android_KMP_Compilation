package ca.hojat.gamehub.core.domain.auth.datastores

import javax.inject.Inject

class AuthDataStores @Inject constructor(
    val local: AuthLocalDataStore,
    val remote: AuthRemoteDataStore
)
