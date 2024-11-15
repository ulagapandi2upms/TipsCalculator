package com.ulaga

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ulaga.Util.calculateTipAmount
import com.ulaga.Util.calculateTotalPerPerson
import com.ulaga.components.InputField
import com.ulaga.ui.theme.SimpleAppTheme
import com.ulaga.widgets.RoundedIconButton

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp {
                Column {
                    BillForm()
                }
            }
        }
    }
}

@Composable
fun MyApp(content: @Composable () -> Unit) {
    SimpleAppTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            content()
        }
    }
}


@Composable
fun TopHeader(totalPerPerson: MutableState<String>) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(all = 12.dp)
            .clip(shape = CircleShape.copy(all = CornerSize(12.dp))), color = Color(0xFFE9D7F7)

    ) {
        Column(
            Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            var total = ""
            if (totalPerPerson.value.isNotEmpty()) {
                total = "%.2f".format(totalPerPerson.value.toDouble())
            }
            Text(
                text = "Total Per Person",
                style = MaterialTheme.typography.titleMedium,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = "$$total",
                style = MaterialTheme.typography.displayMedium,
                color = Color.Black,
                fontWeight = FontWeight.ExtraBold,
            )
        }
    }


}

@Preview
@Composable
fun BillForm(
    modifier: Modifier = Modifier,
) {
    val totalBillState = remember { mutableStateOf("") }
    val sliderPositionState = remember {
        mutableIntStateOf(0)
    }
    val splitByState = remember {
        mutableIntStateOf(1)
    }
    val tipAmountState = remember {
        mutableDoubleStateOf(0.0)
    }
    val totalBillPerPersonState = remember { mutableStateOf("") }
    val validState = remember(key1 = totalBillState.value) {
        totalBillState.value.trim().isNotEmpty()
    }
    Log.d("TAG", "BillForm: $validState")
    val keyBoardController = LocalSoftwareKeyboardController.current

    TopHeader(totalBillPerPersonState)
    Surface(
        modifier = Modifier
            .padding(2.dp)
            .fillMaxWidth()
            .padding(12.dp),
        shape = RoundedCornerShape(corner = CornerSize(size = 8.dp)),
        border = BorderStroke(width = 1.dp, color = Color.LightGray)
    ) {
        Column(
            modifier = Modifier.padding(6.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            InputField(
                valueState = totalBillState,
                labelId = "Enter Bill",
                enabled = true,
                isSingleLine = true,
                onAction = KeyboardActions {
                    Log.d("TAG", "BillForm: $validState")
                    if (!validState) return@KeyboardActions
                    totalBillPerPersonState.value = calculateTotalPerPerson(
                        totalBill = totalBillState.value.toDouble(),
                        tipPercentage = sliderPositionState.intValue,
                        splitBy = splitByState.intValue
                    ).toString()

                    keyBoardController?.hide()
                },
            )

            if (validState) {
                Row(
                    modifier = Modifier.padding(vertical = 3.dp, horizontal = 16.dp),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = "Split",
                        modifier = Modifier.align(
                            alignment = Alignment.CenterVertically,
                        ),
                    )
                    Spacer(modifier = Modifier.width(120.dp))
                    Row(
                        modifier = Modifier.padding(horizontal = 3.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        RoundedIconButton(imageVector = Icons.Default.Remove, onClick = {
                            if (splitByState.intValue != 1) {
                                splitByState.intValue--
                            }
                            totalBillPerPersonState.value = calculateTotalPerPerson(
                                totalBill = totalBillState.value.toDouble(),
                                tipPercentage = sliderPositionState.intValue,
                                splitBy = splitByState.intValue
                            ).toString()
                        })
                        Text(
                            text = "${splitByState.intValue}",
                            modifier = Modifier
                                .padding(horizontal = 10.dp)
                                .align(Alignment.CenterVertically),
                            style = MaterialTheme.typography.bodyLarge,
                        )
                        RoundedIconButton(imageVector = Icons.Default.Add, onClick = {
                            splitByState.intValue++
                            totalBillPerPersonState.value = calculateTotalPerPerson(
                                totalBill = totalBillState.value.toDouble(),
                                tipPercentage = sliderPositionState.intValue,
                                splitBy = splitByState.intValue
                            ).toString()
                            tipAmountState.doubleValue = calculateTipAmount(
                                totalBillState.value.toDouble(), sliderPositionState.intValue
                            )
                        })
                    }

                }

                //Tip
                Row(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                    Text(
                        text = "Tip",
                        modifier = Modifier.align(alignment = Alignment.CenterVertically)
                    )
                    Spacer(modifier = Modifier.width(180.dp))
                    Text(
                        text = "$${"%.2f".format(tipAmountState.doubleValue)}",
                        modifier = Modifier.align(alignment = Alignment.CenterVertically)
                    )
                }

                //Slider
                Row(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                    Text(
                        text = "${sliderPositionState.intValue}%",
                        modifier = Modifier
                            .align(alignment = Alignment.CenterVertically)
                            .padding(end = 12.dp)
                    )
                    Slider(value = sliderPositionState.intValue.toFloat(), onValueChange = {
                        sliderPositionState.intValue = it.toInt()
                        tipAmountState.doubleValue = calculateTipAmount(
                            totalBillState.value.toDouble(), sliderPositionState.intValue
                        )

                        totalBillPerPersonState.value = calculateTotalPerPerson(
                            totalBill = totalBillState.value.toDouble(),
                            tipPercentage = sliderPositionState.intValue,
                            splitBy = splitByState.intValue
                        ).toString()
                    }, valueRange = 0f..100f)
                }
            } else {
                Box { }
            }
        }
    }
}

