import React from 'react'
import { StyleSheet, Pressable, Text, PressableProps } from 'react-native'

export const Button = ({ children, ...rest }: PressableProps) => {
  return (
    <Pressable style={styles.button} {...rest}>
      <Text style={styles.buttonText}>{children}</Text>
    </Pressable>
  )
}

const styles = StyleSheet.create({
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
