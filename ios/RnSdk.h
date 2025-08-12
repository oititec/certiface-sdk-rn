#import <RnSdkSpec/RnSdkSpec.h>

@interface RnSdk : NSObject <NativeRnSdkSpec>

- (void)testString:(NSString *_Nullable)appKey;

- (void)startJourney:(NSString *_Nonnull)appKey
           onSuccess:(RCTResponseSenderBlock _Nonnull)onSuccess
             onError:(RCTResponseSenderBlock _Nonnull)onError
     isCustomEnabled:(NSNumber *_Nullable)isCustomEnabled
               theme:(NSDictionary *_Nullable)theme;

@end
