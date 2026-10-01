package com.example.hw1

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.hw1.ui.theme.HW1Theme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            val name = stringResource(R.string.first_and_second_name)
            val group = stringResource(R.string.group)
            HW1Theme {
                Scaffold(
                    bottomBar = {
                        Button(
                            onClick = {
                                val intent = Intent(context, SecondActivity::class.java).apply {
                                    putExtra("NAME_AND_GROUP", name + "/" + group)
                                }

                                context.startActivity(intent)
                            },
                            modifier = Modifier.fillMaxWidth().padding(40.dp)
                        ) {Text("Продолжить")}
                    }
                ) {innerPadding ->
                    Column(
                        modifier = Modifier.fillMaxSize().padding(innerPadding),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = name)
                        Spacer(modifier = Modifier.padding(vertical = 16.dp))
                        Text(text = group)
                    }
                }
            }
        }
    }
}
