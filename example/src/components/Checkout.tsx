import { Pressable, FlatList, Text, View, StyleSheet } from 'react-native'
import { products, shipping, subtotal, total } from '../constants/data'
import { Item } from './Item'
import React from 'react'
import { formatCurrency } from '../utils/currency'

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
      <Pressable style={styles.button} onPress={onCheckout}>
        <Text style={styles.buttonText}>Checkout</Text>
      </Pressable>
    </View>
  )
}

const styles = StyleSheet.create({
  checkout: {
    padding: 16,
    display: 'flex',
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
  button: {
    backgroundColor: '#1B4889',
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
    borderRadius: 4,
    height: 48,
  },
  buttonText: {
    color: '#fff',
    fontSize: 16,
    fontWeight: '500',
  },
})
