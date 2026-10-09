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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.IntegrationInstructions
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Webhook
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
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

@Composable
fun DeveloperApiSection(
    apiServices: List<ApiServiceModel>,
    onServiceClick: (ApiServiceModel) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        // Section Title: API & DÉVELOPPEURS
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 6.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(TopZynOrange.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Code,
                    contentDescription = null,
                    tint = TopZynOrange,
                    modifier = Modifier.size(14.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "API & DÉVELOPPEURS",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = TopZynDarkBlack,
                letterSpacing = 1.1.sp
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Display cards in pairs of two
        val pairs = apiServices.chunked(2)
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            pairs.forEach { pair ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    pair.forEach { service ->
                        Box(modifier = Modifier.weight(1f)) {
                            ApiServiceCard(
                                service = service,
                                onClick = { onServiceClick(service) }
                            )
                        }
                    }
                    // If odd number in row, fill remaining space cleanly
                    if (pair.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
fun ApiServiceCard(
    service: ApiServiceModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .testTag("api_card_${service.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = TopZynWhite
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, TopZynCardBorder, RoundedCornerShape(16.dp))
                .padding(14.dp)
        ) {
            // Icon + Status Badge Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Service Icon
                val iconVector = when (service.iconType) {
                    "moncash_api" -> Icons.Default.PhoneAndroid
                    "natcash_api" -> Icons.Default.PhoneAndroid
                    "webhooks" -> Icons.Default.Webhook
                    else -> Icons.Default.IntegrationInstructions
                }
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(TopZynOrange.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = iconVector,
                        contentDescription = service.name,
                        tint = TopZynOrange,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Status Badge
                val (badgeBg, badgeText) = when (service.status) {
                    "Disponible" -> Pair(TopZynGreenLight, TopZynGreen)
                    "Bientôt" -> Pair(TopZynOrangeContainer, TopZynOrange)
                    else -> Pair(Color(0xFFF4F4F5), TopZynTextMuted)
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(badgeBg)
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = service.status,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = badgeText
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Service Name
            Text(
                text = service.name,
                fontSize = 14.5.sp,
                fontWeight = FontWeight.Bold,
                color = TopZynTextPrimary
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Short Description
            Text(
                text = service.description,
                fontSize = 11.5.sp,
                fontWeight = FontWeight.Normal,
                color = TopZynTextSecondary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 15.sp
            )
        }
    }
}
