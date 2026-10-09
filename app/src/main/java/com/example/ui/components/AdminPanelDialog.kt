package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ApiServiceModel
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminPanelDialog(
    apiServices: List<ApiServiceModel>,
    onToggleApiStatus: (serviceId: String, currentStatus: String) -> Unit,
    onAddGame: (name: String, category: String, minPrice: Double, currency: String, drawableName: String) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var newGameName by remember { mutableStateOf("") }
    var newGameCategory by remember { mutableStateOf("Jeux Mobile") }
    var newGamePrice by remember { mutableStateOf("120") }
    var newGameCurrency by remember { mutableStateOf("Diamants") }
    var addError by remember { mutableStateOf<String?>(null) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = TopZynWhite,
        dragHandle = null
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 18.dp)
                .testTag("admin_panel_sheet")
        ) {
            // Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(TopZynOrange.copy(alpha = 0.12f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AdminPanelSettings,
                                contentDescription = null,
                                tint = TopZynOrange,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Panneau d'Administration",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = TopZynTextPrimary
                            )
                            Text(
                                text = "Gestion en temps réel de la plateforme TopZyn",
                                fontSize = 12.sp,
                                color = TopZynTextSecondary
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFF4F4F5))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Fermer",
                            tint = TopZynDarkBlack,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Section 1: Add New Game to Catalog
                Text(
                    text = "1. Ajouter un nouveau jeu au catalogue",
                    fontSize = 14.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = TopZynDarkBlack
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = newGameName,
                    onValueChange = { newGameName = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Nom du jeu (Ex: Brawl Stars, Roblox)") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = TopZynOrange,
                        unfocusedBorderColor = TopZynCardBorder
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = newGamePrice,
                        onValueChange = { newGamePrice = it },
                        modifier = Modifier.weight(1f),
                        label = { Text("Prix minimum") },
                        suffix = { Text("GDS") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = TopZynOrange,
                            unfocusedBorderColor = TopZynCardBorder
                        )
                    )

                    OutlinedTextField(
                        value = newGameCurrency,
                        onValueChange = { newGameCurrency = it },
                        modifier = Modifier.weight(1f),
                        label = { Text("Unité monnaie") },
                        placeholder = { Text("Gems, UC") },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = TopZynOrange,
                            unfocusedBorderColor = TopZynCardBorder
                        )
                    )
                }

                if (addError != null) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = addError ?: "", color = Color(0xFFDC2626), fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = {
                        if (newGameName.isBlank()) {
                            addError = "Veuillez entrer le nom du jeu"
                            return@Button
                        }
                        val price = newGamePrice.toDoubleOrNull() ?: 100.0
                        onAddGame(
                            newGameName.trim(),
                            newGameCategory,
                            price,
                            newGameCurrency.trim().ifBlank { "Diamants" },
                            "img_free_fire"
                        )
                        newGameName = ""
                        addError = null
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = TopZynOrange,
                        contentColor = TopZynWhite
                    )
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Ajouter le jeu au catalogue", fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Section 2: Manage API Services Status
                Text(
                    text = "2. Gestion des Services & API (Activer / Désactiver)",
                    fontSize = 14.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = TopZynDarkBlack
                )

                Spacer(modifier = Modifier.height(10.dp))
            }

            items(apiServices) { service ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, TopZynCardBorder)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = service.name,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = TopZynTextPrimary
                            )
                            Text(
                                text = "Statut : ${service.status}",
                                fontSize = 12.sp,
                                color = when (service.status) {
                                    "Disponible" -> TopZynGreen
                                    "Bientôt" -> TopZynOrange
                                    else -> TopZynTextMuted
                                },
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        OutlinedButton(
                            onClick = { onToggleApiStatus(service.id, service.status) },
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Sync,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Changer", fontSize = 11.5.sp)
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
