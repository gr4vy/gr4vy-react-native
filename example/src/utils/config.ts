import type { Gr4vyConfig } from '@gr4vy/embed-react-native'

import { GR4VY_ID, TOKEN } from '@env'
import { products, total } from '../constants/data'

let cartItems = products.map(({ title, price }) => ({
  name: title,
  quantity: 1,
  unitAmount: price,
}))

const shipping = {
  name: 'shipping',
  quantity: 1,
  unitAmount: 375,
}

cartItems.push(shipping)

export const config: Gr4vyConfig = {
  gr4vyId: `${GR4VY_ID || 'spider'}`,
  token: `${TOKEN || ''}`,
  environment: 'sandbox',
  buyerExternalIdentifier: 'user-001',
  amount: total,
  currency: 'GBP',
  country: 'GB',
  store: 'ask',
  intent: 'capture',
  applePayMerchantId: 'merchant.com.gr4vy.demo',
  cartItems,
  debugMode: true,
}

export const darkTheme: Gr4vyConfig['theme'] = {
  colors: {
    text: '#fff',
    subtleText: '#a1b0bd',
    labelText: '#fff',
    primary: '#fff',
    pageBackground: '#1d334b',
    containerBackgroundUnchecked: '#1d334b',
    containerBackground: '#2c4765',
    containerBorder: '#304c6a',
    inputBorder: '#f2f2f2',
    inputBackground: '#2a4159',
    inputText: '#fff',
    inputRadioBorder: '#fff',
    inputRadioBorderChecked: '#fff',
    danger: '#ff556a',
    dangerBackground: '#2c4765',
    dangerText: '#fff',
    info: '#3ea2ff',
    infoBackground: '#e7f2fb',
    infoText: '#0367c4',
    focus: '#4844ff',
    headerText: '#ffffff',
    headerBackground: '#2c4765',
  },
  borderWidths: {
    container: 'thin',
    input: 'thin',
  },
  radii: {
    container: 'subtle',
    input: 'subtle',
  },
  shadows: {
    focusRing: '0 0 0 2px #ffffff, 0 0 0 4px #4844ff',
  },
}
