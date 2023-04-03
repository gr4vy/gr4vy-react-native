#import "RCTCartItem.h"

@implementation RCTCartItem
- (instancetype)initWithName:(NSString *)name quantity:(NSInteger)quantity unitAmount:(NSInteger)unitAmount
{
  self = [super init];
  if (self) {
    _name = [name copy];
    _quantity = quantity;
    _unitAmount = unitAmount;
  }
  return self;
}
@end