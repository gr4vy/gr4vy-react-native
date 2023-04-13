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
