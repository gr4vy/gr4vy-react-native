import Foundation
import gr4vy_ios

@objc(EmbedReactNative)
class EmbedReactNative: NSObject {
  let GR4VY_TRANSACTION_CREATED = "GR4VY_TRANSACTION_CREATED"
  let GR4VY_TRANSACTION_FAILED = "GR4VY_TRANSACTION_FAILED"
  let GR4VY_ERROR = "GR4VY_ERROR"

  func gr4vyInit(gr4vyId: String,
                 token: String,
                 amount: Int,
                 currency: String,
                 country: String,
                 buyerId: String?,
                 externalIdentifier: String?,
                 store: String?,
                 display: String?,
                 intent: String?,
                 metadata: [String: String]?,
                 paymentSource: String?,
                 cartItems: [RCTCartItem]?,
                 environment: String?,
                 debugMode: Bool = false,
                 completion: @escaping(_ gr4vy: Gr4vy?) -> Void)  {
    var paymentSourceConverted: Gr4vyPaymentSource?
    if paymentSource != nil {
        paymentSourceConverted = Gr4vyPaymentSource(rawValue: paymentSource!)
    }

    var cartItemsConverted: [Gr4vyCartItem]?
    if let cartItems = cartItems {
      cartItemsConverted = cartItems.compactMap { (item: RCTCartItem) -> Gr4vyCartItem? in
        return try? Gr4vyCartItem(name: item.name, quantity: item.quantity, unitAmount: item.unitAmount)
      }
    }

    DispatchQueue.main.async(execute: {  
      guard let gr4vy = Gr4vy(gr4vyId: gr4vyId,
                              token: token,
                              amount: amount,
                              currency: currency,
                              country: country,
                              buyerId: buyerId,
                              externalIdentifier: externalIdentifier,
                              store: store,
                              display: display,
                              intent: intent,
                              metadata: metadata,
                              paymentSource: paymentSourceConverted,
                              cartItems: cartItemsConverted,
                              environment: (environment != nil && environment?.lowercased() == "production") ? .production : .sandbox,
                              debugMode: debugMode) else {
        completion(nil)
        return
      }

      completion(gr4vy)
    })
  }
  
  @objc
  func constantsToExport() -> [AnyHashable : Any]! {
    return [
      GR4VY_TRANSACTION_CREATED: GR4VY_TRANSACTION_CREATED,
      GR4VY_TRANSACTION_FAILED: GR4VY_TRANSACTION_FAILED,
      GR4VY_ERROR: GR4VY_ERROR
    ]
  }
  
  @objc
  func showPaymentSheet(
    _ gr4vyId: String,
    token: String,
    amount: Double,
    currency: String,
    country: String,
    buyerId: String?,
    externalIdentifier: String?,
    store: String?,
    display: String?,
    intent: String?,
    metadata: [String: String]?,
    paymentSource: String?,
    cartItems: [RCTCartItem]?,
    environment: String?,
    debugMode: Bool)
  {
    gr4vyInit(gr4vyId: gr4vyId,
             token: token,
             amount: Int(amount),
             currency: currency,
             country: country,
             buyerId: buyerId,
             externalIdentifier: externalIdentifier,
             store: store,
             display: display,
             intent: intent,
             metadata: metadata,
             paymentSource: paymentSource,
             cartItems: cartItems,
             environment: environment,
             debugMode: debugMode) { (gr4vy) in
      if gr4vy == nil {
        EmbedReactNativeEvents.emitter.sendEvent(
          withName: "onEvent",
          body: [
            "name": "generalError",
            "data": [
              "message" : "Failed to initialize Gr4vy SDK"
            ]
          ]
        )
      }

      DispatchQueue.main.async(execute: {
          let presentingViewController = RCTPresentedViewController()
        
          gr4vy!.launch(
            presentingViewController: presentingViewController!,
            onEvent: { event in
              
              switch event {
              case .transactionFailed(let transactionID, let status, let paymentMethodID):
                EmbedReactNativeEvents.emitter.sendEvent(
                  withName: "onEvent",
                  body: [
                    "name": "transactionFailed",
                    "data": [
                      "success": true,
                      "transactionId": transactionID,
                      "status": status,
                      "paymentMethodId": paymentMethodID as Any
                    ]
                  ]
                )
                break
              case .transactionCreated(let transactionID, let status, let paymentMethodID):
                EmbedReactNativeEvents.emitter.sendEvent(
                  withName: "onEvent",
                  body: [
                    "name": "transactionCreated",
                    "data": [
                      "success": true,
                      "transactionId": transactionID,
                      "status": status,
                      "paymentMethodId": paymentMethodID as Any
                    ]
                  ]
                )
                break
              case .generalError(let error):
                EmbedReactNativeEvents.emitter.sendEvent(
                  withName: "onEvent",
                  body: [
                    "name": "generalError",
                    "data": [
                      "message" : error.description
                    ]
                  ]
                )
                break
              case .paymentMethodSelected(let id, let method, let mode):
                EmbedReactNativeEvents.emitter.sendEvent(
                  withName: "onEvent",
                  body: [
                    "name": "paymentSelected",
                    "data": [
                      "id" : id,
                      "method": method,
                      "mode": mode
                    ]
                  ]
                )
                break
              }
            })
      })
    }
  }
  
  @objc
  static func requiresMainQueueSetup() -> Bool {
    return true
  }
}
