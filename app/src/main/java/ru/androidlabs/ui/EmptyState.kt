package ru.androidlabs.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.androidlabs.R

@Composable
fun EmptyState(message: String, modifier: Modifier = Modifier, internetError: Boolean = false) {
    Column(modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Box(Modifier.size(120.dp)) {
            if (internetError) {
                Image(painterResource(R.drawable.ic_internet), null, Modifier.size(120.dp))
            } else {
                Image(painterResource(R.drawable.ic_empty_note), null, Modifier.offset(9.dp, 3.dp).size(102.dp, 94.dp))
                Image(painterResource(R.drawable.ic_empty_face), null, Modifier.offset(37.dp, 28.dp).size(69.dp))
                Image(painterResource(R.drawable.ic_empty_expression), null, Modifier.offset(37.dp, 28.dp).size(69.dp))
                Image(painterResource(R.drawable.ic_empty_stem), null, Modifier.offset(80.dp, 6.dp).size(31.dp, 110.dp))
            }
        }
        Spacer(Modifier.height(16.dp))
        Text(message, Modifier.padding(horizontal = 24.dp), fontSize = 19.sp, fontWeight = FontWeight.Medium, textAlign = TextAlign.Center)
    }
}
