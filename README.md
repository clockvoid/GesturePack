# GesturePack
A set of Gesture enabled UIs for Android

[![](https://jitpack.io/v/clockvoid/GesturePack.svg)](https://jitpack.io/#clockvoid/GesturePack)

## Description
Androidでジェスチャーを使用したUIを作成するためのライブラリです．

いくつかのジェスチャーが用意されており，自分の使いたいジェスチャーが実装されたView部品を自分のプロジェクトに追加するだけでジェスチャー対応のアプリができます．

## Installation
`build.gradle`に下記を追加してください．

```groovy
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}

dependencies {
    implementation 'com.github.clockvoid:GesturePack:0.1.0'
}
```

## Usage
今のところ，用意されているのは横方向にスワイプすると戻るジェスチャーと，縦方向にスワイプすると戻るジェスチャーの`FrameLayout`に対する実装です．

縦方向にスワイプするタイプでは，`VerticalDraggableFrameLayout`の中に`NestedScrollView`などの縦にスクロールするViewを入れ，その下に自分の使いたいViewを追加します．
このライブラリでは，縦のスクロールが可能なUIに対して，縦方向のジェスチャーをつけることができ，中のViewのスクロールが終わったら，ジェスチャーが開始します．

横方法にスワイプするタイプでは，`HorizontalDraggableFrameLayout`の中に`RecylerView`などの横方向のスクロールに対応したViewを入れ，その下に自分の使いたいViewを追加します．
