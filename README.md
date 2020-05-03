<p align="center"><a href="https://ezralazuardy.com/orb" target="_blank" rel="noopener noreferrer"><img width="200" src="https://github.com/ezralazuardy/orb/blob/master/images/orb-logo.png" alt="orb logo"></a></p>

<p align="center">
  <a href="https://www.codacy.com/manual/ezralazuardy/orb?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=ezralazuardy/orb&amp;utm_campaign=Badge_Grade"><img src="https://api.codacy.com/project/badge/Grade/3904f9eccb6b4606a25a428e1abd9b1d" alt="code quality" target="_blank" rel="noopener noreferrer"></a>
  <a href="https://codeclimate.com/github/ezralazuardy/orb/maintainability"><img src="https://api.codeclimate.com/v1/badges/3966ef5b3239786fc2b3/maintainability" alt="maintainability" target="_blank" rel="noopener noreferrer"></a>
  <a href="https://circleci.com/gh/ezralazuardy/orb"><img src="https://img.shields.io/circleci/build/github/ezralazuardy/orb" alt="build status" target="_blank" rel="noopener noreferrer"></a>
  <a href="https://jitpack.io/#ezralazuardy/orb"><img src="https://img.shields.io/github/v/release/ezralazuardy/orb" alt="release" target="_blank" rel="noopener noreferrer"></a>
  <a href="https://android-arsenal.com/api?level=15#l15"><img src="https://img.shields.io/badge/API-16%2B-blue.svg" alt="minimum API" target="_blank" rel="noopener noreferrer"></a>
  <a href="https://github.com/ezralazuardy/orb/issues"><img src="https://img.shields.io/github/issues/ezralazuardy/orb?color=red" alt="issues" target="_blank" rel="noopener noreferrer"></a>
  <a href="https://github.com/ezralazuardy/orb/blob/master/LICENSE"><img src="https://img.shields.io/github/license/ezralazuardy/orb" alt="license" target="_blank" rel="noopener noreferrer"></a>
</p>

---

Orb is a lifecycle-aware asynchronous network monitoring library to simplify the needs of monitoring network state in Android. This library can help you monitor (observe) the current network state of Android device. It can give you the current connection status, connection type, and etc (please read the detail in the [wiki](#%EF%B8%8F-wiki)) in realtime change events without blocking the main thread. Orb really works well with the [MVVM architecture pattern](https://developer.android.com/jetpack/docs/guide#recommended-app-arch) in Android.

#### How it works
Orb is an implementation of [Android Live Data](https://developer.android.com/topic/libraries/architecture/livedata) that use an observable pattern to get the network state data in realtime. This is what makes Orb lifecycle-aware. Since the lifecycle of Live Data object is already handled automatically by Android lifecycle, you don't need to handle the Orb lifecycle manually. It's guarantee you to be flexible and no memory leak. You can just start Orb and forget about it, it'll handle the lifecycle based on your Activity lifecycle automatically. The Orb lifecycle is already explaned in the [wiki](#%EF%B8%8F-wiki).

How Orb determine the current network state and type is by using [ConnectivityManager](https://developer.android.com/reference/android/net/ConnectivityManager). And due to some Android ConnectivityManager API deprecation, applying network managing algorithm can be a little bit hard and tricky. Here's come Orb to the rescue. Orb is simple, powerful, sweet, and the most important, idiomatic!. It's written in pure Kotlin.


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

All the action defined inside observe function will be converted into an observer. Make sure you've initialize the Orb only in the **onCreate()** function of your activity. Why?, because Orb only can observe a single observer and that need to be happened in the creation process of your activity.

What if you want to change the observer?, like changing the observer action after you observing the Orb? or changing the observer action when each time Orb detecting network changes? to do that, please read more about Orb advanced usage in the [wiki](#%EF%B8%8F-wiki).

If you don't want to use a direct written observer, you can use the Orb observer function builder, and pass the observer to observe function parameter.

```kotlin
// use the Orb observer function builder
val observer = orbObserver {
  Log.d(this::javaClass.simpleName, it.toString()) // 'it' is an OrbResponse object
  // do something awesome..
}

Orb.with(this).observe(observer)
```

Orb observer will be called each time Orb is detecting network changes in the device. The observe function also gives you an **OrbResponse** that hold the network state information, so that you can interact with it.

Please note that Orb will observing the network asynchronously. It's mean that Orb will **not** blocking your main thread, resulting in hang and stuttering effect on Android screen. And Orb is also lifecycle-aware. You don't need to worry about memory leak, all already handled by Orb. You just need to focus on functionality of your app, and let Orb do all the network monitoring stuff. Isn't that awesome? 😍️️

#### The OrbResponse
The observe method will return an **OrbResponse** object (accessible by keyword **it** by default) when each time Orb detecting a change in device network state. This object hold some properties as follows:

| Property     | Value Type | Default Value    | Information                                    |
| ------------ | ---------- | ---------------- | ---------------------------------------------- |
| state        | OrbState   | OrbState.UNKNOWN | Current network state of the device            |
| type         | OrbType    | OrbType.UNKNOWN  | Current network type of the device             |
| errorMessage | String     | null             | The message when error happened in Orb process |

Please read more about the **OrbResponse** in the [wiki](#%EF%B8%8F-wiki).

<br/>

## 📖️ Wiki
Read the Orb Wiki [here](https://github.com/ezralazuardy/orb/wiki).

<br/>

## 🤔️ Sample Implementation
You can try the sample implementation of Orb by cloning this repository to your local, and run the [app](https://github.com/ezralazuardy/orb/tree/master/app) module.

<br/>

## 👷️ Contributing
All contributions are welcomed. Please make a [pull request](https://github.com/ezralazuardy/orb/pulls) so that I can review your changes.

Before start making contributions to Orb, please read the [contribution guidelines](https://github.com/ezralazuardy/orb/blob/master/CONTRIBUTING.md) and [code of conduct](https://github.com/ezralazuardy/orb/blob/master/CODE_OF_CONDUCT.md).

<br/>

## 🛡️ Security Policy
Read the current Orb project's security policy [here](https://github.com/ezralazuardy/orb/security/policy).

<br/>

## 🗒️ Side Note
Orb is at it's early stage. If you experiencing an error or bug, please report it to [issues](https://github.com/ezralazuardy/orb/issues) page.
