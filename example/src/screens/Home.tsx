import React, { useEffect } from 'react'
import { SafeAreaView, StatusBar, StyleSheet } from 'react-native'
import EmbedReactNative, {
  EmbedReactNativeEventEmitter,
  Gr4vyEvent,
} from '@gr4vy/embed-react-native'
import { Checkout } from '../components/Checkout'
import { useConfig } from '../contexts/Config'

const onEvent = (event: Gr4vyEvent) => {
  const { name, data } = event
  console[name === 'generalError' ? 'error' : 'log'](name, data)
}

export const Home = () => {
  const { config } = useConfig()

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
    <SafeAreaView style={styles.container}>
      <StatusBar barStyle="default" />
      <Checkout onCheckout={handleCheckout} />
    </SafeAreaView>
  )
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
  },
})
