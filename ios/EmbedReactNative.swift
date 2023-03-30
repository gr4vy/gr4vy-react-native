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
                 environment: String?,
                 debugMode: Bool = false,
                 completion: @escaping(_ gr4vy: Gr4vy?) -> Void)  {
    DispatchQueue.main.async(execute: {
      guard let gr4vy = Gr4vy(gr4vyId: gr4vyId,
                              token: token,
                              amount: amount,
                              currency: currency,
                              country: country,
                              buyerId: buyerId,
                              externalIdentifier: externalIdentifier,
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
    environment: String?,
    debugMode: Bool,
    errorCallback: @escaping RCTResponseSenderBlock,
    successCallback: @escaping RCTResponseSenderBlock)
  {
    gr4vyInit(gr4vyId: gr4vyId,
             token: token,
             amount: Int(amount),
             currency: currency,
             country: country,
             buyerId: buyerId,
             externalIdentifier: externalIdentifier,
             environment: environment,
             debugMode: debugMode) { (gr4vy) in
      if gr4vy == nil {
        errorCallback([NSNull(), "Failed to initialize Gr4vy SDK"])
      }

      DispatchQueue.main.async(execute: {
          let presentingViewController = RCTPresentedViewController()
        
          gr4vy!.launch(
            presentingViewController: presentingViewController!,
            onEvent: { event in
              
              switch event {
              case .transactionFailed(let transactionID, let status, let paymentMethodID):
                successCallback([[
                  "success": false,
                  "transactionId": transactionID,
                  "status": status,
                  "paymentMethodId": paymentMethodID as Any
                ]])
                break
              case .transactionCreated(let transactionID, let status, let paymentMethodID):
                successCallback([[
                  "success": true,
                  "transactionId": transactionID,
                  "status": status,
                  "paymentMethodId": paymentMethodID as Any
                ]])
                break
              case .generalError(let error):
                errorCallback([error.description, self.GR4VY_ERROR])
                break
              case .paymentMethodSelected(let id, let method, let mode):
                EmbedReactNativeEvents.emitter.sendEvent(
                  withName: "onPaymentMethodSelected",
                  body: ["id" : id, "method": method, "mode": mode]
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
