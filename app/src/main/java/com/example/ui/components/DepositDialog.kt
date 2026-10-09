package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCard
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
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
fun DepositDialog(
    onDismiss: () -> Unit,
    onConfirmDeposit: (amountGds: Double, method: String, phoneOrRef: String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var selectedMethod by remember { mutableStateOf("MonCash") }
    var amountInput by remember { mutableStateOf("1000") }
    var phoneInput by remember { mutableStateOf("+509 3412 8890") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val quickAmounts = listOf(250.0, 500.0, 1000.0, 2500.0, 5000.0)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = TopZynWhite,
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 18.dp)
                .testTag("deposit_dialog")
        ) {
            // Header
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
                            imageVector = Icons.Default.AddCard,
                            contentDescription = null,
                            tint = TopZynOrange,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Ajouter des fonds",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TopZynTextPrimary
                        )
                        Text(
                            text = "Rechargez votre portefeuille TopZyn en GDS",
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

            // Method Selector
            Text(
                text = "Sélectionnez le moyen de paiement",
                fontSize = 13.5.sp,
                fontWeight = FontWeight.Bold,
                color = TopZynDarkBlack
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                listOf("MonCash", "Natcash", "Virement").forEach { method ->
                    val isSelected = selectedMethod == method
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { selectedMethod = method }
                            .testTag("deposit_method_$method"),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) TopZynOrangeContainer.copy(alpha = 0.4f) else TopZynWhite
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            1.5.dp,
                            if (isSelected) TopZynOrange else TopZynCardBorder
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.PhoneAndroid,
                                contentDescription = null,
                                tint = if (isSelected) TopZynOrange else TopZynTextSecondary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = method,
                                fontSize = 12.5.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) TopZynOrange else TopZynTextPrimary
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Quick Amount Chips
            Text(
                text = "Montant en Gourdes (GDS)",
                fontSize = 13.5.sp,
                fontWeight = FontWeight.Bold,
                color = TopZynDarkBlack
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                quickAmounts.forEach { amount ->
                    val isSelected = amountInput == amount.toInt().toString()
                    FilterChip(
                        selected = isSelected,
                        onClick = { amountInput = amount.toInt().toString() },
                        label = {
                            Text(
                                text = "${amount.toInt()} GDS",
                                fontSize = 11.5.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = TopZynOrange,
                            selectedLabelColor = TopZynWhite,
                            containerColor = Color(0xFFF4F4F5),
                            labelColor = TopZynTextPrimary
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Amount Input
            OutlinedTextField(
                value = amountInput,
                onValueChange = {
                    amountInput = it
                    errorMessage = null
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("deposit_amount_input"),
                label = { Text("Montant personnalisé") },
                suffix = { Text("GDS", fontWeight = FontWeight.Bold, color = TopZynOrange) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = TopZynOrange,
                    unfocusedBorderColor = TopZynCardBorder
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Phone / Account number
            OutlinedTextField(
                value = phoneInput,
                onValueChange = { phoneInput = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("deposit_phone_input"),
                label = { Text("Numéro de compte / téléphone $selectedMethod") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = TopZynOrange,
                    unfocusedBorderColor = TopZynCardBorder
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Fee info
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(TopZynGreenLight)
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "✓ Frais de transaction : 0 GDS (Offert par TopZyn)",
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TopZynGreen
                )
            }

            if (errorMessage != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = errorMessage ?: "",
                    fontSize = 12.sp,
                    color = Color(0xFFDC2626)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Confirm Button
            Button(
                onClick = {
                    val parsed = amountInput.toDoubleOrNull()
                    if (parsed == null || parsed <= 0) {
                        errorMessage = "Veuillez entrer un montant valide supérieur à 0"
                        return@Button
                    }
                    if (phoneInput.isBlank()) {
                        errorMessage = "Veuillez renseigner un numéro valide"
                        return@Button
                    }
                    onConfirmDeposit(parsed, selectedMethod, phoneInput.trim())
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("confirm_deposit_button"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = TopZynOrange,
                    contentColor = TopZynWhite
                )
            ) {
                Text(
                    text = "Confirmer le dépôt",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
