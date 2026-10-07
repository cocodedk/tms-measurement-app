package com.cocode.tmsmeasurement.ui.about

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.cocode.tmsmeasurement.BuildConfig
import com.cocode.tmsmeasurement.R

/**
 * The About page, in the cocode-apps standard order: name and version with "Check for updates",
 * what the app does, privacy, links, credits and licences, made by Cocode, support.
 */
@Composable
internal fun AboutScreen(innerPadding: PaddingValues) {
    val context = LocalContext.current
    val open = { link: AboutLink -> openLink(context, aboutUrl(link)) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AboutSection(R.string.app_name) {
            Text(
                text = stringResource(
                    R.string.about_version,
                    BuildConfig.VERSION_NAME,
                    BuildConfig.VERSION_CODE
                ),
                style = MaterialTheme.typography.bodyMedium
            )
            Button(
                onClick = { open(AboutLink.Updates) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.about_check_updates))
            }
        }
        AboutSection(R.string.about_what_title) {
            AboutBody(R.string.about_body)
        }
        AboutSection(R.string.about_privacy_title) {
            AboutBody(R.string.about_privacy)
            AboutBody(R.string.about_privacy_permissions)
            AboutButton(R.string.about_privacy_link) { open(AboutLink.Privacy) }
        }
        AboutSection(R.string.about_links_title) {
            AboutButton(R.string.about_website) { open(AboutLink.Website) }
            AboutButton(R.string.about_source) { open(AboutLink.Source) }
            AboutButton(R.string.about_report) { open(AboutLink.Issues) }
        }
        AboutSection(R.string.about_credits) {
            AboutBody(R.string.about_credits_body)
        }
        AboutSection(R.string.about_made_by) {
            AboutBody(R.string.about_made_by_contact)
        }
        // Support: reserved for the Support phase (standard/support.md). Nothing is shown until then.
    }
}

/** One About section: a card whose title is a TalkBack heading. */
@Composable
private fun AboutSection(@StringRes title: Int, content: @Composable ColumnScope.() -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = stringResource(title),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.semantics { heading() }
            )
            content()
        }
    }
}

@Composable
private fun AboutBody(@StringRes text: Int) {
    Text(text = stringResource(text), style = MaterialTheme.typography.bodyMedium)
}

@Composable
private fun AboutButton(@StringRes label: Int, onClick: () -> Unit) {
    OutlinedButton(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Text(stringResource(label))
    }
}
