# @volst/react-native-tuya

This is a fork of [TuyaInc/tuyasmart-home-sdk-react-native](https://github.com/TuyaInc/tuyasmart-home-sdk-react-native), fixing some issues we were having that were not merged. Refer to the commit logs for our changes.

## Feature Overview

Tuya Smart APP SDK provides the interface package for the communication with hardware and Tuya Cloud to accelerate the application development process, including the following features:

Hardware functions (network configuration, control, status reporting, regular tasks, groups, firmware upgrades, sharing)
Account system (phone number, email registration, login, password reset and other general account functions)
Tuya Cloud HTTP API interface package

## Getting started

```
npm install @volst/react-native-tuya
```

This library contains native code which is automatically linked in React Native >= 0.59. For iOS, run `cd ios && pod install`.

## Examples

See the demo in https://github.com/TuyaInc/tuyasmart-home-sdk-react-native/tree/master/Example before using.

## Doc

Refer to Details: [Tuya Smart Doc: tuyasmart-home-sdk-react-native](https://tuyakae.gitbook.io/tuyasmart-home-sdk-react-native)


## Usage

In the Tuya development environment create a new app and make sure you have an "App key", "App secret" and "Secure image". [Read how to do this](https://tuyainc.github.io/tuyasmart_home_ios_sdk_doc/en/resource/Preparation.html).

### iOS

Put the secure image into the root path of your project as [explained here](https://tuyainc.github.io/tuyasmart_home_ios_sdk_doc/en/resource/Preparation.html).

In `ios/AppDelegate.m`, add the following import;

```obj-c
#import <TuyaSmartHomeKit/TuyaSmartKit.h>
```

Then, under the `roootView.backgroundColor` line in the same file, add this:

```obj-c
  #ifdef DEBUG
    [[TuyaSmartSDK sharedInstance] setDebugMode:YES];
  #endif

  [[TuyaSmartSDK sharedInstance] startWithAppKey:@"xxx" secretKey:@"xxx"];
```

Now replace the `xxx` with your app key and secret key.

### Android

TODO

In your app MainApplication ,you should initSDK 

You can choose one of the following two ways to go initSDK

* the first one

```

Appkey and appSecret are configured in the AndroidManifest.xml file, and corresponding permissions are also configured

<meta-data
android:name="TUYA_SMART_APPKEY"
android:value="Appkey" />
<meta-data
android:name="TUYA_SMART_SECRET"
android:value="AppSecret" />

TuyaCoreModule.Companion.initTuyaSDKWithoutOptions()
```

* the second one

```
TuyaCoreModule.Companion.initTuyaSDk("xxxxxxxxxxxxxxxxxxxxx","xxxxxxxxxxxxxxxxxxxxx",this);

```

* how to use

```

@Override
  public void onCreate() {
    super.onCreate();
    SoLoader.init(this, /* native exopackage */ false);
    // Fill in appkey and appsecret of the application below
   // TuyaCoreModule.Companion.initTuyaSDKWithoutOptions();
  //TuyaCoreModule.Companion.initTuyaSDk("xxxxxxxxxxxxxxxxxxxxx","xxxxxxxxxxxxxxxxxxxxx",this);
  //TuyaCoreModule.setSDKDebug(true) //if you have some problem ,You can grab the log for us
  }


```

## Local Development

### `yarn start`

Runs the project in development/watch mode. Your project will be rebuilt upon changes. TSDX has a special logger for you convenience. Error messages are pretty printed and formatted for compatibility VS Code's Problems tab.

<img src="https://user-images.githubusercontent.com/4060187/52168303-574d3a00-26f6-11e9-9f3b-71dbec9ebfcb.gif" width="600" />

Your library will be rebuilt if you make edits.

### `yarn build`

Bundles the package to the `dist` folder.
The package is optimized and bundled with Rollup into multiple formats (CommonJS, UMD, and ES Module).

<img src="https://user-images.githubusercontent.com/4060187/52168322-a98e5b00-26f6-11e9-8cf6-222d716b75ef.gif" width="600" />
