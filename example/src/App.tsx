import { GR4VY_ID, TOKEN } from '@env'
import React from 'react'
import { SafeAreaView, StatusBar } from 'react-native'
import { Checkout } from './components/Checkout'
import EmbedReactNative, {
  EmbedReactNativeEventEmitter,
  Gr4vyEvent,
} from '@gr4vy/embed-react-native'
import { total } from './constants/data'

const config = {
  gr4vyId: `${GR4VY_ID}`,
  env: 'sandbox',
  token: `${TOKEN}`,
  amount: total,
  currency: 'USD',
  country: 'US',
  buyerId: null,
  externalIdentifier: null,
  store: 'ask',
  display: 'all',
  intent: 'capture',
  metadata: {},
  paymentSource: null,
  cartItems: null,
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
    EmbedReactNative.showPaymentSheet(
      config.gr4vyId,
      config.token,
      config.amount,
      config.currency,
      config.country,
      config.buyerId,
      config.externalIdentifier,
      config.store,
      config.display,
      config.intent,
      config.metadata,
      config.paymentSource,
      config.cartItems,
      config.env,
      config.debugMode
    )
  }

  return (
    <SafeAreaView>
      <StatusBar barStyle="default" />
      <Checkout onCheckout={handleCheckout} />
    </SafeAreaView>
  )
}

export default App
