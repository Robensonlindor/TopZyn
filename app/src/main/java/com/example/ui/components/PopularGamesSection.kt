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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Gamepad
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.GameItem
import com.example.ui.theme.TopZynCardBorder
import com.example.ui.theme.TopZynDarkBlack
import com.example.ui.theme.TopZynOrange
import com.example.ui.theme.TopZynTextPrimary
import com.example.ui.theme.TopZynTextSecondary
import com.example.ui.theme.TopZynWhite

@Composable
fun PopularGamesSection(
    games: List<GameItem>,
    onGameClick: (GameItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        // Title: "Jeux populaires" + subtle decorative line on the same line
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Jeux populaires",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TopZynDarkBlack
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Subtle decorative line on the same line
            HorizontalDivider(
                modifier = Modifier
                    .weight(1f)
                    .height(1.dp),
                color = TopZynCardBorder
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // 2-Column Grid of Games
        val pairs = games.chunked(2)
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            pairs.forEach { pair ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    pair.forEach { game ->
                        Box(modifier = Modifier.weight(1f)) {
                            PopularGameCard(
                                game = game,
                                onClick = { onGameClick(game) }
                            )
                        }
                    }
                    if (pair.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
fun PopularGameCard(
    game: GameItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Resolve drawable resource
    val drawableRes = when (game.imageDrawableName) {
        "img_free_fire" -> R.drawable.img_free_fire
        "img_pubg_mobile" -> R.drawable.img_pubg_mobile
        "img_mobile_legends" -> R.drawable.img_mobile_legends
        "img_cod_mobile" -> R.drawable.img_cod_mobile
        else -> R.drawable.ic_topzyn_logo
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .clickable(onClick = onClick)
            .testTag("game_card_${game.id}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = TopZynWhite
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, TopZynCardBorder, RoundedCornerShape(18.dp))
        ) {
            // Upper part: Large real high quality game image covering top without distortion
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.25f)
                    .clip(RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp))
                    .background(Color(0xFF18181B))
            ) {
                Image(
                    painter = painterResource(id = drawableRes),
                    contentDescription = game.name,
                    modifier = Modifier.matchParentSize(),
                    contentScale = ContentScale.Crop
                )

                // Category pill tag
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(TopZynDarkBlack.copy(alpha = 0.75f))
                        .padding(horizontal = 7.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = game.currencyName,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = TopZynWhite
                    )
                }
            }

            // Lower part: Game name + Price + Circular Arrow Button
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 10.dp)
            ) {
                // Game Name
                Text(
                    text = game.name,
                    fontSize = 14.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = TopZynTextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Bottom row: Starting price + small circular arrow button on right
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "À partir de",
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Normal,
                            color = TopZynTextSecondary
                        )
                        Text(
                            text = "${"%.0f".format(game.minPriceGds)} GDS",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = TopZynOrange
                        )
                    }

                    // Small circular button with arrow on right
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(TopZynOrange)
                            .clickable(onClick = onClick)
                            .testTag("game_action_${game.id}"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Recharger ${game.name}",
                            tint = TopZynWhite,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}
