package com.cocode.tmsmeasurement.ui.about

import org.junit.Assert.assertEquals
import org.junit.Test

/** Pure tests for [aboutUrl]: every About-screen link, both update targets, and the site's language pages. */
class AboutLinksTest {

    @Test
    fun `updates open the latest GitHub release until the app is live on F-Droid`() {
        assertEquals(
            "https://github.com/cocodedk/tms-measurement-app/releases/latest",
            aboutUrl(AboutLink.Updates, "en", fdroidLive = false)
        )
    }

    @Test
    fun `updates open the F-Droid page once the app is live there`() {
        assertEquals(
            "https://f-droid.org/packages/com.cocode.tmsmeasurement/",
            aboutUrl(AboutLink.Updates, "en", fdroidLive = true)
        )
    }

    @Test
    fun `the default update target follows the FDROID_LIVE flag`() {
        assertEquals(aboutUrl(AboutLink.Updates, "en", FDROID_LIVE), aboutUrl(AboutLink.Updates, "en"))
    }

    @Test
    fun `privacy opens the English policy in English`() {
        assertEquals("https://tms.cocode.dk/privacy/", aboutUrl(AboutLink.Privacy, "en"))
    }

    @Test
    fun `privacy opens the Danish policy in Danish`() {
        assertEquals("https://tms.cocode.dk/da/privacy/", aboutUrl(AboutLink.Privacy, "da"))
    }

    @Test
    fun `privacy falls back to the English policy in a language the site lacks`() {
        for (language in listOf("fa", "ar", "zh", "de")) {
            assertEquals("https://tms.cocode.dk/privacy/", aboutUrl(AboutLink.Privacy, language))
        }
    }

    @Test
    fun `website opens the English site in English`() {
        assertEquals("https://tms.cocode.dk/", aboutUrl(AboutLink.Website, "en"))
    }

    @Test
    fun `website opens the Danish site in Danish`() {
        assertEquals("https://tms.cocode.dk/da/", aboutUrl(AboutLink.Website, "da"))
    }

    @Test
    fun `website falls back to the English site in a language the site lacks`() {
        for (language in listOf("fa", "ar", "zh", "de")) {
            assertEquals("https://tms.cocode.dk/", aboutUrl(AboutLink.Website, language))
        }
    }

    @Test
    fun `source opens the GitHub repository in every language`() {
        for (language in listOf("en", "da", "fa")) {
            assertEquals("https://github.com/cocodedk/tms-measurement-app", aboutUrl(AboutLink.Source, language))
        }
    }

    @Test
    fun `report a problem opens the GitHub issues in every language`() {
        for (language in listOf("en", "da", "fa")) {
            assertEquals(
                "https://github.com/cocodedk/tms-measurement-app/issues",
                aboutUrl(AboutLink.Issues, language)
            )
        }
    }
}
