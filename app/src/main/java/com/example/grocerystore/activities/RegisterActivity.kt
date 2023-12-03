package com.example.grocerystore.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.example.grocerystore.ui.theme.GroceryStoreTheme
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            auth = FirebaseAuth.getInstance()
            GroceryStoreTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RegisterComp(applicationContext, auth)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterComp(context: Context, auth: FirebaseAuth) {
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(value = email.value,
            onValueChange = { text ->
                email.value = text
            },
            placeholder = { Text("Email") })

        TextField(
            value = password.value,
            onValueChange = { text ->
                password.value = text
            },
            placeholder = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        )
        Button(onClick = {
            register(email.value, password.value, context, auth)
        }) {
            Text(text = "Register")
        }
    }
}

fun register(email: String, password: String, context: Context, auth: FirebaseAuth) {
    val inWelAct = Intent(context, LoginActivity::class.java)
    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { registrationTask ->
        if (registrationTask.isSuccessful) {
            inWelAct.putExtra("user_mail", auth.currentUser?.email)
            inWelAct.putExtra("user", auth.currentUser)
            inWelAct.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(inWelAct)
        } else {
            Toast.makeText(context, "Registration failed.", Toast.LENGTH_SHORT).show()
        }
    }
}
