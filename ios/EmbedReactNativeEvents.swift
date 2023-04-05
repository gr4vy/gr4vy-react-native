import Foundation

@objc(EmbedReactNativeEvents)
open class EmbedReactNativeEvents: RCTEventEmitter {

  public static var emitter: RCTEventEmitter!

  override init() {
    super.init()
    EmbedReactNativeEvents.emitter = self
  }

  @objc override public static func requiresMainQueueSetup() -> Bool {
    return false
  }
  
  open override func supportedEvents() -> [String] {
    ["onEvent"]
  }
}
