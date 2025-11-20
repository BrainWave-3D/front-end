package com.example.brainwave3d.data.remote.dto.auth

import com.google.gson.annotations.SerializedName

// Updated UserRead to match backend structure
data class PersonalInfo(
    @SerializedName("full_name")
    val fullName: String? = null,

    @SerializedName("date_of_birth")
    val dateOfBirth: String? = null,

    @SerializedName("gender")
    val gender: String? = null
)

data class ClinicalInfo(
    @SerializedName("current_occupation")
    val currentOccupation: String? = null,

    @SerializedName("highest_education_level")
    val highestEducationLevel: String? = null,

    @SerializedName("primary_concerns")
    val primaryConcerns: String? = null,

    @SerializedName("symptom_onset_age")
    val symptomOnsetAge: Int? = null
)

data class MedicalInfo(
    @SerializedName("relevant_history")
    val relevantHistory: String? = null,

    @SerializedName("current_medications")
    val currentMedications: String? = null,

    @SerializedName("family_history")
    val familyHistory: String? = null,

    @SerializedName("sleep_patterns")
    val sleepPatterns: String? = null
)

data class UserRead(
    @SerializedName("id")
    val id: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("personal_info")
    val personalInfo: PersonalInfo? = null,

    @SerializedName("clinical_info")
    val clinicalInfo: ClinicalInfo? = null,

    @SerializedName("medical_info")
    val medicalInfo: MedicalInfo? = null,

    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("updated_at")
    val updatedAt: String
)