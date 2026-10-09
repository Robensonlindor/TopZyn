package com.example.ui.screens.system

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.LockClock
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.TopZynSubPageHeader
import com.example.ui.theme.TopZynBackground
import com.example.ui.theme.TopZynDarkBlack
import com.example.ui.theme.TopZynOrange
import com.example.ui.theme.TopZynTextMuted
import com.example.ui.theme.TopZynTextPrimary
import com.example.ui.theme.TopZynTextSecondary
import com.example.ui.theme.TopZynWhite

// 54. Écran de chargement
@Composable
fun FullLoadingScreen(
    message: String = "Chargement des services TopZyn...",
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(TopZynBackground),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(
                color = TopZynOrange,
                modifier = Modifier.size(48.dp),
                strokeWidth = 4.dp
            )
            Spacer(modifier = Modifier.height(18.dp))
            Text(
                text = message,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = TopZynTextSecondary
            )
        }
    }
}

// 55. Écran vide (Empty State)
@Composable
fun FullEmptyStateScreen(
    title: String = "Aucun contenu disponible",
    description: String = "Les éléments s'afficheront ici dès qu'ils seront ajoutés.",
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(TopZynBackground)
    ) {
        TopZynSubPageHeader(title = title, onBackClick = onBackClick)

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF4F4F5)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Inbox, contentDescription = null, tint = TopZynTextMuted, modifier = Modifier.size(32.dp))
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = TopZynDarkBlack)
                Spacer(modifier = Modifier.height(6.dp))
                Text(description, fontSize = 13.sp, color = TopZynTextSecondary, textAlign = TextAlign.Center)
            }
        }
    }
}

// 56. Écran d’erreur de connexion
@Composable
fun FullConnectionErrorScreen(
    onRetry: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(TopZynBackground)
    ) {
        TopZynSubPageHeader(title = "Connexion réseau", onBackClick = onBackClick)

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFEF2F2)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.WifiOff, contentDescription = null, tint = Color(0xFFEF4444), modifier = Modifier.size(36.dp))
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text("Connexion Internet Interrompue", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = TopZynDarkBlack)
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    "Impossible de joindre les serveurs TopZyn. Vérifiez votre connexion Wi-Fi ou mobile et réessayez.",
                    fontSize = 13.sp,
                    color = TopZynTextSecondary,
                    textAlign = TextAlign.Center,
                    lineHeight = 18.sp
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = onRetry,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = TopZynOrange, contentColor = TopZynWhite)
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Réessayer la connexion", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// 57. Écran de session expirée
@Composable
fun FullSessionExpiredScreen(
    onReconnect: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(TopZynBackground)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFFF7ED)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.LockClock, contentDescription = null, tint = TopZynOrange, modifier = Modifier.size(36.dp))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text("Session Expirée", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = TopZynDarkBlack)
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                "Pour votre sécurité, votre session s'est terminée automatiquement. Veuillez vous reconnecter.",
                fontSize = 13.5.sp,
                color = TopZynTextSecondary,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = onReconnect,
                modifier = Modifier.fillMaxWidth(0.7f).height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = TopZynOrange)
            ) {
                Text("Se reconnecter", fontWeight = FontWeight.Bold)
            }
        }
    }
}

// 58. Écran de maintenance
@Composable
fun FullMaintenanceScreen(
    onCheckStatus: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(TopZynDarkBlack)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF1F1F24)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Build, contentDescription = null, tint = TopZynOrange, modifier = Modifier.size(40.dp))
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text("Maintenance Planifiée", fontWeight = FontWeight.Black, fontSize = 22.sp, color = TopZynWhite)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "La plateforme TopZyn fait peau neuve pour améliorer la vitesse de vos recharges. Nous serons de retour très rapidement.",
                fontSize = 13.5.sp,
                color = Color(0xFFA1A1AA),
                textAlign = TextAlign.Center,
                lineHeight = 19.sp
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = onCheckStatus,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = TopZynOrange)
            ) {
                Text("Vérifier le statut du service", fontWeight = FontWeight.Bold)
            }
        }
    }
}
