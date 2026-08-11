#import "RnSdk.h"
#import "RnSdk-Swift.h"
#import <AVFoundation/AVFoundation.h>
#import <stdatomic.h>

@implementation RnSdk {
  RnSdkImpl *moduleImpl;
  atomic_bool cameraPermissionInFlight;
}

- (instancetype)init {
  self = [super init];
  if (self) {
    moduleImpl = [RnSdkImpl new];
    atomic_init(&cameraPermissionInFlight, false);
  }
  return self;
}

RCT_EXPORT_MODULE(CertifaceRnSdk)

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

  bool expected = false;
  if (!atomic_compare_exchange_strong(&cameraPermissionInFlight, &expected, true)) {
    reject(@"ERROR", @"Camera permission request already in progress", nil);
    return;
  }

  [AVCaptureDevice requestAccessForMediaType:AVMediaTypeVideo
                           completionHandler:^(BOOL granted) {
    atomic_store(&cameraPermissionInFlight, false);
    dispatch_async(dispatch_get_main_queue(), ^{
      resolve(@(granted));
    });
  }];
}

- (void)startJourney:(NSString *)appKey
         environment:(NSString *)environment
            provider:(NSString *)provider
           onSuccess:(RCTResponseSenderBlock)onSuccess
             onError:(RCTResponseSenderBlock)onError
     isCustomEnabled:(NSNumber *)isCustomEnabled
               theme:(NSDictionary *)theme {
  BOOL customEnabled = isCustomEnabled ? [isCustomEnabled boolValue] : NO;
  [moduleImpl startJourneyWithAppKey:appKey
                         environment:environment
                            provider:provider
                     isCustomEnabled:customEnabled
                               theme:theme
                           onSuccess:^(NSString *_Nonnull result) {
    onSuccess(@[ result ]);
  }
                             onError:^(NSString *_Nonnull error) {
    onError(@[ error ]);
  }];
}

- (void)startSaasJourney:(NSString *)token
             environment:(NSString *)environment
               onSuccess:(RCTResponseSenderBlock)onSuccess
                 onError:(RCTResponseSenderBlock)onError
         isCustomEnabled:(NSNumber *)isCustomEnabled
                   theme:(NSDictionary *)theme {
  BOOL customEnabled = isCustomEnabled ? [isCustomEnabled boolValue] : NO;
  [moduleImpl startSaasJourneyWithToken:token
                            environment:environment
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
