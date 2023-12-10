@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.grocerystore.activities

import ProductDBViewModel
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import com.example.grocerystore.classes.ProductRepository
import com.example.grocerystore.ui.theme.BuyProductButton
import com.example.grocerystore.ui.theme.GroceryStoreTheme
import com.example.grocerystore.ui.theme.interFamily
import com.example.grocerystore.viewmodel.ListViewModel
import com.example.grocerystore.viewmodel.OptionsViewModel

class MainActivity : ComponentActivity() {

    private lateinit var listViewModel: ListViewModel
    lateinit var lazyListState: MutableState<LazyGridState>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val optionsViewModel = OptionsViewModel(context = applicationContext)
            listViewModel = ListViewModel(applicationContext)
            ProductRepository.key = if (listViewModel.loadListType()) "Shared" else listViewModel.loadListTypeString()
            val dbViewModel = ProductDBViewModel(application)
            val products by dbViewModel.products.collectAsState(emptyMap())
            GroceryStoreTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    ScaffoldCom(
                        "MiniProject", optionsViewModel, dbViewModel, products
                    )
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ScaffoldCom(
        name: String,
        optionsViewModel: OptionsViewModel,
        dbvm: ProductDBViewModel,
        productList: Map<String, Product>
    ) {
        val formatter = Formatter()
        val totalPrice = remember {
            mutableDoubleStateOf(0.0)
        }

        Scaffold(floatingActionButtonPosition = FabPosition.End, floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val addProductIntent = Intent(applicationContext, AddProductActivity::class.java)
                    addProductIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    applicationContext.startActivity(addProductIntent)
                },
                containerColor = optionsViewModel.returnColorOption(),
                contentColor = MaterialTheme.colorScheme.onPrimary
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
                            val optionIntent = Intent(applicationContext, OptionActivity::class.java)
                            optionIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            applicationContext.startActivity(optionIntent)
                        },
                        modifier = Modifier.padding(10.dp),
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.baseline_settings_36), // Customize with your settings icon
                            contentDescription = "Settings",
                            tint = MaterialTheme.colorScheme.onPrimary
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
                            text = "Total: $" + formatter.formatDoubleToString(totalPrice.doubleValue),
                            fontSize = optionsViewModel.returnSizeOption().sp,
                            fontFamily = interFamily,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.padding(start = 32.dp)
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 32.dp),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            TextButton(
                                shape = RoundedCornerShape(10.dp),
                                onClick = {
                                    totalPrice.doubleValue = 0.0
                                    dbvm.updateProducts(
                                        boughtValidation(
                                            productList.toList()
                                        )
                                    )
                                    lazyListState.value = LazyGridState()
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
                    .fillMaxSize()
                    .padding(scaffoldPadding)
            ) {
                SwitchButtons(dbvm, optionsViewModel, productList)
                LazyGridProducts(dbvm, totalPrice, productList)
            }
        })
    }

    @Composable
    fun SwitchButtons(
        dbvm: ProductDBViewModel,
        optionsViewModel: OptionsViewModel,
        productList: Map<String, Product>
    ) {
        val listViewModel = ListViewModel(applicationContext)
        var switchState by remember { mutableStateOf(listViewModel.loadListType()) }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Top
            ) {
                Box(modifier = Modifier
                    .width(210.dp)
                    .fillMaxHeight()
                    .clickable {
                        switchState = false
                        listViewModel.saveListType(false)
                        ProductRepository.key = listViewModel.loadListTypeString()
                        dbvm.switchList(ProductRepository.key)

                        println("${ProductRepository.key} {${productList.size}}")
                    }
                    .clip(RoundedCornerShape(bottomEnd = 16.dp, bottomStart = 16.dp))
                    .background(if (!switchState) optionsViewModel.returnColorOption() else Color.LightGray),
                    contentAlignment = Alignment.Center) {
                    Text(
                        text = "Personal list",
                        color = MaterialTheme.colorScheme.onSecondary,
                        fontSize = 16.sp,
                        fontFamily = interFamily,
                        fontWeight = FontWeight.Medium
                    )
                }
                Box(modifier = Modifier
                    .width(210.dp)
                    .fillMaxHeight()
                    .clickable {
                        switchState = true
                        listViewModel.saveListType(true)
                        ProductRepository.key = "Shared"
                        dbvm.switchList(ProductRepository.key)

                        println("${ProductRepository.key} {${productList.size}}")
                    }
                    .clip(RoundedCornerShape(bottomEnd = 16.dp, bottomStart = 16.dp))
                    .background(if (switchState) optionsViewModel.returnColorOption() else Color.LightGray),
                    contentAlignment = Alignment.Center) {
                    Text(
                        text = "Shared list",
                        color = MaterialTheme.colorScheme.onSecondary,
                        fontSize = 16.sp,
                        fontFamily = interFamily,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
    private fun totalPrice(productList: Map<String, Product>): Double {
        val formatter = Formatter()
        return productList.toList()
            .sumOf { it.second.boughtQuantity * formatter.formatStringToDouble(it.second.productPrice) }
    }


    private fun boughtValidation(productList: List<Pair<String, Product>>): List<Pair<String, Product>> {
        return productList.filter { product ->
            product.second.boughtQuantity > 0
        }
    }

    private fun zerosBought(productList: Map<String, Product>) {
        for (product in productList.values) product.boughtQuantity = 0
    }

    @Composable
    fun LazyGridProducts(
        dbvm: ProductDBViewModel, tPrice: MutableState<Double>, productList: Map<String, Product>
    ) {
        val formatter = Formatter()
        val optionsViewModel = OptionsViewModel(applicationContext)
        tPrice.value = totalPrice(productList)

        lazyListState = remember { mutableStateOf(LazyGridState()) }
        LazyVerticalGrid(
            state = lazyListState.value,
            modifier = Modifier
                .padding(bottom = 16.dp, start = 16.dp, end = 16.dp)
                .fillMaxSize(),
            columns = GridCells.Fixed(2)
        ) {
            items(
                items = productList.toList(),
            ) { product: Pair<String, Product> ->

                //var boughtQuantity by remember { mutableStateOf(productList[product.second.id]!!.boughtQuantity) }
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
                            painter = painterResource(id = productList[product.second.id]!!.imageId.toInt()),
                            contentDescription = "ProductImage",
                            modifier = Modifier.size(149.dp)
                        )
                        Text(
                            text = productList[product.second.id]!!.productName,
                            fontSize = 16.sp,
                            fontFamily = interFamily,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "Price: ${productList[product.second.id]!!.productPrice}$",
                            fontSize = 14.sp,
                            fontFamily = interFamily,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Quantity: ${productList[product.second.id]!!.productQuantity}",
                            fontSize = 12.sp,
                            fontFamily = interFamily,
                            fontWeight = FontWeight.Light
                        )
                        Text(
                            text = "Is Bought: ${productList[product.second.id]!!.bought}",
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
                                    if (productList[product.second.id]!!.boughtQuantity > 0) {
                                        productList[product.second.id]!!.boughtQuantity--
                                        tPrice.value -= formatter.formatStringToDouble(productList[product.second.id]!!.productPrice)
                                        dbvm.updateProduct(productList[product.second.id]!!)
                                    }
                                    println("${productList[product.second.id]!!.boughtQuantity} ")
                                    lazyListState.value = LazyGridState()
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
                                text = "${productList[product.second.id]!!.boughtQuantity}",
                                modifier = Modifier.padding(bottom = 5.dp),
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontSize = 12.sp,
                                fontFamily = interFamily,
                                fontWeight = FontWeight.SemiBold
                            )
                            IconButton(
                                onClick = {
                                    if (productList[product.second.id]!!.boughtQuantity < productList[product.second.id]!!.productQuantity) {
                                        productList[product.second.id]!!.boughtQuantity++
                                        tPrice.value += formatter.formatStringToDouble(productList[product.second.id]!!.productPrice)
                                        dbvm.updateProduct(productList[product.second.id]!!)
                                    }
                                    lazyListState.value = LazyGridState()
                                    println("${productList[product.second.id]!!.boughtQuantity}")
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
        }
    }
}