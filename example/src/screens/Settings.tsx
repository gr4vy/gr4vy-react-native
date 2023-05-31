import React from 'react'
import { StyleSheet, View, Text, ScrollView } from 'react-native'
import { Picker } from '@react-native-picker/picker'
import { getVersion, getBuildNumber } from 'react-native-device-info'
import { Button } from '../components/Button'
import { darkTheme } from '../utils/config'
import { useConfig } from '../contexts/Config'

export const Settings = () => {
  const { config, setConfig } = useConfig()

  const save = () => {
    setConfig(config)
  }

  return (
    <View style={styles.container}>
      <View style={styles.info}>
        <Text>
          app version: {getVersion()}({getBuildNumber()})
        </Text>
        <Text>gr4vyId: {config.gr4vyId}</Text>
      </View>
      <ScrollView style={styles.form}>
        <View style={styles.inputGroup}>
          <Text style={styles.label}>Locale</Text>
          <Picker
            selectedValue={config?.locale}
            onValueChange={(value: string) =>
              setConfig({ ...config, locale: value })
            }
          >
            <Picker.Item label="US" value="en-US" />
            <Picker.Item label="BR" value="pt-BR" />
            <Picker.Item label="IT" value="it-IT" />
            <Picker.Item label="ES" value="es-ES" />
          </Picker>
        </View>
        <View style={styles.inputGroup}>
          <Text style={styles.label}>Country</Text>
          <Picker
            selectedValue={config?.country}
            onValueChange={(value: string) =>
              setConfig({ ...config, country: value })
            }
          >
            <Picker.Item label="United States" value="US" />
            <Picker.Item label="Great Britain" value="GB" />
            <Picker.Item label="Brazil" value="BR" />
            <Picker.Item label="Italy" value="IT" />
            <Picker.Item label="Spain" value="ES" />
          </Picker>
        </View>
        <View style={styles.inputGroup}>
          <Text style={styles.label}>Currency</Text>
          <Picker
            selectedValue={config?.currency}
            onValueChange={(value: string) =>
              setConfig({ ...config, currency: value })
            }
          >
            <Picker.Item label="US Dollar" value="USD" />
            <Picker.Item label="British Pound" value="GBP" />
            <Picker.Item label="Brazilian Real" value="BRL" />
            <Picker.Item label="Euro" value="EUR" />
          </Picker>
        </View>
        <View style={styles.inputGroup}>
          <Text style={styles.label}>Theme</Text>
          <Picker
            selectedValue={config?.theme ? 'dark' : 'light'}
            onValueChange={(value: string) =>
              setConfig({
                ...config,
                theme: value === 'dark' ? darkTheme : undefined,
              })
            }
          >
            <Picker.Item label="Light" value="light" />
            <Picker.Item label="Dark" value="dark" />
          </Picker>
        </View>
      </ScrollView>
      <Button onPress={save}>Save</Button>
    </View>
  )
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
    padding: 16,
  },
  info: {
    paddingVertical: 16,
    marginBottom: 16,
    borderBottomColor: '#1B4889',
    borderBottomWidth: StyleSheet.hairlineWidth,
  },
  form: {
    marginVertical: 16,
  },
  inputGroup: {
    alignSelf: 'center',
    width: '100%',
    maxWidth: '75%',
    marginBottom: 16,
  },
  label: {
    fontSize: 16,
    textAlign: 'center',
  },
})
