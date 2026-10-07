package com.cocode.tmsmeasurement.ui.about

import com.cocode.tmsmeasurement.BuildConfig

enum class AboutLink { Updates, Website, Privacy, Source, Issues }

/**
 * Flip to true once the app is live on F-Droid (apps.yml in cocodedk/cocode-apps says
 * `fdroid: live`). Until then "See the latest version" opens the latest GitHub release.
 */
const val FDROID_LIVE = false

private const val SITE = "https://tms.cocode.dk"
private const val REPO = "https://github.com/cocodedk/tms-measurement-app"

/**
 * Languages the site has both a home page and a privacy page for, at `<site>/<code>/` and
 * `<site>/<code>/privacy/`. Persian, Arabic and Chinese are switched on the English home page
 * (`?lang=`) and have no pages of their own, so they stay on the English pages.
 */
private val SITE_LANGUAGES = setOf("da")

private fun sitePage(language: String, path: String = ""): String =
    if (language in SITE_LANGUAGES) "$SITE/$language/$path" else "$SITE/$path"

/**
 * The address each About-screen link opens. The website and privacy links follow [language] (a code
 * such as "da" from the app's current locale) and open the English pages when the site has none in
 * that language. The app itself never fetches any of them.
 */
fun aboutUrl(link: AboutLink, language: String, fdroidLive: Boolean = FDROID_LIVE): String = when (link) {
    AboutLink.Updates ->
        if (fdroidLive) "https://f-droid.org/packages/${BuildConfig.APPLICATION_ID}/"
        else "$REPO/releases/latest"
    AboutLink.Website -> sitePage(language)
    AboutLink.Privacy -> sitePage(language, "privacy/")
    AboutLink.Source -> REPO
    AboutLink.Issues -> "$REPO/issues"
}
