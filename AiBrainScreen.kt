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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.SentimentSatisfied
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AiChatMessage
import com.example.data.model.ArabicAnalysis
import com.example.ui.ZuolingoViewModel
import com.example.ui.theme.DuolingoGoldCrown
import com.example.ui.theme.DuolingoGreen
import com.example.ui.theme.DuolingoGreenDark
import com.example.ui.theme.WhatsAppBubbleIncomingLight
import com.example.ui.theme.WhatsAppBubbleOutgoingLight
import com.example.ui.theme.WhatsAppChatBackgroundLight
import com.example.ui.theme.WhatsAppTeal
import com.example.ui.theme.WhatsAppTickBlue
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AiBrainScreen(
    viewModel: ZuolingoViewModel
) {
    val messages by viewModel.chatMessages.collectAsState()
    val isThinking by viewModel.isAiThinking.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()

    var inputText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    var showMenu by remember { mutableStateOf(false) }
    var showWallpaperDialog by remember { mutableStateOf(false) }
    var showBubbleDialog by remember { mutableStateOf(false) }

    LaunchedEffect(messages.size, isThinking) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    // Dynamic WhatsApp Chat Wallpaper backgrounds
    val chatBackground = when (userProfile.chatBackgroundIndex) {
        1 -> Brush.verticalGradient(listOf(Color(0xFFE8F5E9), Color(0xFFC8E6C9))) // Emerald Garden
        2 -> Brush.verticalGradient(listOf(Color(0xFF263238), Color(0xFF102027))) // Desert Night
        3 -> Brush.verticalGradient(listOf(Color(0xFFEDE7F6), Color(0xFFD1C4E9))) // Royal Velvet
        else -> Brush.verticalGradient(listOf(Color(0xFFEFEAE2), Color(0xFFE5DDD5))) // WhatsApp Classic
    }

    // Outer container for desktop centering
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEFEAE2)),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 680.dp)
                .background(chatBackground)
                .testTag("ai_brain_screen")
        ) {
            // --- 1. WHATSAPP STYLE TOP APP BAR ---
            Surface(
                color = WhatsAppTeal,
                shadowElevation = 4.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        // Avatar
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF25D366))
                        ) {
                            Text(text = "🦉", fontSize = 24.sp)
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column {
                            Text(
                                text = "AI Brain (خبير النحو والصرف)",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = Color.White
                            )
                            Text(
                                text = if (isThinking) "typing..." else "online • Arabic Grammar Scholar",
                                fontSize = 11.sp,
                                color = if (isThinking) Color(0xFFD7FFB8) else Color.White.copy(alpha = 0.85f)
                            )
                        }
                    }

                    // 3-Dots Menu
                    Box {
                        IconButton(onClick = { showMenu = true }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "Menu",
                                tint = Color.White
                            )
                        }

                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("🎨 Change Chat Wallpaper") },
                                onClick = {
                                    showMenu = false
                                    showWallpaperDialog = true
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("💬 Bubble Style") },
                                onClick = {
                                    showMenu = false
                                    showBubbleDialog = true
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("🗑️ Clear Chat History") },
                                onClick = {
                                    showMenu = false
                                    viewModel.clearChat()
                                }
                            )
                        }
                    }
                }
            }

            // --- 2. MESSAGES LIST (No distracting words at the top, pure chat) ---
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                contentPadding = PaddingValues(vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(messages, key = { it.id }) { msg ->
                    if (msg.isUser) {
                        WhatsAppOutgoingBubble(message = msg)
                    } else {
                        WhatsAppIncomingAiCard(
                            message = msg,
                            onSpeak = { viewModel.audioHelper.speakArabic(it) }
                        )
                    }
                }

                if (isThinking) {
                    item {
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color.White)
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 2.dp,
                                color = WhatsAppTeal
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Analyzing Arabic Nahw & Sarf...",
                                fontSize = 12.sp,
                                color = Color(0xFF757575)
                            )
                        }
                    }
                }
            }

            // --- 3. WHATSAPP CHAT INPUT BAR (Without harsh outlines, perfect alignment) ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp, bottom = 8.dp, top = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // White Pill Container
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(26.dp))
                        .background(Color.White)
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.SentimentSatisfied,
                        contentDescription = "Emoji",
                        tint = Color(0xFF8696A0),
                        modifier = Modifier.size(22.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    BasicTextField(
                        value = inputText,
                        onValueChange = { inputText = it },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("ai_brain_input_field"),
                        textStyle = TextStyle(
                            fontSize = 15.sp,
                            color = Color(0xFF111B21)
                        ),
                        cursorBrush = SolidColor(WhatsAppTeal),
                        maxLines = 4,
                        decorationBox = { innerTextField ->
                            if (inputText.isEmpty()) {
                                Text(
                                    text = "Type any Arabic word or sentence...",
                                    fontSize = 14.sp,
                                    color = Color(0xFF8696A0)
                                )
                            }
                            innerTextField()
                        }
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Icon(
                        imageVector = Icons.Default.AttachFile,
                        contentDescription = "Attach",
                        tint = Color(0xFF8696A0),
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(6.dp))

                // Green Circular Send Button
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(WhatsAppTeal)
                        .clickable(enabled = inputText.isNotBlank() && !isThinking) {
                            if (inputText.isNotBlank()) {
                                viewModel.sendAiBrainPrompt(inputText)
                                inputText = ""
                            }
                        }
                        .testTag("ai_brain_send_button")
                ) {
                    Icon(
                        imageVector = if (inputText.isNotBlank()) Icons.AutoMirrored.Filled.Send else Icons.Default.Mic,
                        contentDescription = "Send",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }

    // Wallpaper Selection Dialog
    if (showWallpaperDialog) {
        AlertDialog(
            onDismissRequest = { showWallpaperDialog = false },
            title = { Text("Choose Chat Wallpaper", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    val wallpapers = listOf("WhatsApp Classic", "Emerald Oasis", "Desert Night", "Royal Velvet")
                    wallpapers.forEachIndexed { idx, name ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (userProfile.chatBackgroundIndex == idx) Color(0xFFE8F5E9) else Color(0xFFF5F5F5))
                                .clickable {
                                    viewModel.setChatBackground(idx)
                                    showWallpaperDialog = false
                                }
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = name,
                                fontWeight = if (userProfile.chatBackgroundIndex == idx) FontWeight.Bold else FontWeight.Normal,
                                color = if (userProfile.chatBackgroundIndex == idx) DuolingoGreenDark else Color(0xFF212121),
                                modifier = Modifier.weight(1f)
                            )
                            if (userProfile.chatBackgroundIndex == idx) {
                                Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = DuolingoGreen)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showWallpaperDialog = false }) {
                    Text("Close")
                }
            }
        )
    }

    // Bubble Style Dialog
    if (showBubbleDialog) {
        AlertDialog(
            onDismissRequest = { showBubbleDialog = false },
            title = { Text("Choose Bubble Style", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    val styles = listOf("WhatsApp Green (Default)", "Clean Minimal", "Emerald Glow")
                    styles.forEachIndexed { idx, style ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (userProfile.chatBubbleStyleIndex == idx) Color(0xFFE8F5E9) else Color(0xFFF5F5F5))
                                .clickable {
                                    viewModel.setChatBubbleStyle(idx)
                                    showBubbleDialog = false
                                }
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = style,
                                fontWeight = if (userProfile.chatBubbleStyleIndex == idx) FontWeight.Bold else FontWeight.Normal,
                                color = if (userProfile.chatBubbleStyleIndex == idx) DuolingoGreenDark else Color(0xFF212121),
                                modifier = Modifier.weight(1f)
                            )
                            if (userProfile.chatBubbleStyleIndex == idx) {
                                Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = DuolingoGreen)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showBubbleDialog = false }) {
                    Text("Close")
                }
            }
        )
    }
}

@Composable
fun WhatsAppOutgoingBubble(message: AiChatMessage) {
    val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
    val formattedTime = timeFormat.format(Date(message.timestamp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .clip(RoundedCornerShape(topStart = 14.dp, topEnd = 2.dp, bottomStart = 14.dp, bottomEnd = 14.dp))
                .background(Color(0xFFE7FFDB))
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Column {
                Text(
                    text = message.text,
                    color = Color(0xFF111B21),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row(
                    modifier = Modifier.align(Alignment.End),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = formattedTime,
                        fontSize = 10.sp,
                        color = Color(0xFF667781)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Icon(
                        imageVector = Icons.Default.DoneAll,
                        contentDescription = "Read",
                        tint = WhatsAppTickBlue,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun WhatsAppIncomingAiCard(
    message: AiChatMessage,
    onSpeak: (String) -> Unit
) {
    val analysis = message.analysis
    val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
    val formattedTime = timeFormat.format(Date(message.timestamp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = 340.dp)
                .clip(RoundedCornerShape(topStart = 2.dp, topEnd = 16.dp, bottomStart = 16.dp, bottomEnd = 16.dp))
                .background(Color.White)
                .padding(14.dp)
        ) {
            Column {
                if (analysis != null) {
                    // Header with Arabic Word and Speaker
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = analysis.word,
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Black,
                                color = DuolingoGreenDark
                            )
                            Text(
                                text = analysis.transliteration,
                                fontSize = 13.sp,
                                color = Color(0xFF757575)
                            )
                        }

                        IconButton(
                            onClick = { onSpeak(analysis.word) },
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFE8F5E9))
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                                contentDescription = "Pronounce",
                                tint = DuolingoGreenDark,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Part of speech badge
                    Row {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFE8F5E9))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = "🏷️ ${analysis.partOfSpeech.arabicTitle} (${analysis.partOfSpeech.urduTitle} / ${analysis.partOfSpeech.englishTitle})",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = DuolingoGreenDark
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // 1. Meaning
                    AiSectionItem(
                        icon = "📖",
                        title = "المعنى (Meaning)",
                        body = "• اردو: ${analysis.urduMeaning}\n• English: ${analysis.englishMeaning}",
                        accentColor = Color(0xFF00897B)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // 2. Nahw & I'rab
                    AiSectionItem(
                        icon = "📚",
                        title = "النحو والإعراب (Nahw / Syntax)",
                        body = analysis.nahw,
                        accentColor = Color(0xFF1E88E5)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // 3. Sarf & Morphology
                    AiSectionItem(
                        icon = "🔬",
                        title = "الصرف والاشتقاق (Sarf / Root)",
                        body = analysis.sarf,
                        accentColor = Color(0xFF8E24AA)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // 4. Kaeda (Simple rule for kids & beginners)
                    AiSectionItem(
                        icon = "💡",
                        title = "القاعدة الميسرة (Rule / Kaeda)",
                        body = analysis.qaida,
                        accentColor = Color(0xFFE65100)
                    )

                    if (analysis.examples.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(6.dp))
                        AiSectionItem(
                            icon = "🌟",
                            title = "أمثلة (Examples)",
                            body = analysis.examples.joinToString("\n") { (arb, trans) -> "• $arb ➔ $trans" },
                            accentColor = Color(0xFF2E7D32)
                        )
                    }
                } else {
                    // Plain text response
                    Text(
                        text = message.text,
                        fontSize = 14.sp,
                        color = Color(0xFF111B21),
                        lineHeight = 20.sp
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Time footer
                Text(
                    text = formattedTime,
                    fontSize = 10.sp,
                    color = Color(0xFF667781),
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}

@Composable
fun AiSectionItem(
    icon: String,
    title: String,
    body: String,
    accentColor: Color
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFFF7F9FA))
            .padding(8.dp)
    ) {
        Column {
            Text(
                text = "$icon $title",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = accentColor
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = body,
                fontSize = 12.sp,
                color = Color(0xFF37474F),
                lineHeight = 17.sp
            )
        }
    }
}
