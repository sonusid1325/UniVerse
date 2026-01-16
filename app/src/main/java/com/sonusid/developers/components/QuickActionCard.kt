package com.sonusid.developers.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sonusid.developers.modals.ActionIcon
import com.sonusid.developers.modals.QuickAction


@Composable
fun QuickActionCard(action: QuickAction) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(80.dp)
    ) {
        Surface(
            onClick = { action.onClick() },
            modifier = Modifier.size(72.dp),
            shape = RoundedCornerShape(20.dp),
            color = action.containerColor,
            tonalElevation = 2.dp
        ) {
            Box(contentAlignment = Alignment.Center) {
                when (val icon = action.icon) {
                    is ActionIcon.Vector -> Icon(
                        imageVector = icon.imageVector,
                        contentDescription = action.title,
                        tint = action.color,
                        modifier = Modifier.size(28.dp)
                    )
                    is ActionIcon.Drawable -> Icon(
                        painter = painterResource(id = icon.resId),
                        contentDescription = action.title,
                        tint = action.color,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            action.title,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}