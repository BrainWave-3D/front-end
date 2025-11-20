package com.example.brainwave3d.common


object Constants {

    // ========== API Configuration ==========

    const val BASE_URL = "http://10.0.2.2:8000/"

    // For Physical Device (replace with your local IP)
    // const val BASE_URL = "http://192.168.1.x:8000/"

    // For Production
    // const val BASE_URL = "https://your-production-api.com/"

    // Static file URLs (if needed)
    const val BASE_URL_AVATAR = "${BASE_URL}static/avatars/"
    const val BASE_URL_MRI_SCANS = "${BASE_URL}static/mri-scans/"


    // ========== Network Timeout Configuration ==========

    // Connection timeout: Time allowed to establish connection with server
    // Default: 10 seconds
    // Recommended: 15-30 seconds for unstable networks
    const val CONNECT_TIMEOUT = 30L // seconds

    // Read timeout: Time between each byte received from server
    // Default: 10 seconds
    // Recommended: 30-60 seconds for large responses or slow servers
    const val READ_TIMEOUT = 60L // seconds

    // Write timeout: Time between each byte sent to server
    // Default: 10 seconds
    // Recommended: 30-60 seconds for large file uploads (like MRI scans)
    const val WRITE_TIMEOUT = 60L // seconds

    // Call timeout: Maximum time for entire request-response cycle
    // Default: 0 (no timeout)
    // Recommended: 90-120 seconds for operations that include ML inference
    const val CALL_TIMEOUT = 120L // seconds


    // ========== Retry Configuration ==========

    // Enable retry on connection failure
    const val RETRY_ON_CONNECTION_FAILURE = true

    // Maximum number of retry attempts
    const val MAX_RETRY_ATTEMPTS = 3

    // Retry delay in milliseconds
    const val RETRY_DELAY_MS = 1000L


    // ========== DataStore Configuration ==========

    // Preferences DataStore name
    const val PREFERENCES_NAME = "adhd_preferences"


    // ========== File Upload Configuration ==========

    // Maximum file size for MRI scan uploads (in MB)
    const val MAX_MRI_FILE_SIZE_MB = 100

    // Maximum file size in bytes
    const val MAX_MRI_FILE_SIZE_BYTES = MAX_MRI_FILE_SIZE_MB * 1024 * 1024

    // Allowed file types
    val ALLOWED_MRI_FILE_TYPES = listOf(
        "image/nifti",
        "image/dicom",
        "image/jpeg",
        "image/png",
        "application/octet-stream"
    )


    // ========== UI Configuration ==========

    // Maximum length for text inputs
    const val MAX_NAME_LENGTH = 100
    const val MAX_BIO_LENGTH = 500
    const val MAX_MEDICAL_HISTORY_LENGTH = 2000
    const val MAX_CONCERNS_LENGTH = 1000

    // Password constraints
    const val MIN_PASSWORD_LENGTH = 8
    const val MAX_PASSWORD_LENGTH = 128

    // Age constraints
    const val MIN_AGE = 0
    const val MAX_AGE = 120


    // ========== Cache Configuration ==========

    // Cache size for OkHttp (in MB)
    const val CACHE_SIZE = 10L * 1024 * 1024 // 10 MB

    // Cache validity duration
    const val CACHE_MAX_AGE = 60 * 60 * 24 // 24 hours


    // ========== API Endpoints ==========

    // Auth endpoints
    const val ENDPOINT_SIGNUP = "auth/signup"
    const val ENDPOINT_LOGIN = "auth/login"
    const val ENDPOINT_LOGOUT = "auth/logout"
    const val ENDPOINT_REFRESH_TOKEN = "auth/refresh"

    // User endpoints
    const val ENDPOINT_USER_PROFILE = "user/profile"
    const val ENDPOINT_UPDATE_PROFILE = "user/update"

    // ADHD Detection endpoints (adjust based on your backend)
    const val ENDPOINT_UPLOAD_MRI = "detection/upload"
    const val ENDPOINT_ANALYZE_MRI = "detection/analyze"
    const val ENDPOINT_GET_RESULTS = "detection/results"
    const val ENDPOINT_HISTORY = "detection/history"


    // ========== HTTP Status Codes ==========

    const val HTTP_OK = 200
    const val HTTP_CREATED = 201
    const val HTTP_BAD_REQUEST = 400
    const val HTTP_UNAUTHORIZED = 401
    const val HTTP_FORBIDDEN = 403
    const val HTTP_NOT_FOUND = 404
    const val HTTP_CONFLICT = 409
    const val HTTP_UNPROCESSABLE_ENTITY = 422
    const val HTTP_INTERNAL_SERVER_ERROR = 500


    // ========== SharedPreferences Keys ==========

    const val PREF_ACCESS_TOKEN = "access_token"
    const val PREF_REFRESH_TOKEN = "refresh_token"
    const val PREF_USER_ID = "user_id"
    const val PREF_USER_EMAIL = "user_email"
    const val PREF_USER_NAME = "user_name"
    const val PREF_IS_FIRST_LAUNCH = "is_first_launch"


    // ========== Date/Time Formats ==========

    const val DATE_FORMAT_DISPLAY = "dd/MM/yyyy"
    const val DATE_FORMAT_API = "yyyy-MM-dd"
    const val DATETIME_FORMAT_API = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"


    // ========== Error Messages ==========

    const val ERROR_NETWORK = "Network error. Please check your connection."
    const val ERROR_TIMEOUT = "Request timed out. Please try again."
    const val ERROR_UNKNOWN = "An unexpected error occurred."
    const val ERROR_UNAUTHORIZED = "Session expired. Please login again."
    const val ERROR_SERVER = "Server error. Please try again later."
    const val ERROR_FILE_TOO_LARGE = "File size exceeds maximum limit of ${MAX_MRI_FILE_SIZE_MB}MB"
    const val ERROR_INVALID_FILE_TYPE = "Invalid file type. Please upload a valid MRI scan."


    // ========== Success Messages ==========

    const val SUCCESS_LOGIN = "Login successful"
    const val SUCCESS_SIGNUP = "Account created successfully"
    const val SUCCESS_LOGOUT = "Logged out successfully"
    const val SUCCESS_PROFILE_UPDATE = "Profile updated successfully"
    const val SUCCESS_FILE_UPLOAD = "File uploaded successfully"


    // ========== Loading Messages ==========

    const val LOADING_LOGIN = "Logging in..."
    const val LOADING_SIGNUP = "Creating account..."
    const val LOADING_UPLOAD = "Uploading MRI scan..."
    const val LOADING_ANALYSIS = "Analyzing scan. This may take 30-60 seconds..."


    // ========== Animation Durations ==========

    const val ANIMATION_DURATION_SHORT = 300
    const val ANIMATION_DURATION_MEDIUM = 500
    const val ANIMATION_DURATION_LONG = 800


    // ========== Logging ==========

    const val ENABLE_LOGGING = true // Set to false in production
    const val LOG_TAG = "ADHD_APP"
}
