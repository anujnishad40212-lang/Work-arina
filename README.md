# TaskFlow

Native offline-first Android to-do list app. Kotlin + Jetpack Compose + Room + MVVM.

## Build

1. Open in Android Studio (Koala or newer) and let it sync, OR
2. From CLI: `./gradlew assembleRelease` (run `gradle wrapper` first if `gradlew` is missing the wrapper jar).

## Notes

- No internet permission, no third-party SDKs, no analytics.
- Min SDK 24, Target SDK 34.
- All data stored locally via Room.