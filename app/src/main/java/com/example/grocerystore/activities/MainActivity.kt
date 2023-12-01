@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.grocerystore.activities

import ProductDBViewModel
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.grocerystore.Formatter
import com.example.grocerystore.R
import com.example.grocerystore.classes.Product
import com.example.grocerystore.ui.theme.BuyProductButton
import com.example.grocerystore.ui.theme.GroceryStoreTheme
import com.example.grocerystore.ui.theme.interFamily
import com.example.grocerystore.viewmodel.OptionsViewModel
import kotlinx.coroutines.flow.StateFlow

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContent {
            val optionsViewModel = OptionsViewModel(context = applicationContext)
            val dbViewModel = ProductDBViewModel(application)

            //null //TODO viewModel<OptionsViewModel>(factory = SettingsViewModelFactory(applicationContext))

            GroceryStoreTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ScaffoldCom(
                        "MiniProject",
                        dbViewModel.products,
                        dbViewModel,
                        applicationContext,
                        optionsViewModel
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldCom(
    name: String,
    productList: StateFlow<HashMap<String, Product>>,
    dbViewModel: ProductDBViewModel,
    context: Context,
    optionsViewModel: OptionsViewModel
) {

    val productListFlow by productList.collectAsState(emptyMap<String, Product>())
    val totalPrice = remember {
        mutableStateOf(0.0)
    }
    val formatter = Formatter()

    Scaffold(floatingActionButtonPosition = FabPosition.End, floatingActionButton = {
        FloatingActionButton(
            onClick = {
                val intent = Intent(context, AddProductActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            }, containerColor = optionsViewModel.returnColorOption(), contentColor = MaterialTheme.colorScheme.onPrimary
        ) {
            Icon(
                Icons.Rounded.Add,
                contentDescription = "Add",
            )
        }
    }, topBar = {
        TopAppBar(modifier = Modifier.fillMaxWidth(),
            colors = TopAppBarDefaults.largeTopAppBarColors(
                containerColor = optionsViewModel.returnColorOption()
            ),
            title = {
                Text(
                    text = name,
                    fontSize = optionsViewModel.returnSizeOption().sp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            },
            actions = {
                IconButton(
                    onClick = {
                        val intent = Intent(context, OptionActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        context.startActivity(intent)
                    },
                    modifier = Modifier.padding(10.dp),
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.baseline_settings_36), // Customize with your settings icon
                        contentDescription = "Settings", tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            })

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .requiredHeight(60.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.topappbar),
                contentDescription = "topAppBar",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    }, bottomBar = {

        BottomAppBar(
            tonalElevation = 10.dp, contentPadding = PaddingValues(0.dp)

        ) {
            Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter
            ) {
                Image(
                    painter = painterResource(id = R.drawable.footer),
                    contentDescription = "botAppBar",
                    colorFilter = ColorFilter.tint(optionsViewModel.returnColorOption()),
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier.fillMaxSize()
                )
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Text(
                        text = "Total: $" + formatter.formatDoubleToString(totalPrice.value),
                        fontSize = optionsViewModel.returnSizeOption().sp,
                        fontFamily = interFamily,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.padding(start = 32.dp)
                    )

                    Box(
                        modifier = Modifier.fillMaxWidth().padding(end = 32.dp), contentAlignment = Alignment.CenterEnd
                    ) {
                        TextButton(
                            shape = RoundedCornerShape(10.dp),
                            onClick = {
                                totalPrice.value = 0.0
                                dbViewModel.updateProducts(
                                    boughtValidation(
                                        productListFlow
                                    )
                                )
                            },
                            colors = BuyProductButton(),
                        ) {
                            Text(
                                text = "Buy products",
                                fontSize = optionsViewModel.returnSizeOption().sp,
                                fontFamily = interFamily,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }
            }
        }
    }, content = { scaffoldPadding ->
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
                .padding(scaffoldPadding)
        ) {
            //LazyVertical Grid of Products
            LazyGridProducts(productListFlow, totalPrice, dbViewModel, context)
        }
    })
}

fun totalPrice(productList: List<Pair<String, Product>>): Double {
    return productList.sumOf { it.second.boughtQuantity * it.second.productPrice }
}


fun boughtValidation(productList: Map<String, Product>): Map<String, Product> {
    return productList.filter { product ->
        product.value.boughtQuantity > 0
    }
}

@Composable
fun LazyGridProducts(
    productListFlow: Map<String, Product>,
    tPrice: MutableState<Double>,
    dbViewModel: ProductDBViewModel,
    context: Context
) {
    val state = LazyGridState()
    val productList = productListFlow.toList()

    tPrice.value = totalPrice(productList)
    LazyVerticalGrid(
        columns = GridCells.Fixed(2), state = state, modifier = Modifier.fillMaxSize()
    ) {
        items(productList) { product ->
            //Items in the grid
            ProductItem(product, tPrice, dbViewModel, context)
        }
    }
}


@Composable
fun ProductItem(
    product: Pair<String, Product>,
    tPrice: MutableState<Double>,
    dbViewModel: ProductDBViewModel,
    context: Context
) {
    val formatter = Formatter()
    val optionsViewModel = OptionsViewModel(context = context)
    Box(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .width(175.dp)
            .height(260.dp),
        contentAlignment = Alignment.TopStart
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Image(
                painter = painterResource(id = product.second.imageId),
                contentDescription = "ProductImage",
                modifier = Modifier.size(149.dp)
            )
            Text(
                text = product.second.productName,
                fontSize = 16.sp,
                fontFamily = interFamily,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "$${formatter.formatDoubleToString(product.second.productPrice)}",
                fontSize = 14.sp,
                fontFamily = interFamily,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "Quantity: ${product.second.productQuantity}",
                fontSize = 12.sp,
                fontFamily = interFamily,
                fontWeight = FontWeight.Light
            )
            Text(
                text = "Is Bought: ${product.second.isBought}",
                fontSize = 14.sp,
                fontFamily = interFamily,
                fontWeight = FontWeight.SemiBold
            )
        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .width(32.dp)
                .height(90.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.adder_bg),
                contentDescription = "adder_bg",
                colorFilter = ColorFilter.tint(optionsViewModel.returnColorOption()),
                modifier = Modifier.fillMaxSize()
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconButton(
                    onClick = {
                        if (product.second.boughtQuantity > 0) {
                            product.second.boughtQuantity--
                            tPrice.value -= product.second.productPrice
                            dbViewModel.updateProduct(product as Map.Entry<String, Product>)
                        }
                    },
                    modifier = Modifier
                        .width(24.dp)
                        .height(24.dp),
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.minus),
                        contentDescription = "Minus",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                Text(
                    text = "${product.second.boughtQuantity}",
                    modifier = Modifier.padding(bottom = 5.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 12.sp,
                    fontFamily = interFamily,
                    fontWeight = FontWeight.SemiBold
                )
                IconButton(
                    onClick = {
                        if (product.second.boughtQuantity < product.second.productQuantity) {
                            product.second.boughtQuantity++
                            tPrice.value += product.second.productPrice
                            dbViewModel.updateProduct(product as Map.Entry<String, Product>)
                        }
                    },
                    modifier = Modifier
                        .width(24.dp)
                        .height(24.dp),
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.plus),
                        contentDescription = "Plus",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}