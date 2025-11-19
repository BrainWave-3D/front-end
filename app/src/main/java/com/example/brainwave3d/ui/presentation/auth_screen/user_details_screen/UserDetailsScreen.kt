package com.example.brainwave3d.ui.presentation.auth_screen.user_details_screen

// PatientDetailsScreen.kt
import android.app.DatePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.brainwave3d.ui.navigation.Screen
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDetailsScreen(
    onNavigateBack: () -> Unit,
    onSubmit: (PatientDetails) -> Unit
) {
    var fullName by remember { mutableStateOf("") }
    var dateOfBirth by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var occupation by remember { mutableStateOf("") }
    var education by remember { mutableStateOf("") }
    var medicalHistory by remember { mutableStateOf("") }
    var currentMedications by remember { mutableStateOf("") }
    var familyHistoryADHD by remember { mutableStateOf("") }
    var symptomOnsetAge by remember { mutableStateOf("") }
    var primaryConcerns by remember { mutableStateOf("") }
    var sleepPatterns by remember { mutableStateOf("") }

    var expanded by remember { mutableStateOf(false) }
    var familyHistoryExpanded by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val genderOptions = listOf("Male", "Female", "Other", "Prefer not to say")
    val familyHistoryOptions = listOf("Yes", "No", "Unknown")

    // Date Picker Dialog
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, day ->
            dateOfBirth = String.format("%02d/%02d/%d", day, month + 1, year)
            // Calculate age
            val birthCalendar = Calendar.getInstance()
            birthCalendar.set(year, month, day)
            val currentYear = Calendar.getInstance().get(Calendar.YEAR)
            val birthYear = birthCalendar.get(Calendar.YEAR)
            age = (currentYear - birthYear).toString()
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    // Set max date to today (can't be born in the future)
    datePickerDialog.datePicker.maxDate = System.currentTimeMillis()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Patient Details") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            Text(
                text = "Personal Information",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "Please provide accurate information for ADHD assessment",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Full Name
            OutlinedTextField(
                value = fullName,
                onValueChange = { fullName = it },
                label = { Text("Full Name *") },
                placeholder = { Text("Enter your full name") },
                leadingIcon = {
                    Icon(Icons.Default.Person, contentDescription = "Name")
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Date of Birth with Date Picker
            OutlinedTextField(
                value = dateOfBirth,
                onValueChange = { },
                label = { Text("Date of Birth *") },
                placeholder = { Text("Select date of birth") },
                leadingIcon = {
                    Icon(Icons.Default.CalendarToday, contentDescription = "Date")
                },
                trailingIcon = {
                    IconButton(onClick = { datePickerDialog.show() }) {
                        Icon(Icons.Default.Edit, contentDescription = "Pick Date")
                    }
                },
                readOnly = true,
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Age (Auto-calculated)
            OutlinedTextField(
                value = age,
                onValueChange = { age = it },
                label = { Text("Age *") },
                placeholder = { Text("Enter or auto-calculated from DOB") },
                leadingIcon = {
                    Icon(Icons.Default.Calculate, contentDescription = "Age")
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Gender Dropdown
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = gender,
                    onValueChange = { },
                    label = { Text("Gender *") },
                    placeholder = { Text("Select gender") },
                    leadingIcon = {
                        Icon(Icons.Default.Wc, contentDescription = "Gender")
                    },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    genderOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                gender = option
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Clinical Information Section
            Text(
                text = "Clinical Information",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Occupation/Education Status
            OutlinedTextField(
                value = occupation,
                onValueChange = { occupation = it },
                label = { Text("Current Occupation/Student Status") },
                placeholder = { Text("e.g., Software Engineer, Student, etc.") },
                leadingIcon = {
                    Icon(Icons.Default.Work, contentDescription = "Occupation")
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Education Level
            OutlinedTextField(
                value = education,
                onValueChange = { education = it },
                label = { Text("Highest Education Level") },
                placeholder = { Text("e.g., High School, Bachelor's, etc.") },
                leadingIcon = {
                    Icon(Icons.Default.School, contentDescription = "Education")
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Primary Concerns
            OutlinedTextField(
                value = primaryConcerns,
                onValueChange = { primaryConcerns = it },
                label = { Text("Primary Concerns/Symptoms *") },
                placeholder = { Text("Describe your main concerns (inattention, hyperactivity, etc.)") },
                leadingIcon = {
                    Icon(Icons.Default.Description, contentDescription = "Concerns")
                },
                maxLines = 1,
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Symptom Onset Age
            OutlinedTextField(
                value = symptomOnsetAge,
                onValueChange = { symptomOnsetAge = it },
                label = { Text("Age When Symptoms First Appeared *") },
                placeholder = { Text("e.g., 7 years old") },
                leadingIcon = {
                    Icon(Icons.Default.Timeline, contentDescription = "Onset Age")
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                maxLines = 1,
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                supportingText = {
                    Text(
                        text = "ADHD symptoms typically appear before age 12",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Medical History Section
            Text(
                text = "Medical History",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Medical History
            OutlinedTextField(
                value = medicalHistory,
                onValueChange = { medicalHistory = it },
                label = { Text("Relevant Medical History") },
                placeholder = { Text("Any mental health conditions.") },
                leadingIcon = {
                    Icon(Icons.Default.LocalHospital, contentDescription = "Medical History")
                },
                maxLines = 1,
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Current Medications
            OutlinedTextField(
                value = currentMedications,
                onValueChange = { currentMedications = it },
                label = { Text("Current Medications") },
                placeholder = { Text("List all current medications") },
                leadingIcon = {
                    Icon(Icons.Default.MedicalServices, contentDescription = "Medications")
                },
                maxLines = 1,
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Family History of ADHD
            ExposedDropdownMenuBox(
                expanded = familyHistoryExpanded,
                onExpandedChange = { familyHistoryExpanded = !familyHistoryExpanded }
            ) {
                OutlinedTextField(
                    value = familyHistoryADHD,
                    onValueChange = { },
                    label = { Text("Family History of ADHD *") },
                    placeholder = { Text("Select option") },
                    leadingIcon = {
                        Icon(Icons.Default.FamilyRestroom, contentDescription = "Family History")
                    },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = familyHistoryExpanded)
                    },
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = familyHistoryExpanded,
                    onDismissRequest = { familyHistoryExpanded = false }
                ) {
                    familyHistoryOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                familyHistoryADHD = option
                                familyHistoryExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Sleep Patterns
            OutlinedTextField(
                value = sleepPatterns,
                onValueChange = { sleepPatterns = it },
                label = { Text("Sleep Patterns") },
                placeholder = { Text("Describe your typical sleep schedule and quality") },
                leadingIcon = {
                    Icon(Icons.Default.Bedtime, contentDescription = "Sleep")
                },
                maxLines = 1,
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Submit Button
            Button(
                onClick = {
                    val patientDetails = PatientDetails(
                        fullName = fullName,
                        dateOfBirth = dateOfBirth,
                        age = age,
                        gender = gender,
                        occupation = occupation,
                        education = education,
                        primaryConcerns = primaryConcerns,
                        symptomOnsetAge = symptomOnsetAge,
                        medicalHistory = medicalHistory,
                        currentMedications = currentMedications,
                        familyHistoryADHD = familyHistoryADHD,
                        sleepPatterns = sleepPatterns
                    )
                    onSubmit(patientDetails)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = fullName.isNotEmpty() &&
                        dateOfBirth.isNotEmpty() &&
                        age.isNotEmpty() &&
                        gender.isNotEmpty() &&
                        primaryConcerns.isNotEmpty() &&
                        symptomOnsetAge.isNotEmpty() &&
                        familyHistoryADHD.isNotEmpty()
            ) {
                Text(
                    text = "Submit Details",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "* Required fields",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// Data class to hold patient details
data class PatientDetails(
    val fullName: String,
    val dateOfBirth: String,
    val age: String,
    val gender: String,
    val occupation: String,
    val education: String,
    val primaryConcerns: String,
    val symptomOnsetAge: String,
    val medicalHistory: String,
    val currentMedications: String,
    val familyHistoryADHD: String,
    val sleepPatterns: String
)
