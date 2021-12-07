//
//  TuyaBLERNActivatorModule.m
//  TuyaRnDemo
//
//  Created by 浩天 on 2019/2/28.
//  Copyright © 2019年 Facebook. All rights reserved.
//

#import "TuyaBLERNActivatorModule.h"
#import <TuyaSmartActivatorKit/TuyaSmartActivatorKit.h>
#import <TuyaSmartBaseKit/TuyaSmartBaseKit.h>
#import <TuyaSmartDeviceKit/TuyaSmartDeviceKit.h>
#import <TuyaSmartBLEKit/TuyaSmartBLEWifiActivator.h>
#import "TuyaRNUtils+Network.h"
#import "YYModel.h"

@interface activationParams : NSObject
@property(assign) long long homeId;
@property(assign) NSString *deviceId;
@property(assign) NSString *productId;
@property(assign) NSString *ssid;
@property(assign) NSString *password;
@property(copy, nonatomic) RCTPromiseResolveBlock promiseResolveBlock;
@property(copy, nonatomic) RCTPromiseRejectBlock promiseRejectBlock;
@end

// Bluetooth Pairing
static activationParams * activationParamsInstance = nil;
static TuyaBLERNActivatorModule * activatorInstance = nil;

@interface TuyaBLERNActivatorModule()<TuyaSmartBLEWifiActivatorDelegate>

@property(copy, nonatomic) RCTPromiseResolveBlock promiseResolveBlock;
@property(copy, nonatomic) RCTPromiseRejectBlock promiseRejectBlock;

@end

@implementation TuyaBLERNActivatorModule

RCT_EXPORT_MODULE(TuyaBLEActivatorModule)

RCT_EXPORT_METHOD(initActivator:(activationParams *)params resolver:(RCTPromiseResolveBlock)resolver rejecter:(RCTPromiseRejectBlock)rejecter) {
  if (activatorInstance == nil) {
    activatorInstance = [TuyaBLERNActivatorModule new];
  }

  [TuyaSmartBLEWifiActivator sharedInstance].delegate = activatorInstance;
  activatorInstance.promiseResolveBlock = resolver;
  activatorInstance.promiseRejectBlock = rejecter;

  [[TuyaSmartBLEWifiActivator sharedInstance] startConfigBLEWifiDeviceWithUUID:activationParamsInstance.deviceId homeId:activationParamsInstance.homeId productId:activationParamsInstance.productId ssid:activationParamsInstance.ssid password:activationParamsInstance.password  timeout:100 success:^{
      // Wait for activation
    } failure:^(NSError *error) {
      if (activationParamsInstance.promiseRejectBlock) {
        [TuyaRNUtils rejecterWithError:error handler:rejecter];
      }
      return;
    }];
}

- (void)bleWifiActivator:(TuyaSmartBLEWifiActivator *)activator didReceiveBLEWifiConfigDevice:(TuyaSmartDeviceModel *)deviceModel error:(NSError *)error {
  if (!error && deviceModel) {
    if (activationParamsInstance.promiseResolveBlock) {
      self.promiseResolveBlock([deviceModel yy_modelToJSONObject]);
    }
  }
  if (error) {
    if (activationParamsInstance.promiseRejectBlock) {
      [TuyaRNUtils rejecterWithError:error handler:activatorInstance.promiseRejectBlock];
    }
  }

}

@end
