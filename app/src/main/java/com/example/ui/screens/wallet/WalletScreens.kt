package com.example.ui.screens.wallet

import android.content.Intent
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.NorthEast
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.SouthWest
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.TransactionRecord
import com.example.data.model.UserAccount
import com.example.ui.components.TopZynSubPageHeader
import com.example.ui.theme.TopZynBackground
import com.example.ui.theme.TopZynCardBorder
import com.example.ui.theme.TopZynDarkBlack
import com.example.ui.theme.TopZynDarkCard
import com.example.ui.theme.TopZynGreen
import com.example.ui.theme.TopZynGreenLight
import com.example.ui.theme.TopZynOrange
import com.example.ui.theme.TopZynOrangeContainer
import com.example.ui.theme.TopZynTextMuted
import com.example.ui.theme.TopZynTextPrimary
import com.example.ui.theme.TopZynTextSecondary
import com.example.ui.theme.TopZynWhite
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// 26 & 27. Écran de dépôt et détails d'instructions de paiement
@Composable
fun FullDepositScreen(
    user: UserAccount?,
    onDepositSubmit: (amountGds: Double, method: String, reference: String) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedMethod by remember { mutableStateOf("MonCash") }
    var amountInput by remember { mutableStateOf("1000") }
    var userPhone by remember { mutableStateOf("+509 3412 8890") }
    var txnIdOrRef by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val quickAmounts = listOf("250", "500", "1000", "2500", "5000")

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(TopZynBackground)
    ) {
        TopZynSubPageHeader(
            title = "Ajouter des fonds",
            subtitle = "Dépôt sur votre portefeuille GDS",
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text("1. Sélectionnez le mode de dépôt", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TopZynDarkBlack)

            Spacer(modifier = Modifier.height(10.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                listOf("MonCash", "Natcash", "Virement").forEach { method ->
                    val isSelected = selectedMethod == method
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { selectedMethod = method },
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) TopZynOrangeContainer.copy(alpha = 0.4f) else TopZynWhite
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            1.5.dp,
                            if (isSelected) TopZynOrange else TopZynCardBorder
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(vertical = 12.dp, horizontal = 4.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(Icons.Default.PhoneAndroid, contentDescription = null, tint = if (isSelected) TopZynOrange else TopZynTextSecondary)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(method, fontWeight = FontWeight.Bold, fontSize = 12.5.sp, color = if (isSelected) TopZynOrange else TopZynDarkBlack)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Instructions Box
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF7ED)),
                border = androidx.compose.foundation.BorderStroke(1.dp, TopZynOrange.copy(alpha = 0.3f))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Instructions de paiement $selectedMethod :",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = TopZynOrange
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = if (selectedMethod == "MonCash")
                            "1. Composez *202# ou ouvrez l'application MonCash.\n2. Envoyez le montant au compte marchand TopZyn : +509 3800 1122.\n3. Renseignez l'ID de transaction reçu par SMS ci-dessous."
                        else if (selectedMethod == "Natcash")
                            "1. Ouvrez votre application Natcash ou composez le code USSD.\n2. Effectuez le transfert vers : +509 4100 2233.\n3. Indiquez la référence de transfert ci-dessous."
                        else
                            "Effectuez le virement bancaire vers UNIBANK / SOGEBANK au compte TopZyn S.A. : 129-8839-0199.",
                        fontSize = 12.sp,
                        color = TopZynTextSecondary,
                        lineHeight = 17.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text("2. Montant du dépôt en GDS", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TopZynDarkBlack)

            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(quickAmounts) { amt ->
                    val isSelected = amountInput == amt
                    FilterChip(
                        selected = isSelected,
                        onClick = { amountInput = amt },
                        label = { Text("$amt GDS", fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = TopZynOrange,
                            selectedLabelColor = TopZynWhite
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = amountInput,
                onValueChange = { amountInput = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Montant (GDS)") },
                suffix = { Text("GDS", fontWeight = FontWeight.Bold, color = TopZynOrange) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = TopZynOrange,
                    unfocusedBorderColor = TopZynCardBorder,
                    focusedContainerColor = TopZynWhite,
                    unfocusedContainerColor = TopZynWhite
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            OutlinedTextField(
                value = txnIdOrRef,
                onValueChange = { txnIdOrRef = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("ID de transaction ou N° de référence SMS") },
                placeholder = { Text("Ex: MC992819 ou NC10293") },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = TopZynOrange,
                    unfocusedBorderColor = TopZynCardBorder,
                    focusedContainerColor = TopZynWhite,
                    unfocusedContainerColor = TopZynWhite
                )
            )

            if (errorMessage != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(errorMessage ?: "", color = Color(0xFFDC2626), fontSize = 12.5.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    val amount = amountInput.toDoubleOrNull()
                    if (amount == null || amount <= 0) {
                        errorMessage = "Veuillez entrer un montant valide"
                        return@Button
                    }
                    if (txnIdOrRef.isBlank()) {
                        errorMessage = "Veuillez renseigner la référence de transaction reçue"
                        return@Button
                    }
                    onDepositSubmit(amount, selectedMethod, txnIdOrRef.trim())
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = TopZynOrange,
                    contentColor = TopZynWhite
                )
            ) {
                Text("Valider le dépôt", fontSize = 15.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// 29. Écran de transfert d’argent
@Composable
fun FullTransferScreen(
    user: UserAccount?,
    onTransferSubmit: (recipient: String, amountGds: Double, note: String) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var recipient by remember { mutableStateOf("") }
    var amountInput by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val currentBalance = user?.balanceGds ?: 0.0

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(TopZynBackground)
    ) {
        TopZynSubPageHeader(
            title = "Transfert d'argent",
            subtitle = "Envoi direct entre comptes TopZyn",
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = TopZynDarkBlack)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Solde disponible", color = Color(0xFFA1A1AA), fontSize = 13.sp)
                    Text("${"%.2f".format(currentBalance)} GDS", color = TopZynWhite, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            OutlinedTextField(
                value = recipient,
                onValueChange = { recipient = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Numéro ou Identifiant TopZyn du destinataire") },
                placeholder = { Text("+509 3412 8890 ou @jean99") },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = TopZynOrange,
                    unfocusedBorderColor = TopZynCardBorder,
                    focusedContainerColor = TopZynWhite,
                    unfocusedContainerColor = TopZynWhite
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = amountInput,
                onValueChange = { amountInput = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Montant à transférer") },
                suffix = { Text("GDS", fontWeight = FontWeight.Bold, color = TopZynOrange) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = TopZynOrange,
                    unfocusedBorderColor = TopZynCardBorder,
                    focusedContainerColor = TopZynWhite,
                    unfocusedContainerColor = TopZynWhite
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = note,
                onValueChange = { note = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Motif / Note (Optionnel)") },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = TopZynOrange,
                    unfocusedBorderColor = TopZynCardBorder,
                    focusedContainerColor = TopZynWhite,
                    unfocusedContainerColor = TopZynWhite
                )
            )

            if (errorMessage != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(errorMessage ?: "", color = Color(0xFFDC2626), fontSize = 12.5.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (recipient.isBlank()) {
                        errorMessage = "Veuillez renseigner le destinataire"
                        return@Button
                    }
                    val amount = amountInput.toDoubleOrNull()
                    if (amount == null || amount <= 0) {
                        errorMessage = "Montant invalide"
                        return@Button
                    }
                    if (currentBalance < amount) {
                        errorMessage = "Solde insuffisant"
                        return@Button
                    }
                    onTransferSubmit(recipient.trim(), amount, note.trim())
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = TopZynDarkBlack,
                    contentColor = TopZynWhite
                )
            ) {
                Text("Envoyer les Gourdes", fontSize = 15.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// 30. Écran de retrait
@Composable
fun FullWithdrawScreen(
    user: UserAccount?,
    onWithdrawSubmit: (amountGds: Double, method: String, account: String) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedMethod by remember { mutableStateOf("MonCash") }
    var amountInput by remember { mutableStateOf("") }
    var accountPhone by remember { mutableStateOf("+509 3412 8890") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val currentBalance = user?.balanceGds ?: 0.0

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(TopZynBackground)
    ) {
        TopZynSubPageHeader(
            title = "Retirer des fonds",
            subtitle = "Virement vers votre compte mobile",
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = TopZynDarkBlack)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Solde disponible au retrait", color = Color(0xFFA1A1AA), fontSize = 13.sp)
                    Text("${"%.2f".format(currentBalance)} GDS", color = TopZynWhite, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text("Méthode de réception", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TopZynDarkBlack)

            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                listOf("MonCash", "Natcash").forEach { m ->
                    val isSelected = selectedMethod == m
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { selectedMethod = m },
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) TopZynOrangeContainer.copy(alpha = 0.4f) else TopZynWhite
                        ),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, if (isSelected) TopZynOrange else TopZynCardBorder)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(Icons.Default.PhoneAndroid, contentDescription = null, tint = if (isSelected) TopZynOrange else TopZynTextSecondary)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(m, fontWeight = FontWeight.Bold, color = if (isSelected) TopZynOrange else TopZynDarkBlack)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            OutlinedTextField(
                value = accountPhone,
                onValueChange = { accountPhone = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Numéro de compte $selectedMethod bénéficiaire") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = TopZynOrange,
                    unfocusedBorderColor = TopZynCardBorder,
                    focusedContainerColor = TopZynWhite,
                    unfocusedContainerColor = TopZynWhite
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = amountInput,
                onValueChange = { amountInput = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Montant à retirer (GDS)") },
                suffix = { Text("GDS", fontWeight = FontWeight.Bold, color = TopZynOrange) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = TopZynOrange,
                    unfocusedBorderColor = TopZynCardBorder,
                    focusedContainerColor = TopZynWhite,
                    unfocusedContainerColor = TopZynWhite
                )
            )

            if (errorMessage != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(errorMessage ?: "", color = Color(0xFFDC2626), fontSize = 12.5.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    val amount = amountInput.toDoubleOrNull()
                    if (amount == null || amount <= 0) {
                        errorMessage = "Veuillez entrer un montant valide"
                        return@Button
                    }
                    if (currentBalance < amount) {
                        errorMessage = "Solde insuffisant pour ce retrait"
                        return@Button
                    }
                    onWithdrawSubmit(amount, selectedMethod, accountPhone.trim())
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = TopZynOrange,
                    contentColor = TopZynWhite
                )
            ) {
                Text("Demander le retrait", fontSize = 15.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// 31. Écran d’historique des transactions
@Composable
fun FullTransactionHistoryScreen(
    transactions: List<TransactionRecord>,
    onTransactionClick: (TransactionRecord) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedFilter by remember { mutableStateOf("Tous") }
    val filters = listOf("Tous", "Dépôts", "Recharges", "Transferts", "Retraits")
    val dateFormat = SimpleDateFormat("dd MMM yyyy à HH:mm", Locale.FRANCE)

    val filtered = transactions.filter {
        when (selectedFilter) {
            "Dépôts" -> it.type == "DEPOT"
            "Recharges" -> it.type == "RECHARGE"
            "Transferts" -> it.type == "TRANSFERT"
            "Retraits" -> it.type == "RETRAIT"
            else -> true
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(TopZynBackground)
    ) {
        TopZynSubPageHeader(
            title = "Historique des transactions",
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(filters) { f ->
                    FilterChip(
                        selected = selectedFilter == f,
                        onClick = { selectedFilter = f },
                        label = { Text(f, fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = TopZynOrange,
                            selectedLabelColor = TopZynWhite
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (filtered.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Aucune transaction trouvée", color = TopZynTextMuted, fontSize = 14.sp)
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(filtered) { tx ->
                        val isPositive = tx.amountGds > 0
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onTransactionClick(tx) },
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
                                            .clip(CircleShape)
                                            .background(if (isPositive) TopZynGreenLight else TopZynOrangeContainer),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = if (isPositive) Icons.Default.Add else Icons.Default.NorthEast,
                                            contentDescription = null,
                                            tint = if (isPositive) TopZynGreen else TopZynOrange,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(tx.title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = TopZynTextPrimary)
                                        Text(tx.details, fontSize = 11.5.sp, color = TopZynTextSecondary)
                                        Text(dateFormat.format(Date(tx.timestamp)), fontSize = 10.5.sp, color = TopZynTextMuted)
                                    }
                                }

                                Text(
                                    text = if (isPositive) "+${"%.2f".format(tx.amountGds)} GDS" else "${"%.2f".format(tx.amountGds)} GDS",
                                    fontSize = 14.5.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (isPositive) TopZynGreen else TopZynDarkBlack
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// 32 & 33. Écran de détails d’une transaction & Reçu partageable
@Composable
fun FullTransactionReceiptScreen(
    transaction: TransactionRecord,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val dateFormat = SimpleDateFormat("dd MMMM yyyy à HH:mm", Locale.FRANCE)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(TopZynBackground)
    ) {
        TopZynSubPageHeader(
            title = "Reçu Officiel TopZyn",
            subtitle = transaction.referenceCode,
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            // Official Receipt Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = TopZynWhite),
                border = androidx.compose.foundation.BorderStroke(1.dp, TopZynCardBorder)
            ) {
                Column(
                    modifier = Modifier.padding(22.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Logo Header
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_topzyn_logo),
                            contentDescription = null,
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(8.dp))
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("TopZyn", fontSize = 20.sp, fontWeight = FontWeight.Black, color = TopZynDarkBlack)
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text("REÇU DE TRANSACTION", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TopZynTextMuted, letterSpacing = 1.sp)

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = if (transaction.amountGds > 0) "+${"%.2f".format(transaction.amountGds)} GDS" else "${"%.2f".format(transaction.amountGds)} GDS",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Black,
                        color = if (transaction.amountGds > 0) TopZynGreen else TopZynDarkBlack
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    HorizontalDivider(color = TopZynCardBorder)

                    Spacer(modifier = Modifier.height(14.dp))

                    ReceiptRow("Référence", transaction.referenceCode, isMonospace = true)
                    Spacer(modifier = Modifier.height(8.dp))
                    ReceiptRow("Type d'opération", transaction.title)
                    Spacer(modifier = Modifier.height(8.dp))
                    ReceiptRow("Date et heure", dateFormat.format(Date(transaction.timestamp)))
                    Spacer(modifier = Modifier.height(8.dp))
                    ReceiptRow("Détails", transaction.details)
                    Spacer(modifier = Modifier.height(8.dp))
                    ReceiptRow("Statut", transaction.status, isSuccess = true)

                    Spacer(modifier = Modifier.height(20.dp))

                    // QR / Code verify
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFFF4F4F5))
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.QrCode, contentDescription = null, tint = TopZynOrange, modifier = Modifier.size(24.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Vérifié et sécurisé par TopZyn Haïti", fontSize = 11.5.sp, color = TopZynTextSecondary)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    val shareIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(
                            Intent.EXTRA_TEXT,
                            "Reçu TopZyn : ${transaction.title} de ${"%.2f".format(transaction.amountGds)} GDS (Réf : ${transaction.referenceCode})"
                        )
                        type = "text/plain"
                    }
                    context.startActivity(Intent.createChooser(shareIntent, "Partager le reçu TopZyn"))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = TopZynOrange,
                    contentColor = TopZynWhite
                )
            ) {
                Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Partager le reçu", fontSize = 15.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun ReceiptRow(label: String, value: String, isMonospace: Boolean = false, isSuccess: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontSize = 12.5.sp, color = TopZynTextMuted)
        Text(
            value,
            fontSize = 12.5.sp,
            fontWeight = if (isSuccess) FontWeight.Bold else FontWeight.Medium,
            fontFamily = if (isMonospace) FontFamily.Monospace else FontFamily.Default,
            color = if (isSuccess) TopZynGreen else TopZynTextPrimary
        )
    }
}
