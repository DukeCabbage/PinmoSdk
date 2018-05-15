# PinmoSdk

## Installation
#### Maven (local)
Create a folder for the artifacts, such as `libs`, then specify it as a maven repository in app `build.gradle`<br/>
This path can be relative or absolute, such as `url "file:/Users/JohnDoe/myMaven/repository"`
```groovy
repository {
    maven {
        url "file:libs/"
    }
}
```
Put the .aar and .pom files in `libs` with a chain of enclosing folders
```
libs/com/evilnut/pinmo/pinmosdk/x.y.z/pinmosdk-x.y.z.aar
libs/com/evilnut/pinmo/pinmosdk/x.y.z/pinmosdk-x.y.z.pom
```

Add dependency in app `build.gradle`
```groovy
dependencies {
    implementation "com.evilnut.pinmo:pinmosdk:x.y.z"
}
```

#### Using AAR directly
Create a folder called `libs` under app module, then in the app `build.gradle` specify the following
```groovy
repositories {
    flatDir {
        dirs 'libs'
    }
}
```
Put the pinmosdk-x.y.x.aar in `libs` folder, then include it in the app dependencies<br/>
And since the transitive dependencies are not bundled in the aar, you need to include these in your app `build.gradle` as well
```groovy
dependencies {
    implementation(name: "pinmosdk-x.y.z", ext: "aar")

    implementation "com.android.support:design:27.1.1"
    // Force resolution
    implementation "com.android.support:cardview-v7:27.1.1"
    implementation "com.android.support:customtabs:27.1.1"
    implementation "com.android.support:exifinterface:27.1.1"

    // ReactiveX
    implementation "io.reactivex.rxjava2:rxjava:2.1.12"
    implementation "io.reactivex.rxjava2:rxandroid:2.0.2"

    implementation("com.squareup.retrofit2:retrofit:2.4.0") { exclude module: "okhttp" }
    implementation "com.squareup.retrofit2:adapter-rxjava2:2.4.0"
    implementation "com.squareup.retrofit2:converter-gson:2.4.0"

    implementation "com.squareup.okhttp3:okhttp:3.10.0"
    implementation "com.squareup.okhttp3:logging-interceptor:3.10.0"

    implementation "com.google.code.gson:gson:2.8.2"

    implementation "com.squareup.picasso:picasso:2.71828"

    implementation "com.jakewharton.timber:timber:4.7.0"

    implementation "com.tencent.mm.opensdk:wechat-sdk-android-without-mta:5.1.0"

    implementation "com.facebook.android:facebook-share:4.29.0"
}
```


## Setup
#### Api keys
In order for the Pinmo sdk to function, you need to register and obtain the following credentials first
1. Obtain app id,app secret and endpoint from Pinmo
2. Obtain Wechat app id
3. Obtain Facebook app id

#### Facebook setup
Add a string resource called "facebook_app_id" in your `strings.xml`, with the Facebook app id you obtained earlier
```xml
<string name="facebook_app_id" translatable="false">xxx</string>
```
Then reference it in your `AndroidManifest.xml`
```xml
<meta-data
    android:name="com.facebook.sdk.ApplicationId"
    android:value="@string/facebook_app_id"/>
```

#### Initializing Pinmo sdk
At app start, construct a PinmoAppOptions instance and use it to initialize and PinmoApp instance
```java
final String PINMO_APP_ID = "XXX";
final String PINMO_APP_SECRET = "XXX";
final String PINMO_END_POINT = "XXX";
final String WECHAT_APP_ID = "XXX";

final PinmoAppOptions options = new PinmoAppOptions.Builder()
            .appId(PINMO_APP_ID)
            .appSecret(PINMO_APP_SECRET)
            .pinmoEndpoint(PINMO_END_POINT)
            .wechatAppId(WECHAT_APP_ID)
            .build();
            
PinmoApp.initApp(options);
```


## Usage
Pinmo will cache the last successfully fetched feed, so you can display whenever it's ready<br/>
To fetch or refresh the feed for a certain user, call `fetchFeed(String email)`
```java
final String email = "test123@gmail.com";
PinmoApp.get().fetchFeed(email);
```
To observe on the fetching status and determine when to display the content, or just to get the fetched data
```java
@Override
protected void onResume() {
    super.onResume();

    // PinmoApp feed publisher will broadcast to all subscribers whenever there's a new feed fetched
    PinmoApp.get().getFeedPublisher()
            .subscribe(new Observer<PinmoFeed>() {
                @Override
                public void onSubscribe(Disposable d) { Timber.i("Start fetching"); }

                @Override
                public void onNext(final PinmoFeed feed) { Timber.i(feed); }

                @Override
                public void onError(Throwable e) { Timber.e(e); }

                @Override
                public void onComplete() {}
            });
}

// And remember to dispose the subscription
@Override
protected void onPause() {
    super.onPause();
    if (disposable != null) disposable.dispose();
}
```
The share and share success dialogs are instances of DialogFragment, to display them
```java
// Wechat share to moments
PinmoApp.get().getShareDialog(false)
        .show(getSupportFragmentManager(), "share");

// Wechat share to friends or groups
PinmoApp.get().getShareDialog(false)
        .show(getSupportFragmentManager(), "share");

// Display the success popup, with a link to Pinmo app on Play Store
PinmoApp.get().getSuccessDialog()
        .show(getSupportFragmentManager(), "success");
```
