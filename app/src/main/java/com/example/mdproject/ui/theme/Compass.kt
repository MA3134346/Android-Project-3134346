package com.example.mdproject.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.mdproject.R

@Preview(showBackground = true)
@Composable
fun Compass() {
    Box(modifier = Modifier.fillMaxWidth(), Alignment.Center){
        DegreeText()
    }
    Box(modifier = Modifier.fillMaxSize(), Alignment.Center){
        CompassSprite()
    }
}
@Composable
fun DegreeText(){
    Text("Filled")
}
@Composable
fun CompassSprite() {
    Image(
        painter = painterResource(R.drawable.compass),
        contentDescription = "Compass",
    )
}
