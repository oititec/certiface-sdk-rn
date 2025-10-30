#import "RnSdk.h"
#import "RnSdk-Swift.h"
#import <AVFoundation/AVFoundation.h>

@implementation RnSdk {
  RnSdkImpl *moduleImpl;
}

- (instancetype)init {
  self = [super init];
  if (self) {
    moduleImpl = [RnSdkImpl new];
  }
  return self;
}

RCT_EXPORT_MODULE()

- (void)checkCameraPermission:(RCTPromiseResolveBlock)resolve
                       reject:(RCTPromiseRejectBlock)reject {
  AVAuthorizationStatus status =
  [AVCaptureDevice authorizationStatusForMediaType:AVMediaTypeVideo];

  resolve(@(status == AVAuthorizationStatusAuthorized));
}

- (void)requestCameraPermission:(RCTPromiseResolveBlock)resolve
                         reject:(RCTPromiseRejectBlock)reject {
  AVAuthorizationStatus status =
  [AVCaptureDevice authorizationStatusForMediaType:AVMediaTypeVideo];

  if (status == AVAuthorizationStatusAuthorized) {
    resolve(@YES);
    return;
  }

  [AVCaptureDevice requestAccessForMediaType:AVMediaTypeVideo
                           completionHandler:^(BOOL granted) {
    dispatch_async(dispatch_get_main_queue(), ^{
      resolve(@(granted));
    });
  }];
}

- (void)startJourney:(NSString *)appKey
           onSuccess:(RCTResponseSenderBlock)onSuccess
             onError:(RCTResponseSenderBlock)onError
     isCustomEnabled:(NSNumber *)isCustomEnabled
               theme:(NSDictionary *)theme {
  BOOL customEnabled = isCustomEnabled ? [isCustomEnabled boolValue] : NO;
  [moduleImpl startJourneyWithAppKey:appKey
                     isCustomEnabled:customEnabled
                               theme:theme
                           onSuccess:^(NSString *_Nonnull result) {
    onSuccess(@[ result ]);
  }
                             onError:^(NSString *_Nonnull error) {
    onError(@[ error ]);
  }];
}

- (std::shared_ptr<facebook::react::TurboModule>)getTurboModule:
(const facebook::react::ObjCTurboModule::InitParams &)params {
  return std::make_shared<facebook::react::NativeRnSdkSpecJSI>(params);
}

@end
