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
                 cartItems: [Gr4vyCartItem]?,
                 environment: String?,
                 debugMode: Bool = false,
                 completion: @escaping(_ gr4vy: Gr4vy?) -> Void)  {
    var paymentSourceConverted: Gr4vyPaymentSource?
    if paymentSource != nil {
        paymentSourceConverted = Gr4vyPaymentSource(rawValue: paymentSource!)
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
                              cartItems: cartItems,
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
  func showPaymentSheet(_ config: [String: Any])
  {
    guard let gr4vyId = config["gr4vyId"] as? String,
          let environment = config["environment"] as? String?,
          let token = config["token"] as? String,
          let amount = config["amount"] as? Double,
          let currency = config["currency"] as? String,
          let country = config["country"] as? String,
          let buyerId = config["buyerId"] as? String?,
          let externalIdentifier = config["externalIdentifier"] as? String?,
          let store = config["store"] as? String?,
          let display = config["display"] as? String?,
          let intent = config["intent"] as? String?,
          let metadata = config["metadata"] as? [String: String]?,
          let paymentSource = config["paymentSource"] as? String?,
          let cartItems = config["cartItems"] as? [Gr4vyCartItem]?,
          let debugMode = config["debugMode"] as? Bool
    else {
        EmbedReactNativeEvents.emitter.sendEvent(
          withName: "onEvent",
          body: [
            "name": "generalError",
            "data": [
              "message" : "Invalid configuration"
            ]
          ]
        )
        return
    }
      
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
