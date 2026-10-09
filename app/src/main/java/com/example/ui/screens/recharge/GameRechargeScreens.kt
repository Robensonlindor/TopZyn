package com.example.ui.screens.recharge

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.GameItem
import com.example.data.model.GamePackage
import com.example.data.model.OrderRecord
import com.example.data.model.UserAccount
import com.example.ui.components.TopZynSubPageHeader
import com.example.ui.theme.TopZynBackground
import com.example.ui.theme.TopZynCardBorder
import com.example.ui.theme.TopZynDarkBlack
import com.example.ui.theme.TopZynGold
import com.example.ui.theme.TopZynGoldLight
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

fun getGameDrawableRes(drawableName: String): Int {
    return when (drawableName) {
        "img_free_fire" -> R.drawable.img_free_fire
        "img_pubg_mobile" -> R.drawable.img_pubg_mobile
        "img_mobile_legends" -> R.drawable.img_mobile_legends
        "img_cod_mobile" -> R.drawable.img_cod_mobile
        else -> R.drawable.ic_topzyn_logo
    }
}

// 15. Écran de détails d’un jeu
@Composable
fun GameDetailScreen(
    game: GameItem,
    onStartRechargeClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(TopZynBackground)
            .verticalScroll(rememberScrollState())
    ) {
        TopZynSubPageHeader(
            title = game.name,
            subtitle = game.category,
            onBackClick = onBackClick
        )

        // Banner Image
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(210.dp)
                .background(TopZynDarkBlack)
        ) {
            Image(
                painter = painterResource(id = getGameDrawableRes(game.imageDrawableName)),
                contentDescription = game.name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(TopZynDarkBlack.copy(alpha = 0.85f))
                    .padding(horizontal = 10.dp, vertical = 5.dp)
            ) {
                Text(
                    text = "Devise : ${game.currencyName}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = TopZynGold
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = game.name,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = TopZynDarkBlack
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "À partir de ${"%.0f".format(game.minPriceGds)} GDS",
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TopZynOrange
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "À propos du service",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = TopZynDarkBlack
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = game.description.ifBlank {
                    "Recharge officielle et instantanée disponible 24h/24 et 7j/7 sur TopZyn. Entrez votre identifiant de compte et recevez vos ${game.currencyName} en moins d'une minute !"
                },
                fontSize = 13.sp,
                color = TopZynTextSecondary,
                lineHeight = 19.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Information Card
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = TopZynWhite),
                border = androidx.compose.foundation.BorderStroke(1.dp, TopZynCardBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = TopZynGreen, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Aucun mot de passe requis (ID joueur uniquement)", fontSize = 12.5.sp, color = TopZynTextPrimary)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = TopZynGreen, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Crédit instantané sur votre compte de jeu", fontSize = 12.5.sp, color = TopZynTextPrimary)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = TopZynGreen, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Paiement sécurisé via Portefeuille GDS, MonCash ou Natcash", fontSize = 12.5.sp, color = TopZynTextPrimary)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onStartRechargeClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("start_recharge_button"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = TopZynOrange,
                    contentColor = TopZynWhite
                )
            ) {
                Text("Procéder à la recharge", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(8.dp))
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(18.dp))
            }
        }
    }
}

// 16-20. Écran de recharge dédié (Free Fire, PUBG, MLBB, COD, etc.)
@Composable
fun DedicatedGameRechargeScreen(
    game: GameItem,
    packages: List<GamePackage>,
    user: UserAccount?,
    onConfirmOrder: (pkg: GamePackage, playerId: String, zoneId: String?, paymentMethod: String) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var playerId by remember { mutableStateOf("") }
    var zoneId by remember { mutableStateOf("") }
    var selectedPackageId by remember(packages) {
        mutableStateOf(packages.firstOrNull { it.isPopular }?.id ?: packages.firstOrNull()?.id ?: "")
    }
    var selectedPaymentMethod by remember { mutableStateOf("WALLET") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val selectedPackage = packages.firstOrNull { it.id == selectedPackageId }
    val walletBalance = user?.balanceGds ?: 0.0

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(TopZynBackground)
    ) {
        TopZynSubPageHeader(
            title = "Recharge ${game.name}",
            subtitle = "Saisie des identifiants & packs",
            onBackClick = onBackClick
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 14.dp)
        ) {
            // Step 1: Identifier Input
            item {
                Text(
                    text = "1. Informations du compte (${game.name})",
                    fontSize = 14.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = TopZynDarkBlack
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = playerId,
                    onValueChange = {
                        playerId = it
                        errorMessage = null
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("game_player_id_field"),
                    label = { Text(game.idPlaceholder) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = TopZynOrange,
                        unfocusedBorderColor = TopZynCardBorder,
                        focusedContainerColor = TopZynWhite,
                        unfocusedContainerColor = TopZynWhite
                    )
                )

                if (game.requiresZoneId) {
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = zoneId,
                        onValueChange = { zoneId = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("game_zone_id_field"),
                        label = { Text(game.zonePlaceholder) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = TopZynOrange,
                            unfocusedBorderColor = TopZynCardBorder,
                            focusedContainerColor = TopZynWhite,
                            unfocusedContainerColor = TopZynWhite
                        )
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = "2. Choisissez votre pack (${game.currencyName})",
                    fontSize = 14.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = TopZynDarkBlack
                )

                Spacer(modifier = Modifier.height(10.dp))
            }

            // Package List
            items(packages) { pkg ->
                val isSelected = pkg.id == selectedPackageId
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .clickable { selectedPackageId = pkg.id }
                        .testTag("package_select_${pkg.id}"),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) TopZynOrangeContainer.copy(alpha = 0.45f) else TopZynWhite
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        if (isSelected) 2.dp else 1.dp,
                        if (isSelected) TopZynOrange else TopZynCardBorder
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(if (isSelected) TopZynOrange else Color(0xFFF4F4F5)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Diamond,
                                    contentDescription = null,
                                    tint = if (isSelected) TopZynWhite else TopZynOrange,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(pkg.title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = TopZynTextPrimary)
                                if (pkg.bonus != null) {
                                    Text(pkg.bonus, color = TopZynGreen, fontSize = 11.5.sp, fontWeight = FontWeight.SemiBold)
                                }
                            }
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "${"%.0f".format(pkg.priceGds)} GDS",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Black,
                                color = if (isSelected) TopZynOrange else TopZynDarkBlack
                            )
                            if (pkg.isPopular) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(TopZynGoldLight)
                                        .padding(horizontal = 5.dp, vertical = 1.dp)
                                ) {
                                    Text("POPULAIRE", fontSize = 8.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFFB45309))
                                }
                            }
                        }
                    }
                }
            }

            // Step 3: Payment Choice
            item {
                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = "3. Mode de paiement",
                    fontSize = 14.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = TopZynDarkBlack
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Wallet Option
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedPaymentMethod = "WALLET" },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = TopZynWhite),
                    border = androidx.compose.foundation.BorderStroke(
                        if (selectedPaymentMethod == "WALLET") 1.5.dp else 1.dp,
                        if (selectedPaymentMethod == "WALLET") TopZynOrange else TopZynCardBorder
                    )
                ) {
                    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = selectedPaymentMethod == "WALLET",
                            onClick = { selectedPaymentMethod = "WALLET" },
                            colors = RadioButtonDefaults.colors(selectedColor = TopZynOrange)
                        )
                        Icon(Icons.Default.AccountBalanceWallet, contentDescription = null, tint = TopZynOrange)
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text("Solde Wallet TopZyn (Recommandé)", fontWeight = FontWeight.Bold, fontSize = 13.5.sp, color = TopZynTextPrimary)
                            Text("Disponible : ${"%.2f".format(walletBalance)} GDS", fontSize = 11.5.sp, color = TopZynTextSecondary)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // MonCash Option
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedPaymentMethod = "MONCASH" },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = TopZynWhite),
                    border = androidx.compose.foundation.BorderStroke(
                        if (selectedPaymentMethod == "MONCASH") 1.5.dp else 1.dp,
                        if (selectedPaymentMethod == "MONCASH") TopZynOrange else TopZynCardBorder
                    )
                ) {
                    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = selectedPaymentMethod == "MONCASH",
                            onClick = { selectedPaymentMethod = "MONCASH" },
                            colors = RadioButtonDefaults.colors(selectedColor = TopZynOrange)
                        )
                        Icon(Icons.Default.PhoneAndroid, contentDescription = null, tint = TopZynOrange)
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text("MonCash", fontWeight = FontWeight.Bold, fontSize = 13.5.sp, color = TopZynTextPrimary)
                            Text("Paiement mobile instantané", fontSize = 11.5.sp, color = TopZynTextSecondary)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Natcash Option
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedPaymentMethod = "NATCASH" },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = TopZynWhite),
                    border = androidx.compose.foundation.BorderStroke(
                        if (selectedPaymentMethod == "NATCASH") 1.5.dp else 1.dp,
                        if (selectedPaymentMethod == "NATCASH") TopZynOrange else TopZynCardBorder
                    )
                ) {
                    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = selectedPaymentMethod == "NATCASH",
                            onClick = { selectedPaymentMethod = "NATCASH" },
                            colors = RadioButtonDefaults.colors(selectedColor = TopZynOrange)
                        )
                        Icon(Icons.Default.PhoneAndroid, contentDescription = null, tint = TopZynOrange)
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text("Natcash", fontWeight = FontWeight.Bold, fontSize = 13.5.sp, color = TopZynTextPrimary)
                            Text("Paiement direct Natcash", fontSize = 11.5.sp, color = TopZynTextSecondary)
                        }
                    }
                }

                if (errorMessage != null) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(errorMessage ?: "", color = Color(0xFFDC2626), fontSize = 12.5.sp)
                }

                Spacer(modifier = Modifier.height(20.dp))

                val totalGds = selectedPackage?.priceGds ?: 0.0

                Button(
                    onClick = {
                        if (playerId.isBlank()) {
                            errorMessage = "Veuillez renseigner votre ID joueur"
                            return@Button
                        }
                        if (game.requiresZoneId && zoneId.isBlank()) {
                            errorMessage = "Veuillez renseigner le Zone ID"
                            return@Button
                        }
                        if (selectedPackage == null) {
                            errorMessage = "Veuillez sélectionner un pack"
                            return@Button
                        }
                        if (selectedPaymentMethod == "WALLET" && walletBalance < totalGds) {
                            errorMessage = "Solde insuffisant (${"%.2f".format(walletBalance)} GDS dispo). Déposez des fonds."
                            return@Button
                        }
                        onConfirmOrder(selectedPackage, playerId.trim(), zoneId.trim().ifBlank { null }, selectedPaymentMethod)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("submit_game_order_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = TopZynOrange,
                        contentColor = TopZynWhite
                    )
                ) {
                    Text("Confirmer la commande • ${"%.0f".format(totalGds)} GDS", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}

// 21. Écran de récapitulatif de la commande
// 22. Écran de confirmation de la recharge
// 23. Écran de suivi de la commande
// 24. Écran de détails d’une commande
@Composable
fun OrderTrackingScreen(
    order: OrderRecord,
    onBackToHome: () -> Unit,
    onContactSupport: (orderNumber: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val dateFormat = SimpleDateFormat("dd MMMM yyyy à HH:mm", Locale.FRANCE)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(TopZynBackground)
    ) {
        TopZynSubPageHeader(
            title = "Suivi de Commande",
            subtitle = order.orderNumber,
            onBackClick = onBackToHome
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            // Big Status Circle
            Box(
                modifier = Modifier
                    .size(76.dp)
                    .clip(CircleShape)
                    .background(TopZynGreenLight)
                    .border(2.dp, TopZynGreen.copy(alpha = 0.3f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Check, contentDescription = null, tint = TopZynGreen, modifier = Modifier.size(40.dp))
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text("Commande Confirmée !", fontSize = 20.sp, fontWeight = FontWeight.Black, color = TopZynDarkBlack)
            Text("N° ${order.orderNumber}", fontSize = 13.sp, color = TopZynOrange, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(20.dp))

            // Status Stepper Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = TopZynWhite),
                border = androidx.compose.foundation.BorderStroke(1.dp, TopZynCardBorder)
            ) {
                Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Statut en temps réel", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TopZynDarkBlack)

                    OrderStepRow(step = "1", title = "Paiement validé", description = "Montant de ${"%.2f".format(order.priceGds)} GDS reçu", isDone = true)
                    OrderStepRow(step = "2", title = "Vérification de l'ID Joueur", description = "Compte joueur valide : ${order.playerId}", isDone = true)
                    OrderStepRow(step = "3", title = "Livraison sur le serveur", description = "Recharge ${order.packageTitle} créditée", isDone = order.status == "REUSSIE")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Order Details Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = TopZynWhite),
                border = androidx.compose.foundation.BorderStroke(1.dp, TopZynCardBorder)
            ) {
                Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Détails du récapitulatif", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TopZynDarkBlack)

                    DetailItemRow("Jeu", order.gameName)
                    HorizontalDivider(color = TopZynCardBorder.copy(alpha = 0.5f))
                    DetailItemRow("Pack commandé", order.packageTitle)
                    HorizontalDivider(color = TopZynCardBorder.copy(alpha = 0.5f))
                    DetailItemRow("Identifiant Joueur (UID)", order.playerId)
                    if (order.zoneId != null) {
                        HorizontalDivider(color = TopZynCardBorder.copy(alpha = 0.5f))
                        DetailItemRow("Zone ID", order.zoneId)
                    }
                    HorizontalDivider(color = TopZynCardBorder.copy(alpha = 0.5f))
                    DetailItemRow("Mode de paiement", order.paymentMethod)
                    HorizontalDivider(color = TopZynCardBorder.copy(alpha = 0.5f))
                    DetailItemRow("Montant total", "${"%.2f".format(order.priceGds)} GDS")
                    HorizontalDivider(color = TopZynCardBorder.copy(alpha = 0.5f))
                    DetailItemRow("Date", dateFormat.format(Date(order.timestamp)))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onBackToHome,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = TopZynDarkBlack,
                    contentColor = TopZynWhite
                )
            ) {
                Text("Retour à l'Accueil", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedButton(
                onClick = { onContactSupport(order.orderNumber) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(14.dp)
            ) {
                Icon(Icons.Default.HelpOutline, contentDescription = null, modifier = Modifier.size(16.dp), tint = TopZynOrange)
                Spacer(modifier = Modifier.width(6.dp))
                Text("Un problème ? Contacter le support", color = TopZynOrange, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun OrderStepRow(step: String, title: String, description: String, isDone: Boolean) {
    Row(verticalAlignment = Alignment.Top) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(if (isDone) TopZynGreen else Color(0xFFE4E4E7)),
            contentAlignment = Alignment.Center
        ) {
            if (isDone) {
                Icon(Icons.Default.Check, contentDescription = null, tint = TopZynWhite, modifier = Modifier.size(14.dp))
            } else {
                Text(step, fontSize = 11.sp, color = TopZynTextMuted)
            }
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TopZynDarkBlack)
            Text(description, fontSize = 11.5.sp, color = TopZynTextSecondary)
        }
    }
}

@Composable
fun DetailItemRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontSize = 12.5.sp, color = TopZynTextMuted)
        Text(value, fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = TopZynTextPrimary)
    }
}
