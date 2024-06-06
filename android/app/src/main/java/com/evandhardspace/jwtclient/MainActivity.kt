package com.evandhardspace.jwtclient

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.evandhardspace.jwtclient.ui.theme.JWTClientTheme

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JWTClientTheme {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .fillMaxSize(0.5f),
                    verticalArrangement = Arrangement.SpaceAround,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Form("Sign Up") { username, password ->
                        viewModel.signUp(username, password)
                    }
                    Divider()
                    Form("Sign In") { username, password ->
                        viewModel.signIn(username, password)
                    }
                }
            }
        }
    }
}

@Composable
fun Form(
    title: String,
    modifier: Modifier = Modifier,
    onSubmit: (username: String, password: String) -> Unit,
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(title)
        Spacer(Modifier.height(16.dp))
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = username,
            placeholder = { Text("Username")},
            onValueChange = { value -> username = value },
        )
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = password,
            placeholder = { Text("Password")},
            onValueChange = { value -> password = value },
        )
        Spacer(Modifier.height(4.dp))
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { onSubmit(username, password) },
        ) {
            Text("Submit")
        }
    }
}
