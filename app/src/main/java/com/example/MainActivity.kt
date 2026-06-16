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
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.clickable
import androidx.compose.foundation.Canvas
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.TrustLayerViewModel
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.Primary

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

sealed class Screen(val route: String, val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    object Rides : Screen("rides", "Ride", Icons.Filled.DirectionsCar)
    object Map : Screen("map", "Trust Map", Icons.Filled.Map)
    object Trust : Screen("trust", "Trust Profile", Icons.Filled.Shield)
    object Avatar : Screen("avatar", "Avatars", Icons.Filled.PhotoCamera)
}

@Composable
fun RideNityApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    
    val items = listOf(
        Screen.Rides,
        Screen.Map,
        Screen.Trust,
        Screen.Avatar
    )

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
                items.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.label) },
                        label = { Text(screen.label) },
                        selected = currentRoute == screen.route,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(navController, startDestination = Screen.Rides.route, modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            composable(Screen.Rides.route) { RidesScreen() }
            composable(Screen.Map.route) { TrustMapScreen() }
            composable(Screen.Trust.route) { TrustLayerScreen() }
            composable(Screen.Avatar.route) { AvatarGenScreen() }
        }
    }
}

@Composable
fun RidesScreen() {
    var selectedRideType by remember { mutableStateOf("Ride Now") }
    var rideState by remember { mutableStateOf("none") }

    LaunchedEffect(rideState) {
        if (rideState == "searching") {
            kotlinx.coroutines.delay(2000)
            rideState = "assigned"
        } else if (rideState == "assigned") {
            kotlinx.coroutines.delay(2500)
            rideState = "in_transit"
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
        // Sub-navigation
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.secondaryContainer,
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        ) {
            Row(modifier = Modifier.padding(4.dp)) {
                listOf("Ride Now", "CommuNity").forEach { type ->
                    val isSelected = selectedRideType == type
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .padding(2.dp)
                            .height(40.dp)
                            .clickable { selectedRideType = type },
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                        contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondaryContainer
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(text = type, fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.labelLarge)
                        }
                    }
                }
            }
        }

        // Map area placeholder
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                // Background visual lines to fake a map
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawLine(
                        color = Primary.copy(alpha = 0.2f),
                        start = androidx.compose.ui.geometry.Offset(100f, 100f),
                        end = androidx.compose.ui.geometry.Offset(400f, 400f),
                        strokeWidth = 4f
                    )
                    drawLine(
                        color = Primary.copy(alpha = 0.2f),
                        start = androidx.compose.ui.geometry.Offset(400f, 100f),
                        end = androidx.compose.ui.geometry.Offset(100f, 600f),
                        strokeWidth = 4f
                    )
                    drawLine(
                        color = Primary.copy(alpha = 0.2f),
                        start = androidx.compose.ui.geometry.Offset(0f, 700f),
                        end = androidx.compose.ui.geometry.Offset(1000f, 700f),
                        strokeWidth = 4f
                    )
                }
                
                // Current Location Pin
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Surface(
                        shape = RoundedCornerShape(percent = 50),
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(48.dp),
                        border = androidx.compose.foundation.BorderStroke(4.dp, MaterialTheme.colorScheme.surface)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Box(modifier = Modifier.size(8.dp).background(Color.White, RoundedCornerShape(percent = 50)))
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surface,
                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                    ) {
                        Text(
                            text = "CURRENT LOCATION",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    }
                }

                // Bottom Panel inside Map
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.surface),
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    androidx.compose.animation.AnimatedContent(
                        targetState = rideState,
                        label = "ride_state_animation",
                        transitionSpec = {
                            (fadeIn(animationSpec = androidx.compose.animation.core.tween(400)) + 
                             slideInVertically(animationSpec = androidx.compose.animation.core.tween(400), initialOffsetY = { height -> height / 3 })) togetherWith 
                            (fadeOut(animationSpec = androidx.compose.animation.core.tween(400)) + 
                             slideOutVertically(animationSpec = androidx.compose.animation.core.tween(400), targetOffsetY = { height -> -height / 3 })) using SizeTransform(clip = false)
                        }
                    ) { state ->
                        when(state) {
                            "none" -> {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
                                    ) {
                                        Text(
                                            "TRUSTLAYER™ PROTECTED",
                                            color = MaterialTheme.colorScheme.primary,
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            letterSpacing = 1.sp
                                        )
                                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                            Box(modifier = Modifier.size(6.dp).background(MaterialTheme.colorScheme.primary, RoundedCornerShape(50)))
                                            Box(modifier = Modifier.size(6.dp).background(MaterialTheme.colorScheme.primary, RoundedCornerShape(50)))
                                            Box(modifier = Modifier.size(6.dp).background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f), RoundedCornerShape(50)))
                                        }
                                    }
                                    
                                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                        Surface(
                                            shape = RoundedCornerShape(12.dp),
                                            color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
                                            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                                            modifier = Modifier.weight(1f).height(40.dp)
                                        ) {
                                            Box(contentAlignment = Alignment.CenterStart, modifier = Modifier.padding(horizontal = 12.dp)) {
                                                Text("To: Central Station...", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                            }
                                        }
                                        Button(
                                            onClick = { rideState = "searching" },
                                            shape = RoundedCornerShape(12.dp),
                                            modifier = Modifier.size(40.dp),
                                            contentPadding = PaddingValues(0.dp)
                                        ) {
                                            Icon(Icons.Filled.ArrowForward, contentDescription = "Go", modifier = Modifier.size(20.dp))
                                        }
                                    }
                                }
                            }
                            "searching" -> {
                                Column(modifier = Modifier.padding(16.dp).fillMaxWidth().clickable { rideState = "assigned" }, horizontalAlignment = Alignment.CenterHorizontally) {
                                    androidx.compose.material3.CircularProgressIndicator(modifier = Modifier.size(32.dp), color = MaterialTheme.colorScheme.primary, strokeWidth = 3.dp)
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text("Finding trusted driver...", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("Scanning TrustLayer for top matches", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                            "assigned" -> {
                                Column(modifier = Modifier.padding(16.dp).fillMaxWidth().clickable { rideState = "in_transit" }) {
                                    Text("Driver Assigned", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Surface(shape = RoundedCornerShape(50), color = MaterialTheme.colorScheme.secondaryContainer, modifier = Modifier.size(40.dp)) {
                                            Icon(Icons.Filled.Person, contentDescription = null, modifier = Modifier.padding(8.dp))
                                        }
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text("Jane Doe", fontWeight = FontWeight.Bold)
                                            Text("Trust Score: 99.2", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
                                        }
                                        Text("2 mins", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                            "in_transit" -> {
                                Column(modifier = Modifier.padding(16.dp).fillMaxWidth().clickable { rideState = "none" }) {
                                    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                                        Text("Heading to destination", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                                        Text("ETA 15m", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                                    }
                                    Spacer(modifier = Modifier.height(12.dp))
                                    androidx.compose.material3.LinearProgressIndicator(progress = { 0.3f }, modifier = Modifier.fillMaxWidth().height(6.dp), color = MaterialTheme.colorScheme.primary, trackColor = MaterialTheme.colorScheme.surfaceVariant, strokeCap = androidx.compose.ui.graphics.StrokeCap.Round)
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Dynamic Content based on selection
        if (selectedRideType == "CommuNity") {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "RECENT TRUST-RIDES", 
                    style = MaterialTheme.typography.labelSmall, 
                    fontWeight = FontWeight.Bold, 
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "View All", 
                    style = MaterialTheme.typography.labelSmall, 
                    fontWeight = FontWeight.Medium, 
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface,
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
                        modifier = Modifier.size(40.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Box(modifier = Modifier.size(20.dp).background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f), RoundedCornerShape(50)), contentAlignment = Alignment.Center) {
                                Box(modifier = Modifier.size(8.dp).background(MaterialTheme.colorScheme.primary, RoundedCornerShape(50)))
                            }
                        }
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Office CommuNity", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface, style = MaterialTheme.typography.bodyMedium)
                        Text("Driver: Aarav S. (Fixed Rate)", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("VERIFIED", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelSmall)
                        Text("9:00 AM", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        } else {
            // Ride Now options
            if (rideState == "none") {
                Text("Available Drivers", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp))
                
                val rideOptions = listOf(
                    Triple("RideNity Auto", "Driver: Sam • 3 mins", "Trust: 95.0"),
                    Triple("RideNity EV", "Driver: Alex • 5 mins", "Trust: 99.1"),
                    Triple("RideNity XL", "Driver: Maria • 8 mins", "Trust: 98.5")
                )
                
                rideOptions.forEach { (type, details, trust) ->
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.surface,
                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp).clickable { rideState = "searching" }
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Filled.DirectionsCar, contentDescription = null, modifier = Modifier.size(32.dp), tint = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(type, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                                Text(details, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Surface(shape = RoundedCornerShape(8.dp), color = MaterialTheme.colorScheme.primaryContainer) {
                                    Text(trust, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Book", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelSmall)
                            }
                        }
                    }
                }
            } else {
                AnimatedContent(
                    targetState = rideState,
                    transitionSpec = {
                        (fadeIn(animationSpec = androidx.compose.animation.core.tween(500)) + 
                         slideInVertically(animationSpec = androidx.compose.animation.core.tween(500), initialOffsetY = { height -> height / 2 })) togetherWith 
                        (fadeOut(animationSpec = androidx.compose.animation.core.tween(500)) + 
                         slideOutVertically(animationSpec = androidx.compose.animation.core.tween(500), targetOffsetY = { height -> -height / 2 })) using SizeTransform(clip = false)
                    },
                    label = "rideStateAnimation"
                ) { state ->
                    when(state) {
                        "searching" -> SearchingComponent()
                        "assigned" -> AssignedComponent()
                        "in_transit" -> InTransitComponent()
                    }
                }
            }
        }
    }
}

@Composable
fun SearchingComponent() {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.primary, strokeWidth = 2.dp)
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text("Finding your trusted driver...", fontWeight = FontWeight.Bold)
                Text("Analyzing nearby peer ratings", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
fun AssignedComponent() {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(shape = RoundedCornerShape(50), color = MaterialTheme.colorScheme.primary, modifier = Modifier.size(40.dp)) {
                    Icon(Icons.Filled.Person, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.padding(8.dp))
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text("Driver Assigned: Alex", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
                    Text("Trust Score: 99.1 • Arriving in 3 mins", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onPrimaryContainer)
                }
            }
        }
    }
}

@Composable
fun InTransitComponent() {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Security, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(32.dp))
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text("En Route to Destination", fontWeight = FontWeight.Bold)
                    Text("Ride monitored by TrustLayer", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("12 mins", fontWeight = FontWeight.Bold)
                    Text("Remaining", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            LinearProgressIndicator(
                progress = { 0.3f },
                modifier = Modifier.fillMaxWidth().height(4.dp),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surface
            )
        }
    }
}

@Composable
fun TrustMapScreen() {
    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
        Text("Community Trust Map", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        
        Card(
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.fillMaxWidth().weight(1f).padding(bottom = 24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawLine(Primary.copy(alpha = 0.2f), androidx.compose.ui.geometry.Offset(100f, 100f), androidx.compose.ui.geometry.Offset(400f, 300f), strokeWidth = 8f)
                    drawLine(Primary.copy(alpha = 0.2f), androidx.compose.ui.geometry.Offset(400f, 300f), androidx.compose.ui.geometry.Offset(200f, 700f), strokeWidth = 8f)
                    drawLine(Primary.copy(alpha = 0.2f), androidx.compose.ui.geometry.Offset(400f, 300f), androidx.compose.ui.geometry.Offset(800f, 500f), strokeWidth = 8f)
                    
                    drawCircle(Primary.copy(alpha = 0.1f), radius = 150f, center = androidx.compose.ui.geometry.Offset(400f, 300f))
                    drawCircle(Primary.copy(alpha = 0.1f), radius = 200f, center = androidx.compose.ui.geometry.Offset(200f, 700f))
                }
                
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.align(Alignment.TopEnd).padding(16.dp)
                ) {
                    Text("Safe Corridor Active", modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                }

                Column(modifier = Modifier.align(Alignment.Center).offset(x = (-30).dp, y = (-50).dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Filled.Verified, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(32.dp))
                    Surface(shape = RoundedCornerShape(8.dp), color = MaterialTheme.colorScheme.surface) {
                        Text("High Trust Zone", style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(4.dp))
                    }
                }
                
                Column(modifier = Modifier.align(Alignment.BottomStart).offset(x = 50.dp, y = (-100).dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Filled.LocalPolice, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(32.dp))
                    Surface(shape = RoundedCornerShape(8.dp), color = MaterialTheme.colorScheme.surface) {
                        Text("Monitored Area", style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(4.dp))
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

             RatingComponent()
             
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

@Composable
fun RatingComponent() {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text("Rate Your Last Ride", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(shape = RoundedCornerShape(50), color = MaterialTheme.colorScheme.secondaryContainer, modifier = Modifier.size(40.dp)) {
                    Icon(Icons.Filled.Person, contentDescription = null, modifier = Modifier.padding(8.dp))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text("Driver: Jane Doe", fontWeight = FontWeight.Bold)
                    Text("Today, 9:00 AM", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
                (1..5).forEach { i ->
                    Icon(Icons.Filled.Star, contentDescription = "Star", tint = if (i <= 4) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline, modifier = Modifier.size(32.dp))
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text("Did the driver feel safe and compliant?", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Surface(shape = RoundedCornerShape(12.dp), color = MaterialTheme.colorScheme.primaryContainer) {
                    Text("Yes, driving safely", modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                }
                Surface(shape = RoundedCornerShape(12.dp), color = MaterialTheme.colorScheme.surfaceVariant) {
                    Text("Smooth ride", modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), style = MaterialTheme.typography.labelSmall)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {}, modifier = Modifier.fillMaxWidth().height(48.dp), shape = RoundedCornerShape(16.dp)) {
                Text("Submit Review")
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
