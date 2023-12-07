package com.example.grocerystore.activities

import ProductDBViewModel
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.grocerystore.R
import com.example.grocerystore.classes.Product
import com.example.grocerystore.ui.theme.BuyProductButton
import com.example.grocerystore.ui.theme.GroceryStoreTheme
import com.example.grocerystore.viewmodel.ListViewModel
import com.example.grocerystore.viewmodel.OptionsViewModel

class AddProductActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val optionsViewModel = OptionsViewModel(applicationContext)
            ListViewModel(applicationContext)
            val dbViewModel = ProductDBViewModel(application)
            GroceryStoreTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AddProductCompose(optionsViewModel, applicationContext, dbViewModel)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductCompose(
    optionsViewModel: OptionsViewModel,
    context: Context,
    dbViewModel: ProductDBViewModel
) {
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .fillMaxWidth(),
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = optionsViewModel.returnColorOption()
                ),
                navigationIcon = {
                    IconButton(
                        onClick = {
                            val intent = Intent(context, MainActivity::class.java)
                            context.startActivity(intent)
                        },
                        modifier = Modifier.padding(10.dp),
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.baseline_arrow_back_ios_24),
                            contentDescription = "Settings",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                title = {
                    Text(
                        "Add product",
                        fontSize = 22.sp,
                        color = MaterialTheme.colorScheme.onPrimary,
                    )
                },
            )
            //Image of products PNG
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
        },
        content = { scaffoldPadding ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize()
                    .padding(scaffoldPadding)
            ) {
                ProductInputFields(dbViewModel, context)
            }
        })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductInputFields(dbViewModel: ProductDBViewModel, context: Context) {

    val productName = remember { mutableStateOf("") }
    val productPrice = remember { mutableStateOf("1.0") }
    val productQuantity = remember { mutableStateOf("1") }
    var productImage by remember { mutableStateOf(R.drawable.cross.toLong()) }

    val images = listOf(
        R.drawable.apple,
        R.drawable.banana,
        R.drawable.orange,
        R.drawable.pineapple,
        R.drawable.watermelon,
        R.drawable.juice,
        R.drawable.strawberry,
        R.drawable.apple_green,
        R.drawable.canada_apple
    )

    Box(
        contentAlignment = Alignment.TopStart,
        modifier = Modifier
            .width(300.dp)
            .height(105.dp)
            .padding(top = 10.dp)
    ) {
        Text(
            text = "Product Name:",
            fontSize = 18.sp
        )
        TextField(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            value = productName.value,
            onValueChange = { newText ->
                productName.value = newText
            },
            label = { Text("Name") }
        )
    }

    Box(
        contentAlignment = Alignment.TopStart,
        modifier = Modifier
            .width(300.dp)
            .height(105.dp)
            .padding(top = 10.dp)
    ) {
        Text(
            text = "Product Price:",
            fontSize = 18.sp
        )
        TextField(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            value = productPrice.value,
            onValueChange = { newText ->
                productPrice.value = newText
            },
            label = { Text("Price") }
        )
    }

    Box(
        contentAlignment = Alignment.TopStart,
        modifier = Modifier
            .width(300.dp)
            .height(105.dp)
            .padding(top = 10.dp)
    ) {
        Text(
            text = "Product Quantity:",
            fontSize = 18.sp
        )
        TextField(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            value = productQuantity.value,
            onValueChange = { newText ->
                productQuantity.value = newText
            },
            label = { Text("Quantity") }
        )
    }

    Spacer(modifier = Modifier.height(25.dp))
    Text(text = "Product Image:", fontSize = 18.sp)

    Image(
        painter = painterResource(id = productImage.toInt()),
        contentDescription = "Product",
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .width(150.dp)
    )

    ImagePicker(images = images, onImageSelected = { selectedImage ->
        productImage = selectedImage.toLong()
    })

    Button(
        onClick = {
            dbViewModel.insertProduct(
                Product(
                    "new",
                    productName.value,
                    productPrice.value,
                    productQuantity.value.toLong(),
                    0,
                    productImage,
                    false
                )
            )
            val intent = Intent(context, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        },
        modifier = Modifier.width(200.dp),
        colors = BuyProductButton()
    ) {
        Text(text = "Add product", fontSize = 25.sp)
    }
}

@Composable
fun ImagePicker(images: List<Int>, onImageSelected: (Int) -> Unit) {
    var isDialogVisible by remember { mutableStateOf(false) }

    // Display the main content
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(16.dp)
    ) {
        Button(onClick = { isDialogVisible = true }) {
            Text("Pick Image", fontSize = 18.sp)
        }
        Spacer(modifier = Modifier.height(16.dp))
        val selectedImage = remember { mutableStateOf<Int?>(null) }
        selectedImage.value?.let {
            Image(
                painter = painterResource(id = it),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
        }
        if (isDialogVisible) {
            AlertDialog(
                onDismissRequest = { isDialogVisible = false },
                title = { Text("Pick an Image") },
                confirmButton = {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(3.dp)
                    ) {
                        items(images) { image ->
                            Image(
                                painter = painterResource(id = image),
                                contentDescription = "Product",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(100.dp)
                                    .width(100.dp)

                                    .clickable {
                                        onImageSelected(image)
                                        isDialogVisible = false
                                    }
                            )
                        }
                    }
                }
            )
        }
    }
}

