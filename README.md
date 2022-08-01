# MarqueeTextCompose

Marquee text for jetpack compose.

Inspired by: https://stackoverflow.com/a/68980032/9819094

Marquee text effect hasn't been implemented in jetpack compose yet.

This is Sample movie and code.

https://user-images.githubusercontent.com/1423942/182198918-79262721-8661-43a0-864e-0b1af3fd3f34.mp4

```kotlin
MarqueeText(
    modifier = Modifier.align(Alignment.CenterStart),
    durationMs = 3000,
    delayMs = 2000,
    spaceRatio = 0.2f,
    text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
)
```
