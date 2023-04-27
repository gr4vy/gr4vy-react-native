import { NativeModules, NativeEventEmitter, Platform } from 'react-native'

const { EmbedReactNative, EmbedReactNativeEvents } = NativeModules

export interface Gr4vyError {
  message: string
}

export interface Gr4vyTransactionResult {
  success: boolean
  transactionId: string
  status: string
  paymentMethodId?: string
}

export interface Gr4vyPaymentMethod {
  id: number
  method: string
  mode: string
}

export type Gr4vyEvent = {
  name: 'transactionCreated' | 'transactionFailed' | 'generalError'
  data: Gr4vyError | Gr4vyTransactionResult
}

export type Gr4vyConfig = {
  gr4vyId: string
  environment?: string | null
  token: string
  amount: number
  currency: string
  country: string
  buyerId?: string | null
  externalIdentifier?: string | null
  store?: 'ask' | boolean
  display?: 'all' | 'addOnly' | 'storedOnly' | 'supportsTokenization'
  intent?: 'authorize' | 'capture'
  metadata?: Record<string, string>
  paymentSource?: 'installment' | 'recurring' | null
  applePayMerchantId?: string
  cartItems?: Array<{
    name: string
    quantity: number
    unitAmount: number
  }> | null
  theme?: {
    fonts?: {
      body?: string
    }
    borderWidths?: {
      container?: 'none' | 'thin' | 'thick'
      input?: 'thin' | 'thick'
    }
    colors?: {
      primary?: string
      text?: string
      subtleText?: string
      labelText?: string
      containerBackgroundUnchecked?: string
      containerBackground?: string
      containerBorder?: string
      pageBackground?: string
      inputBorder?: string
      inputText?: string
      inputBackground?: string
      inputRadioBorder?: string
      inputRadioBorderChecked?: string
      info?: string
      infoBackground?: string
      infoText?: string
      danger?: string
      dangerBackground?: string
      dangerText?: string
      focus?: string
      headerBackground?: string
      headerText?: string
    }
    radii?: {
      input?: 'none' | 'subtle' | 'rounded'
      container?: 'none' | 'subtle' | 'rounded'
    }
    shadows?: {
      focusRing?: string
    }
  }
  buyerExternalIdentifier?: string
  locale?: string
  statementDescriptor?: {
    name?: string
    description?: string
    phoneNumber?: string
    city?: string
    url?: string
  }
  requireSecurityCode?: boolean
  shippingDetailsId?: string
  debugMode?: boolean
}

export interface Gr4vyInterface {
  showPaymentSheet(config: Gr4vyConfig): void
}

const LINKING_ERROR =
  `The package '@gr4vy/embed-react-native' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo Go\n'

export const EmbedReactNativeEventEmitter = new NativeEventEmitter(
  EmbedReactNativeEvents
)

export default (EmbedReactNative
  ? EmbedReactNative
  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR)
        },
      }
    )) as Gr4vyInterface
