import { GR4VY_ID, TOKEN } from '@env'
import React, { useEffect } from 'react'
import { SafeAreaView, StatusBar } from 'react-native'
import { Checkout } from './components/Checkout'
import EmbedReactNative, {
  EmbedReactNativeEventEmitter,
  Gr4vyConfig,
  Gr4vyEvent,
} from '@gr4vy/embed-react-native'
import { products, total } from './constants/data'

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

const config: Gr4vyConfig = {
  gr4vyId: `${GR4VY_ID}`,
  token: `${TOKEN}`,
  amount: total,
  currency: 'USD',
  country: 'US',
  intent: 'capture',
  applePayMerchantId: 'merchant.com.gr4vy.demo',
  cartItems,
  theme: {
    fonts: {
      body: 'google:Poppins',
    },
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
  },
  debugMode: true,
}

const onEvent = (event: Gr4vyEvent) => {
  const { name, data } = event
  console[name === 'generalError' ? 'error' : 'log'](name, data)
}

function App(): JSX.Element {
  const handleCheckout = () => {
    EmbedReactNative.showPaymentSheet(config)
  }

  useEffect(() => {
    const onEventSubscription = EmbedReactNativeEventEmitter.addListener(
      'onEvent',
      onEvent
    )

    return () => {
      onEventSubscription.remove()
    }
  }, [])

  return (
    <SafeAreaView>
      <StatusBar barStyle="default" />
      <Checkout onCheckout={handleCheckout} />
    </SafeAreaView>
  )
}

export default App
