package com.example.travelapp.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DropdownFilterMenu(
    modifier: Modifier = Modifier,
    type: String,
    distance: Int,
    onSelect: (String) -> Unit,
    selectDistance: (Int) -> Unit
) {
    val selectedButton = remember { mutableStateOf(type) }
    val sliderPosition = remember { mutableIntStateOf(distance) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .wrapContentHeight()
            .background(Color(0x00000000)),
        contentAlignment = Alignment.CenterEnd
    ) {
        Column {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(top = 72.dp, end = 80.dp, bottom = 0.dp, start = 16.dp)
                    .height(32.dp)
                    .background(
                        Color(0xFF000000),
                        shape = RoundedCornerShape(
                            topStart = 8.dp,
                            bottomStart = 8.dp
                        )
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = modifier.width(5.dp))

                Box(
                    modifier = modifier
                        .weight(1f)
                        .height(24.dp)
                        .background(
                            color = if (selectedButton.value == "tourismPlaces") Color.White else Color.Black,
                            shape = RoundedCornerShape(8.dp)
                        )

                        .clickable {
                            selectedButton.value = "tourismPlaces"; onSelect("tourismPlaces")
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Tourism",
                        color = if (selectedButton.value == "tourismPlaces") Color.Black else Color.White,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = modifier.width(5.dp))

                Box(
                    modifier = modifier
                        .weight(1f)
                        .height(24.dp)
                        .background(
                            color = if (selectedButton.value == "historicPlaces") Color.White else Color.Black,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable {
                            selectedButton.value = "historicPlaces"; onSelect("historicPlaces")
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Historic",
                        color = if (selectedButton.value == "historicPlaces") Color.Black else Color.White,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = modifier.width(5.dp))

                Box(
                    modifier = modifier
                        .weight(1f)
                        .height(24.dp)
                        .background(
                            color = if (selectedButton.value == "museumAndArcPlaces") Color.White else Color.Black,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable {
                            selectedButton.value =
                                "museumAndArcPlaces"; onSelect("museumAndArcPlaces")
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Museum",
                        color = if (selectedButton.value == "museumAndArcPlaces") Color.Black else Color.White,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Column(modifier.background(color = Color(0xFFF0F0F0)))
            {
                Text(
                    modifier = modifier.padding(
                        top = 8.dp,
                        end = 0.dp,
                        bottom = 0.dp,
                        start = 24.dp
                    ),
                    text = "Distance Value: ${sliderPosition.intValue}",
                    style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold)
                )
                Slider(
                    value = sliderPosition.intValue.toFloat(),
                    onValueChange = { newValue ->
                        sliderPosition.intValue = newValue.toInt()
                        selectDistance(newValue.toInt())
                    },
                    valueRange = 500f..20000f,
                    steps = 38,
                    modifier = modifier.padding(
                        top = 0.dp,
                        end = 16.dp,
                        bottom = 0.dp,
                        start = 16.dp
                    )
                )
            }
        }
    }

}