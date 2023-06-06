import React, { PropsWithChildren } from 'react'
import { Platform, StyleSheet, View } from 'react-native'
import { PickerProps, Picker as RNPicker } from '@react-native-picker/picker'

export const Picker = ({
  children,
  ...rest
}: PropsWithChildren<PickerProps<string>>) => {
  return (
    <View style={styles.container}>
      <RNPicker style={styles.style} itemStyle={styles.itemStyle} {...rest}>
        {children}
      </RNPicker>
    </View>
  )
}

Picker.Item = RNPicker.Item

const styles = StyleSheet.create({
  container: {
    marginTop: 16,
    overflow: 'hidden',
  },
  style: {
    marginTop: Platform.OS === 'ios' ? -64 : 0,
  },
  itemStyle: {
    alignSelf: 'center',
    width: '100%',
    fontSize: 16,
  },
})
