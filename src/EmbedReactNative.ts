import { NativeModules, NativeEventEmitter, Platform } from 'react-native'

const { EmbedReactNative, EmbedReactNativeEvents } = NativeModules

export interface Gr4vyTransactionResult {
  sucess: boolean
  transactionId: string
  status: string
  paymentMethodId?: string
}

export interface Gr4vyPaymentMethod {
  id: number
  method: string
  mode: string
}

export interface Gr4vyInterface {
  showPaymentSheet(
    gr4vId: string,
    token: string,
    amount: number,
    currency: string,
    country: string,
    buyerId?: string | null,
    externalIdentifier?: string | null,
    store?: 'ask' | boolean,
    display?: 'all' | 'addOnly' | 'storedOnly' | 'supportsTokenization',
    intent?: 'authorize' | 'capture',
    metadata?: Record<string, string>,
    paymentSource?: 'installment' | 'recurring' | null,
    cartItems?: {
      name: string
      quantity: string
      unitAmount: string
    } | null,
    environment?: string | null,
    debugMode?: boolean,
    onError?: (error: string) => void,
    onTransaction?: (transaction: Gr4vyTransactionResult) => void
  ): void
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
