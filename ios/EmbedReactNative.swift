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
                 applePayMerchantId: String?,
                 theme: Gr4vyTheme?,
                 buyerExternalIdentifier: String?,
                 locale: String?,
                 statementDescriptor: Gr4vyStatementDescriptor?,
                 requireSecurityCode: Bool?,
                 shippingDetailsId: String?,
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
                              applePayMerchantId: applePayMerchantId,
                              theme: theme,
                              buyerExternalIdentifier: buyerExternalIdentifier,
                              locale: locale,
                              statementDescriptor: statementDescriptor,
                              requireSecurityCode: requireSecurityCode,
                              shippingDetailsId: shippingDetailsId,
                              debugMode: debugMode) else {
        completion(nil)
        return
      }

      completion(gr4vy)
    })
  }
    
  func buildTheme(_ source: [String: [String: String?]?]?) -> Gr4vyTheme? {
    guard let theme = source,
          let fonts = theme["fonts"] ?? [:],
          let fontsBody = fonts["body"] ?? "",
          let colors = theme["colors"] ?? [:],
          let colorsText = colors["text"] ?? "",
          let colorsSubtleText = colors["subtleText"] ?? "",
          let colorsLabelText = colors["labelText"] ?? "",
          let colorsPrimary = colors["primary"] ?? "",
          let colorsPageBackground = colors["pageBackground"] ?? "",
          let colorsContainerBackgroundUnchecked = colors["containerBackgroundUnchecked"] ?? "",
          let colorsContainerBackground = colors["containerBackground"] ?? "",
          let colorsContainerBorder = colors["containerBorder"] ?? "",
          let colorsInputBorder = colors["inputBorder"] ?? "",
          let colorsInputBackground = colors["inputBackground"] ?? "",
          let colorsInputText = colors["inputText"] ?? "",
          let colorsInputRadioBorder = colors["inputRadioBorder"] ?? "",
          let colorsInputRadioBorderChecked = colors["inputRadioBorderChecked"] ?? "",
          let colorsDanger = colors["danger"] ?? "",
          let colorsDangerBackground = colors["dangerBackground"] ?? "",
          let colorsDangerText = colors["dangerText"] ?? "",
          let colorsInfo = colors["info"] ?? "",
          let colorsInfoBackground = colors["infoBackground"] ?? "",
          let colorsInfoText = colors["infoText"] ?? "",
          let colorsFocus = colors["focus"] ?? "",
          let colorsHeaderText = colors["headerText"] ?? "",
          let colorsHeaderBackground = colors["headerBackground"] ?? "",
          let borderWidths = theme["borderWidths"] ?? [:],
          let borderWidthsContainer = borderWidths["container"] ?? "",
          let borderWidthsInput = borderWidths["input"] ?? "",
          let radii = theme["radii"] ?? [:],
          let radiiContainer = radii["container"] ?? "",
          let radiiInput = radii["input"] ?? "",
          let shadows = theme["shadows"] ?? [:],
          let shadowsFocusRing = shadows["focusRing"] ?? ""
    else {
      return nil
    }

    return Gr4vyTheme(
      fonts: Gr4vyFonts(
        body: fontsBody
      ),
      colors: Gr4vyColours(
        text: colorsText,
        subtleText: colorsSubtleText,
        labelText: colorsLabelText,
        primary: colorsPrimary,
        pageBackground: colorsPageBackground,
        containerBackgroundUnchecked: colorsContainerBackgroundUnchecked,
        containerBackground: colorsContainerBackground,
        containerBorder: colorsContainerBorder,
        inputBorder: colorsInputBorder,
        inputBackground: colorsInputBackground,
        inputText: colorsInputText,
        inputRadioBorder: colorsInputRadioBorder,
        inputRadioBorderChecked: colorsInputRadioBorderChecked,
        danger: colorsDanger,
        dangerBackground: colorsDangerBackground,
        dangerText: colorsDangerText,
        info: colorsInfo,
        infoBackground: colorsInfoBackground,
        infoText: colorsInfoText,
        focus: colorsFocus,
        headerText: colorsHeaderText,
        headerBackground: colorsHeaderBackground
      ),
      borderWidths: Gr4vyBorderWidths(
        container: borderWidthsContainer,
        input: borderWidthsInput
      ),
      radii: Gr4vyRadii(
        container: radiiContainer,
        input: radiiInput
      ),
      shadows: Gr4vyShadows(
        focusRing: shadowsFocusRing
      )
    )
  }
    
  func convertStatementDescriptor(_ source: [String: String?]?) -> Gr4vyStatementDescriptor? {
    guard let statementDescriptor = source,
          let name = statementDescriptor["name"] ?? "",
          let description = statementDescriptor["description"] ?? "",
          let phoneNumber = statementDescriptor["phoneNumber"] ?? "",
          let city = statementDescriptor["city"] ?? "",
          let url = statementDescriptor["url"] ?? "" else {
        return nil
    }
      
    return Gr4vyStatementDescriptor(
        name: name,
        description: description,
        phoneNumber: phoneNumber,
        city: city,
        url: url
    )
  }
    
  func convertCartItems(_ cartItems: NSArray?) -> [Gr4vyCartItem] {
    guard let cartItems = cartItems else {
      return []
    }

    var result = [Gr4vyCartItem]()
    for item in cartItems {
      guard let dict = item as? [String: Any],
            let name = dict["name"] as? String,
            let quantity = dict["quantity"] as? Int,
            let unitAmount = dict["unitAmount"] as? Int else {
          return []
      }
      result.append(Gr4vyCartItem(name: name, quantity: quantity, unitAmount: unitAmount))
    }

    return result
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
          let cartItems = config["cartItems"] as? NSArray?,
          let environment = config["environment"] as? String?,
          let applePayMerchantId = config["applePayMerchantId"] as? String?,
          let theme = config["theme"] as? [String: [String: String?]?]?,
          let buyerExternalIdentifier = config["buyerExternalIdentifier"] as? String?,
          let locale = config["locale"] as? String?,
          let statementDescriptor = config["statementDescriptor"] as? [String: String?]?,
          let requireSecurityCode = config["requireSecurityCode"] as? Bool?,
          let shippingDetailsId = config["shippingDetailsId"] as? String?,
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
             cartItems: convertCartItems(cartItems),
             environment: environment,
             applePayMerchantId: applePayMerchantId,
             theme: buildTheme(theme),
             buyerExternalIdentifier: buyerExternalIdentifier,
             locale: locale,
             statementDescriptor: convertStatementDescriptor(statementDescriptor),
             requireSecurityCode: requireSecurityCode,
             shippingDetailsId: shippingDetailsId,
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
                      "success": false,
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
                // TODO: remove on the next SDK update, needs to be here
                // otherwise a build error is thrown.
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
