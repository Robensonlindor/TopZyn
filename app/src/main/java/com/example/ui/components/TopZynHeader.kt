package com.example.ui.components

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.UserAccount
import com.example.ui.theme.TopZynCardBorder
import com.example.ui.theme.TopZynDarkBlack
import com.example.ui.theme.TopZynOrange
import com.example.ui.theme.TopZynTextMuted
import com.example.ui.theme.TopZynTextPrimary
import com.example.ui.theme.TopZynTextSecondary
import com.example.ui.theme.TopZynWhite

@Composable
fun TopZynHeader(
    user: UserAccount?,
    unreadNotificationsCount: Int,
    onNotificationsClick: () -> Unit,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(TopZynWhite)
            .padding(horizontal = 20.dp, vertical = 14.dp)
    ) {
        // Top row: Brand Logo + Action icons (Notifications & Profile Avatar)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // TopZyn Official Brand Logo & Name
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { /* Brand tap */ }
                    .padding(vertical = 4.dp)
            ) {
                // Circular/Rounded Logo Image
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(TopZynDarkBlack)
                        .border(1.5.dp, TopZynOrange.copy(alpha = 0.6f), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_topzyn_logo),
                        contentDescription = "Logo TopZyn",
                        modifier = Modifier.size(38.dp),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Top",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            color = TopZynDarkBlack,
                            letterSpacing = (-0.5).sp
                        )
                        Text(
                            text = "Zyn",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            color = TopZynOrange,
                            letterSpacing = (-0.5).sp
                        )
                    }
                    Text(
                        text = "RECHARGE & PAIEMENT",
                        fontSize = 8.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = TopZynTextMuted,
                        letterSpacing = 1.2.sp
                    )
                }
            }

            // Right side: Notifications + Profile Avatar
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Notifications icon button
                IconButton(
                    onClick = onNotificationsClick,
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(TopZynCardBorder.copy(alpha = 0.4f))
                        .testTag("notification_button")
                ) {
                    BadgedBox(
                        badge = {
                            if (unreadNotificationsCount > 0) {
                                Badge(
                                    containerColor = TopZynOrange,
                                    contentColor = TopZynWhite,
                                    modifier = Modifier.offset(x = 2.dp, y = (-2).dp)
                                ) {
                                    Text(
                                        text = if (unreadNotificationsCount > 9) "9+" else unreadNotificationsCount.toString(),
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notifications",
                            tint = TopZynDarkBlack,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }

                // Profile Avatar
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(TopZynOrange.copy(alpha = 0.12f))
                        .border(1.5.dp, TopZynOrange, CircleShape)
                        .clickable(onClick = onProfileClick)
                        .testTag("profile_avatar_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profil utilisateur",
                        tint = TopZynOrange,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Greeting Section: Bonjour, [Nom] & Que souhaitez-vous faire aujourd'hui ?
        val userName = user?.fullName?.ifBlank { "Client TopZyn" } ?: "Client TopZyn"
        Text(
            text = "Bonjour, $userName",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = TopZynTextPrimary
        )

        Spacer(modifier = Modifier.height(3.dp))

        Text(
            text = "Que souhaitez-vous faire aujourd’hui ?",
            fontSize = 13.5.sp,
            fontWeight = FontWeight.Normal,
            color = TopZynTextSecondary
        )
    }
}
