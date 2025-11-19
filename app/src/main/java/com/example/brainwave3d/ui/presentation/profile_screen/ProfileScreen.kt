package com.example.brainwave3d.ui.presentation.profile_screen

// ProfileScreen.kt - COMPLETE CODE WITH CONFIRMATION DIALOG
import android.app.DatePickerDialog
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.brainwave3d.ui.presentation.auth_screen.user_details_screen.PatientDetails
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onNavigateBack: () -> Unit,
    currentUserData: PatientDetails? = null
) {
    var isEditMode by remember { mutableStateOf(false) }
    var showSaveDialog by remember { mutableStateOf(false) }
    var showDiscardDialog by remember { mutableStateOf(false) }

    // Initialize with current data or defaults
    var fullName by remember { mutableStateOf(currentUserData?.fullName ?: "") }
    var dateOfBirth by remember { mutableStateOf(currentUserData?.dateOfBirth ?: "") }
    var age by remember { mutableStateOf(currentUserData?.age ?: "") }
    var gender by remember { mutableStateOf(currentUserData?.gender ?: "") }
    var occupation by remember { mutableStateOf(currentUserData?.occupation ?: "") }
    var education by remember { mutableStateOf(currentUserData?.education ?: "") }
    var medicalHistory by remember { mutableStateOf(currentUserData?.medicalHistory ?: "") }
    var currentMedications by remember { mutableStateOf(currentUserData?.currentMedications ?: "") }
    var familyHistoryADHD by remember { mutableStateOf(currentUserData?.familyHistoryADHD ?: "") }
    var symptomOnsetAge by remember { mutableStateOf(currentUserData?.symptomOnsetAge ?: "") }
    var primaryConcerns by remember { mutableStateOf(currentUserData?.primaryConcerns ?: "") }
    var sleepPatterns by remember { mutableStateOf(currentUserData?.sleepPatterns ?: "") }

    var genderExpanded by remember { mutableStateOf(false) }
    var familyHistoryExpanded by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val genderOptions = listOf("Male", "Female", "Other", "Prefer not to say")
    val familyHistoryOptions = listOf("Yes", "No", "Unknown")

    // Function to revert all changes
    fun revertChanges() {
        fullName = currentUserData?.fullName ?: ""
        dateOfBirth = currentUserData?.dateOfBirth ?: ""
        age = currentUserData?.age ?: ""
        gender = currentUserData?.gender ?: ""
        occupation = currentUserData?.occupation ?: ""
        education = currentUserData?.education ?: ""
        medicalHistory = currentUserData?.medicalHistory ?: ""
        currentMedications = currentUserData?.currentMedications ?: ""
        familyHistoryADHD = currentUserData?.familyHistoryADHD ?: ""
        symptomOnsetAge = currentUserData?.symptomOnsetAge ?: ""
        primaryConcerns = currentUserData?.primaryConcerns ?: ""
        sleepPatterns = currentUserData?.sleepPatterns ?: ""
    }

    // Date Picker Dialog
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, day ->
            dateOfBirth = String.format("%02d/%02d/%d", day, month + 1, year)
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

    datePickerDialog.datePicker.maxDate = System.currentTimeMillis()

    // Handle back button press when in edit mode with confirmation dialog
    if (isEditMode) {
        BackHandler {
            showDiscardDialog = true
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Profile") },
                navigationIcon = {
                    IconButton(onClick = {
                        if (isEditMode) {
                            showDiscardDialog = true
                        } else {
                            onNavigateBack()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    if (!isEditMode) {
                        // Edit Button - shown when NOT in edit mode
                        IconButton(onClick = { isEditMode = true }) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit Profile"
                            )
                        }
                    } else {
                        // Cancel Button - shown when in edit mode
                        TextButton(
                            onClick = { showDiscardDialog = true }
                        ) {
                            Text("Cancel")
                        }
                        // Save Button - shown when in edit mode
                        TextButton(
                            onClick = { showSaveDialog = true }
                        ) {
                            Text(
                                text = "Save",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            if (isEditMode) {
                ExtendedFloatingActionButton(
                    onClick = { showSaveDialog = true },
                    icon = {
                        Icon(
                            Icons.Default.Save,
                            contentDescription = "Save"
                        )
                    },
                    text = { Text("Save Changes") },
                    containerColor = MaterialTheme.colorScheme.primary
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // Profile Header Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Profile Picture Placeholder
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profile Picture",
                        modifier = Modifier.size(60.dp),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = fullName.ifEmpty { "User Name" },
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = if (age.isNotEmpty()) "$age years old" else "Age not set",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )

                // Edit mode indicator
                if (isEditMode) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(
                        color = MaterialTheme.colorScheme.tertiaryContainer,
                        shape = MaterialTheme.shapes.small
                    ) {
                        Text(
                            text = "Editing Mode",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onTertiaryContainer,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Profile Details Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                // Personal Information Section
                SectionHeader("Personal Information")

                Spacer(modifier = Modifier.height(16.dp))

                ProfileField(
                    label = "Full Name",
                    value = fullName,
                    onValueChange = { fullName = it },
                    isEditMode = isEditMode,
                    leadingIcon = Icons.Default.Person
                )

                Spacer(modifier = Modifier.height(12.dp))

                ProfileDateField(
                    label = "Date of Birth",
                    value = dateOfBirth,
                    isEditMode = isEditMode,
                    leadingIcon = Icons.Default.CalendarToday,
                    onEditClick = { if (isEditMode) datePickerDialog.show() }
                )

                Spacer(modifier = Modifier.height(12.dp))

                ProfileField(
                    label = "Age",
                    value = age,
                    onValueChange = { age = it },
                    isEditMode = isEditMode,
                    leadingIcon = Icons.Default.Calculate,
                    keyboardType = KeyboardType.Number
                )

                Spacer(modifier = Modifier.height(12.dp))

                if (isEditMode) {
                    ExposedDropdownMenuBox(
                        expanded = genderExpanded,
                        onExpandedChange = { genderExpanded = !genderExpanded }
                    ) {
                        OutlinedTextField(
                            value = gender,
                            onValueChange = { },
                            label = { Text("Gender") },
                            leadingIcon = {
                                Icon(Icons.Default.Wc, contentDescription = "Gender")
                            },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = genderExpanded)
                            },
                            readOnly = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor()
                        )

                        ExposedDropdownMenu(
                            expanded = genderExpanded,
                            onDismissRequest = { genderExpanded = false }
                        ) {
                            genderOptions.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option) },
                                    onClick = {
                                        gender = option
                                        genderExpanded = false
                                    }
                                )
                            }
                        }
                    }
                } else {
                    ProfileField(
                        label = "Gender",
                        value = gender,
                        onValueChange = { },
                        isEditMode = false,
                        leadingIcon = Icons.Default.Wc
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Professional Information Section
                SectionHeader("Professional Information")

                Spacer(modifier = Modifier.height(16.dp))

                ProfileField(
                    label = "Occupation",
                    value = occupation,
                    onValueChange = { occupation = it },
                    isEditMode = isEditMode,
                    leadingIcon = Icons.Default.Work
                )

                Spacer(modifier = Modifier.height(12.dp))

                ProfileField(
                    label = "Education Level",
                    value = education,
                    onValueChange = { education = it },
                    isEditMode = isEditMode,
                    leadingIcon = Icons.Default.School
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Clinical Information Section
                SectionHeader("Clinical Information")

                Spacer(modifier = Modifier.height(16.dp))

                ProfileMultilineField(
                    label = "Primary Concerns",
                    value = primaryConcerns,
                    onValueChange = { primaryConcerns = it },
                    isEditMode = isEditMode,
                    leadingIcon = Icons.Default.Description
                )

                Spacer(modifier = Modifier.height(12.dp))

                ProfileField(
                    label = "Symptom Onset Age",
                    value = symptomOnsetAge,
                    onValueChange = { symptomOnsetAge = it },
                    isEditMode = isEditMode,
                    leadingIcon = Icons.Default.Timeline,
                    keyboardType = KeyboardType.Number
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Medical History Section
                SectionHeader("Medical History")

                Spacer(modifier = Modifier.height(16.dp))

                ProfileMultilineField(
                    label = "Medical History",
                    value = medicalHistory,
                    onValueChange = { medicalHistory = it },
                    isEditMode = isEditMode,
                    leadingIcon = Icons.Default.LocalHospital
                )

                Spacer(modifier = Modifier.height(12.dp))

                ProfileMultilineField(
                    label = "Current Medications",
                    value = currentMedications,
                    onValueChange = { currentMedications = it },
                    isEditMode = isEditMode,
                    leadingIcon = Icons.Default.MedicalServices
                )

                Spacer(modifier = Modifier.height(12.dp))

                if (isEditMode) {
                    ExposedDropdownMenuBox(
                        expanded = familyHistoryExpanded,
                        onExpandedChange = { familyHistoryExpanded = !familyHistoryExpanded }
                    ) {
                        OutlinedTextField(
                            value = familyHistoryADHD,
                            onValueChange = { },
                            label = { Text("Family History of ADHD") },
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
                } else {
                    ProfileField(
                        label = "Family History of ADHD",
                        value = familyHistoryADHD,
                        onValueChange = { },
                        isEditMode = false,
                        leadingIcon = Icons.Default.FamilyRestroom
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                ProfileMultilineField(
                    label = "Sleep Patterns",
                    value = sleepPatterns,
                    onValueChange = { sleepPatterns = it },
                    isEditMode = isEditMode,
                    leadingIcon = Icons.Default.Bedtime
                )

                // Add extra padding at bottom when FAB is visible
                if (isEditMode) {
                    Spacer(modifier = Modifier.height(80.dp))
                } else {
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }

    // Discard Changes Confirmation Dialog
    if (showDiscardDialog) {
        AlertDialog(
            onDismissRequest = { showDiscardDialog = false },
            icon = {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = "Warning",
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(32.dp)
                )
            },
            title = {
                Text(
                    text = "Discard Changes?",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "You have unsaved changes. Are you sure you want to discard them? This action cannot be undone.",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        revertChanges()
                        isEditMode = false
                        showDiscardDialog = false
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text(
                        text = "Discard",
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDiscardDialog = false }
                ) {
                    Text("Keep Editing")
                }
            },
            containerColor = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp
        )
    }

    // Save Confirmation Dialog
    if (showSaveDialog) {
        AlertDialog(
            onDismissRequest = { showSaveDialog = false },
            icon = {
                Icon(
                    imageVector = Icons.Default.Save,
                    contentDescription = "Save",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(32.dp)
                )
            },
            title = {
                Text(
                    text = "Save Changes?",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "Do you want to save the changes to your profile? Your updated information will be stored securely.",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        val updatedDetails = PatientDetails(
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

                        // TODO: Save logic here (database, API, ViewModel, etc.)
                        // Example: viewModel.updateUserProfile(updatedDetails)

                        showSaveDialog = false
                        isEditMode = false
                    }
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { showSaveDialog = false }) {
                    Text("Cancel")
                }
            },
            containerColor = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp
        )
    }
}

// Helper Composables
@Composable
fun SectionHeader(title: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            imageVector = Icons.Default.Segment,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
    }
    HorizontalDivider(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        color = MaterialTheme.colorScheme.outlineVariant
    )
}

@Composable
fun ProfileField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isEditMode: Boolean,
    leadingIcon: androidx.compose.ui.graphics.vector.ImageVector,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    if (isEditMode) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            leadingIcon = {
                Icon(leadingIcon, contentDescription = label)
            },
            singleLine = true,
            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                keyboardType = keyboardType
            ),
            modifier = Modifier.fillMaxWidth()
        )
    } else {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = label,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = value.ifEmpty { "Not set" },
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (value.isEmpty())
                            MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                        else
                            MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
fun ProfileDateField(
    label: String,
    value: String,
    isEditMode: Boolean,
    leadingIcon: androidx.compose.ui.graphics.vector.ImageVector,
    onEditClick: () -> Unit
) {
    if (isEditMode) {
        OutlinedTextField(
            value = value,
            onValueChange = { },
            label = { Text(label) },
            leadingIcon = {
                Icon(leadingIcon, contentDescription = label)
            },
            trailingIcon = {
                IconButton(onClick = onEditClick) {
                    Icon(Icons.Default.Edit, contentDescription = "Pick Date")
                }
            },
            readOnly = true,
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
    } else {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = label,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = value.ifEmpty { "Not set" },
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (value.isEmpty())
                            MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                        else
                            MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
fun ProfileMultilineField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isEditMode: Boolean,
    leadingIcon: androidx.compose.ui.graphics.vector.ImageVector
) {
    if (isEditMode) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            leadingIcon = {
                Icon(leadingIcon, contentDescription = label)
            },
            minLines = 2,
            maxLines = 4,
            modifier = Modifier.fillMaxWidth()
        )
    } else {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.Top
            ) {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = label,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = value.ifEmpty { "Not set" },
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (value.isEmpty())
                            MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                        else
                            MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}
