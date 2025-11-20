package com.example.brainwave3d.data.remote.dto.auth

import com.google.gson.annotations.SerializedName

data class AuthTokens(
    @SerializedName("access_token")
    val accessToken: String,

    @SerializedName("refresh_token")
    val refreshToken: String,

    @SerializedName("token_type")
    val tokenType: String = "bearer"
)