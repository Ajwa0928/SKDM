package com.example.crescent.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.crescent.model.MessageItem
import com.example.crescent.ui.CrescentViewModel
import com.example.ui.theme.*

@Composable
fun MessagesScreen(
    viewModel: CrescentViewModel,
    modifier: Modifier = Modifier
) {
    val messages by viewModel.messages.collectAsState()
    val student by viewModel.selectedStudent.collectAsState()
    val preSelectedRecipient by viewModel.composeRecipient.collectAsState()

    var showComposeDialog by remember { mutableStateOf(false) }

    // If pre-selected recipient exists (e.g. clicked from teacher profile), trigger compose
    LaunchedEffect(preSelectedRecipient) {
        if (preSelectedRecipient != null) {
            showComposeDialog = true
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(top = 12.dp, bottom = 88.dp)
        ) {
            // Header Info Card
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CrescentGreenPrimary),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column {
                                Text(
                                    text = "Direct School Communication",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = "Send queries to teachers, principal & administrative desks",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color(0xFFE2F5ED)
                                )
                            }
                            Surface(
                                color = CrescentGoldSecondary,
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = "${messages.size} Messages",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF2C2200),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Quick Desk shortcuts
            item {
                Text(
                    text = "Quick Contact Desks",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AssistChip(
                        onClick = {
                            viewModel.setComposeRecipient("Prof. Dr. Abdul Qayyum", "Principal")
                            showComposeDialog = true
                        },
                        label = { Text("Principal", fontSize = 11.sp) },
                        leadingIcon = { Icon(Icons.Default.School, null, modifier = Modifier.size(14.dp)) }
                    )
                    AssistChip(
                        onClick = {
                            viewModel.setComposeRecipient("Assoc. Prof. Tariq Mehmood", "Head of Physics")
                            showComposeDialog = true
                        },
                        label = { Text("Physics Dept", fontSize = 11.sp) },
                        leadingIcon = { Icon(Icons.Default.Science, null, modifier = Modifier.size(14.dp)) }
                    )
                    AssistChip(
                        onClick = {
                            viewModel.setComposeRecipient("Accounts Office", "Administration")
                            showComposeDialog = true
                        },
                        label = { Text("Accounts", fontSize = 11.sp) },
                        leadingIcon = { Icon(Icons.Default.Payment, null, modifier = Modifier.size(14.dp)) }
                    )
                }
            }

            // Message stream
            items(messages) { msg ->
                MessageBubbleCard(msg)
            }
        }

        // Compose Message Floating Action Button
        ExtendedFloatingActionButton(
            onClick = { showComposeDialog = true },
            icon = { Icon(Icons.Default.Edit, contentDescription = null) },
            text = { Text("Compose Message") },
            containerColor = CrescentGreenPrimary,
            contentColor = Color.White,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .testTag("compose_message_fab")
        )

        if (showComposeDialog) {
            ComposeMessageDialog(
                initialRecipient = preSelectedRecipient?.first ?: "Prof. Dr. Abdul Qayyum (Principal)",
                initialRole = preSelectedRecipient?.second ?: "Principal",
                onDismiss = {
                    showComposeDialog = false
                    viewModel.clearComposeRecipient()
                },
                onSend = { recName, recRole, subject, text ->
                    viewModel.sendMessage(recName, recRole, subject, text)
                    showComposeDialog = false
                    viewModel.clearComposeRecipient()
                }
            )
        }
    }
}

@Composable
private fun MessageBubbleCard(msg: MessageItem) {
    val isUser = msg.isFromUser
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        Card(
            shape = RoundedCornerShape(
                topStart = 14.dp,
                topEnd = 14.dp,
                bottomStart = if (isUser) 14.dp else 2.dp,
                bottomEnd = if (isUser) 2.dp else 14.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = if (isUser) CrescentGreenPrimary else MaterialTheme.colorScheme.surface
            ),
            border = if (!isUser) CardDefaults.outlinedCardBorder() else null,
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            modifier = Modifier.widthIn(max = 320.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (isUser) "You (${msg.senderRole})" else msg.senderName,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isUser) CrescentGoldSecondary else CrescentGreenPrimary
                    )

                    Text(
                        text = msg.timestamp,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isUser) Color(0xFFE2F5ED) else MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 10.sp
                    )
                }

                if (msg.subject.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = msg.subject,
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.bodySmall,
                        color = if (isUser) Color.White else MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = msg.messageText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isUser) Color.White else MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
private fun ComposeMessageDialog(
    initialRecipient: String,
    initialRole: String,
    onDismiss: () -> Unit,
    onSend: (String, String, String, String) -> Unit
) {
    var recipientName by remember { mutableStateOf(initialRecipient) }
    var recipientRole by remember { mutableStateOf(initialRole) }
    var subject by remember { mutableStateOf("") }
    var messageBody by remember { mutableStateOf("") }

    val recipientsList = listOf(
        "Prof. Dr. Abdul Qayyum (Principal)" to "Principal",
        "Assoc. Prof. Tariq Mehmood" to "Head of Physics",
        "Madam Zulaikha Bibi" to "Biology Faculty",
        "Engr. Farhan Ullah" to "Computer Science Head",
        "Sir Noor Muhammad" to "Head of Mathematics",
        "Prof. Aslam Pervez" to "English Faculty",
        "Accounts Office (Mr. Shafi)" to "Administration",
        "Examination Controller Desk" to "Academic Affairs"
    )

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Send Message",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = CrescentGreenPrimary
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Text(
                    text = "The Crescent School & Degree College Mirali Communication Desk",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text("Recipient:", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)

                // Quick Recipient selection row
                LazyColumn(
                    modifier = Modifier.heightIn(max = 120.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(recipientsList) { pair ->
                        val isSelected = recipientName == pair.first
                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                recipientName = pair.first
                                recipientRole = pair.second
                            },
                            label = { Text(pair.first, fontSize = 11.sp) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = subject,
                    onValueChange = { subject = it },
                    label = { Text("Subject / Query Purpose") },
                    placeholder = { Text("e.g. Leave Request / Examination syllabus query") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_message_subject")
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = messageBody,
                    onValueChange = { messageBody = it },
                    label = { Text("Message Body *") },
                    placeholder = { Text("Write your message to the faculty or school desk...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp)
                        .testTag("input_message_body")
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            val sub = subject.ifBlank { "Academic Inquiry" }
                            val body = messageBody.ifBlank { "Assalam-o-Alaikum Sir, I would like to inquire about the upcoming examination preparation schedule." }
                            onSend(recipientName, recipientRole, sub, body)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = CrescentGreenPrimary),
                        modifier = Modifier.testTag("confirm_send_message_button")
                    ) {
                        Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Send Message")
                    }
                }
            }
        }
    }
}
