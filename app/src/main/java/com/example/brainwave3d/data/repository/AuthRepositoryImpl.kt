package com.example.brainwave3d.data.repository

import android.util.Log
import com.example.brainwave3d.common.Resource
import com.example.brainwave3d.data.pref.BrainWavePref
import com.example.brainwave3d.data.remote.AuthApi
import com.example.brainwave3d.data.remote.dto.auth.AuthResponse
import com.example.brainwave3d.data.remote.dto.auth.LoginRequest
import com.example.brainwave3d.data.remote.dto.auth.LogoutResponse
import com.example.brainwave3d.data.remote.dto.auth.RefreshTokenRequest
import com.example.brainwave3d.data.remote.dto.auth.SignupRequest
import com.example.brainwave3d.domain.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.json.JSONObject
import retrofit2.HttpException

class AuthRepositoryImpl(
    private val authApi: AuthApi,
    private val pref: BrainWavePref
) : AuthRepository {

    companion object {
        private const val TAG = "AuthRepositoryImpl"
    }

    override suspend fun signup(signupRequest: SignupRequest): Flow<Resource<AuthResponse>> = flow {
        emit(Resource.Loading())
        try {
            val response = authApi.signup(signupRequest)

            if (response.isSuccessful && response.body() != null) {
                val authResponse = response.body()!!

                // Save tokens to preferences
                authResponse.tokens.accessToken.let { pref.saveAccessToken(it) }
                authResponse.tokens.refreshToken.let { pref.saveRefreshToken(it) }
                authResponse.user.id.let { pref.saveUserId(it) }

                Log.d(TAG, "Signup successful: ${authResponse.user.email}")
                emit(Resource.Success(authResponse))
            } else {
                val errorBody = response.errorBody()?.string()
                val errorMessage = parseErrorMessage(errorBody)
                Log.e(TAG, "Signup error: $errorMessage")
                emit(Resource.Error(message = errorMessage))
            }
        } catch (e: HttpException) {
            val errorMessage = handleHttpException(e, "signup")
            emit(Resource.Error(message = errorMessage))
        } catch (e: Exception) {
            val errorMessage = e.message ?: "An unexpected error occurred during signup"
            Log.e(TAG, "Signup exception: $errorMessage", e)
            emit(Resource.Error(message = errorMessage))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun login(loginRequest: LoginRequest): Flow<Resource<AuthResponse>> = flow {
        emit(Resource.Loading())
        try {
            val response = authApi.login(loginRequest)

            if (response.isSuccessful && response.body() != null) {
                val authResponse = response.body()!!

                // Save tokens to preferences
                authResponse.tokens.accessToken.let { pref.saveAccessToken(it) }
                authResponse.tokens.refreshToken.let { pref.saveRefreshToken(it) }
                authResponse.user.id.let { pref.saveUserId(it) }

                Log.d(TAG, "Login successful: ${authResponse.user.email}")
                emit(Resource.Success(authResponse))
            } else {
                val errorBody = response.errorBody()?.string()
                val errorMessage = parseErrorMessage(errorBody)
                Log.e(TAG, "Login error: $errorMessage")
                emit(Resource.Error(message = errorMessage))
            }
        } catch (e: HttpException) {
            val errorMessage = handleHttpException(e, "login")
            emit(Resource.Error(message = errorMessage))
        } catch (e: Exception) {
            val errorMessage = e.message ?: "An unexpected error occurred during login"
            Log.e(TAG, "Login exception: $errorMessage", e)
            emit(Resource.Error(message = errorMessage))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun logout(refreshToken: String): Flow<Resource<LogoutResponse>> = flow {
        emit(Resource.Loading())
        try {
            val response = authApi.logout(RefreshTokenRequest(refreshToken))

            if (response.isSuccessful && response.body() != null) {
                val logoutResponse = response.body()!!

                // Clear all authentication-related data on successful logout
                pref.clearAccessToken()
                pref.clearRefreshToken()
                pref.clearUserId()

                Log.d(TAG, "Logout successful: ${logoutResponse.detail}")
                emit(Resource.Success(logoutResponse))
            } else {
                val errorBody = response.errorBody()?.string()
                val errorMessage = parseErrorMessage(errorBody)
                Log.e(TAG, "Logout error: $errorMessage")

                // Clear tokens even on error to ensure logout
                pref.clearAccessToken()
                pref.clearRefreshToken()
                pref.clearUserId()

                emit(Resource.Error(message = errorMessage))
            }
        } catch (e: HttpException) {
            val errorMessage = handleHttpException(e, "logout")

            // Clear tokens even on error to ensure logout
            pref.clearAccessToken()
            pref.clearRefreshToken()
            pref.clearUserId()

            emit(Resource.Error(message = errorMessage))
        } catch (e: Exception) {
            val errorMessage = e.message ?: "An unexpected error occurred during logout"
            Log.e(TAG, "Logout exception: $errorMessage", e)

            // Clear tokens even on exception to ensure logout
            pref.clearAccessToken()
            pref.clearRefreshToken()
            pref.clearUserId()

            emit(Resource.Error(message = errorMessage))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun refreshToken(refreshToken: String): Flow<Resource<AuthResponse>> = flow {
        emit(Resource.Loading())
        try {
            val response = authApi.refreshToken(RefreshTokenRequest(refreshToken))

            if (response.isSuccessful && response.body() != null) {
                val authResponse = response.body()!!

                // Save new tokens to preferences
                authResponse.tokens.accessToken.let { pref.saveAccessToken(it) }
                authResponse.tokens.refreshToken.let { pref.saveRefreshToken(it) }

                Log.d(TAG, "Token refresh successful")
                emit(Resource.Success(authResponse))
            } else {
                val errorBody = response.errorBody()?.string()
                val errorMessage = parseErrorMessage(errorBody)
                Log.e(TAG, "Token refresh error: $errorMessage")
                emit(Resource.Error(message = errorMessage))
            }
        } catch (e: HttpException) {
            val errorMessage = handleHttpException(e, "token refresh")
            emit(Resource.Error(message = errorMessage))
        } catch (e: Exception) {
            val errorMessage = e.message ?: "An unexpected error occurred during token refresh"
            Log.e(TAG, "Token refresh exception: $errorMessage", e)
            emit(Resource.Error(message = errorMessage))
        }
    }.flowOn(Dispatchers.IO)

    /**
     * Parse error message from response body
     */
    private fun parseErrorMessage(errorBody: String?): String {
        return try {
            if (errorBody.isNullOrEmpty()) {
                "An error occurred"
            } else {
                val json = JSONObject(errorBody)
                json.optString("detail") ?: json.optString("message") ?: "An error occurred"
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error parsing error body: ${e.message}")
            "An error occurred"
        }
    }

    /**
     * Handle HTTP exceptions with proper error messages
     */
    private fun handleHttpException(e: HttpException, operation: String): String {
        val errorBody = e.response()?.errorBody()?.string()
        val errorMessage = parseErrorMessage(errorBody) ?: e.message()

        Log.e(TAG, "HTTP error during $operation (${e.code()}): $errorMessage")

        return when (e.code()) {
            400 -> "Invalid request. Please check your input."
            401 -> "Unauthorized. Please login again."
            403 -> "Access forbidden."
            404 -> "Resource not found."
            409 -> "Conflict. User may already exist."
            422 -> "Validation error. Please check your input."
            500 -> "Server error. Please try again later."
            else -> errorMessage ?: "An error occurred during $operation"
        }
    }
}
