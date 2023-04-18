import Foundation
import gr4vy_ios

//public struct Gr4vyFonts {
//    public var body: String?
//
//    public init(body: String? = nil) {
//        self.body = body
//    }
//}
//
//public struct Gr4vyTheme {
//    public var fonts: Gr4vyFonts?
//
//    public init(fonts: Gr4vyFonts? = nil) {
//        self.fonts = fonts
//    }
//
//    public init(bodyFont: String? = nil) {
//        let fonts = Gr4vyFonts(body: bodyFont)
//        self.init(fonts: fonts)
//    }
//}

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
                 theme: Gr4vyTheme?,
                 debugMode: Bool = false,
                 completion: @escaping(_ gr4vy: Gr4vy?) -> Void)  {
    var paymentSourceConverted: Gr4vyPaymentSource?
    if paymentSource != nil {
        paymentSourceConverted = Gr4vyPaymentSource(rawValue: paymentSource!)
    }

//    var cartItemsConverted: [Gr4vyCartItem]?
//    if let cartItems = cartItems {
//      cartItemsConverted = cartItems.compactMap { (item: RCTCartItem) -> Gr4vyCartItem? in
//        return try? Gr4vyCartItem(name: item.name, quantity: item.quantity, unitAmount: item.unitAmount)
//      }
//    }
    
//    var themeConverted: Gr4vyTheme?
//    if theme != nil {
//        themeConverted = Gr4vyTheme(fonts: Gr4vyFonts(body: "google:Lato"))
//    }

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
                              theme: theme,
                              debugMode: debugMode) else {
        completion(nil)
        return
      }

      completion(gr4vy)
    })
  }
    
  func buildTheme(_ source: [String: Any]?) -> Gr4vyTheme? {
    guard let theme = source,
          let fonts = theme["fonts"] as? [String: String],
          let fontsBody = fonts["body"],
          let colors = theme["colors"] as? [String: String],
          let colorsText = colors["text"],
          let colorsSubtleText = colors["subtleText"],
          let colorsLabelText = colors["labelText"],
          let colorsPrimary = colors["primary"],
          let colorsPageBackground = colors["pageBackground"],
          let colorsContainerBackgroundUnchecked = colors["containerBackgroundUnchecked"],
          let colorsContainerBorder = colors["containerBorder"],
          let colorsInputBorder = colors["inputBorder"],
          let colorsInputBackground = colors["inputBackground"],
          let colorsInputText = colors["inputText"],
          let colorsDanger = colors["danger"],
          let colorsDangerBackground = colors["dangerBackground"],
          let colorsDangerText = colors["dangerText"],
          let colorsInfo = colors["info"],
          let colorsInfoBackground = colors["infoBackground"],
          let colorsInfoText = colors["infoText"],
          let colorsFocus = colors["focus"],
          let colorsHeaderText = colors["headerText"],
          let colorsHeaderBackground = colors["headerBackground"],
          let borderWidths = theme["borderWidths"] as? [String: String],
          let borderWidthsContainer = borderWidths["container"],
          let borderWidthsInput = borderWidths["input"],
          let radii = theme["radii"] as? [String: String],
          let radiiContainer = radii["container"],
          let radiiInput = radii["input"],
          let shadows = theme["shadows"] as? [String: String],
          let shadowsFocusRing = shadows["focusRing"]
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
        containerBackgroundUnchecked: colorsContainerBackgroundUnchecked
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
//    let configTheme = config["theme"] as? String
//    if let configThemeUTF8 = configTheme?.utf8 {
//        let configData = Data(configThemeUTF8)
//        guard let object = try? JSONSerialization.jsonObject(with: configData, options: []),
//              let data = try? JSONSerialization.data(withJSONObject: object, options: [.prettyPrinted]),
//              let myTest = NSString(data: data, encoding: String.Encoding.utf8.rawValue) else {
//            EmbedReactNativeEvents.emitter.sendEvent(
//              withName: "onEvent",
//              body: [
//                "name": "generalError",
//                "data": [
//                  "message" : "Nope"
//                ]
//              ]
//            )
//            return
//        }
//        EmbedReactNativeEvents.emitter.sendEvent(
//          withName: "onEvent",
//          body: [
//            "name": "generalError",
//            "data": [
//              "message" : "Test \(myTest)"
//            ]
//          ]
//        )
//    }
    
//      guard let configTheme = config["theme"] as? String,
//            let object = try? JSONSerialization.jsonObject(with: Data(configTheme.utf8), options: []) else {
//          EmbedReactNativeEvents.emitter.sendEvent(
//            withName: "onEvent",
//            body: [
//              "name": "generalError",
//              "data": [
//                "message" : "Fuck"
//              ]
//            ]
//          )
//          return
//      }
      
//      do {
//          let configTheme = config["theme"] as? String
//          if let configThemeUTF8 = configTheme?.utf8 {
//              let configData = Data(configThemeUTF8)
//              let object = try? JSONSerialization.jsonObject(with: configData, options: [])
//          }
//
//          EmbedReactNativeEvents.emitter.sendEvent(
//            withName: "onEvent",
//            body: [
//              "name": "generalError",
//              "data": [
//                "message" : "WHAT: \(configTheme)"
//              ]
//            ]
//          )
//      } catch let error {
//          EmbedReactNativeEvents.emitter.sendEvent(
//            withName: "onEvent",
//            body: [
//              "name": "generalError",
//              "data": [
//                "message" : "ERROR: \(error)"
//              ]
//            ]
//          )
//          return
//      }
      
//      guard let myTest = try? JSONSerialization.data(withJSONObject: config["theme"], options: .withoutEscapingSlashes) else {
//          EmbedReactNativeEvents.emitter.sendEvent(
//            withName: "onEvent",
//            body: [
//              "name": "generalError",
//              "data": [
//                "message" : "Bro"
//              ]
//            ]
//          )
//          return
//      }
      
//      EmbedReactNativeEvents.emitter.sendEvent(
//        withName: "onEvent",
//        body: [
//          "name": "generalError",
//          "data": [
//            "message" : "Bro \(String(decoding: myTest, as: UTF8.self))"
//          ]
//        ]
//      )
    
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
//          let theme = config["theme"] as? Gr4vyTheme?,
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
      
//      guard let themeConfig = config["theme"] as? [String: Any],
//            let fonts = themeConfig["fonts"] as? [String: String],
//            let body = fonts["body"]
//      else {
//          // Handle configuration error
//          return
//      }
//
//      let theme = Gr4vyTheme(fonts: Gr4vyFonts(body: body))
      
      let theme = config["theme"] as? [String: Any]
      
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
             theme: buildTheme(theme),
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
