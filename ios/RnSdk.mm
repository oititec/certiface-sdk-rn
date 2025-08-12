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

- (NSNumber *)multiply:(double)a b:(double)b {
  NSNumber *result = @(a * b);
  return result;
}

- (void)checkCameraPermission:(nonnull RCTPromiseResolveBlock)resolve
                       reject:(nonnull RCTPromiseRejectBlock)reject {
  AVAuthorizationStatus status =
      [AVCaptureDevice authorizationStatusForMediaType:AVMediaTypeVideo];

  resolve(@(status == AVAuthorizationStatusAuthorized));
}

- (void)requestCameraPermission:(nonnull RCTPromiseResolveBlock)resolve
                         reject:(nonnull RCTPromiseRejectBlock)reject {
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
           onSuccess:(nonnull RCTResponseSenderBlock)onSuccess
             onError:(nonnull RCTResponseSenderBlock)onError
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

- (void)testString:(NSString *)appKey {
  [moduleImpl testStringWithString:appKey];
}

- (std::shared_ptr<facebook::react::TurboModule>)getTurboModule:
    (const facebook::react::ObjCTurboModule::InitParams &)params {
  return std::make_shared<facebook::react::NativeRnSdkSpecJSI>(params);
}

@end
