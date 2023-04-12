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
  name:
    | 'transactionCreated'
    | 'transactionFailed'
    | 'paymentMethodSelected'
    | 'generalError'
  data: Gr4vyError | Gr4vyTransactionResult
}

export type Gr4vyConfig = {
  gr4vId: string
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
  cartItems?: {
    name: string
    quantity: string
    unitAmount: string
  } | null
  environment?: string | null
  debugMode?: boolean
}

export interface Gr4vyInterface {
  showPaymentSheet(...args: Gr4vyConfig[keyof Gr4vyConfig][]): void
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
