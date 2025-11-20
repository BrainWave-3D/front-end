package com.example.brainwave3d.data.remote.dto.auth

import com.google.gson.annotations.SerializedName

data class UserBase(
    @SerializedName("name")
    val name: String? = null,

    @SerializedName("age")
    val age: Int? = null,

    @SerializedName("bio")
    val bio: String? = null
)