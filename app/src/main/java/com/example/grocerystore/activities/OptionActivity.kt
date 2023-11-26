package com.example.grocerystore.activities

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
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.grocerystore.R
import com.example.grocerystore.ui.theme.GroceryStoreTheme

class OptionActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GroceryStoreTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Options("Settings")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Options(name: String) {

    val context = LocalContext.current
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .fillMaxWidth(),
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
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
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_back), // Customize with your settings icon
                            contentDescription = "Settings", tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                title = {
                    Text(
                        name,
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
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize()
                    .padding(scaffoldPadding)
            ) {
                OptionsActivity()
            }
        })
}

@Composable
fun OptionsActivity() {
    var expandedColor by remember { mutableStateOf(false) }
    var selectedColor by remember { mutableStateOf("Red") }
    var expandedSize by remember { mutableStateOf(false) }
    var selectedSize by remember { mutableStateOf("Medium") }
    var darkCheck by remember {
        mutableStateOf(false)
    }

    val colorOptions = listOf("Red", "Blue", "Green")
    val sizeOptions = listOf("Small", "Medium", "Large")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    )  {
        Text("Select Theme Color:")
        Box(
            modifier = Modifier.clickable { expandedColor = true }
        ) {
            Button(
                onClick = { expandedColor = true },
            ) {
                Text(selectedColor)
            }
            DropdownMenu(
                expanded = expandedColor, // Set to true to expand the menu
                onDismissRequest = { expandedColor = false },
            ) {
                colorOptions.forEach { color ->
                    DropdownMenuItem(
                        text = { Text(text = color) },
                        onClick = {
                            selectedColor = color
                            expandedColor = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Size Option
        Text("Select Text Size:")
        Box(
            modifier = Modifier.clickable { expandedSize = true }
        ) {
            Button(
                onClick = { expandedSize = true },
            ) {
                Text(selectedSize)
            }
            DropdownMenu(
                expanded = expandedSize, // Set to true to expand the menu
                onDismissRequest = { expandedSize = false },
            ) {
                sizeOptions.forEach { size ->
                    DropdownMenuItem(
                        text = { Text(text = size) },
                        onClick = {
                            selectedSize = size
                            expandedSize = false
                        })
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Dark mode
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Dark Mode:")
            Spacer(modifier = Modifier.width(16.dp))
            Switch(
                checked = darkCheck,
                onCheckedChange = { darkCheck = it },
                colors = SwitchDefaults.colors(checkedThumbColor = MaterialTheme.colorScheme.onPrimary)
            )
        }
    }
}