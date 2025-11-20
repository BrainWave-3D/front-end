package com.example.brainwave3d.data.remote.dto.auth

import com.google.gson.annotations.SerializedName

data class LogoutResponse(
    @SerializedName("detail")
    val detail: String
)