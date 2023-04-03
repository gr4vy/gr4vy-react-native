#import <React/RCTBridgeModule.h>
#import <React/RCTEventEmitter.h>
// #import <React/RCTConvert.h>
#import <Foundation/Foundation.h>

// typedef NS_ENUM(NSInteger, PaymentSource) {
//   installment = 1,
//   recurring = 2
// };

// @implementation RCTConvert (PaymentSource)
//     RCT_ENUM_CONVERTER(
//         PaymentSource,
//         (@{
//             @"installment": @(installment),
//             @"recurring": @(recurring)
//         }),
//         installment,
//         integerValue
//     )
// @end

@implementation CartItemObj: NSObject
    NSString *name;
    int quantity;
    int unitAmount;
@end

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
    cartItems:(NSArray<CartItemObj *>)cartItems
    environment:(NSString *)environment
    debugMode:(BOOL)debugMode
    errorCallback:(RCTResponseSenderBlock)errorCallback
    successCallback:(RCTResponseSenderBlock)successCallback)
@end
  
@interface RCT_EXTERN_MODULE(EmbedReactNativeEvents, RCTEventEmitter)
  RCT_EXTERN_METHOD(supportedEvents)
@end
