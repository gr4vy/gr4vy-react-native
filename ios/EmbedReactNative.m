#import <React/RCTBridgeModule.h>
#import <React/RCTConvert.h>
#import <React/RCTEventEmitter.h>
#import <Foundation/Foundation.h>

@interface RCT_EXTERN_MODULE(EmbedReactNative, NSObject)
  RCT_EXTERN_METHOD(showPaymentSheet:(NSDictionary *)config)
@end
  
@interface RCT_EXTERN_MODULE(EmbedReactNativeEvents, RCTEventEmitter)
  RCT_EXTERN_METHOD(supportedEvents)
@end
