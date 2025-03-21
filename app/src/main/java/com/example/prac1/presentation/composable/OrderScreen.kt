package com.example.prac1.presentation.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.prac1.R
import com.example.prac1.data.api.model.OrderDataModel
import com.example.prac1.domain.model.CartItem
import com.example.prac1.domain.model.Flower
import com.example.prac1.presentation.viewmodel.OrderViewModel

/**
 * Order details screen composable showing information about a specific order
 *
 * @param orderId ID of the order to display
 * @param orderViewModel ViewModel managing order data and state
 * @param navigateBack Callback for back navigation
 * @param onItemClick Callback when an order item is clicked
 * @param onFavorite Callback when favorite button is clicked
 * @param isFavorite Function to check if item is favorite
 * @author Sofia Bakalskaya
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderScreen(
    orderId: String?,
    orderViewModel: OrderViewModel,
    navigateBack: () -> Unit,
    onItemClick: (Flower) -> Unit,
    onFavorite: (id: String) -> Unit,
    isFavorite: (id: String) -> Boolean,
) {
    val currentOrder by orderViewModel.order.collectAsState(null)
    val orderItemsMap by orderViewModel.flowersMap.collectAsState(emptyMap())
    LaunchedEffect(orderId) {
        if (orderId != null) {
            orderViewModel.loadData(orderId)
        }
    }
    DisposableEffect(Unit) {
        onDispose {
            orderViewModel.clearData()
        }
    }
    Scaffold(
        containerColor = colorResource(R.color.Neutral20),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        modifier = Modifier.height(30.dp),
                        text = stringResource(R.string.order_details),
                        color = colorResource(R.color.Text),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    Icon(imageVector = ImageVector.vectorResource(R.drawable.left_icon),
                        contentDescription = null,
                        tint = colorResource(R.color.Text),
                        modifier = Modifier
                            .clip(
                                CircleShape
                            )
                            .size(42.dp)
                            .clickable {
                                navigateBack()
                            })
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { paddingValues ->
        if (currentOrder == null) {
            CircularProgressIndicator()
        } else {
            val order = currentOrder as OrderDataModel
            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item { Spacer(modifier = Modifier.padding(2.dp)) }
                item {
                    Text(
                        text = stringResource(R.string.general),
                        color = colorResource(R.color.Text),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                //card
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(4.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = colorResource(R.color.Neutral10)
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    modifier = Modifier.weight(1f),
                                    text = stringResource(R.string.timestamp),
                                    color = colorResource(R.color.Primary),
                                    fontSize = 16.sp
                                )
                                Text(
                                    text = order.created_at.toString(),
                                    color = colorResource(R.color.Text),
                                    fontSize = 16.sp
                                )
                            }
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp)

                            ) {
                                Text(
                                    modifier = Modifier.weight(1f),
                                    text = stringResource(R.string.total_cost),
                                    color = colorResource(R.color.Primary),
                                    fontSize = 16.sp
                                )
                                Text(
                                    text = "₽${order.total_price}",
                                    color = colorResource(R.color.Text),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
                item {
                    Text(
                        text = stringResource(R.string.items),
                        color = colorResource(R.color.Text),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                items(orderItemsMap.size) { i ->
                    val cartItem = orderItemsMap.keys.toList()[i]
                    val flower = orderItemsMap[cartItem]

                    if (flower == null) {
                        Text(text = "Loading flower for item ${cartItem.id}...")
                    } else {
                        OrderItemCard(
                            flower = flower,
                            cartItem = cartItem,
                            onClick = { onItemClick(flower) },
                            isFavorite = isFavorite,
                            onFavorite = onFavorite
                        )
                    }
                }
                item { Spacer(modifier = Modifier.padding(32.dp)) }
            }
        }
    }
}

/**
 * Composable card representing a single item in an order
 *
 * @param flower Flower data to display
 * @param cartItem Cart item data including quantity
 * @param onClick Callback when item is clicked
 * @param onFavorite Callback when favorite button is clicked
 * @param isFavorite Function to check if item is favorite
 * @author Sofia Bakalskaya
 */
@Composable
fun OrderItemCard(
    flower: Flower,
    cartItem: CartItem,
    onClick: () -> Unit,
    onFavorite: (id: String) -> Unit,
    isFavorite: (id: String) -> Boolean,
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(R.color.Neutral10)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            GlideImage(
                imageUrl = flower.imageUrl,
                description = flower.name,
                modifier = Modifier
                    .width(100.dp)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(8.dp))
            )
            Box(modifier = Modifier.height(100.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopStart),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = flower.name,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = colorResource(R.color.Text)
                        )
                        Text(
                            text = "₽${flower.price * cartItem.quantity}",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = colorResource(R.color.Text)
                        )
                    }
                }
                var favorite by remember { mutableStateOf(isFavorite(flower.id)) }
                Icon(
                    modifier = Modifier
                        .size(20.dp)
                        .align(Alignment.BottomStart)
                        .offset(y = (-6).dp)
                        .clickable(interactionSource = null, indication = null) {
                            onFavorite(flower.id)
                            favorite = !favorite
                        },
                    imageVector = if (favorite) ImageVector.vectorResource(R.drawable.heart_filled)
                    else ImageVector.vectorResource(R.drawable.heart),
                    contentDescription = null,
                    tint = if (favorite) colorResource(R.color.Primary) else colorResource(R.color.Neutral60)
                )
                QuantityCard(
                    modifier = Modifier
                        .height(32.dp)
                        .align(Alignment.BottomEnd),
                    quantity = cartItem.quantity
                )
            }
        }
    }
}

/**
 * Composable showing the quantity of an ordered item
 *
 * @param quantity Number of items ordered
 * @param modifier Modifier for styling and layout
 * @author Sofia Bakalskaya
 */
@Composable
fun QuantityCard(
    quantity: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .border(
                width = 1.dp,
                shape = RoundedCornerShape(8.dp),
                color = colorResource(R.color.Neutral40)
            )
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .background(
                color = Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        Text(
            text = "X$quantity",
            modifier = Modifier
                .fillMaxHeight(),
            textAlign = TextAlign.Center,
            fontSize = 14.sp,
        )
    }
}