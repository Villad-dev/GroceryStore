package com.example.grocerystore.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.grocerystore.R
import com.example.grocerystore.classes.ProductRepository
import com.example.grocerystore.ui.theme.GroceryStoreTheme
import com.example.grocerystore.ui.theme.interFamily
import com.example.grocerystore.ui.theme.lightColorScheme
import com.example.grocerystore.viewmodel.ListViewModel
import com.example.grocerystore.viewmodel.OptionsViewModel
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val optionsViewModel = OptionsViewModel(context = applicationContext)
            val listViewModel = ListViewModel(applicationContext)
            auth = FirebaseAuth.getInstance()

            val email = remember { mutableStateOf("vladicicre@gmail.com") }
            val password = remember { mutableStateOf("123456") }
            var checkBoxState = remember {
                mutableStateOf(false)
            }
            GroceryStoreTheme {
                lightColorScheme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    Scaffold(
                        containerColor = Color(0xFFCADCFF),
                        topBar = {
                            TopAppBar(
                                title = {},
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(70.dp),
                                colors = TopAppBarDefaults.largeTopAppBarColors(
                                    containerColor = optionsViewModel.returnColorOption()
                                )
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .requiredHeight(70.dp)
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.shape),
                                    contentDescription = "topAppBar",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        },

                        bottomBar = {
                            BottomAppBar(
                                modifier = Modifier
                                    .height(139.dp)
                                    .background(Color(0xFFCADCFF))
                                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                                tonalElevation = 10.dp, contentPadding = PaddingValues(0.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(Color(0xFFFFFFFF)),
                                    contentAlignment = Alignment.TopCenter,
                                ) {
                                    TextButton(
                                        modifier = Modifier
                                            .padding(top = 16.dp)
                                            .width(350.dp)
                                            .height(48.dp),
                                        shape = RoundedCornerShape(10.dp),
                                        onClick = {
                                            val mail = omitMail(email.value)
                                            listViewModel.saveListTypeString(mail)
                                            ProductRepository.key = if (listViewModel.loadListType()) "Shared" else mail
                                            login(
                                                email.value,
                                                password.value,
                                                applicationContext,
                                                auth
                                            )

                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = optionsViewModel.returnColorOption(),
                                            contentColor = Color.White
                                        )
                                    ) {
                                        Text(
                                            text = AnnotatedString("Sign up"),
                                            fontSize = 16.sp,
                                            fontFamily = interFamily,
                                            fontWeight = FontWeight.Medium,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .padding(4.dp)
                                        )
                                    }
                                    ClickableText(
                                        modifier = Modifier.padding(top = 80.dp),
                                        text = buildAnnotatedString {
                                            withStyle(style = SpanStyle(color = Color(0xFF9396AA))) {
                                                append("Already have an account?! ")
                                            }
                                            withStyle(
                                                style = SpanStyle(
                                                    color = optionsViewModel.returnColorOption(),
                                                    textDecoration = TextDecoration.Underline
                                                )
                                            ) {
                                                append("Sign up")
                                                addStringAnnotation(
                                                    "Activity",
                                                    "RegisterActivity",
                                                    start = 0,
                                                    end = 6
                                                )
                                            }
                                        },
                                        onClick = {
                                            val intent = Intent(
                                                applicationContext,
                                                RegisterActivity::class.java
                                            )
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                            application.startActivity(intent)
                                        }
                                    )
                                }
                            }
                        },
                    ) { scaffoldPadding ->
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(
                                    start = 16.dp,
                                    end = 16.dp,
                                    top = scaffoldPadding.calculateTopPadding() + 16.dp,
                                    bottom = scaffoldPadding.calculateBottomPadding() + 16.dp
                                )
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0xFFFFFFFF)),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Top
                        ) {
                            LoginComp(email, password, checkBoxState)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginComp(email: MutableState<String>, password: MutableState<String>, checkBoxState: MutableState<Boolean>) {

    Text(
        text = "Sign in",
        fontFamily = interFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        modifier = Modifier.padding(top = 40.dp)
    )
    Text(
        text = "Sign in with your email and password\n" +
                "to continue",
        fontFamily = interFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(top = 16.dp)
    )
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(350.dp)
            .padding(top = 48.dp, start = 16.dp, end = 16.dp)
    ) {
        Text(
            modifier = Modifier.align(Alignment.Start),
            text = "Email",
            fontSize = 14.sp
        )
        OutlinedTextField(
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedTextColor = Color.Black
            ),
            textStyle = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF9396AA)
            ),
            value = email.value,
            onValueChange = { text ->
                email.value = text
            },
            placeholder = {
                Text(
                    text = "Email",
                    color = Color.Black
                )
            }
        )
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(350.dp)
            .padding(top = 32.dp, start = 16.dp, end = 16.dp)
    ) {
        Text(
            modifier = Modifier.align(Alignment.Start),
            text = "Password",
            fontSize = 14.sp
        )
        OutlinedTextField(
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedTextColor = Color.Black
            ),
            textStyle = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF9396AA)
            ),
            value = password.value,
            onValueChange = { text ->
                password.value = text
            },
            placeholder = {
                Text(
                    text = "Password",
                    color = Color.Black
                )
            },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        )
    }
    Row(
        modifier = Modifier
            .padding(top = 16.dp)
            .width(350.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(contentAlignment = Alignment.CenterStart) {
            Checkbox(
                checked = checkBoxState.value,
                onCheckedChange = { checkBoxState.value = it })
            Text(
                modifier = Modifier.padding(start = 40.dp),
                text = "Remember me",
                fontSize = 14.sp
            )
        }
        ClickableText(
            text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = Color(235, 87, 87),
                        fontSize = 14.sp
                    )
                ) {
                    append("Forgot password?")
                }
            },
            onClick = {
                //TODO
            }
        )
    }
}

fun omitMail(email: String): String {
    return email.substringBefore("@")
}

fun login(email: String, password: String, context: Context, auth: FirebaseAuth) {
    val inWelAct = Intent(context, MainActivity::class.java)
    auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
        if (it.isSuccessful) {
            inWelAct.putExtra("user_mail", auth.currentUser?.email)
            inWelAct.putExtra("user", auth.currentUser)
            inWelAct.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(inWelAct)
        } else {
            Toast.makeText(context, "Log-in failed.", Toast.LENGTH_SHORT).show()
        }
    }
}