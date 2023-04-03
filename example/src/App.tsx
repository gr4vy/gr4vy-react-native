import { GR4VY_ID, TOKEN } from '@env'
import React from 'react'
import { SafeAreaView, StatusBar } from 'react-native'
import { Checkout } from './components/Checkout'
import EmbedReactNative, {
  EmbedReactNativeEventEmitter,
  Gr4vyTransactionResult,
  Gr4vyPaymentMethod,
} from '@gr4vy/embed-react-native'
import { total } from './constants/data'

const onPaymentMethodSelected = (paymentMethod: Gr4vyPaymentMethod) => {
  console.log('onPaymentMethodSelected', paymentMethod)
}

const config = {
  gr4vyId: `${GR4VY_ID}`,
  env: 'sandbox',
  token: `${TOKEN}`,
  amount: total,
  currency: 'USD',
  country: 'US',
}

function App(): JSX.Element {
  const onPaymentMethodSelectedSubscription =
    EmbedReactNativeEventEmitter.addListener(
      'onPaymentMethodSelected',
      onPaymentMethodSelected
    )

  const handleCheckout = () => {
    EmbedReactNative.showPaymentSheet(
      config.gr4vyId,
      config.token,
      config.amount,
      config.currency,
      config.country,
      null,
      config.env,
      (error: string) => {
        console.error(error)
        onPaymentMethodSelectedSubscription.remove()
      },
      (transactionResult: Gr4vyTransactionResult) => {
        console.log(transactionResult)
        onPaymentMethodSelectedSubscription.remove()
      }
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
