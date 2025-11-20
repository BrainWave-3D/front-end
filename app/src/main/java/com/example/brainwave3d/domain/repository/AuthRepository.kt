package com.example.brainwave3d.domain.repository

import com.example.brainwave3d.common.Resource
import com.example.brainwave3d.data.remote.dto.auth.AuthResponse
import com.example.brainwave3d.data.remote.dto.auth.LoginRequest
import com.example.brainwave3d.data.remote.dto.auth.LogoutResponse
import com.example.brainwave3d.data.remote.dto.auth.SignupRequest
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    suspend fun signup(signupRequest: SignupRequest): Flow<Resource<AuthResponse>>

    suspend fun login(loginRequest: LoginRequest): Flow<Resource<AuthResponse>>

    suspend fun logout(refreshToken: String): Flow<Resource<LogoutResponse>>

    suspend fun refreshToken(refreshToken: String): Flow<Resource<AuthResponse>>
}
