package com.cocode.tmsmeasurement.ui.about

import org.junit.Assert.assertEquals
import org.junit.Test

/** Pure tests for [aboutUrl]: every About-screen link, and both update targets. */
class AboutLinksTest {

    @Test
    fun `updates open the latest GitHub release until the app is live on F-Droid`() {
        assertEquals(
            "https://github.com/cocodedk/tms-measurement-app/releases/latest",
            aboutUrl(AboutLink.Updates, fdroidLive = false)
        )
    }

    @Test
    fun `updates open the F-Droid page once the app is live there`() {
        assertEquals(
            "https://f-droid.org/packages/com.cocode.tmsmeasurement/",
            aboutUrl(AboutLink.Updates, fdroidLive = true)
        )
    }

    @Test
    fun `the default update target follows the FDROID_LIVE flag`() {
        assertEquals(aboutUrl(AboutLink.Updates, FDROID_LIVE), aboutUrl(AboutLink.Updates))
    }

    @Test
    fun `privacy opens the privacy policy page`() {
        assertEquals("https://tms.cocode.dk/privacy/", aboutUrl(AboutLink.Privacy))
    }

    @Test
    fun `website opens the app site`() {
        assertEquals("https://tms.cocode.dk", aboutUrl(AboutLink.Website))
    }

    @Test
    fun `source opens the GitHub repository`() {
        assertEquals("https://github.com/cocodedk/tms-measurement-app", aboutUrl(AboutLink.Source))
    }

    @Test
    fun `report a problem opens the GitHub issues`() {
        assertEquals(
            "https://github.com/cocodedk/tms-measurement-app/issues",
            aboutUrl(AboutLink.Issues)
        )
    }
}
