package com.example.brainwave3d.data.remote.dto.auth

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @SerializedName("user")
    val user: UserRead,

    @SerializedName("tokens")
    val tokens: AuthTokens
)