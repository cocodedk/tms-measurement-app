Transcranial Magnetic Stimulation Measurement App – Product Requirement Document (PRD)
Overview

The goal of this project is to build an Android application that reproduces the Beam F3 heuristic used for transcranial magnetic stimulation (TMS) targeting. It will compute and save measurements based on three simple head distances (tragus–tragus, nasion–inion and head circumference) so that clinicians can quickly locate the F3 position (left dorsolateral prefrontal cortex) without a neuronavigation system. The app also retains results for each client to support clinical documentation and follow‑up.

Background and Scientific Basis

The Beam F3 method is a simplified scalp‑based heuristic for locating the F3 EEG position, which corresponds to the typical coil placement for TMS treatment of major depressive disorder. Researchers have shown that two geometric parameters accurately approximate the F3 location when expressed as percentages of standard head measurements:

Circumferential arc (X) – the distance along the head circumference from the midline (FPz–Oz plane) to the F3 plane. In a cohort of 100 MRIs, this arc consistently represented about 11.54 % of the head circumference measured through FPz–Oz.

Radial arc (Y) – the distance from the vertex (Cz) to the F3 point. When expressed relative to head dimensions, Y averaged 26.37 % of the mean of the nasion–inion and left‑to‑right tragus distances. MRI‑guided measurements showed that the MRI‑guided Y distance was on average 0.35 cm longer than the Beam F3 estimate; adding this adjustment (≈3.63 %) improved concordance.

The PRD leverages these findings to compute the coordinates used by clinicians. The calculation method is also consistent with updated scalp heuristics recommended by Mir‑Moghtadaei and colleagues (2015) and with web implementations of the Beam F3 program.

Calculation Method

Given three inputs — tragus–tragus distance (TTT), nasion–inion distance (NI) and head circumference (HC) — the app performs the following calculations:

Average anterior–posterior/mediolateral distance –

Avg
=
TTT
+
NI
2
Avg=
2
TTT+NI
	​


Circumferential distance from midline (X) –
The F3 plane lies approximately 11.54 % of the head circumference from the midline, so:

𝑋
=
0.1154
×
HC
X=0.1154×HC

Radial distance from vertex (Y) –
The radial distance from Cz to F3 is roughly 26.37 % of the average of the TTT and NI distances:

𝑌
=
0.2637
×
Avg
Y=0.2637×Avg

Adjusted radial distance (Yₐd) –
MRI studies have shown that Beam F3 underestimates the radial distance by about 0.35 cm. To improve concordance with MRI‑guided neuronavigation, an adjustment is added:

𝑌
adj
=
𝑌
+
0.35
 
cm
Y
adj
	​

=Y+0.35cm

These computations yield two key values (X and Yₐd). Clinicians use them together with the head measurements to mark the F3 location: measure X cm along the circumference from the midline (toward the left ear), identify a line from the vertex through the pre‑auricular point on the left, and measure Yₐd cm along that line to locate F3. The app displays X and Y and clarifies that Yₐd is an adjustment recommended by Mir‑Moghtadaei et al. (2015).

Functional Requirements
Data Input

Client identification –
Users can enter a client’s name or identifier. This associates saved measurements with the correct individual.

Head measurements –
Provide input fields (numeric with unit “cm”) for:

Tragus to Tragus (TTT)

Nasion to Inion (NI)

Head Circumference (HC)

Validation –
The app validates that all measurement fields contain positive numeric values before calculations are performed. It also alerts the user if the client name is empty.

Calculation & Display

Compute F3 distances –
Upon pressing Calculate, the app computes X, Y and Yₐd using the formulas above. Calculated values are rounded to two decimal places.

Results panel –
The app displays a summary such as:

“Distance along circumference (X) = 6.62 cm; Distance from vertex (Y) = 9.60 cm; Adjusted Y = 9.95 cm.”

Informational note –
A note explaining that Yₐd includes a 0.35 cm adjustment to better match MRI‑guided targeting should accompany the results. This ensures users understand the difference between unadjusted and adjusted values.

Data Storage & Retrieval

Persist measurements –
After calculation, the app saves the measurement record (client identifier, input values, computed X, Y and Yₐd, and a timestamp) to on‑device storage. Multiple records per client are allowed.

Local storage –
Use a persistent mechanism such as SharedPreferences (with JSON serialization) or SQLite/Room for larger data volumes. Sensitive data are stored locally; the app does not transmit personal data off the device.

History view –
A scrollable list shows previous measurements sorted by most recent. Each item displays the client name, date/time, and computed distances. Selecting a record reveals the full details.

Editing & deletion –
The app allows deletion of individual records. Editing past measurements can optionally be supported but is not mandatory for the initial release.

Optional Enhancements

Unit conversions –
Provide a toggle between centimeters and inches. Conversion should happen automatically for both inputs and outputs.

Exporting data –
Allow users to export measurement history as a CSV or JSON file for backup or integration into electronic medical records.

User instructions & diagrams –
Include annotated diagrams showing how to measure the head distances and how to locate the F3 position using the computed X and Y values. The diagrams can be adapted from publicly available illustrations of the 10–20 EEG system.

Security –
Support biometric or pass‑code authentication before accessing saved measurements to protect patient data.

Cloud sync (future) –
Integrate optional encrypted cloud backup to allow clinicians to access measurement history across devices. This would require compliance with healthcare data protection regulations.

Non‑Functional Requirements

Performance –
Calculations must complete instantly upon user action. Data storage and retrieval operations should not noticeably delay UI interactions.

Usability –
The interface must be intuitive for clinicians with minimal training. Inputs should use numeric keyboards, and results should be clearly labelled. Error states (e.g., invalid input) should provide concise instructions.

Accessibility –
Follow Android accessibility guidelines, including large touch targets, appropriate contrast and support for screen readers.

Privacy –
All client data remain on the device by default. If optional cloud features are added, they must use secure, encrypted channels and abide by relevant privacy regulations (e.g., GDPR, HIPAA). No usage analytics are transmitted without explicit user consent.

Compatibility –
Support Android API 23 (Android 6.0) and above. The app should adapt gracefully to both phone and tablet screen sizes.

Acceptance Criteria

Accurate calculations –
When provided with sample measurements, the app must compute X and Y based on the formulas: X = 0.1154 × HC; Y = 0.2637 × ((TTT + NI)/2); Yₐd = Y + 0.35 cm. Results match manual calculations within rounding tolerance.

Data persistence –
Saved records persist across app restarts. Deleting the app data clears the measurement history.

Input validation –
The app does not perform calculations when any field is empty or non‑numeric and displays a user‑friendly error message.

History display –
Saved measurements are listed chronologically with client names and timestamps. Selecting a record shows full details (inputs and outputs).

No external network access –
The app operates entirely offline unless optional cloud functionality is explicitly enabled.

Conclusion

This PRD outlines the scope and specifications for an Android application that implements the Beam F3 TMS measurement method. By providing a simple interface for head measurements, accurately computing F3 coordinates based on published heuristics, and storing results per client, the app will streamline the workflow for clinicians performing TMS without relying on expensive neuronavigation systems. Future enhancements (unit conversions, exporting, secure storage and cloud sync) can be added iteratively once the core functionality meets the acceptance criteria.
