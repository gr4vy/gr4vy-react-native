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
  approvalUrl?: string
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

export type Gr4vyBillingDetails = {
  firstName?: string
  lastName?: string
  emailAddress?: string
  phoneNumber?: string
  address?: {
    houseNumberOrName?: string
    line1?: string
    line2?: string
    organization?: string
    city?: string
    postalCode?: string
    country?: string
    state?: string
    stateCode?: string
  }
  taxId?: {
    value?: string
    kind?: string
  }
}

export type Gr4vyShippingDetails = Omit<Gr4vyBillingDetails, 'taxId'>

export type Gr4vyConfig = {
  gr4vyId: string
  environment?: string | undefined
  token: string
  amount: number
  currency: string
  country: string
  buyerId?: string | undefined
  externalIdentifier?: string | undefined
  store?: 'ask' | boolean
  display?: 'all' | 'addOnly' | 'storedOnly' | 'supportsTokenization'
  intent?: 'authorize' | 'preferAuthorize' | 'capture'
  metadata?: Record<string, string>
  paymentSource?: 'installment' | 'recurring' | undefined
  applePayMerchantId?: string
  cartItems?:
    | Array<{
        name: string
        quantity: number
        unitAmount: number
        discountAmount?: number
        taxAmount?: number
        externalIdentifier?: string
        sku?: string
        productUrl?: string
        imageUrl?: string
        categories?: string[]
        productType?: string
      }>
    | undefined
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
  merchantAccountId?: string
  connectionOptions?: Record<string, unknown>
  buyer?: {
    displayName?: string | null
    externalIdentifier?: string | null
    billingDetails?: Gr4vyBillingDetails
    shippingDetails?: Gr4vyShippingDetails
  }
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
