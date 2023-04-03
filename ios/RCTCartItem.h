#import <Foundation/Foundation.h>
#import <React/RCTBridgeModule.h>

@interface RCTCartItem : NSObject
@property (nonatomic, copy) NSString *name;
@property (nonatomic, assign) NSInteger quantity;
@property (nonatomic, assign) NSInteger unitAmount;
- (instancetype)initWithName:(NSString *)name quantity:(NSInteger)quantity unitAmount:(NSInteger)unitAmount;
@end