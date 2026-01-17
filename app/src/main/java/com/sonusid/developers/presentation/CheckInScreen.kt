package com.sonusid.developers.presentation

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.BarcodeView
import com.journeyapps.barcodescanner.DefaultDecoderFactory
import com.sonusid.developers.viewmodels.EventViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckInScreen(
    viewModel: EventViewModel,
    onBackClick: () -> Unit = {}
) {
    var lastScannedData by remember { mutableStateOf<String?>(null) }
    val primaryColor = MaterialTheme.colorScheme.primary

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("QR Ticket Scanner", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Scanner Viewport
            Box(
                modifier = Modifier
                    .size(280.dp)
                    .clip(RoundedCornerShape(32.dp))
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                // Camera Preview
                AndroidView(
                    factory = { context ->
                        BarcodeView(context).apply {
                            val formats = listOf(com.google.zxing.BarcodeFormat.QR_CODE)
                            decoderFactory = DefaultDecoderFactory(formats)
                            decodeContinuous(object : BarcodeCallback {
                                override fun barcodeResult(result: BarcodeResult?) {
                                    result?.text?.let { data ->
                                        if (data != lastScannedData) {
                                            lastScannedData = data
                                            viewModel.approveRegistration(data)
                                        }
                                    }
                                }
                                override fun possibleResultPoints(resultPoints: List<com.google.zxing.ResultPoint>?) {}
                            })
                            resume()
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )

                // Custom Material UI Overlay
                QRScannerOverlay(primaryColor)
            }

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                "QR Scanning Active",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                lastScannedData?.let { "Success! Scanned: $it" } ?: "Point the camera at the event QR ticket to instantly check in the attendee.",
                style = MaterialTheme.typography.bodyLarge,
                color = if (lastScannedData != null) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                lineHeight = 24.sp
            )

            Spacer(modifier = Modifier.height(64.dp))

            OutlinedButton(
                onClick = onBackClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    "Finish Scanning",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun QRScannerOverlay(lineColor: Color) {
    val infiniteTransition = rememberInfiniteTransition(label = "scannerLine")
    val linePosition by infiniteTransition.animateFloat(
        initialValue = 0.1f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "linePosition"
    )

    Box(modifier = Modifier.fillMaxSize()) {
        // Corner Brackets
        Canvas(modifier = Modifier.fillMaxSize().padding(32.dp)) {
            val strokeWidth = 4.dp.toPx()
            val cornerSize = 40.dp.toPx()
            
            // Top Left
            drawLine(lineColor, Offset(0f, 0f), Offset(cornerSize, 0f), strokeWidth, StrokeCap.Round)
            drawLine(lineColor, Offset(0f, 0f), Offset(0f, cornerSize), strokeWidth, StrokeCap.Round)
            
            // Top Right
            drawLine(lineColor, Offset(size.width, 0f), Offset(size.width - cornerSize, 0f), strokeWidth, StrokeCap.Round)
            drawLine(lineColor, Offset(size.width, 0f), Offset(size.width, cornerSize), strokeWidth, StrokeCap.Round)
            
            // Bottom Left
            drawLine(lineColor, Offset(0f, size.height), Offset(cornerSize, size.height), strokeWidth, StrokeCap.Round)
            drawLine(lineColor, Offset(0f, size.height), Offset(0f, size.height - cornerSize), strokeWidth, StrokeCap.Round)
            
            // Bottom Right
            drawLine(lineColor, Offset(size.width, size.height), Offset(size.width - cornerSize, size.height), strokeWidth, StrokeCap.Round)
            drawLine(lineColor, Offset(size.width, size.height), Offset(size.width, size.height - cornerSize), strokeWidth, StrokeCap.Round)
        }

        // Animated Scanning Line
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(linePosition)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .align(Alignment.BottomCenter)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color.Transparent, lineColor, Color.Transparent),
                        )
                    )
            )
            // Glowing effect for the line
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp)
                    .align(Alignment.BottomCenter)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color.Transparent, lineColor.copy(alpha = 0.3f), Color.Transparent),
                        )
                    )
            )
        }
    }
}
