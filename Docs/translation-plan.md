# Multilingual plan (app + GitHub page)

## Scope
- Languages: Persian (fa), Arabic (ar), Traditional Chinese for Taiwan (zh-TW), plus existing English.
- Surfaces: Android app UI + GitHub Pages site at `docs/index.html`.

## App plan
1. Extract all UI text into `strings.xml`.
2. Add locale resources in `values-fa`, `values-ar`, and `values-zh-rTW`.
3. Keep RTL support enabled (`supportsRtl=true`) and verify alignment in RTL.
4. Localize date/number formatting using the device locale.
5. QA: switch device language to each locale and validate inputs, errors, and history.

## GitHub page plan
1. Add a language selector UI near the hero section.
2. Convert page copy, labels, and alt/aria text into a translation dictionary.
3. Update page text/attributes at runtime; set `lang` + `dir` for RTL languages.
4. Use fonts that support Arabic/Persian/Chinese scripts.
5. QA: verify layout and readability in RTL (fa/ar) and zh-TW.

## Implementation notes
- Keep formulas and units consistent (`cm`) across locales.
- Provide English fallback if a translation key is missing.
