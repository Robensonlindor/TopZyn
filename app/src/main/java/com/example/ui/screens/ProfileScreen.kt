package com.example.ui.screens

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
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserAccount
import com.example.ui.theme.TopZynBackground
import com.example.ui.theme.TopZynCardBorder
import com.example.ui.theme.TopZynDarkBlack
import com.example.ui.theme.TopZynGreen
import com.example.ui.theme.TopZynOrange
import com.example.ui.theme.TopZynOrangeContainer
import com.example.ui.theme.TopZynTextMuted
import com.example.ui.theme.TopZynTextPrimary
import com.example.ui.theme.TopZynTextSecondary
import com.example.ui.theme.TopZynWhite

@Composable
fun ProfileScreen(
    user: UserAccount?,
    onNavigateDetailedProfile: () -> Unit,
    onNavigateEditProfile: () -> Unit,
    onNavigateLanguage: () -> Unit,
    onNavigateSecurity: () -> Unit,
    onNavigateSessions: () -> Unit,
    onNavigateNotificationPrefs: () -> Unit,
    onNavigateHelpFaq: () -> Unit,
    onNavigateContactSupport: () -> Unit,
    onNavigateTerms: () -> Unit,
    onNavigatePrivacy: () -> Unit,
    onOpenAdminPanel: () -> Unit,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(TopZynBackground)
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp)
            .testTag("profile_screen")
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Profile Avatar & Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onNavigateDetailedProfile),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = TopZynWhite),
            border = androidx.compose.foundation.BorderStroke(1.dp, TopZynCardBorder)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(76.dp)
                        .clip(CircleShape)
                        .background(TopZynOrange.copy(alpha = 0.12f))
                        .border(2.dp, TopZynOrange, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Avatar",
                        tint = TopZynOrange,
                        modifier = Modifier.size(42.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = user?.fullName ?: "Client TopZyn",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TopZynTextPrimary
                )

                Text(
                    text = user?.email ?: "client@topzyn.com",
                    fontSize = 13.sp,
                    color = TopZynTextSecondary
                )

                Text(
                    text = user?.phone ?: "+509 3412 8890",
                    fontSize = 12.sp,
                    color = TopZynTextMuted
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = onNavigateEditProfile,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = TopZynOrangeContainer,
                        contentColor = TopZynOrange
                    )
                ) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(15.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Modifier mes informations", fontSize = 12.5.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Section: Paramètres & Sécurité
        Text("Paramètres & Sécurité", fontSize = 14.5.sp, fontWeight = FontWeight.Bold, color = TopZynDarkBlack)
        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = TopZynWhite),
            border = androidx.compose.foundation.BorderStroke(1.dp, TopZynCardBorder)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                ProfileMenuRow("Sécurité du compte & Code PIN", Icons.Default.Security, onNavigateSecurity)
                HorizontalDivider(color = TopZynCardBorder)
                ProfileMenuRow("Langue de l'application (${user?.language ?: "Français"})", Icons.Default.Language, onNavigateLanguage)
                HorizontalDivider(color = TopZynCardBorder)
                ProfileMenuRow("Sessions & Appareils connectés", Icons.Default.Devices, onNavigateSessions)
                HorizontalDivider(color = TopZynCardBorder)
                ProfileMenuRow("Préférences de notifications", Icons.Default.Notifications, onNavigateNotificationPrefs)
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Section: Aide & Légal
        Text("Support & Informations", fontSize = 14.5.sp, fontWeight = FontWeight.Bold, color = TopZynDarkBlack)
        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = TopZynWhite),
            border = androidx.compose.foundation.BorderStroke(1.dp, TopZynCardBorder)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                ProfileMenuRow("Aide & Questions Fréquentes (FAQ)", Icons.Default.HelpOutline, onNavigateHelpFaq)
                HorizontalDivider(color = TopZynCardBorder)
                ProfileMenuRow("Service Client & WhatsApp", Icons.Default.SupportAgent, onNavigateContactSupport)
                HorizontalDivider(color = TopZynCardBorder)
                ProfileMenuRow("Conditions Générales d'Utilisation", Icons.Default.Policy, onNavigateTerms)
                HorizontalDivider(color = TopZynCardBorder)
                ProfileMenuRow("Politique de Confidentialité", Icons.Default.Policy, onNavigatePrivacy)
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Section: Administration
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onOpenAdminPanel),
            shape = RoundedCornerShape(16.dp),
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
                    Icon(Icons.Default.AdminPanelSettings, contentDescription = null, tint = TopZynOrange)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("Panneau d'Administration", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text("Ajout de jeux, prix, gestion API", fontSize = 11.5.sp, color = TopZynTextSecondary)
                    }
                }
                Icon(Icons.AutoMirrored.Filled.ArrowForwardIos, contentDescription = null, modifier = Modifier.size(14.dp), tint = TopZynTextMuted)
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Logout Button
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onLogoutClick),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF2F2)),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFCA5A5))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Logout, contentDescription = null, tint = Color(0xFFDC2626))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("Se déconnecter", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFFDC2626))
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))
    }
}

@Composable
fun ProfileMenuRow(title: String, icon: ImageVector, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = TopZynOrange, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Text(title, fontWeight = FontWeight.Medium, fontSize = 13.5.sp, color = TopZynTextPrimary)
        }
        Icon(Icons.AutoMirrored.Filled.ArrowForwardIos, contentDescription = null, modifier = Modifier.size(13.dp), tint = TopZynTextMuted)
    }
}
