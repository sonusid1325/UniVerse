package com.sonusid.developers.modals

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

sealed class ActionIcon {
    data class Vector(val imageVector: ImageVector) : ActionIcon()
    data class Drawable(val resId: Int) : ActionIcon()
}

data class QuickAction(
    val title: String,
    val icon: ActionIcon,
    val color: Color,
    val containerColor: Color,
    val onClick: () -> Unit = {}
)
