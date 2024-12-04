package kz.bloom.ui.subscription.create_subscription.content

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import kz.bloom.R
import kz.bloom.ui.subscription.create_subscription.component.CreateSubscriptionComponent
import kz.bloom.ui.ui_components.LabeledTextField
import kz.bloom.ui.ui_components.PrimaryButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateSubscriptionContent(
    modifier: Modifier = Modifier,
    component: CreateSubscriptionComponent
) {
    val model = component.model.subscribeAsState()

    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val focusManager = LocalFocusManager.current

    var showSheet by remember { mutableStateOf(false) }


    Box(modifier = modifier) {
        if (showSheet) {
            ModalBottomSheet(
                onDismissRequest = { showSheet = false },
                sheetState = bottomSheetState,
                content = {
                    Column(modifier = Modifier) {
                        Spacer(modifier = Modifier.height(21.dp))
                        Column(
                            verticalArrangement = Arrangement.spacedBy(19.5.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            HorizontalDivider()
                        }
                        model.value.subscriptionType.forEach {
                            Box(modifier = Modifier.clickable {
                                component.chosenComposition(it.name)
                                showSheet = false
                            }) {
                                Text(
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                        .padding(top = 19.5.dp, bottom = 19.5.dp),
                                    text = it.name,
                                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 14.sp),
                                    color = MaterialTheme.colorScheme.secondary
                                )
                                HorizontalDivider(modifier = Modifier.align(Alignment.BottomCenter))
                            }
                        }
                    }
                }
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary)
                .padding(top = 42.dp)
                .padding(horizontal = 21.dp)
        ) {
            Icon(
                modifier = Modifier.clickable { component.onNavigateBack() },
                painter = painterResource(id = R.drawable.ic_arrow_back_black),
                contentDescription = null
            )
            Column(
                modifier = Modifier.padding(top = 33.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Text(
                    style = MaterialTheme.typography.bodyLarge,
                    text = "СОЗДАТЬ ПОДПИСКУ"
                )
                Text(
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.secondary,
                    text = "Придумайте название и выбирайте тип из списка\nдля оформления подписки"
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            LabeledTextField(
                modifier = Modifier.padding(top = 4.dp),
                value = model.value.subscriptionName,
                textFieldStyle = MaterialTheme.typography.bodySmall,
                onValueChange = { component.fillSubName(name = it) },
                isError = model.value.subscriptionNameErrorText.isNotEmpty(),

                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                ),
                label = "НАЗВАНИЕ ПОДПИСКИ",
                placeholder = ""
            )

            if (model.value.subscriptionNameErrorText.isNotEmpty()) {
                Text(
                    text = model.value.subscriptionNameErrorText,
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 8.sp),
                    color = MaterialTheme.colorScheme.error
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Box(modifier = Modifier.clickable {
                showSheet = true
            }) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(11.dp)
                ) {
                    if (model.value.pickedSubscriptionName.isEmpty()) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "ТИП ПОДПИСКИ",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.secondary
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Icon(
                                modifier = Modifier
                                    .rotate(90f)
                                    .size(24.dp),
                                painter = painterResource(id = R.drawable.ic_expand_right_24),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.secondary
                            )
                        }
                        HorizontalDivider(
                            color = if (model.value.subscriptionTypeError.isNotEmpty()) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.secondary
                        )
                    } else {
                        Row(modifier = Modifier.padding(top = 26.dp)) {
                            Text(
                                text = model.value.pickedSubscriptionName,
                                style = MaterialTheme.typography.bodySmall
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Icon(
                                modifier = Modifier
                                    .rotate(90f)
                                    .size(24.dp),
                                painter = painterResource(id = R.drawable.ic_expand_right_24),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.secondary
                            )
                        }
                        HorizontalDivider(
                            color = if (model.value.subscriptionTypeError.isNotEmpty()) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.secondary
                        )
                    }
                    if (model.value.subscriptionTypeError.isNotEmpty()) {
                        Text(
                            text = model.value.subscriptionTypeError,
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 8.sp),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
            PrimaryButton(
                modifier = Modifier.padding(bottom = 21.dp, top = 35.dp),
                text = "СОЗДАТЬ ПОДПИСКУ",
                textStyle = MaterialTheme.typography.bodySmall,
                onClick = { component.createSub() }
            )
        }
    }
}