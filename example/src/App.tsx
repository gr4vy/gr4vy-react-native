/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 */

import { GR4VY_ID, TOKEN } from '@env'
import React from 'react'
import {
  SafeAreaView,
  ScrollView,
  StatusBar,
  useColorScheme,
  View,
  Button,
} from 'react-native'

import { Colors, Header } from 'react-native/Libraries/NewAppScreen'

import EmbedReactNative, {
  EmbedReactNativeEventEmitter,
  Gr4vyTransactionResult,
  Gr4vyPaymentMethod,
} from '@gr4vy/embed-react-native'

function App(): JSX.Element {
  const isDarkMode = useColorScheme() === 'dark'

  const backgroundStyle = {
    backgroundColor: isDarkMode ? Colors.darker : Colors.lighter,
  }

  const onPaymentMethodSelected = (paymentMethod: Gr4vyPaymentMethod) => {
    console.log('onPaymentMethodSelected', paymentMethod)
  }

  const startPayment = () => {
    const gr4vyId = `${GR4VY_ID}`
    const env = 'sandbox'
    const token = `${TOKEN}`
    const amount = 1299
    const currency = 'USD'
    const country = 'US'
    const buyerId = 'baa7b3b3-a4f1-49e3-afb0-0f41b48f5aa2'
    const externalIdentifier = null
    const store = 'ask'
    const display = 'all'
    const intent = 'capture'
    const metadata = {}
    const paymentSource = null
    const cartItems = null
    const debugMode = true

    const onPaymentMethodSelectedSubscription =
      EmbedReactNativeEventEmitter.addListener(
        'onPaymentMethodSelected',
        onPaymentMethodSelected
      )

    EmbedReactNative.showPaymentSheet(
      gr4vyId,
      token,
      amount,
      currency,
      country,
      buyerId,
      externalIdentifier,
      store,
      display,
      intent,
      metadata,
      paymentSource,
      cartItems,
      env,
      debugMode,
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
    <SafeAreaView style={backgroundStyle}>
      <StatusBar
        barStyle={isDarkMode ? 'light-content' : 'dark-content'}
        backgroundColor={backgroundStyle.backgroundColor}
      />
      <ScrollView
        contentInsetAdjustmentBehavior="automatic"
        style={backgroundStyle}
      >
        <Header />
        <View
          style={{
            padding: 10,
            backgroundColor: isDarkMode ? Colors.black : Colors.white,
          }}
        >
          <Button title="Start Payment" onPress={startPayment} />
        </View>
      </ScrollView>
    </SafeAreaView>
  )
}

export default App
