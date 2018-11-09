# GesturePack
A set of Gesture enabled UIs for Android

[![](https://jitpack.io/v/clockvoid/GesturePack.svg)](https://jitpack.io/#clockvoid/GesturePack)

![](https://i.imgur.com/ZROm1RE.gif)

## Description
This is an Android library to make UI with gestures.

There are some gestures in this library, and you can use View structure with gesture implemented.

## Installation
Add these code in your `build.gradle`.

```groovy
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}

dependencies {
    implementation 'com.github.clockvoid:GesturePack:Tag'
}
```

## Usage
For now, I implement a gesture for `FrameLayout` that swipe horizontally to back and swipe vertically to back

To use vertical type gesture, you have to implement `VerticalDraggableFrameLayout` on vertical scrollable view like `NestedScrollView`.
And implement your view in the the `NestedScrollView`.

To use horizontal type gesture, you have to implement `HorizontalDraggableFrameLayout` on horizontal scrollable view like `RecyclerView`.
