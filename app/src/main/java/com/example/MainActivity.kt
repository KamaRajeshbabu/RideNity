package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.TrustLayerViewModel
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                RideNityApp()
            }
        }
    }
}

@Composable
fun RideNityApp() {
    var selectedTab by remember { mutableStateOf(0) }
    
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "RideNity",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "Trusted mobility, every day.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Surface(
                    shape = RoundedCornerShape(percent = 50),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(percent = 50))
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "TRUST 98.4",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        },
        bottomBar = {
            NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.DirectionsCar, contentDescription = "Rides") },
                    label = { Text("Rides") },
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Shield, contentDescription = "Trust") },
                    label = { Text("TrustLayer") },
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.PhotoCamera, contentDescription = "Avatars") },
                    label = { Text("Avatars") },
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 }
                )
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            when (selectedTab) {
                0 -> RidesScreen()
                1 -> TrustLayerScreen()
                2 -> AvatarGenScreen()
            }
        }
    }
}

@Composable
fun RidesScreen() {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
        item {
            Text("RideNity CommuNity", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(50),
                            color = MaterialTheme.colorScheme.primaryContainer,
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                Icons.Filled.Business, 
                                contentDescription = null, 
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Office Commute", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Driver: Jane Doe (Trust Score: 99)", style = MaterialTheme.typography.bodySmall)
                    Text("Guaranteed pickup at 8:00 AM", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            Text("RideNity Now", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }
        items(3) {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.ElectricCar, contentDescription = null, modifier = Modifier.size(40.dp), tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("EV Premium", fontWeight = FontWeight.Bold)
                        Text("3 mins away", style = MaterialTheme.typography.bodySmall)
                    }
                    Button(
                        shape = RoundedCornerShape(12.dp),
                        onClick = { }
                    ) {
                        Text("Book")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrustLayerScreen(viewModel: TrustLayerViewModel = viewModel()) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
         item {
             Text("Trust Profile Dashboard", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
             Spacer(modifier = Modifier.height(16.dp))
             
             Card(
                 shape = RoundedCornerShape(24.dp),
                 colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                 border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                 modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
             ) {
                 Column(modifier = Modifier.padding(20.dp)) {
                     Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                         Text("Your Trust Score", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                         Surface(
                             shape = RoundedCornerShape(percent = 50),
                             color = MaterialTheme.colorScheme.primaryContainer
                         ) {
                             Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)) {
                                 Box(modifier = Modifier.size(6.dp).background(MaterialTheme.colorScheme.primary, RoundedCornerShape(percent = 50)))
                                 Spacer(modifier = Modifier.width(6.dp))
                                 Text("Verified Driver", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                             }
                         }
                     }
                     Spacer(modifier = Modifier.height(8.dp))
                     Row(verticalAlignment = Alignment.Bottom) {
                         Text("98.4", style = MaterialTheme.typography.displayMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                         Text("/100", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(bottom = 6.dp))
                     }
                     Spacer(modifier = Modifier.height(16.dp))
                     LinearProgressIndicator(
                         progress = { 0.984f },
                         modifier = Modifier.fillMaxWidth().height(8.dp),
                         color = MaterialTheme.colorScheme.primary,
                         trackColor = MaterialTheme.colorScheme.surfaceVariant,
                         strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
                     )
                     Spacer(modifier = Modifier.height(24.dp))
                     Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                         Column {
                             Text("TOTAL TRIPS", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                             Text("1,240", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                         }
                         Column {
                             Text("INCIDENTS", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                             Text("0", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                         }
                         Column {
                             Text("PEER RATING", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                             Text("4.9/5", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                         }
                     }
                 }
             }

             Text("Shared Accountability Engine", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
             Text("Evaluate incidents with Gemini High Thinking", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
             Spacer(modifier = Modifier.height(16.dp))
             
             Card(
                 shape = RoundedCornerShape(24.dp),
                 colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                 modifier = Modifier.fillMaxWidth()
             ) {
                 Column(modifier = Modifier.padding(20.dp)) {
                     Row(verticalAlignment = Alignment.CenterVertically) {
                         Icon(Icons.Filled.Warning, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                         Spacer(modifier = Modifier.width(8.dp))
                         Text("Incident Alert: Route Deviation & Overspeeding", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
                     }
                     Spacer(modifier = Modifier.height(12.dp))
                     Text("Speed Log: 85mph in 60mph zone.", style = MaterialTheme.typography.bodyMedium)
                     Text("Driver Notes: Passenger requested speeding to catch flight.", style = MaterialTheme.typography.bodyMedium)
                 }
             }
             
             Spacer(modifier = Modifier.height(16.dp))
             
             Button(
                 shape = RoundedCornerShape(16.dp),
                 onClick = { viewModel.evaluateIncident("Passenger requested speeding to catch flight.", "85mph in 60mph zone.") },
                 modifier = Modifier.fillMaxWidth()
                     .height(56.dp),
                 enabled = !viewModel.isAnalyzing
             ) {
                 if (viewModel.isAnalyzing) {
                     CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                     Spacer(modifier = Modifier.width(8.dp))
                     Text("Analyzing with High Thinking...")
                 } else {
                     Text("Evaluate Liability")
                 }
             }
             
             Spacer(modifier = Modifier.height(16.dp))
             
             if (viewModel.trustScoreAnalysis != null) {
                 Card(
                     shape = RoundedCornerShape(24.dp),
                     modifier = Modifier.fillMaxWidth(), 
                     colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                 ) {
                     Column(modifier = Modifier.padding(20.dp)) {
                         Text("Evaluation Result", fontWeight = FontWeight.Bold)
                         Spacer(modifier = Modifier.height(8.dp))
                         Text(viewModel.trustScoreAnalysis!!, style = MaterialTheme.typography.bodyMedium)
                     }
                 }
             }
         }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AvatarGenScreen(viewModel: TrustLayerViewModel = viewModel()) {
    var prompt by remember { mutableStateOf("A trustworthy driver mascot, 3d render, smiling") }
    var selectedSize by remember { mutableStateOf("1K") }

    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
        Text("Generate Driver/Vehicle Asset", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedTextField(
            value = prompt,
            onValueChange = { prompt = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Prompt") },
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline
            )
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        Text("Select Size:", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
        Row {
            listOf("1K", "2K", "4K").forEach { size ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = size == selectedSize,
                        onClick = { selectedSize = size },
                        colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.primary)
                    )
                    Text(size, style = MaterialTheme.typography.bodySmall)
                    Spacer(modifier = Modifier.width(16.dp))
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            shape = RoundedCornerShape(16.dp),
            onClick = { viewModel.generateVehicleAvatar(prompt, selectedSize) },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            enabled = !viewModel.isGeneratingImage
        ) {
            if (viewModel.isGeneratingImage) {
                 CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
            } else {
                 Text("Generate Asset")
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        viewModel.generatedImage?.let { bmp ->
            Card(shape = RoundedCornerShape(24.dp), modifier = Modifier.fillMaxWidth()) {
                Image(
                    bitmap = bmp.asImageBitmap(),
                    contentDescription = "Generated Image",
                    modifier = Modifier.fillMaxWidth().aspectRatio(1f)
                )
            }
        }
    }
}
