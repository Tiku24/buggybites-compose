# BuggyBites

A small Jetpack Compose food-order practice app created for a developer with about six months of internship experience.

The app is intentionally usable but has a beginner-level bug backlog. Start with the GitHub Issues labelled `good first bug`, then move up through `level: 2` and `level: 3`. Each issue contains symptoms, expected behaviour, and a small acceptance checklist.

## What you will practice

- Compose state and recomposition
- Lists, filtering, and stable item identity
- Form validation
- Material 3 theming
- Writing small ViewModel unit tests

## Run it

1. Open this folder in the latest stable Android Studio.
2. Allow the Gradle sync to complete.
3. Run the `app` configuration on an emulator or device running Android 8.0 (API 26) or newer.

No API key or backend is needed. Data resets whenever the app process is restarted.

## Suggested learning order

1. Reproduce the issue and write down what happened.
2. Find the smallest relevant composable or ViewModel function.
3. Fix one behaviour at a time.
4. Add a focused test once you know the desired result.
5. Open a pull request that references the issue, for example `Fixes #1`.

## Intentional-bug policy

The initial bugs are product-behaviour bugs, not build-breaking mistakes. The app should compile and give you a safe debugging playground. Do not read the issue labels/descriptions as implementation instructions—use them as a bug report, just like a real team would.
