package com.example.brainwave3d.data.pref

interface BrainWavePref {
    suspend fun saveAccessToken(token: String)
    suspend fun getAccessToken(): String?
    suspend fun clearAccessToken()

    suspend fun saveRefreshToken(token: String)
    suspend fun getRefreshToken(): String?
    suspend fun clearRefreshToken()

    suspend fun saveUserId(userId: String)
    suspend fun getUserId(): String?
    suspend fun clearUserId()

    suspend fun clearAll()
}
