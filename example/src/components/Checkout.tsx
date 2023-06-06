import { FlatList, Text, View, StyleSheet } from 'react-native'
import React from 'react'
import { products, shipping, subtotal, total } from '../constants/data'
import { Button } from '../components/Button'
import { formatCurrency } from '../utils/currency'
import { Item } from './Item'

interface CheckoutProps {
  onCheckout: () => void
}

export const Checkout = ({ onCheckout }: CheckoutProps) => {
  return (
    <View style={styles.checkout}>
      <Text style={styles.title}>Shopping Cart</Text>
      <FlatList
        style={styles.products}
        data={products}
        renderItem={({ item }) => <Item product={item} />}
        keyExtractor={(item) => item.id}
      />
      <View style={styles.summary}>
        <View style={styles.summaryItem}>
          <Text>Subtotal</Text>
          <Text>{formatCurrency(subtotal)}</Text>
        </View>
        <View style={styles.summaryItem}>
          <Text>Shipping costs</Text>
          <Text>{formatCurrency(shipping)}</Text>
        </View>
        <View style={styles.summaryItem}>
          <Text style={styles.summaryTotal}>Total</Text>
          <Text style={styles.summaryTotal}>{formatCurrency(total)}</Text>
        </View>
      </View>
      <Button onPress={onCheckout}>Checkout</Button>
    </View>
  )
}

const styles = StyleSheet.create({
  checkout: {
    padding: 16,
    display: 'flex',
    flex: 1,
    flexDirection: 'column',
    gap: 24,
    backgroundColor: '#fff',
  },
  title: {
    fontSize: 16,
    textTransform: 'uppercase',
    color: '#474C50',
    fontWeight: '700',
  },
  products: {
    borderTopColor: '#D9DEE3',
    borderTopWidth: 1,
  },
  summary: {
    display: 'flex',
    flexDirection: 'column',
    gap: 8,
  },
  summaryItem: {
    display: 'flex',
    flexDirection: 'row',
    justifyContent: 'space-between',
    paddingHorizontal: 8,
  },
  summaryTotal: {
    fontWeight: '700',
  },
})
