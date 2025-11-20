package com.example.brainwave3d.data.remote

import com.example.brainwave3d.data.remote.dto.auth.AuthResponse
import com.example.brainwave3d.data.remote.dto.auth.LoginRequest
import com.example.brainwave3d.data.remote.dto.auth.LogoutResponse
import com.example.brainwave3d.data.remote.dto.auth.RefreshTokenRequest
import com.example.brainwave3d.data.remote.dto.auth.SignupRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("/auth/signup")
    suspend fun signup(
        @Body signupRequest: SignupRequest
    ): Response<AuthResponse>

    @POST("/auth/login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): Response<AuthResponse>

    @POST("/auth/logout")
    suspend fun logout(
        @Body refreshTokenRequest: RefreshTokenRequest
    ): Response<LogoutResponse>

    // Optional: Refresh token endpoint (if you add it later)
    @POST("/auth/refresh")
    suspend fun refreshToken(
        @Body refreshTokenRequest: RefreshTokenRequest
    ): Response<AuthResponse>
}