package com.example.frontendgestoreta.ui.screens_user

import android.annotation.SuppressLint
import android.preference.PreferenceManager
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.frontendgestoreta.R
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import kotlinx.coroutines.launch
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import android.Manifest
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically

val LightPurpleBg = Color(0xFFD0C4FF)
val SelectedPurple = Color(0xFF6750A4)
val SurfaceWhite = Color.White
val SheetPeekHeight = 240.dp // Altura visible cuando el panel está abajo

data class MapCategory(val name: String, val iconRes: Int, val isSelected: Boolean = false)
enum class MapTab { SEARCH, FILTER, SAVED }

data class MapEvent(
    val name: String,
    val lat: Double,
    val lon: Double,
    val categoryName: String,
    val iconRes: Int
)

fun getResizedDrawableMaintainAspectRatio(context: android.content.Context, iconRes: Int, targetHeightPx: Int): Drawable {
    val originalBitmap = BitmapFactory.decodeResource(context.resources, iconRes)
    val aspectRatio = originalBitmap.width.toFloat() / originalBitmap.height.toFloat()
    val targetWidthPx = (targetHeightPx * aspectRatio).toInt()
    val finalWidth = if (targetWidthPx > 0) targetWidthPx else 1
    val finalHeight = if (targetHeightPx > 0) targetHeightPx else 1
    val scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, finalWidth, finalHeight, true)
    if (originalBitmap != scaledBitmap) {
        originalBitmap.recycle()
    }

    return BitmapDrawable(context.resources, scaledBitmap)
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen() {

    var currentTab by remember { mutableStateOf(MapTab.FILTER) }
    var selectedEventPopup by remember { mutableStateOf<MapEvent?>(null) }
    val savedEvents = remember { mutableStateListOf<MapEvent>() }
    var mapController by remember { mutableStateOf<org.osmdroid.api.IMapController?>(null) }
    val scope = rememberCoroutineScope()
    var searchQuery by remember { mutableStateOf("") }
    var mapView by remember { mutableStateOf<MapView?>(null) }
    var showNoEventsMessage by remember { mutableStateOf(false) }

    // Coordenadas para pines
    val allEvents = remember {
        listOf(
            MapEvent("Casal Na Jordana", 39.4816, -0.3824, "Casales", R.drawable.ic_map_pin_casales),
            MapEvent("Casal Convento Jerusalén", 39.4682, -0.3835, "Casales", R.drawable.ic_map_pin_casales),
            MapEvent("Casal Plaza del Pilar", 39.4736, -0.3805, "Casales", R.drawable.ic_map_pin_casales),
            MapEvent("Casal Cuba-Literato Azorín", 39.4612, -0.3736, "Casales", R.drawable.ic_map_pin_casales),
            MapEvent("Casal Sueca-Literato Azorín", 39.4620, -0.3745, "Casales", R.drawable.ic_map_pin_casales),
            MapEvent("Casal Almirante Cadarso", 39.4665, -0.3685, "Casales", R.drawable.ic_map_pin_casales),
            MapEvent("Casal L'Antiga de Campanar", 39.4850, -0.3950, "Casales", R.drawable.ic_map_pin_casales),

            MapEvent("Inicio Ofrenda (C/ La Paz)", 39.4739, -0.3746, "Pasacalles", R.drawable.ic_map_pin_pasacalles),
            MapEvent("Inicio Ofrenda (San Vicente)", 39.4730, -0.3755, "Pasacalles", R.drawable.ic_map_pin_pasacalles),
            MapEvent("Recogida de Premios (Ayto)", 39.4699, -0.3763, "Pasacalles", R.drawable.ic_map_pin_pasacalles),
            MapEvent("Pasacalle Russafa", 39.4600, -0.3700, "Pasacalles", R.drawable.ic_map_pin_pasacalles),
            MapEvent("Pasacalle Marítimo", 39.4650, -0.3300, "Pasacalles", R.drawable.ic_map_pin_pasacalles),
        )
    }
    val searchResults = remember(searchQuery, allEvents) {
        if (searchQuery.isBlank()) emptyList()
        else allEvents.filter {
            it.name.contains(searchQuery, ignoreCase = true) ||
                    it.categoryName.contains(searchQuery, ignoreCase = true)
        }
    }

    // Estado del panel deslizable
    val scaffoldState = rememberBottomSheetScaffoldState()

    var categories by remember {
        mutableStateOf(
            listOf(
                MapCategory("Verbenas", R.drawable.ic_verbenas),
                MapCategory("Pirotecnia", R.drawable.ic_pirotecnia),
                MapCategory("Pasacalles", R.drawable.ic_pasacalles, isSelected = true), // Seleccionado inicial
                MapCategory("Cultura", R.drawable.ic_cultura),
                MapCategory("Casales", R.drawable.ic_casales),
            )
        )
    }
    val selectedCategory = categories.find { it.isSelected }

    LaunchedEffect(selectedCategory) {
        selectedCategory?.let { cat ->
            val eventsInCategory = allEvents.filter { it.categoryName == cat.name }

            if (eventsInCategory.isEmpty()) {
                showNoEventsMessage = true
                return@let
            } else {
                showNoEventsMessage = false
            }

            val mv = mapView ?: return@LaunchedEffect

            val minLat = eventsInCategory.minOf { it.lat }
            val maxLat = eventsInCategory.maxOf { it.lat }
            val minLon = eventsInCategory.minOf { it.lon }
            val maxLon = eventsInCategory.maxOf { it.lon }

            val boundingBox = org.osmdroid.util.BoundingBox(
                maxLat,
                maxLon,
                minLat,
                minLon
            )

            // Centra antes de animar
            mv.controller.setCenter(
                GeoPoint(
                    (minLat + maxLat) / 2,
                    (minLon + maxLon) / 2
                )
            )

            // Ajuste correcto de zoom mostrando todos los pines
            val paddingFactor = 0.20 // 10% de margen

            val latPadding = (maxLat - minLat) * paddingFactor
            val lonPadding = (maxLon - minLon) * paddingFactor

            val paddedBoundingBox = org.osmdroid.util.BoundingBox(
                maxLat + latPadding,
                maxLon + lonPadding,
                minLat - latPadding,
                minLon - lonPadding
            )

            // Centra el mapa antes de aplicar el zoom
            mv.controller.setCenter(
                GeoPoint(
                    (minLat + maxLat) / 2,
                    (minLon + maxLon) / 2
                )
            )

            // Ahora ajusta el zoom incluyendo el margen
            mv.zoomToBoundingBox(paddedBoundingBox, true)

        }
    }





    Box(modifier = Modifier.fillMaxSize()) {
        BottomSheetScaffold(
            scaffoldState = scaffoldState,
            sheetPeekHeight = SheetPeekHeight,
            sheetContainerColor = SurfaceWhite,
            sheetShape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
            sheetContent = {
                // Contenido dinámico del panel
                FilterSheetContent(
                    currentTab = currentTab,
                    categories = categories,
                    savedEvents = savedEvents,
                    isExpanded = scaffoldState.bottomSheetState.targetValue == SheetValue.Expanded ||
                            scaffoldState.bottomSheetState.currentValue == SheetValue.Expanded,
                    onCategoryClick = { selectedCategory ->
                        categories = categories.map { cat ->
                            cat.copy(isSelected = cat.name == selectedCategory.name)
                        }
                    },
                    searchQuery = searchQuery,
                    searchResults = searchResults,
                    onSearchQueryChange = { searchQuery = it },
                    onSearchResultClick = { event ->
                        //Activar opción filtro
                        categories = categories.map { cat ->
                            cat.copy(isSelected = cat.name == event.categoryName)
                        }
                        // Mover el mapa al punto
                        mapController?.animateTo(GeoPoint(event.lat, event.lon))
                        mapController?.setZoom(19.5) // Hacemos zoom in para verlo cerca

                        scope.launch {
                            scaffoldState.bottomSheetState.partialExpand()
                        }
                    }
                )
            },
            content = {
                Box(modifier = Modifier.fillMaxSize()) {
                    OsmMapView(
                        modifier = Modifier.fillMaxSize(),
                        events = allEvents,
                        selectedCategory = selectedCategory,
                        onEventClick = { event ->
                            selectedEventPopup = event
                        },
                        onMapReady = { mv ->
                            mapView = mv
                            mapController = mv.controller
                        }

                    )

                    androidx.compose.animation.AnimatedVisibility(
                        visible = selectedEventPopup != null,
                        enter = fadeIn() + androidx.compose.animation.slideInVertically { -it }, // Entra desde arriba
                        exit = fadeOut(),
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(top = 50.dp)
                    ) {
                        // Si selectedEventPopup no es nulo, lo mostramos
                        selectedEventPopup?.let { event ->
                            val isAlreadySaved = savedEvents.contains(event)
                            MapEventPopup(
                                event = event,
                                isSaved = isAlreadySaved,
                                onToggleSave = {
                                    if (isAlreadySaved) {
                                        savedEvents.remove(event)
                                    } else {
                                        savedEvents.add(event)
                                    }
                                },
                                onClose = { selectedEventPopup = null }
                            )
                        }
                    }
                    AnimatedVisibility(
                        visible = showNoEventsMessage,
                        enter = fadeIn() + slideInVertically { it },
                        exit = fadeOut() + slideOutVertically { it },
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(top = 100.dp)
                    ) {
                        Surface(
                            color = Color(0xFF6750A4),
                            shape = RoundedCornerShape(12.dp),
                            shadowElevation = 8.dp
                        ) {
                            Text(
                                text = "No hay eventos en esta categoría",
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                            )
                        }
                    }
                }
            }
        )

        // Barra Flotante (Search, Filter, Save)
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 30.dp) // Margen para despegarlo del borde
                .fillMaxWidth()
        ) {
            BottomFloatingBar(
                currentTab = currentTab,
                onTabSelected = { newTab -> currentTab = newTab }
            )
        }
    }
}

@Composable
fun FilterSheetContent(
    currentTab: MapTab,
    categories: List<MapCategory>,
    savedEvents: List<MapEvent>,
    searchQuery: String,
    searchResults: List<MapEvent>,
    onSearchQueryChange: (String) -> Unit,
    onSearchResultClick: (MapEvent) -> Unit,
    isExpanded: Boolean,
    onCategoryClick: (MapCategory) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .heightIn(min = SheetPeekHeight, max = 800.dp)
    ) {
        AnimatedContent(targetState = currentTab, label = "TabContent") { tab ->
            when (tab) {
                MapTab.SEARCH -> SearchTabContent(
                    query = searchQuery,
                    onQueryChange = onSearchQueryChange,
                    results = searchResults,
                    onResultClick = onSearchResultClick
                )
                MapTab.SAVED -> SavedTabContent(savedEvents)
                MapTab.FILTER -> FilterTabContent(categories, isExpanded, onCategoryClick)
            }
        }
    }
}

@Composable
fun FilterTabContent(categories: List<MapCategory>, isExpanded: Boolean, onCategoryClick: (MapCategory) -> Unit) {
    // Si está expandido -> Grid. Si está colapsado -> Row.
    if (isExpanded) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Todas las Categorías",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 16.dp)
            )
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .padding(bottom = 60.dp)
            ) {
                items(categories) { category ->
                    CategoryItem(category, isCompact = true, onClick = { onCategoryClick(category) })
                }
            }
        }
    } else {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 80.dp)
        ) {
            items(categories) { category ->
                CategoryItem(category, isCompact = false, onClick = { onCategoryClick(category) })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTabContent(
    query: String,
    onQueryChange: (String) -> Unit,
    results: List<MapEvent>,
    onResultClick: (MapEvent) -> Unit )
{
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 400.dp)
            .padding(bottom = 80.dp)
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            placeholder = { Text("Buscar falla o evento...") },
            leadingIcon = { Icon(painterResource(id = R.drawable.ic_map_search), contentDescription = null, modifier = Modifier.size(24.dp)) },
            trailingIcon = if (query.isNotEmpty()) {
                {
                    IconButton(onClick = { onQueryChange("") }) {
                        Icon(painterResource(android.R.drawable.ic_menu_close_clear_cancel), contentDescription = "Borrar")
                    }
                }
            } else null,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            singleLine = true,
            colors = TextFieldDefaults.colors(
                // Texto
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,

                // 2. Cursor en tu color morado
                cursorColor = SelectedPurple,

                // 3. Fondo blanco
                focusedContainerColor = SurfaceWhite,
                unfocusedContainerColor = SurfaceWhite,

            )

        )
        Spacer(modifier = Modifier.height(16.dp))
        if (query.isEmpty()) {
            Text("Escribe para buscar...", style = MaterialTheme.typography.labelLarge, color = Color.Gray)
        } else {
            Text("Resultados (${results.size})", style = MaterialTheme.typography.labelLarge, color = SelectedPurple)
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Lista de Resultados
        androidx.compose.foundation.lazy.LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(results) { event ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onResultClick(event) } // <--- Acción al hacer click
                        .padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Icono
                    Icon(
                        painter = painterResource(id = R.drawable.ic_map),
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))

                    // Texto
                    Column {
                        Text(event.name, fontWeight = FontWeight.SemiBold)
                        Text(
                            event.categoryName,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                }
                Divider(color = Color.LightGray.copy(alpha = 0.3f))
            }
        }
    }
}

@Composable
fun SavedTabContent(savedEvents: List<MapEvent>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 400.dp)
            .padding(bottom = 80.dp)
    ) {
        Text(
            text = "Mis Guardados (${savedEvents.size})",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 16.dp)
        )

        if (savedEvents.isEmpty()) {
            // Mensaje si no hay nada guardado
            Box(modifier = Modifier.fillMaxWidth().padding(40.dp), contentAlignment = Alignment.Center) {
                Text("No tienes eventos guardados aún", color = Color.Gray)
            }
        } else {
            // Lista real
            savedEvents.forEach { event ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(LightPurpleBg.copy(0.5f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = event.iconRes),
                            contentDescription = null,
                            tint = SelectedPurple,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(event.name, fontWeight = FontWeight.SemiBold)
                }
                Divider(color = Color.LightGray.copy(alpha = 0.3f))
            }
        }
    }
}

// ----------------------------------------------------------
// UI ELEMENTS (Botones e Items)
// ----------------------------------------------------------

@Composable
fun CategoryItem(category: MapCategory, isCompact: Boolean, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(if (isCompact) 48.dp else 56.dp)
                .clip(CircleShape)
                .background(SurfaceWhite)
                .border(
                    width = if (category.isSelected) 2.dp else 0.5.dp,
                    color = if (category.isSelected) SelectedPurple else Color.LightGray,
                    shape = CircleShape
                )
                .clickable { onClick() }
        ) {
            Icon(
                painter = painterResource(id = category.iconRes),
                contentDescription = category.name,
                tint = Color.Unspecified,
                modifier = Modifier.size(if (isCompact) 20.dp else 24.dp)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = category.name,
            fontSize = if (isCompact) 10.sp else 12.sp,
            color = Color.Gray,
            fontWeight = FontWeight.Medium,
            maxLines = 1
        )
    }
}

@Composable
fun BottomFloatingBar(
    currentTab: MapTab,
    onTabSelected: (MapTab) -> Unit
) {
    Surface(
        color = LightPurpleBg,
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(70.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Botón 1: Buscar
            Surface(
                color = if (currentTab == MapTab.SEARCH) SurfaceWhite else Color.Transparent,
                shape = CircleShape,
                modifier = Modifier
                    .size(42.dp)
                    .clickable { onTabSelected(MapTab.SEARCH) }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_map_search),
                    contentDescription = "Buscar",
                    tint = if (currentTab == MapTab.SEARCH) SelectedPurple else Color.Black,
                    modifier = Modifier.padding(10.dp)
                )
            }

            // Botón 2: Filtro (Central destacado)
            Surface(
                color = if (currentTab == MapTab.FILTER) SurfaceWhite else Color.Transparent,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .width(100.dp)
                    .height(40.dp)
                    .clickable { onTabSelected(MapTab.FILTER) }
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_map_filter),
                        contentDescription = "Filtrar",
                        tint = if (currentTab == MapTab.FILTER) SelectedPurple else Color.Black,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            // Botón 3: Guardados
            Surface(
                color = if (currentTab == MapTab.SAVED) SurfaceWhite else Color.Transparent,
                shape = CircleShape,
                modifier = Modifier
                    .size(42.dp)
                    .clickable { onTabSelected(MapTab.SAVED) }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_map_save),
                    contentDescription = "Guardados",
                    tint = if (currentTab == MapTab.SAVED) SelectedPurple else Color.Black,
                    modifier = Modifier.padding(10.dp)
                )
            }
        }
    }
}

@SuppressLint("RememberReturnType")
@Composable
fun OsmMapView(
    modifier: Modifier = Modifier,
    events: List<MapEvent>,
    selectedCategory: MapCategory?,
    onEventClick: (MapEvent) -> Unit,
    onMapReady: (MapView) -> Unit
) {
    val context = LocalContext.current

    remember {
        Configuration.getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(context))
        Configuration.getInstance().userAgentValue = context.packageName
    }

    val baseIconSize = 75
    val referenceZoom = 16.0

    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            MapView(ctx).apply {

                val locationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(ctx), this)
                locationOverlay.enableMyLocation() // Activa el punto azul
                overlays.add(locationOverlay)

                setTileSource(TileSourceFactory.MAPNIK)
                setMultiTouchControls(true)
                controller.setZoom(19.0)
                controller.setCenter(GeoPoint(39.4699, -0.3763))
                onMapReady(this)

                // LISTENER DE ZOOM
                addMapListener(object : org.osmdroid.events.MapListener {
                    override fun onScroll(event: org.osmdroid.events.ScrollEvent?): Boolean = true

                    override fun onZoom(event: org.osmdroid.events.ZoomEvent?): Boolean {
                        val currentZoom = event?.zoomLevel ?: 16.0
                        var newSize = (baseIconSize * (currentZoom / referenceZoom)).toInt()

                        // Límites de tamaño
                        if (newSize < 20) newSize = 20
                        if (newSize > 300) newSize = 300

                        overlays.forEach { overlay ->
                            if (overlay is org.osmdroid.views.overlay.Marker) {
                                // Recuperamos el ID ESPECÍFICO del evento
                                val iconResId = overlay.relatedObject as? Int
                                if (iconResId != null) {
                                    overlay.icon = getResizedDrawableMaintainAspectRatio(context, iconResId, newSize)
                                }
                            }
                        }
                        invalidate()
                        return true
                    }
                })
            }
        },
        update = { mapView ->
            mapView.overlays.removeAll { it is org.osmdroid.views.overlay.Marker }

            if (selectedCategory != null) {
                val eventsToShow = events.filter { it.categoryName == selectedCategory.name }

                // Calculamos el tamaño inicial según el zoom actual
                val currentZoom = mapView.zoomLevelDouble
                var initialSize = (baseIconSize * (currentZoom / referenceZoom)).toInt()
                if (initialSize < 20) initialSize = 20

                eventsToShow.forEach { event ->
                    val marker = org.osmdroid.views.overlay.Marker(mapView)
                    marker.position = GeoPoint(event.lat, event.lon)

                    val eventDrawable = getResizedDrawableMaintainAspectRatio(context, event.iconRes, initialSize)
                    marker.icon = eventDrawable
                    marker.relatedObject = event.iconRes
                    marker.setAnchor(org.osmdroid.views.overlay.Marker.ANCHOR_CENTER, org.osmdroid.views.overlay.Marker.ANCHOR_BOTTOM)

                    marker.setOnMarkerClickListener { _, _ ->
                        onEventClick(event)
                        true
                    }
                    mapView.overlays.add(marker)
                }
            }
            mapView.invalidate()
        }
    )
}

@Composable
fun MapEventPopup(
    event: MapEvent,
    isSaved: Boolean,
    onToggleSave: () -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp), // Márgenes externos
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Box {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Icono de la izquierda
                Surface(
                    color = LightPurpleBg.copy(alpha = 0.4f),
                    shape = CircleShape,
                    modifier = Modifier.size(50.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            painter = painterResource(id = event.iconRes),
                            contentDescription = null,
                            tint = SelectedPurple,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(22.dp))

                // Textos de información
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = event.categoryName.uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Gray,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = event.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    // Botones Detalles + Guardar
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Botón Detalles
                        Surface(
                            color = SelectedPurple,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.clickable { /* Acción detalles */ }
                        ) {
                            Text(
                                text = "Ver detalles",
                                color = Color.White,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))
                        Icon(
                            painter = painterResource(id = R.drawable.ic_map_save),
                            contentDescription = "Guardar",
                            tint = if (isSaved) SelectedPurple else Color.Gray,
                            modifier = Modifier
                                .size(20.dp)
                                .clickable { onToggleSave() }
                        )
                    }
                }
            }

            IconButton(
                onClick = onClose,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(6.dp)
            ) {
                Icon(
                    painter = painterResource(id = android.R.drawable.ic_menu_close_clear_cancel),
                    contentDescription = "Cerrar",
                    tint = Color.Gray,
                    modifier = Modifier.size(18.dp)

                )
            }
        }
    }
}