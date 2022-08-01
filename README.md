# MarqueeTextCompose

Marquee text for jetpack compose.

Inspired by: https://stackoverflow.com/a/68980032/9819094

Marquee text effect hasn't been implemented in jetpack compose yet.

Sample movie and code is here.

https://user-images.githubusercontent.com/1423942/182200420-e0946f2f-5bf6-4c05-a90a-71918e36c74e.mp4

```kotlin
MarqueeText(
    modifier = Modifier.align(Alignment.CenterStart),
    durationMs = 3000,
    delayMs = 2000,
    spaceRatio = 0.2f,
    text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
)
```
