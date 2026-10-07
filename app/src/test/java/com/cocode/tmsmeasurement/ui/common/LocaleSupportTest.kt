package com.cocode.tmsmeasurement.ui.common

import org.junit.Assert.assertEquals
import org.junit.Test

/** Pure tests for [normalizeLanguageTag], which maps the app's stored locales to picker tags. */
class LocaleSupportTest {

    @Test
    fun `Danish maps to da`() {
        assertEquals("da", normalizeLanguageTag("da"))
    }

    @Test
    fun `Danish with a region maps to da`() {
        assertEquals("da", normalizeLanguageTag("da-DK"))
    }

    @Test
    fun `only the first of several tags counts`() {
        assertEquals("da", normalizeLanguageTag("da-DK,en-US"))
    }

    @Test
    fun `a three-letter language that merely starts with da is not Danish`() {
        assertEquals(SYSTEM_LANGUAGE_TAG, normalizeLanguageTag("dag"))
    }

    @Test
    fun `existing languages still map to their tags`() {
        assertEquals("en", normalizeLanguageTag("en-GB"))
        assertEquals("fa", normalizeLanguageTag("fa-IR"))
        assertEquals("ar", normalizeLanguageTag("ar"))
        assertEquals("zh-TW", normalizeLanguageTag("zh-Hant-TW"))
    }

    @Test
    fun `blank and unknown tags fall back to the system language`() {
        assertEquals(SYSTEM_LANGUAGE_TAG, normalizeLanguageTag(""))
        assertEquals(SYSTEM_LANGUAGE_TAG, normalizeLanguageTag("sv"))
    }
}
