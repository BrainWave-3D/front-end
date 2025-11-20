package com.example.brainwave3d.data.remote.dto.auth

import com.google.gson.annotations.SerializedName

data class SignupRequest(
    @SerializedName("full_name")
    val fullName: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("password")
    val password: String
)