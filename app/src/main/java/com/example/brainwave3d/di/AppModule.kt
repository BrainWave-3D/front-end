package com.example.brainwave3d.di

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.example.brainwave3d.data.remote.AuthApi
import com.example.brainwave3d.data.repository.AuthRepositoryImpl
import com.example.brainwave3d.domain.repository.AuthRepository
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton
import android.util.Log
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import com.example.brainwave3d.common.Constants
import com.example.brainwave3d.common.Constants.BASE_URL
import com.example.brainwave3d.common.Constants.CONNECT_TIMEOUT
import com.example.brainwave3d.common.Constants.READ_TIMEOUT
import com.example.brainwave3d.common.Constants.WRITE_TIMEOUT
import com.example.brainwave3d.data.local.ADHDPrefImpl
import com.example.brainwave3d.data.pref.BrainWavePref
import com.example.brainwave3d.data.remote.dto.auth.AuthResponse
import com.example.brainwave3d.data.remote.dto.auth.RefreshTokenRequest

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideGson(): Gson {
        return GsonBuilder()
            .setLenient()
            .create()
    }

    @Provides
    @Singleton
    fun provideAuthStateManager(adhdPref: BrainWavePref): AuthStateManager {
        return AuthStateManager(adhdPref)
    }

    @Singleton
    @Provides
    fun provideAuthInterceptor(adhdPref: BrainWavePref): AuthInterceptor =
        AuthInterceptor(adhdPref)

    @Singleton
    @Provides
    fun provideAuthAuthenticator(
        adhdPref: BrainWavePref,
        authStateManager: AuthStateManager
    ): AuthAuthenticator =
        AuthAuthenticator(adhdPref, authStateManager)

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
        authAuthenticator: AuthAuthenticator,
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .authenticator(authAuthenticator)
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        gson: Gson
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        authApi: AuthApi,
        adhdPref: BrainWavePref
    ): AuthRepository {
        return AuthRepositoryImpl(authApi, adhdPref)
    }

    @Provides
    @Singleton
    fun providesDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { emptyPreferences() }
            ),
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            produceFile = {
                context.preferencesDataStoreFile("adhd_preferences")
            }
        )
    }

    @Provides
    @Singleton
    fun provideADHDPref(dataStore: DataStore<Preferences>): BrainWavePref {
        return ADHDPrefImpl(dataStore)
    }
}

// AuthInterceptor - Adds Authorization header to every request
class AuthInterceptor @Inject constructor(
    private val adhdPref: BrainWavePref
) : Interceptor {

    companion object {
        private const val TAG = "AuthInterceptor"
        private const val HEADER_AUTHORIZATION = "Authorization"
        private const val TOKEN_TYPE = "Bearer"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // Get access token
        val token = runBlocking {
            adhdPref.getAccessToken()
        }

        // If no token, proceed without authorization header
        if (token.isNullOrEmpty()) {
            Log.d(TAG, "No access token found, proceeding without auth header")
            return chain.proceed(originalRequest)
        }

        // Add authorization header
        val authenticatedRequest = originalRequest.newBuilder()
            .header(HEADER_AUTHORIZATION, "$TOKEN_TYPE $token")
            .build()

        Log.d(TAG, "Added authorization header to request: ${originalRequest.url}")
        return chain.proceed(authenticatedRequest)
    }
}

// AuthAuthenticator - Fixed version
class AuthAuthenticator @Inject constructor(
    private val adhdPref: BrainWavePref,
    private val authStateManager: AuthStateManager
) : Authenticator {

    companion object {
        private const val TAG = "AuthAuthenticator"
        private const val HEADER_AUTHORIZATION = "Authorization"
        private const val TOKEN_TYPE = "Bearer"
    }

    override fun authenticate(route: Route?, response: Response): Request? {
        Log.e(TAG, "Token refresh attempt - Response code: ${response.code}")

        // Synchronize to prevent multiple simultaneous refresh attempts
        return synchronized(this) {
            val currentToken = runBlocking {
                adhdPref.getAccessToken()
            }

            // Check if token has already been refreshed by another request
            val requestToken = response.request.header(HEADER_AUTHORIZATION)
                ?.removePrefix("$TOKEN_TYPE ")

            if (currentToken != requestToken && !currentToken.isNullOrEmpty()) {
                Log.d(TAG, "Token already refreshed, retrying with new token")
                return response.request.newBuilder()
                    .header(HEADER_AUTHORIZATION, "$TOKEN_TYPE $currentToken")
                    .build()
            }

            // Get refresh token
            val refreshToken = runBlocking {
                adhdPref.getRefreshToken()
            }

            // If no refresh token exists, signal unauthenticated state
            if (refreshToken.isNullOrEmpty()) {
                Log.e(TAG, "No refresh token available, triggering logout")
                runBlocking {
                    authStateManager.setUnauthenticated()
                }
                return null
            }

            runBlocking {
                try {
                    val newTokenResponse = getNewToken(refreshToken)

                    if (!newTokenResponse.isSuccessful || newTokenResponse.body() == null) {
                        Log.e(TAG, "Token refresh failed with code: ${newTokenResponse.code()}")
                        // Token refresh failed - trigger logout flow
                        authStateManager.setUnauthenticated()
                        return@runBlocking null
                    }

                    val authResponse = newTokenResponse.body()!!

                    // Save new tokens - FIXED: accessing tokens correctly
                    adhdPref.saveAccessToken(authResponse.tokens.accessToken)
                    adhdPref.saveRefreshToken(authResponse.tokens.refreshToken)

                    Log.d(TAG, "Token refreshed successfully")

                    // Retry the original request with new token
                    response.request.newBuilder()
                        .header(HEADER_AUTHORIZATION, "$TOKEN_TYPE ${authResponse.tokens.accessToken}")
                        .build()

                } catch (e: Exception) {
                    Log.e(TAG, "Exception during token refresh: ${e.message}", e)
                    authStateManager.setUnauthenticated()
                    null
                }
            }
        }
    }

    private suspend fun getNewToken(refreshToken: String): retrofit2.Response<AuthResponse> {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(Constants.CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(Constants.READ_TIMEOUT, TimeUnit.SECONDS)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

        val service = retrofit.create(AuthApi::class.java)
        return service.refreshToken(RefreshTokenRequest(refreshToken))
    }
}


// AuthStateManager - Manages authentication state across the app
@Singleton
class AuthStateManager @Inject constructor(
    private val adhdPref: BrainWavePref
) {
    companion object {
        private const val TAG = "AuthStateManager"
    }

    private val _authState = MutableSharedFlow<AuthState>(
        replay = 1,
        extraBufferCapacity = 1
    )
    val authState: SharedFlow<AuthState> = _authState.asSharedFlow()

    suspend fun setAuthenticated() {
        Log.d(TAG, "User authenticated")
        _authState.emit(AuthState.Authenticated)
    }

    suspend fun setUnauthenticated() {
        Log.d(TAG, "User unauthenticated, clearing tokens")
        // Clear all tokens
        adhdPref.clearAccessToken()
        adhdPref.clearRefreshToken()
        adhdPref.clearUserId()
        _authState.emit(AuthState.Unauthenticated)
    }

    suspend fun checkAuthStatus() {
        val accessToken = adhdPref.getAccessToken()
        val refreshToken = adhdPref.getRefreshToken()

        if (!accessToken.isNullOrEmpty() && !refreshToken.isNullOrEmpty()) {
            _authState.emit(AuthState.Authenticated)
        } else {
            _authState.emit(AuthState.Unauthenticated)
        }
    }
}

sealed class AuthState {
    object Authenticated : AuthState()
    object Unauthenticated : AuthState()
}
