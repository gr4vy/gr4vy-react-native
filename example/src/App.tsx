import { GR4VY_ID, TOKEN } from '@env'
import React from 'react'
import { SafeAreaView, StatusBar } from 'react-native'
import { Checkout } from './components/Checkout'
import EmbedReactNative, {
  EmbedReactNativeEventEmitter,
  Gr4vyConfig,
  Gr4vyEvent,
} from '@gr4vy/embed-react-native'
import { total } from './constants/data'

const config: Gr4vyConfig = {
  gr4vyId: `${GR4VY_ID}`,
  environment: 'sandbox',
  token: `${TOKEN}`,
  amount: total,
  currency: 'USD',
  country: 'US',
  store: 'ask',
  display: 'all',
  intent: 'capture',
  theme: {
    fonts: {
      body: 'google:Lato',
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
  const onEventSubscription = EmbedReactNativeEventEmitter.addListener(
    'onEvent',
    (event: Gr4vyEvent) => {
      onEvent(event)
      onEventSubscription.remove()
    }
  )

  const handleCheckout = () => {
    EmbedReactNative.showPaymentSheet(config)
  }

  return (
    <SafeAreaView>
      <StatusBar barStyle="default" />
      <Checkout onCheckout={handleCheckout} />
    </SafeAreaView>
  )
}

export default App
