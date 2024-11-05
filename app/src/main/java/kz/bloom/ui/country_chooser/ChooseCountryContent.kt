package kz.bloom.ui.country_chooser

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import kz.bloom.R
import kz.bloom.ui.country_chooser.component.ChooseCountryComponent
import kz.bloom.ui.country_chooser.component.CountryModel
import kz.bloom.ui.ui_components.CustomTextField

@Composable
public fun ChooseCountryContent(
    modifier: Modifier = Modifier,
    component: ChooseCountryComponent
) {
    val model by component.model.subscribeAsState()

    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        focusManager.clearFocus()
                    }
                )
            }
            .padding(top = 16.dp)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(space = 16.dp)
    ) {
        Icon(
            modifier = Modifier
                .clip(shape = CircleShape)
                .clickable(onClick = { component.navigateBack() }),
            painter = painterResource(id = R.drawable.ic_arrow_back_black),
            contentDescription = null
        )

        Text(
            text = "ВЫБЕРИТЕ КОД СТРАНЫ",
            style = MaterialTheme.typography.bodyLarge
        )

        CustomTextField(
            value = model.query,
            onValueChange = { query ->
                component.filterCountries(
                    query = query
                )
            },
            placeholder = "ПОИСК",
            backgroundColor = MaterialTheme.colorScheme.surface,
            keyboardActions = KeyboardActions(
                onSearch = { focusManager.clearFocus() }
            ),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search,
                capitalization = KeyboardCapitalization.Words
            )
        )

        LazyColumn(contentPadding = PaddingValues(bottom = 32.dp)) {
            itemsIndexed(
                items = model.countries,
                key = { _, country -> "${country.name}${country.flagEmoji}" }
            ) { _, country ->
                Column {
                    CountryItem(
                        country = country,
                        onClick = { countryClicked ->
                            component.selectCountry(countryClicked)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun CountryItem(
    modifier: Modifier = Modifier,
    country: CountryModel,
    onClick: (CountryModel) -> Unit
) {
    Surface(
        modifier = modifier
            .clip(shape = RectangleShape)
            .clickable { onClick(country) },
        color = MaterialTheme.colorScheme.surface,
    ) {
        ProvideTextStyle(
            value = MaterialTheme.typography.labelSmall
                .copy(fontWeight = FontWeight.Normal)
        ) {
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 21.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        modifier = Modifier.padding(top = 5.dp).size(size = 24.dp),
                        text = country.flagEmoji
                    )

                    Text(
                        modifier = Modifier.widthIn(min = 32.dp),
                        text = country.dialCode,
                        maxLines = 1,
                        textAlign = TextAlign.End
                    )

                    Text(
                        modifier = Modifier,
                        text = country.name.uppercase(),
                    )
                }
                HorizontalDivider(modifier = Modifier.fillMaxWidth())
            }
        }
    }
}