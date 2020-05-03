<p align="center"><a href="https://ezralazuardy.com/orb" target="_blank" rel="noopener noreferrer"><img width="200" src="https://github.com/ezralazuardy/orb/blob/master/images/orb-logo.png" alt="orb logo"></a></p>

<p align="center">
  <a href="https://app.codacy.com/manual/ezralazuardy/orb?utm_source=github.com&utm_medium=referral&utm_content=ezralazuardy/orb&utm_campaign=Badge_Grade_Dashboard"><img src="https://api.codacy.com/project/badge/Grade/7506107b46da4faf84be2e8b555f75aa" alt="code quality" target="_blank" rel="noopener noreferrer"></a>
  <a href="https://codeclimate.com/github/ezralazuardy/orb/maintainability"><img src="https://api.codeclimate.com/v1/badges/3966ef5b3239786fc2b3/maintainability" alt="maintainability" target="_blank" rel="noopener noreferrer"></a>
  <a href="https://circleci.com/gh/ezralazuardy/orb"><img src="https://img.shields.io/circleci/build/github/ezralazuardy/orb" alt="build status" target="_blank" rel="noopener noreferrer"></a>
  <a href="https://jitpack.io/#ezralazuardy/orb"><img src="https://img.shields.io/github/v/release/ezralazuardy/orb" alt="release" target="_blank" rel="noopener noreferrer"></a>
  <a href="https://android-arsenal.com/api?level=15#l15"><img src="https://img.shields.io/badge/API-16%2B-blue.svg" alt="minimum API" target="_blank" rel="noopener noreferrer"></a>
  <a href="https://github.com/ezralazuardy/orb/issues"><img src="https://img.shields.io/github/issues/ezralazuardy/orb?color=red" alt="issues" target="_blank" rel="noopener noreferrer"></a>
  <a href="https://github.com/ezralazuardy/orb/blob/master/LICENSE"><img src="https://img.shields.io/github/license/ezralazuardy/orb" alt="license" target="_blank" rel="noopener noreferrer"></a>
</p>

---

Orb is a lifecycle-aware network monitoring library to simplify the needs of monitoring network state in Android. This library can help you monitor (observe) the current network state of Android device. It can give you the current connection status, connection type, and etc (please read the detail in the [wiki](#%EF%B8%8F-wiki)) in realtime change events. Orb really works well with the [MVVM architecture pattern](https://developer.android.com/jetpack/docs/guide#recommended-app-arch) in Android.

#### How it works
Orb is an implementation of [Android Live Data](https://developer.android.com/topic/libraries/architecture/livedata) that use an observable pattern to get the network state data in realtime. This is what makes Orb lifecycle-aware. Since the lifecycle of Live Data object is already handled automatically by Android lifecycle, you don't need to handle the Orb lifecycle manually. It's guarantee you to be flexible and no memory-leak. You can just start Orb and forget about it, it'll handle the lifecycle based on your Activity lifecycle automatically. The Orb lifecycle is already explaned in the [wiki](#%EF%B8%8F-wiki).


#### Latest version
See the latest version of Orb [here](https://github.com/ezralazuardy/orb/releases).

<br/>

## ✍️ Installation
#### Gradle setup
```gradle
// project level gradle
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```
```gradle
// module level gradle
dependencies {
    implementation 'com.github.ezralazuardy:orb:$version' //replace '$version' with the latest version of orb
}
```

#### Maven setup
```xml
<!-- <repositories> section of pom.xml -->
<repository>
    <id>jitpack.io</id>
   <url>https://jitpack.io</url>
</repository>
```
```xml
<!-- <dependencies> section of pom.xml -->
<dependency>
    <groupId>com.github.ezralazuardy</groupId>
    <artifactId>orb</artifactId>
    <version>version</version> <!-- replace 'version' with the latest version of orb -->
</dependency>
```

<br/>

## 🚀️ Getting Started
#### Manifest permission settings
Since Orb API need to access and change the current network status of the device, you need to add the following permissions in you App manifest.
```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.CHANGE_NETWORK_STATE" />
```

#### Using Orb
Orb usage is pretty simple, you just need to initialize it in your current context (view), and the rest will be handled by Orb.
```kotlin
Orb.with(this).observe {
    Log.d(this::javaClass.simpleName, it.toString()) // 'it' is an OrbResponse object
    // do something awesome..
}
```

All the action defined inside observe function will be converted into an observer. Make sure you init the Orb only in the **onCreate()** function of your activity. Why?, because Orb only can observe a single observer. And what if you want to change the observer?, like changing the action each time Orb detecting network changes? to do that, please read more about Orb advanced usage in the [wiki](#%EF%B8%8F-wiki).

This observer will be called each time Orb is detecting network changes in the device. The observe function also gives you an **OrbResponse** that hold the network state information, so that you can interact with it.

If you don't want to use an direct observer, and want to change the observer behavior after you observe the Orb, you can use the Orb observer function builder.
```kotlin
// use the Orb observer function builder
var observer = orbObserver {
  Log.d(this::javaClass.simpleName, it.toString()) // 'it' is an OrbResponse object
  // do something awesome..
}

Orb.with(this).observe(observer)
```

And since Orb is lifecycle-aware, you don't need worry about memory leak, all already handled by Orb. You just need to focus on functionality of your app, and let Orb do all the network monitoring stuff. Isn't that awesome? 😍️️

#### The OrbResponse
The observe method will return an **OrbResponse** object (accessible by keyword **it** by default) that hold some properties:

| Properties    | Value    | Default Value    | Information                                    |
| ------------- | -------- | ---------------- | ---------------------------------------------- |
| state         | OrbState | OrbType.UNKNOWN  | Current network state of the device            |
| type          | OrbType  | OrbState.UNKNOWN | Current network type of the device             |
| errorMessage  | String   | null             | The message when error happened in Orb process |

Please read more about eh **OrbResponse** in the [wiki](#%EF%B8%8F-wiki).

<br/>

## 📖️ Wiki
The wiki (documentation) is being written, stay tuned! 🤓️

<br/>

## 🤔️ Sample Implementation
You can try the sample implementation Orb by cloning this repository to your local, and run [app](https://github.com/ezralazuardy/orb/tree/master/app) module.

<br/>

## 👷️ Contributing
All contributions are welcomed. Please make a [pull request](https://github.com/ezralazuardy/orb/pulls) so that I can review your changes.

<br/>

## 🗒️ Side Note
Orb is at it's early stage. If you experiencing an error or bug, please report it to [issues](https://github.com/ezralazuardy/orb/issues) page.
