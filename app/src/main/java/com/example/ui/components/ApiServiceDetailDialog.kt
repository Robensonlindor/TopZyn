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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
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
fun ApiServiceDetailDialog(
    service: ApiServiceModel,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

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
                .testTag("api_service_detail_dialog")
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
                            imageVector = Icons.Default.Code,
                            contentDescription = null,
                            tint = TopZynOrange,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = service.name,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TopZynTextPrimary
                        )
                        Text(
                            text = "Portail Développeur TopZyn",
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

            Spacer(modifier = Modifier.height(16.dp))

            // Status Badge Card
            val isAvailable = service.status == "Disponible"
            val badgeBg = if (isAvailable) TopZynGreenLight else TopZynOrangeContainer
            val badgeText = if (isAvailable) TopZynGreen else TopZynOrange

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(badgeBg)
                    .padding(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Statut actuel : ${service.status}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = badgeText
                        )
                        Text(
                            text = if (isAvailable)
                                "L'environnement API est opérationnel."
                            else
                                "Ce service est en cours de déploiement et sera disponible prochainement.",
                            fontSize = 12.sp,
                            color = TopZynTextSecondary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Description
            Text(
                text = "Description du service",
                fontSize = 13.5.sp,
                fontWeight = FontWeight.Bold,
                color = TopZynDarkBlack
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = service.description,
                fontSize = 13.sp,
                color = TopZynTextSecondary,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (isAvailable) {
                // Endpoint Spec
                Text(
                    text = "Endpoint d'intégration",
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = TopZynDarkBlack
                )
                Spacer(modifier = Modifier.height(6.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF1E1E24))
                        .padding(12.dp)
                ) {
                    Text(
                        text = "POST https://api.topzyn.com/v1/orders/recharge",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 12.sp,
                        color = Color(0xFFE4E4E7)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Sandbox Key
                Text(
                    text = "Clé d'API Sandbox (Test)",
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = TopZynDarkBlack
                )
                Spacer(modifier = Modifier.height(6.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFF4F4F5))
                        .border(1.dp, TopZynCardBorder, RoundedCornerShape(10.dp))
                        .padding(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "tz_test_9921_84821a8c90",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 12.sp,
                            color = TopZynDarkBlack
                        )
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copier",
                            tint = TopZynOrange,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFF8F9FA))
                        .border(1.dp, TopZynCardBorder, RoundedCornerShape(10.dp))
                        .padding(14.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = TopZynTextMuted,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Aucune intégration active pour le moment. Vous recevrez une notification lors du lancement officiel.",
                            fontSize = 12.5.sp,
                            color = TopZynTextSecondary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            Button(
                onClick = onDismiss,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = TopZynDarkBlack,
                    contentColor = TopZynWhite
                )
            ) {
                Text(
                    text = "Fermer",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
