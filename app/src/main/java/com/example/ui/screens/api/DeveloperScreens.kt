package com.example.ui.screens.api

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SignalCellularAlt
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Webhook
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ApiServiceModel
import com.example.ui.components.TopZynSubPageHeader
import com.example.ui.theme.TopZynBackground
import com.example.ui.theme.TopZynCardBorder
import com.example.ui.theme.TopZynDarkBlack
import com.example.ui.theme.TopZynGreen
import com.example.ui.theme.TopZynGreenLight
import com.example.ui.theme.TopZynOrange
import com.example.ui.theme.TopZynOrangeContainer
import com.example.ui.theme.TopZynTextMuted
import com.example.ui.theme.TopZynTextPrimary
import com.example.ui.theme.TopZynTextSecondary
import com.example.ui.theme.TopZynWhite

// 34. Écran API & Développeurs
@Composable
fun FullDeveloperPortalScreen(
    apiServices: List<ApiServiceModel>,
    onOpenDocs: () -> Unit,
    onOpenKeys: () -> Unit,
    onOpenStatus: () -> Unit,
    onServiceClick: (ApiServiceModel) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(TopZynBackground)
    ) {
        TopZynSubPageHeader(
            title = "API & Développeurs",
            subtitle = "Intégration & Recharges automatisées",
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Quick Actions
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable(onClick = onOpenDocs),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = TopZynWhite),
                    border = androidx.compose.foundation.BorderStroke(1.dp, TopZynCardBorder)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Icon(Icons.Default.Description, contentDescription = null, tint = TopZynOrange)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Documentation", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text("Endpoints & Exemples", fontSize = 11.sp, color = TopZynTextSecondary)
                    }
                }

                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable(onClick = onOpenKeys),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = TopZynWhite),
                    border = androidx.compose.foundation.BorderStroke(1.dp, TopZynCardBorder)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Icon(Icons.Default.Key, contentDescription = null, tint = TopZynOrange)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Clés d'API", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text("Gestion des accès", fontSize = 11.sp, color = TopZynTextSecondary)
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onOpenStatus),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = TopZynWhite),
                border = androidx.compose.foundation.BorderStroke(1.dp, TopZynCardBorder)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.SignalCellularAlt, contentDescription = null, tint = TopZynGreen)
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text("Statut du système API", fontWeight = FontWeight.Bold, fontSize = 13.5.sp)
                            Text("Tous les services opérationnels (99.9%)", fontSize = 11.5.sp, color = TopZynTextSecondary)
                        }
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(TopZynGreenLight)
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text("Actif", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TopZynGreen)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text("Services disponibles", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TopZynDarkBlack)

            Spacer(modifier = Modifier.height(10.dp))

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                apiServices.forEach { service ->
                    val isAvailable = service.status == "Disponible"
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onServiceClick(service) },
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = TopZynWhite),
                        border = androidx.compose.foundation.BorderStroke(1.dp, TopZynCardBorder)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(TopZynOrange.copy(alpha = 0.1f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.Code, contentDescription = null, tint = TopZynOrange, modifier = Modifier.size(20.dp))
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(service.name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                    Text(service.description, fontSize = 11.5.sp, color = TopZynTextSecondary, maxLines = 1)
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (isAvailable) TopZynGreenLight else TopZynOrangeContainer)
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    service.status,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isAvailable) TopZynGreen else TopZynOrange
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// 37. Écran de gestion des clés API
@Composable
fun ApiKeysManagementScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    var liveKeyVisible by remember { mutableStateOf(false) }
    var copiedMessage by remember { mutableStateOf(false) }
    val apiKey = "tz_live_9921_84821a8c90fe719"
    var webhookUrl by remember { mutableStateOf("https://monsite.com/api/topzyn/webhook") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(TopZynBackground)
    ) {
        TopZynSubPageHeader(
            title = "Gestion des clés d'API",
            subtitle = "Environnement Production & Sandbox",
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = TopZynWhite),
                border = androidx.compose.foundation.BorderStroke(1.dp, TopZynCardBorder)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text("Clé Secrète de Production (Live Key)", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(8.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFFF4F4F5))
                            .padding(horizontal = 12.dp, vertical = 10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (liveKeyVisible) apiKey else "tz_live_••••••••••••••••••••",
                                fontFamily = FontFamily.Monospace,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Row {
                                IconButton(onClick = { liveKeyVisible = !liveKeyVisible }) {
                                    Icon(if (liveKeyVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility, contentDescription = null, modifier = Modifier.size(18.dp))
                                }
                                IconButton(onClick = {
                                    clipboardManager.setText(AnnotatedString(apiKey))
                                    copiedMessage = true
                                }) {
                                    Icon(Icons.Default.ContentCopy, contentDescription = null, tint = TopZynOrange, modifier = Modifier.size(18.dp))
                                }
                            }
                        }
                    }

                    if (copiedMessage) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("✓ Clé copiée dans le presse-papier", color = TopZynGreen, fontSize = 11.5.sp)
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text("URL de Webhook (Notifications instantanées)", fontWeight = FontWeight.Bold, fontSize = 13.5.sp)
                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = webhookUrl,
                        onValueChange = { webhookUrl = it },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = TopZynOrange,
                            unfocusedBorderColor = TopZynCardBorder
                        )
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = { /* Save webhook */ },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = TopZynDarkBlack)
                    ) {
                        Text("Enregistrer le Webhook", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

// 38. Écran de documentation API
@Composable
fun ApiDocumentationScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(TopZynBackground)
    ) {
        TopZynSubPageHeader(
            title = "Documentation API",
            subtitle = "Spécifications REST v1",
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text("Créer une commande de recharge", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TopZynDarkBlack)
            Spacer(modifier = Modifier.height(6.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF1E1E24))
                    .padding(14.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(TopZynOrange)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text("POST", color = TopZynWhite, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("/v1/orders/recharge", color = TopZynWhite, fontFamily = FontFamily.Monospace, fontSize = 12.5.sp)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Headers:\nAuthorization: Bearer <VOTRE_CLE_API>\nContent-Type: application/json\n\nBody Payload:\n{\n  \"game_id\": \"free_fire\",\n  \"package_id\": \"ff_2\",\n  \"player_id\": \"29841029\",\n  \"currency\": \"GDS\"\n}",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.5.sp,
                        color = Color(0xFFE4E4E7),
                        lineHeight = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text("Réponse 200 OK", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TopZynDarkBlack)
            Spacer(modifier = Modifier.height(6.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFF4F4F5))
                    .padding(14.dp)
            ) {
                Text(
                    text = "{\n  \"status\": \"success\",\n  \"order_number\": \"TZ-2026-99214\",\n  \"amount_gds\": 425.0,\n  \"delivered\": true\n}",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.5.sp,
                    color = TopZynDarkBlack,
                    lineHeight = 16.sp
                )
            }
        }
    }
}

// 39. Écran de statut des services API
@Composable
fun ApiStatusScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(TopZynBackground)
    ) {
        TopZynSubPageHeader(
            title = "Statut des services",
            subtitle = "Disponibilité temps réel",
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = TopZynWhite),
                border = androidx.compose.foundation.BorderStroke(1.dp, TopZynCardBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("TopZyn Core API", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text("Opérationnel", color = TopZynGreen, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                    Text("Latence : 42ms • Disponibilité : 99.98%", fontSize = 12.sp, color = TopZynTextSecondary)
                }
            }

            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = TopZynWhite),
                border = androidx.compose.foundation.BorderStroke(1.dp, TopZynCardBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("MonCash Gateway API", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text("Bientôt", color = TopZynOrange, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                    Text("En cours d'intégration avec l'opérateur", fontSize = 12.sp, color = TopZynTextSecondary)
                }
            }

            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = TopZynWhite),
                border = androidx.compose.foundation.BorderStroke(1.dp, TopZynCardBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Natcash Gateway API", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text("Bientôt", color = TopZynOrange, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                    Text("Phase de tests sandbox préliminaires", fontSize = 12.sp, color = TopZynTextSecondary)
                }
            }
        }
    }
}
