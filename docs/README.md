# Cookbook

[![](https://jitpack.io/v/sababado/Rolo-Cookbook.svg)](https://jitpack.io/#sababado/Rolo-Cookbook)

## About Activity
Add the activity to your manifest
```xml
<activity
    android:name=".ui.AboutActivity"
    android:label="@string/about" />
```

and start the activity with

```kotlin
AboutActivity.goToAboutActivity(context)
```

## Ads
Add meta tags in manafest.

```xml
<meta-data
    android:name="com.google.android.gms.ads.APPLICATION_ID"
    android:value="ca-app-pub-XXXXXXXXXX~YYYYYYYYYY" />
<meta-data
    android:name="rolo.cookbook.admob_pub_id"
    android:value="pub-XXXXXXXXXX" />
<meta-data
    android:name="rolo.cookbook.privacy_url"
    android:value="https://math-for-meat-heads.flycricket.io/privacy.html" />
<meta-data
    android:name="rolo.cookbook.debug_device_id"
    android:value="519A899F0D74FE834B8C598F8FBDB59F" />
```

Add the `ad unit id` to the strings. **The string name must be exactly this.**

```xml
<string name="cookbook_admob_ad_unit_id" translatable="false">ca-app-pub-XXXXXXXXXX/ZZZZZZZZZZ</string>
```

## Authentication

Currently only Google Sign In is supported with a direct link to Firebase User Accounts

### Google Sign In
If you haven't yet specified your app's SHA-1 fingerprint, do so from the [Settings](https://console.firebase.google.com/u/0/project/meat-head-math/settings/general/android:com.sababado.meatheadmath) page of the Firebase console. [Refer to Authenticating Your Client](https://developers.google.com/android/guides/client-auth) for details on how to get your app's SHA-1 fingerprint.

1. Add the debug key SHA1
1. Add the release key SHA1
1. Add the Google Play Console SHA1 (Play Console -> App -> Setup -> App Signing)

### Usage

Add your Google Web Client ID to the manifest

```xml
<meta-data
    android:name="rolo.cookbook.google_web_client_id"
    android:value="@string/web_client_id" />
```

Use the `GoogleSignInHelper` in any activity or fragment that needs the sign in option.
Adding a `UserAccounts` class to the signin helper will automatically attempt to keep the google account
in sync with the user data provider.

Get an instance of `UserAccounts` from the `UserAccountsProvider` class.

For quick authentication, use the `UserAccounts.signInAnonymously()` function.

#### Firebase Authentication
`FirebaseUserAccounts` will work with any existing firebase setup.

*Enable Google Sign-In*
Enable Google Sign-In in the Firebase console:
In the Firebase console, open the Auth section.
On the Sign in method tab, enable the Google sign-in method and click Save.

*Firebase Rules*
- Make sure the rules allow users to edit only their data
- Rules should allow any (linked user) to delete anonymous collection data.


## Permission Handling

### `PermissionManager`

Helper for permissions.

The using activity should implement `ActivityCompat.OnRequestPermissionsResultCallback` and override `onRequestPermissionsResult`.
Start the request with one of the `requestPermission` functions. One simplifies the process with a snackbar, the other is flexible for more detailed usage.
Handle the results in `handlePermissionResult`.

If permissions are denied, the only callback will be in the `handlePermissionResult` function.
Check for one off permissions with `isPermissionAvailable`.

### `FirstTimeUsageHandler`
Use this class to ease the actions of checking for the first time usage of something.


## Location

Add location with the `LocationManager`.
Create an instance of this class then start with `getLastLocation`.
In the using activity, override `onActivityResult]Activity.onActivityResult` and call into `LocationManager.onActivityResult`.

To use this, you'll need to include the Google Play Services dependency in your app.
```gradle
implementation 'com.google.android.gms:play-services-location:17.1.0'
```

You'll need to have permission from the user to use location. A good implementation strategy is to check that first:

```kotlin
// Start the location work, first by checking for permission.
private fun startLocation() = PermissionManager.requestPermission(
    appCompatActivity = this,
    permission = Manifest.permission.ACCESS_COARSE_LOCATION,
    snackbarHost = getSnackbarHostView(),
    rational = R.string.location_rational,
    onPermissionGranted = permissionGrantedCallback
)
```

# Maven Publishing

Use the task `publishToMavenLocal` to push a local build.