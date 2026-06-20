package com.cocode.tmsmeasurement.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.cocode.tmsmeasurement.R
import com.cocode.tmsmeasurement.ui.common.LanguageOption
import com.cocode.tmsmeasurement.ui.common.SYSTEM_LANGUAGE_TAG

@Composable
internal fun SettingsScreen(
    innerPadding: PaddingValues,
    currentLanguageTag: String,
    onLanguageSelected: (String) -> Unit
) {
    val languageOptions = listOf(
        LanguageOption(SYSTEM_LANGUAGE_TAG, R.string.language_system),
        LanguageOption("en", R.string.language_english),
        LanguageOption("fa", R.string.language_persian),
        LanguageOption("ar", R.string.language_arabic),
        LanguageOption("zh-TW", R.string.language_chinese_taiwan)
    )
    var selectedTag by rememberSaveable(currentLanguageTag) { mutableStateOf(currentLanguageTag) }
    val hasChanges = selectedTag != currentLanguageTag

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(horizontal = 16.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = stringResource(R.string.settings_language_title),
                    style = MaterialTheme.typography.titleMedium
                )
                languageOptions.forEach { option ->
                    val isSelected = selectedTag == option.tag
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectedTag = option.tag
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = isSelected,
                            onClick = {
                                selectedTag = option.tag
                            }
                        )
                        Text(
                            text = stringResource(option.labelRes),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
                Button(
                    onClick = { onLanguageSelected(selectedTag) },
                    enabled = hasChanges,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.button_apply_language))
                }
            }
        }
    }
}
