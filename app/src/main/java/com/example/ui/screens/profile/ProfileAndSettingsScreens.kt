package com.example.ui.screens.profile

import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material.icons.filled.VerifiedUser
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
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.FaqItem
import com.example.data.model.UserAccount
import com.example.data.model.UserSessionItem
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

// 40. Écran de profil détaillé
@Composable
fun DetailedProfileScreen(
    user: UserAccount?,
    onNavigateEdit: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(TopZynBackground)
    ) {
        TopZynSubPageHeader(
            title = "Mon Profil",
            subtitle = "Informations du compte",
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(86.dp)
                    .clip(CircleShape)
                    .background(TopZynOrange.copy(alpha = 0.15f))
                    .border(2.dp, TopZynOrange, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Person, contentDescription = null, tint = TopZynOrange, modifier = Modifier.size(48.dp))
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(user?.fullName ?: "Client TopZyn", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TopZynDarkBlack)
            Text("Membre Vérifié TopZyn Haïti", fontSize = 12.sp, color = TopZynGreen, fontWeight = FontWeight.SemiBold)

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = TopZynWhite),
                border = androidx.compose.foundation.BorderStroke(1.dp, TopZynCardBorder)
            ) {
                Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Données personnelles", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    HorizontalDivider(color = TopZynCardBorder)
                    ProfileDataRow("Nom complet", user?.fullName ?: "Jean-Marc Baptiste")
                    ProfileDataRow("Adresse email", user?.email ?: "jmbaptiste@topzyn.com")
                    ProfileDataRow("Téléphone", user?.phone ?: "+509 3412 8890")
                    ProfileDataRow("Devise de référence", "Gourde Haïtienne (GDS)")
                    ProfileDataRow("Rôle", user?.role ?: "UTILISATEUR")
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = onNavigateEdit,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = TopZynOrange, contentColor = TopZynWhite)
            ) {
                Text("Modifier mes coordonnées", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun ProfileDataRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontSize = 12.5.sp, color = TopZynTextMuted)
        Text(value, fontSize = 12.5.sp, fontWeight = FontWeight.Medium, color = TopZynTextPrimary)
    }
}

// 41. Écran de modification des informations
@Composable
fun EditProfileScreen(
    user: UserAccount?,
    onSaveProfile: (name: String, email: String, phone: String) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var name by remember(user) { mutableStateOf(user?.fullName ?: "") }
    var email by remember(user) { mutableStateOf(user?.email ?: "") }
    var phone by remember(user) { mutableStateOf(user?.phone ?: "") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(TopZynBackground)
    ) {
        TopZynSubPageHeader(
            title = "Modifier mon profil",
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nom complet") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = TopZynOrange, unfocusedBorderColor = TopZynCardBorder)
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Adresse email") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = TopZynOrange, unfocusedBorderColor = TopZynCardBorder)
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Numéro de téléphone") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = TopZynOrange, unfocusedBorderColor = TopZynCardBorder)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { onSaveProfile(name.trim(), email.trim(), phone.trim()) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = TopZynOrange, contentColor = TopZynWhite)
            ) {
                Text("Enregistrer les modifications", fontWeight = FontWeight.Bold)
            }
        }
    }
}

// 44. Écran de sélection de la langue
@Composable
fun LanguageSelectionScreen(
    currentLanguage: String,
    onLanguageSelected: (String) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val languages = listOf("Français", "Kreyòl Ayisyen", "English")

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(TopZynBackground)
    ) {
        TopZynSubPageHeader(
            title = "Langue de l'application",
            subtitle = "Choisissez votre langue",
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            languages.forEach { lang ->
                val isSelected = currentLanguage == lang
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onLanguageSelected(lang) },
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = TopZynWhite),
                    border = androidx.compose.foundation.BorderStroke(if (isSelected) 2.dp else 1.dp, if (isSelected) TopZynOrange else TopZynCardBorder)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(lang, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = if (isSelected) TopZynOrange else TopZynDarkBlack)
                        if (isSelected) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = TopZynOrange)
                        }
                    }
                }
            }
        }
    }
}

// 45 & 46. Écran de sécurité du compte & Changement de mot de passe
@Composable
fun SecuritySettingsScreen(
    user: UserAccount?,
    onToggleBiometric: (Boolean) -> Unit,
    onChangePin: (String) -> Unit,
    onNavigateChangePassword: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var biometric by remember(user) { mutableStateOf(user?.biometricEnabled ?: true) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(TopZynBackground)
    ) {
        TopZynSubPageHeader(
            title = "Sécurité du compte",
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = TopZynWhite),
                border = androidx.compose.foundation.BorderStroke(1.dp, TopZynCardBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Fingerprint, contentDescription = null, tint = TopZynOrange)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("Déverrouillage Biométrique", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text("Empreinte / Face Unlock pour confirmer", fontSize = 11.5.sp, color = TopZynTextSecondary)
                            }
                        }
                        Switch(
                            checked = biometric,
                            onCheckedChange = {
                                biometric = it
                                onToggleBiometric(it)
                            },
                            colors = SwitchDefaults.colors(checkedThumbColor = TopZynWhite, checkedTrackColor = TopZynOrange)
                        )
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = TopZynCardBorder)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(onClick = onNavigateChangePassword),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = TopZynOrange)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("Modifier le mot de passe", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text("Dernière modification il y a 3 mois", fontSize = 11.5.sp, color = TopZynTextSecondary)
                            }
                        }
                        Icon(Icons.AutoMirrored.Filled.ArrowForwardIos, contentDescription = null, modifier = Modifier.size(14.dp), tint = TopZynTextMuted)
                    }
                }
            }
        }
    }
}

// 47. Écran de gestion des sessions ou appareils connectés
@Composable
fun SessionManagementScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sessions = listOf(
        UserSessionItem("s1", "Google Pixel / Android 14", "Application Mobile", "Port-au-Prince, Haïti", "190.115.18.2", "Actif maintenant", isCurrent = true),
        UserSessionItem("s2", "Chrome / Windows 11", "Web Desktop", "Pétion-Ville, Haïti", "190.115.18.2", "Hier à 18:42", isCurrent = false)
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(TopZynBackground)
    ) {
        TopZynSubPageHeader(
            title = "Sessions & Appareils",
            subtitle = "Appareils connectés à votre compte",
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            sessions.forEach { s ->
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = TopZynWhite),
                    border = androidx.compose.foundation.BorderStroke(1.dp, TopZynCardBorder)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Devices, contentDescription = null, tint = if (s.isCurrent) TopZynGreen else TopZynTextSecondary)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(s.deviceName, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text("${s.location} • ${s.clientType}", fontSize = 11.5.sp, color = TopZynTextSecondary)
                                Text(s.lastActive, fontSize = 10.5.sp, color = if (s.isCurrent) TopZynGreen else TopZynTextMuted)
                            }
                        }

                        if (s.isCurrent) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(TopZynGreenLight)
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Text("Cet appareil", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TopZynGreen)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            OutlinedButton(
                onClick = { /* Disconnect other devices */ },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Déconnecter tous les autres appareils", color = Color(0xFFDC2626), fontWeight = FontWeight.Bold)
            }
        }
    }
}

// 48. Écran des préférences de notification
@Composable
fun NotificationPreferencesScreen(
    user: UserAccount?,
    onSavePrefs: (recharge: Boolean, promo: Boolean, security: Boolean) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var notifyRecharge by remember(user) { mutableStateOf(user?.notifyRecharge ?: true) }
    var notifyPromo by remember(user) { mutableStateOf(user?.notifyPromo ?: true) }
    var notifySecurity by remember(user) { mutableStateOf(user?.notifySecurity ?: true) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(TopZynBackground)
    ) {
        TopZynSubPageHeader(
            title = "Préférences de notifications",
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = TopZynWhite),
                border = androidx.compose.foundation.BorderStroke(1.dp, TopZynCardBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    NotificationToggleRow("Recharges et commandes", "Notification dès que vos diamants/UC sont livrés", notifyRecharge) {
                        notifyRecharge = it
                        onSavePrefs(notifyRecharge, notifyPromo, notifySecurity)
                    }
                    HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = TopZynCardBorder)
                    NotificationToggleRow("Promotions & Bonus GDS", "Alertes sur les bonus de recharges Free Fire et PUBG", notifyPromo) {
                        notifyPromo = it
                        onSavePrefs(notifyRecharge, notifyPromo, notifySecurity)
                    }
                    HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = TopZynCardBorder)
                    NotificationToggleRow("Alertes de sécurité", "Nouvelles connexions et transferts de fonds", notifySecurity) {
                        notifySecurity = it
                        onSavePrefs(notifyRecharge, notifyPromo, notifySecurity)
                    }
                }
            }
        }
    }
}

@Composable
fun NotificationToggleRow(title: String, subtitle: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 13.5.sp)
            Text(subtitle, fontSize = 11.5.sp, color = TopZynTextSecondary)
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(checkedThumbColor = TopZynWhite, checkedTrackColor = TopZynOrange)
        )
    }
}

// 49. Écran d’aide et de support (FAQ)
@Composable
fun HelpFaqScreen(
    onNavigateContact: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val faqList = listOf(
        FaqItem("f1", "Recharge", "Combien de temps prend la livraison des diamants ?", "La livraison est 100% automatique et prend entre 30 secondes et 2 minutes."),
        FaqItem("f2", "MonCash", "Puis-je recharger avec MonCash sans carte de crédit ?", "Oui ! TopZyn est spécialement conçu pour accepter les paiements MonCash et Natcash en Gourdes."),
        FaqItem("f3", "Wallet", "Mes fonds en Gourdes sont-ils en sécurité ?", "Oui, votre portefeuille TopZyn est protégé et sécurisé par code PIN et biométrie."),
        FaqItem("f4", "Erreur ID", "Que faire si j'ai entré un mauvais ID joueur ?", "Contactez immédiatement notre service client via WhatsApp avec votre numéro de commande.")
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(TopZynBackground)
    ) {
        TopZynSubPageHeader(
            title = "Aide & Questions Fréquentes",
            onBackClick = onBackClick
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = TopZynOrangeContainer)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.SupportAgent, contentDescription = null, tint = TopZynOrange, modifier = Modifier.size(32.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Besoin d'une assistance directe ?", fontWeight = FontWeight.Bold, fontSize = 13.5.sp)
                            Text("Notre équipe est disponible 7j/7 de 8h à 22h", fontSize = 11.5.sp, color = TopZynTextSecondary)
                        }
                        Button(onClick = onNavigateContact, colors = ButtonDefaults.buttonColors(containerColor = TopZynOrange)) {
                            Text("Contact", fontSize = 12.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                Text("Questions Fréquentes (FAQ)", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = TopZynDarkBlack)
                Spacer(modifier = Modifier.height(6.dp))
            }

            items(faqList) { faq ->
                var expanded by remember { mutableStateOf(false) }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { expanded = !expanded },
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
                            Text(faq.question, fontWeight = FontWeight.Bold, fontSize = 13.5.sp, color = TopZynTextPrimary, modifier = Modifier.weight(1f))
                            Text(if (expanded) "▲" else "▼", color = TopZynOrange, fontSize = 12.sp)
                        }
                        if (expanded) {
                            Spacer(modifier = Modifier.height(8.dp))
                            HorizontalDivider(color = TopZynCardBorder.copy(alpha = 0.5f))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(faq.answer, fontSize = 12.5.sp, color = TopZynTextSecondary, lineHeight = 17.sp)
                        }
                    }
                }
            }
        }
    }
}

// 50. Écran de contact du service client
@Composable
fun ContactSupportScreen(
    prefilledOrderNumber: String? = null,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val whatsappMessage = if (prefilledOrderNumber != null)
        "Bonjour TopZyn, j'ai une question concernant ma commande N° $prefilledOrderNumber"
    else
        "Bonjour le support TopZyn, j'ai besoin d'aide."

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(TopZynBackground)
    ) {
        TopZynSubPageHeader(
            title = "Contacter le support",
            subtitle = "Service client TopZyn Haïti",
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = TopZynWhite),
                border = androidx.compose.foundation.BorderStroke(1.dp, TopZynCardBorder)
            ) {
                Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Text("Canaux de communication officiels", fontWeight = FontWeight.Bold, fontSize = 14.5.sp)

                    // WhatsApp
                    ContactChannelRow(
                        title = "WhatsApp Support",
                        detail = "+509 3412 8890 (Réponse rapide)",
                        icon = Icons.Default.PhoneAndroid,
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/50934128890?text=${Uri.encode(whatsappMessage)}"))
                            context.startActivity(intent)
                        }
                    )

                    HorizontalDivider(color = TopZynCardBorder)

                    // Phone Call
                    ContactChannelRow(
                        title = "Appel Téléphonique",
                        detail = "+509 3412 8890 (8h - 22h)",
                        icon = Icons.Default.Phone,
                        onClick = {
                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:+50934128890"))
                            context.startActivity(intent)
                        }
                    )

                    HorizontalDivider(color = TopZynCardBorder)

                    // Email
                    ContactChannelRow(
                        title = "Support par Email",
                        detail = "support@topzyn.com",
                        icon = Icons.Default.Email,
                        onClick = {
                            val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:support@topzyn.com?subject=Demande de support TopZyn"))
                            context.startActivity(intent)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ContactChannelRow(title: String, detail: String, icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(TopZynOrange.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = TopZynOrange, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 13.5.sp)
                Text(detail, fontSize = 11.5.sp, color = TopZynTextSecondary)
            }
        }
        Icon(Icons.AutoMirrored.Filled.ArrowForwardIos, contentDescription = null, modifier = Modifier.size(14.dp), tint = TopZynTextMuted)
    }
}

// 51 & 52. Écran des conditions d’utilisation & Politique de confidentialité
@Composable
fun LegalTextScreen(
    title: String,
    content: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(TopZynBackground)
    ) {
        TopZynSubPageHeader(
            title = title,
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
                Text(
                    text = content,
                    modifier = Modifier.padding(18.dp),
                    fontSize = 13.sp,
                    color = TopZynTextSecondary,
                    lineHeight = 19.sp
                )
            }
        }
    }
}
