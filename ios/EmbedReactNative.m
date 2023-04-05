#import <React/RCTBridgeModule.h>
#import <React/RCTConvert.h>
#import <React/RCTEventEmitter.h>
#import <Foundation/Foundation.h>
#import "RCTCartItem.h"

@interface RCT_EXTERN_MODULE(EmbedReactNative, NSObject)

RCT_EXTERN_METHOD(
  showPaymentSheet:(NSString *)gr4vyId
    token:(NSString *)token
    amount:(double)amount
    currency:(NSString *)currency
    country:(NSString *)country
    buyerId:(NSString *)buyerId
    externalIdentifier:(NSString *)externalIdentifier
    store:(NSString *)store
    display:(NSString *)display
    intent:(NSString *)intent
    metadata:(NSDictionary *)metadata
    paymentSource:(NSString *)paymentSource
    cartItems:(NSArray<RCTCartItem *> *)cartItems
    environment:(NSString *)environment
    debugMode:(BOOL)debugMode)
@end
  
@interface RCT_EXTERN_MODULE(EmbedReactNativeEvents, RCTEventEmitter)
  RCT_EXTERN_METHOD(supportedEvents)
@end

@implementation RCTConvert (RCTCartItemArray)
  RCT_ARRAY_CONVERTER(RCTCartItem)
@end
