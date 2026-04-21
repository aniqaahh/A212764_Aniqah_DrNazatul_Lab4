package com.example.a212764_aniqah_drnazatul_lab4

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController // PENTING
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.a212764_aniqah_drnazatul_lab4.ui.theme.A212764_Aniqah_DrNazatul_Lab4Theme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            A212764_Aniqah_DrNazatul_Lab4Theme {
                val viewModel: TrailViewModel = viewModel()
                val navController = rememberNavController()

                // Scaffold diletak di sini (ROOT)
                Scaffold(
                    bottomBar = { BottomNavBar(navController) }
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        AppNavigation(navController, viewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun AppNavigation(navController: NavHostController, viewModel: TrailViewModel) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeScreen(navController, viewModel) } //homescreen
        composable("details") { DetailScreen(navController, viewModel.selectedTrail.value) } //detail page
        composable("profile") { ProfileScreen() } // profile page
        composable("saved") { SavedScreen(navController, viewModel) }
        composable("map") { Text("Map Screen") }
        composable("activity") { Text("Activity Screen") }
    }
}
@Composable
fun HomeScreen(navController: NavController, viewModel: TrailViewModel) {
    var searchText by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
            Text("TrailFinder", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(16.dp))
            SearchBar(text = searchText, onTextChange = { searchText = it })
            Spacer(modifier = Modifier.height(16.dp))
            CategoryChips()
            Spacer(modifier = Modifier.height(20.dp))
            Text("Recommended for you", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))

            // LOOP SEMUA TRAIL KAT SINI
            viewModel.allTrails
                .filter { it.title.lowercase().contains(searchText.lowercase()) }
                .forEach { trailItem ->
                    TrailFullCard(trail = trailItem, navController = navController, viewModel = viewModel)
                }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }


@Composable
fun SearchBar(text: String, onTextChange: (String) -> Unit) {
    OutlinedTextField(
        value = text,
        onValueChange = onTextChange,
        placeholder = { Text("Search forest, hills...") },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(50.dp),
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
        trailingIcon = {
            Button(
                onClick = { },
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier.padding(end = 4.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                Text("Search")
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface
        )
    )
}

@Composable
fun CategoryChips() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ChipItem("Biking", R.drawable.biking)
        ChipItem("Running", R.drawable.running)
        ChipItem("Hiking", R.drawable.hiking)
    }
}

@Composable
fun ChipItem(text: String, iconRes: Int) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f))
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))

        Text(text, color = MaterialTheme.colorScheme.onPrimaryContainer)
    }
}

@Composable
fun TrailFullCard(trail: Trail, navController: NavController, viewModel: TrailViewModel) {
    var expanded by remember { mutableStateOf(false) }

    // Guna ni terus, tak payah remember lagi
    val isSaved by trail.isSaved

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp) // Tambah padding sikit bagi nampak cantik
            .animateContentSize(animationSpec = tween(400)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp)
    ) {
        Column {
            Box {
                Image(
                    painter = painterResource(id = trail.imageRes), // Guna trail.imageRes
                    contentDescription = null,
                    modifier = Modifier.height(180.dp).fillMaxWidth(),
                    contentScale = ContentScale.Crop
                )

                Row(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.6f), RoundedCornerShape(20.dp))
                ) {
                    IconButton(onClick = {
                        trail.isSaved.value = !trail.isSaved.value
                    }) {
                        Icon(
                            imageVector = if (isSaved) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = null,
                            tint = if (isSaved) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
                        )
                    }
                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(
                            imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(trail.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Text(trail.location, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Surface(
                        color = when (trail.level) {
                            "Hard" -> MaterialTheme.colorScheme.error
                            "Moderate" -> MaterialTheme.colorScheme.secondary
                            else -> MaterialTheme.colorScheme.tertiary
                        },
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(trail.level, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("⭐ ${trail.rating}", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                }
            }

            AnimatedVisibility(visible = expanded) {
                Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)) {
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        InfoItem("DISTANCE", trail.length)
                        InfoItem("ELEVATION", trail.elevation)
                        InfoItem("EST. TIME", trail.time)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            viewModel.selectedTrail.value = trail
                            navController.navigate("details")
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("View Trail Details")
                    }
                }
            }
        }
    }
}

@Composable
fun InfoItem(label: String, value: String) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun BottomNavBar(navController: NavController) {
    // 1. Ambil route semasa dari NavController
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val navItems = listOf(
        "Explore" to Icons.Default.Search,
        "Saved" to Icons.Default.Favorite,
        "Map" to Icons.Default.LocationOn,
        "Activity" to Icons.Default.List,
        "Profile" to Icons.Default.Person
    )

    // 2. Mapping: Sini kunci dia. Hanya route yang ada dalam NavHost akan berfungsi
    // Kalau belum buat screen "saved", "map", "activity", buat sementara letak "home"
    val routes = listOf("home", "saved", "map", "activity", "profile")

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        navItems.forEachIndexed { index, item ->
            val route = routes[index]

            NavigationBarItem(
                // 3. Highlight icon berdasarkan route sebenar, bukan index
                selected = currentRoute == route,
                onClick = {
                    // Hanya navigate kalau route tu wujud
                    if (route != "none") {
                        navController.navigate(route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                label = {
                    Text(
                        text = item.first,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = if (currentRoute == route) FontWeight.Bold else FontWeight.Normal
                    )
                },
                icon = {
                    Icon(
                        imageVector = item.second,
                        contentDescription = item.first,
                        modifier = Modifier.size(24.dp)
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = Color.Gray,
                    unselectedTextColor = Color.Gray,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                )
            )
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun HomePreview() {
//    A212764_Aniqah_DrNazatul_Lab4Theme {
//        // Preview perlukan mock setup supaya tak crash
//        val dummyVm = TrailViewModel()
//        val dummyNav = rememberNavController()
//        HomeScreen(dummyNav, dummyVm)
//    }
//}