package com.cocode.tmsmeasurement.ui.about

import com.cocode.tmsmeasurement.BuildConfig

enum class AboutLink { Updates, Website, Privacy, Source, Issues }

/**
 * Flip to true once the app is live on F-Droid (apps.yml in cocodedk/cocode-apps says
 * `fdroid: live`). Until then "See the latest version" opens the latest GitHub release.
 */
const val FDROID_LIVE = false

private const val SITE = "https://tms.cocode.dk"
private const val PRIVACY = "https://tms.cocode.dk/privacy/"
private const val REPO = "https://github.com/cocodedk/tms-measurement-app"

/** The address each About-screen link opens. The app itself never fetches any of them. */
fun aboutUrl(link: AboutLink, fdroidLive: Boolean = FDROID_LIVE): String = when (link) {
    AboutLink.Updates ->
        if (fdroidLive) "https://f-droid.org/packages/${BuildConfig.APPLICATION_ID}/"
        else "$REPO/releases/latest"
    AboutLink.Website -> SITE
    AboutLink.Privacy -> PRIVACY
    AboutLink.Source -> REPO
    AboutLink.Issues -> "$REPO/issues"
}
