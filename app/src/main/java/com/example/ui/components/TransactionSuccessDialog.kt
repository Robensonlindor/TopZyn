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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import com.example.data.model.TransactionRecord
import com.example.ui.theme.TopZynCardBorder
import com.example.ui.theme.TopZynDarkBlack
import com.example.ui.theme.TopZynGreen
import com.example.ui.theme.TopZynGreenLight
import com.example.ui.theme.TopZynOrange
import com.example.ui.theme.TopZynTextMuted
import com.example.ui.theme.TopZynTextPrimary
import com.example.ui.theme.TopZynTextSecondary
import com.example.ui.theme.TopZynWhite
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionSuccessDialog(
    transaction: TransactionRecord,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val dateFormat = SimpleDateFormat("dd MMMM yyyy à HH:mm", Locale.FRANCE)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = TopZynWhite,
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 20.dp)
                .testTag("transaction_success_sheet"),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Close Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF4F4F5))
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Fermer",
                        tint = TopZynDarkBlack,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            // Big Green Checkmark
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(TopZynGreenLight)
                    .border(2.dp, TopZynGreen.copy(alpha = 0.3f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = TopZynGreen,
                    modifier = Modifier.size(38.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Opération Réussie !",
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                color = TopZynDarkBlack
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = transaction.title,
                fontSize = 14.sp,
                color = TopZynTextSecondary
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Amount in GDS
            Text(
                text = if (transaction.amountGds < 0)
                    "-${"%.2f".format(-transaction.amountGds)} GDS"
                else
                    "+${"%.2f".format(transaction.amountGds)} GDS",
                fontSize = 26.sp,
                fontWeight = FontWeight.Black,
                color = if (transaction.amountGds < 0) TopZynDarkBlack else TopZynGreen
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Receipt Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFF8F9FA))
                    .border(1.dp, TopZynCardBorder, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    ReceiptRow(label = "Référence", value = transaction.referenceCode, isMonospace = true)
                    HorizontalDivider(color = TopZynCardBorder.copy(alpha = 0.6f))
                    ReceiptRow(label = "Date & Heure", value = dateFormat.format(Date(transaction.timestamp)))
                    HorizontalDivider(color = TopZynCardBorder.copy(alpha = 0.6f))
                    ReceiptRow(label = "Statut", value = transaction.status, isSuccess = true)
                    HorizontalDivider(color = TopZynCardBorder.copy(alpha = 0.6f))
                    ReceiptRow(label = "Détails", value = transaction.details)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onDismiss,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("dismiss_success_button"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = TopZynOrange,
                    contentColor = TopZynWhite
                )
            ) {
                Text(
                    text = "Terminer",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun ReceiptRow(
    label: String,
    value: String,
    isMonospace: Boolean = false,
    isSuccess: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 12.5.sp,
            color = TopZynTextMuted
        )
        Text(
            text = value,
            fontSize = 12.5.sp,
            fontWeight = if (isSuccess) FontWeight.Bold else FontWeight.Medium,
            fontFamily = if (isMonospace) FontFamily.Monospace else FontFamily.Default,
            color = if (isSuccess) TopZynGreen else TopZynTextPrimary
        )
    }
}
