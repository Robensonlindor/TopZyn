package com.example.ui.components

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.NorthEast
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.example.data.model.UserAccount
import com.example.ui.theme.TopZynCardBorder
import com.example.ui.theme.TopZynDarkBlack
import com.example.ui.theme.TopZynDarkCard
import com.example.ui.theme.TopZynOrange
import com.example.ui.theme.TopZynTextPrimary
import com.example.ui.theme.TopZynTextSecondary
import com.example.ui.theme.TopZynWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SendDialog(
    user: UserAccount?,
    onDismiss: () -> Unit,
    onConfirmSend: (recipient: String, amountGds: Double, note: String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var recipient by remember { mutableStateOf("") }
    var amountInput by remember { mutableStateOf("") }
    var noteInput by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val currentBalance = user?.balanceGds ?: 0.0

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
                .testTag("send_dialog")
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
                            .background(TopZynDarkCard),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.NorthEast,
                            contentDescription = null,
                            tint = TopZynOrange,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Envoyer des Gourdes",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TopZynTextPrimary
                        )
                        Text(
                            text = "Transfert instantané et gratuit entre utilisateurs TopZyn",
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

            Spacer(modifier = Modifier.height(14.dp))

            // Current balance info
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(TopZynDarkBlack)
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Solde disponible :",
                        fontSize = 12.5.sp,
                        color = Color(0xFFA1A1AA)
                    )
                    Text(
                        text = "${"%.2f".format(currentBalance)} GDS",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TopZynWhite
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Recipient Input
            OutlinedTextField(
                value = recipient,
                onValueChange = {
                    recipient = it
                    errorMessage = null
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("send_recipient_input"),
                label = { Text("Destinataire (Numéro ou Identifiant TopZyn)") },
                placeholder = { Text("Ex: +509 3788 1234 ou @alex99") },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = TopZynOrange,
                    unfocusedBorderColor = TopZynCardBorder
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Amount Input
            OutlinedTextField(
                value = amountInput,
                onValueChange = {
                    amountInput = it
                    errorMessage = null
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("send_amount_input"),
                label = { Text("Montant à envoyer") },
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

            // Note Input
            OutlinedTextField(
                value = noteInput,
                onValueChange = { noteInput = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Motif ou message (optionnel)") },
                placeholder = { Text("Ex: Pour la recharge Free Fire") },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = TopZynOrange,
                    unfocusedBorderColor = TopZynCardBorder
                )
            )

            if (errorMessage != null) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = errorMessage ?: "",
                    fontSize = 12.5.sp,
                    color = Color(0xFFDC2626)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Confirm Button
            Button(
                onClick = {
                    if (recipient.isBlank()) {
                        errorMessage = "Veuillez renseigner le destinataire"
                        return@Button
                    }
                    val amount = amountInput.toDoubleOrNull()
                    if (amount == null || amount <= 0) {
                        errorMessage = "Veuillez entrer un montant valide"
                        return@Button
                    }
                    if (currentBalance < amount) {
                        errorMessage = "Solde insuffisant (${"%.2f".format(currentBalance)} GDS disponibles)"
                        return@Button
                    }
                    onConfirmSend(recipient.trim(), amount, noteInput.trim())
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("confirm_send_button"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1E1E24),
                    contentColor = TopZynWhite
                )
            ) {
                Icon(
                    imageVector = Icons.Default.NorthEast,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = TopZynOrange
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Envoyer le transfert",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
