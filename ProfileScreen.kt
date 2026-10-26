package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.ZuolingoViewModel
import com.example.ui.theme.AppThemeMode
import com.example.ui.theme.CustomColorOptions
import com.example.ui.theme.DuolingoGemBlue
import com.example.ui.theme.DuolingoGoldCrown
import com.example.ui.theme.DuolingoGreen
import com.example.ui.theme.DuolingoGreenDark
import com.example.ui.theme.DuolingoHeartRed
import com.example.ui.theme.DuolingoStreakFlame

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProfileScreen(
    viewModel: ZuolingoViewModel
) {
    val userProfile by viewModel.userProfile.collectAsState()
    var showEditProfileDialog by remember { mutableStateOf(false) }
    var editNameInput by remember { mutableStateOf(userProfile.username) }
    var selectedAvatar by remember { mutableStateOf(userProfile.avatar) }

    val avatarOptions = listOf("🦉", "🦅", "🎓", "🌟", "🦁", "🕌", "🌺", "📚")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 680.dp)
                .testTag("profile_screen"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // --- 1. USER PROFILE CARD ---
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Avatar with Edit Badge
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(74.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFE8F5E9))
                                .clickable {
                                    editNameInput = userProfile.username
                                    selectedAvatar = userProfile.avatar
                                    showEditProfileDialog = true
                                }
                        ) {
                            Text(text = userProfile.avatar, fontSize = 40.sp)
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = userProfile.username,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = userProfile.email,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(DuolingoGreen.copy(alpha = 0.15f))
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "طالب مجتهد • Level ${userProfile.highestUnlockedLevel}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DuolingoGreenDark
                                )
                            }
                        }

                        IconButton(
                            onClick = {
                                editNameInput = userProfile.username
                                selectedAvatar = userProfile.avatar
                                showEditProfileDialog = true
                            },
                            modifier = Modifier.testTag("edit_profile_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit Profile",
                                tint = DuolingoGreen
                            )
                        }
                    }
                }
            }

            // --- 2. LEARNING STATS ---
            item {
                Text(
                    text = "Learning Statistics (إحصائيات التعلم)",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatBox(
                        modifier = Modifier.weight(1f),
                        icon = "🔥",
                        value = "${userProfile.streakDays} Days",
                        label = "Streak",
                        color = DuolingoStreakFlame
                    )
                    StatBox(
                        modifier = Modifier.weight(1f),
                        icon = "💎",
                        value = "${userProfile.gems} XP",
                        label = "Gems / XP",
                        color = DuolingoGemBlue
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatBox(
                        modifier = Modifier.weight(1f),
                        icon = "🎯",
                        value = "${userProfile.completedLevels.size} / 1000",
                        label = "Levels Done",
                        color = DuolingoGoldCrown
                    )
                    StatBox(
                        modifier = Modifier.weight(1f),
                        icon = "❤️",
                        value = "${userProfile.hearts}/${userProfile.maxHearts}",
                        label = "Hearts",
                        color = DuolingoHeartRed
                    )
                }
            }

            // --- 3. THEME SELECTION & CUSTOM COLOR PICKER ---
            item {
                Text(
                    text = "Themes & Appearance (المظهر)",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    // Default Zuolingo Green & White
                    ThemeRowItem(
                        title = "Zuolingo Green (Default)",
                        description = "Classic Duolingo green & white with smooth cards",
                        isSelected = userProfile.themeMode == AppThemeMode.ZUOLINGO_GREEN_WHITE,
                        icon = Icons.Default.LightMode,
                        onClick = { viewModel.setTheme(AppThemeMode.ZUOLINGO_GREEN_WHITE) }
                    )

                    // Dark Theme
                    ThemeRowItem(
                        title = "Dark Mode",
                        description = "Deep slate night theme for comfortable night study",
                        isSelected = userProfile.themeMode == AppThemeMode.DARK,
                        icon = Icons.Default.DarkMode,
                        onClick = { viewModel.setTheme(AppThemeMode.DARK) }
                    )

                    // Custom Color Theme
                    ThemeRowItem(
                        title = "Custom Accent Theme",
                        description = "Choose your own personalized favorite color!",
                        isSelected = userProfile.themeMode == AppThemeMode.CUSTOM,
                        icon = Icons.Default.Palette,
                        onClick = { viewModel.setTheme(AppThemeMode.CUSTOM) }
                    )

                    // Color palette chips (Visible when Custom is selected or to switch color directly)
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = "🎨 Select Custom Color Accent:",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                CustomColorOptions.forEach { (color, name) ->
                                    val isColorSelected = userProfile.customColorHex == color.value.toLong()
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier
                                            .size(42.dp)
                                            .clip(CircleShape)
                                            .background(color)
                                            .clickable {
                                                viewModel.setCustomColorHex(color.value.toLong())
                                            }
                                    ) {
                                        if (isColorSelected) {
                                            Icon(
                                                imageVector = Icons.Default.Check,
                                                contentDescription = name,
                                                tint = Color.White,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // --- 4. PREFERENCES (Sound & Haptics) ---
            item {
                Text(
                    text = "Preferences & Sound (الصوت والاهتزاز)",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        // Sound Switch
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = Icons.AutoMirrored.Filled.VolumeUp, contentDescription = null, tint = DuolingoGreen)
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(text = "Arabic Speech Pronunciation", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                    Text(text = "Pronounce words aloud via TTS", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                            Switch(
                                checked = userProfile.soundEnabled,
                                onCheckedChange = { viewModel.toggleSound(it) },
                                colors = SwitchDefaults.colors(checkedThumbColor = DuolingoGreen)
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Haptics Switch
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = Icons.Default.Vibration, contentDescription = null, tint = DuolingoStreakFlame)
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(text = "Haptic Vibrations", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                    Text(text = "Vibrate on quiz feedback", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                            Switch(
                                checked = userProfile.hapticsEnabled,
                                onCheckedChange = { viewModel.toggleHaptics(it) },
                                colors = SwitchDefaults.colors(checkedThumbColor = DuolingoStreakFlame)
                            )
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }

    // Edit Profile Dialog (Allows editing avatar & name)
    if (showEditProfileDialog) {
        AlertDialog(
            onDismissRequest = { showEditProfileDialog = false },
            title = { Text("Edit Profile & Avatar", fontWeight = FontWeight.Black) },
            text = {
                Column {
                    Text("Select Avatar:", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        avatarOptions.forEach { av ->
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(CircleShape)
                                    .background(if (selectedAvatar == av) Color(0xFFD7FFB8) else Color(0xFFF5F5F5))
                                    .clickable { selectedAvatar = av }
                            ) {
                                Text(text = av, fontSize = 24.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = editNameInput,
                        onValueChange = { editNameInput = it },
                        label = { Text("Display Name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.updateUserProfile(editNameInput, selectedAvatar)
                        showEditProfileDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DuolingoGreen)
                ) {
                    Text("Save", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditProfileDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun StatBox(
    modifier: Modifier,
    icon: String,
    value: String,
    label: String,
    color: Color
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = icon, fontSize = 22.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                fontWeight = FontWeight.Black,
                fontSize = 16.sp,
                color = color
            )
            Text(
                text = label,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun ThemeRowItem(
    title: String,
    description: String,
    isSelected: Boolean,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFFE8F5E9) else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 2.dp else 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isSelected) DuolingoGreenDark else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = description,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Selected",
                    tint = DuolingoGreenDark,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
