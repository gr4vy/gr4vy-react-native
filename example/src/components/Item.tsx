import type { Product } from '../constants/data'
import { StyleSheet, Text, View, Image } from 'react-native'
import { formatCurrency } from '../utils/currency'
import React from 'react'

interface ItemProps {
  product: Product
}

export const Item = ({ product }: ItemProps) => {
  return (
    <View style={styles.item}>
      <View style={styles.details}>
        <Image
          style={styles.img}
          source={{
            uri: product.img,
          }}
        />
        <Text>{product.title}</Text>
      </View>
      <Text>{formatCurrency(product.price)}</Text>
    </View>
  )
}

const styles = StyleSheet.create({
  item: {
    display: 'flex',
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    padding: 8,
    borderBottomColor: '#D9DEE3',
    borderBottomWidth: 1,
  },
  details: {
    display: 'flex',
    flexDirection: 'row',
    alignItems: 'center',
    gap: 24,
  },
  img: {
    width: 48,
    height: 48,
  },
})
