import type { Gr4vyConfig } from '@gr4vy/embed-react-native'

import React, { useState } from 'react'
import { StyleSheet, View, Text, ScrollView } from 'react-native'
import { getVersion, getBuildNumber } from 'react-native-device-info'
import { Button } from '../components/Button'
import { Picker } from '../components/Picker'
import { darkTheme } from '../utils/config'
import { useConfig } from '../contexts/Config'

export const Settings = () => {
  const { config, setConfig } = useConfig()
  const [tmpConfig, setTmpConfig] = useState<Gr4vyConfig>(config)

  const save = () => {
    setConfig(tmpConfig)
  }

  return (
    <View style={styles.container}>
      <View style={styles.info}>
        <Text>
          app version: {getVersion()}({getBuildNumber()})
        </Text>
        <Text>gr4vyId: {config.gr4vyId}</Text>
        <Text>environment: {config.environment}</Text>
      </View>
      <ScrollView style={styles.form}>
        <View style={styles.inputGroup}>
          <Text style={styles.label}>Locale</Text>
          <Picker
            selectedValue={tmpConfig?.locale}
            onValueChange={(value: string) =>
              setTmpConfig({ ...tmpConfig, locale: value })
            }
          >
            <Picker.Item label="US" value="en-us" />
            <Picker.Item label="BR" value="pt-br" />
            <Picker.Item label="IT" value="it-it" />
            <Picker.Item label="ES" value="es-es" />
            <Picker.Item label="FR" value="fr-fr" />
          </Picker>
        </View>
        <View style={styles.inputGroup}>
          <Text style={styles.label}>Country</Text>
          <Picker
            selectedValue={tmpConfig?.country}
            onValueChange={(value: string) =>
              setTmpConfig({ ...tmpConfig, country: value })
            }
          >
            <Picker.Item label="United States" value="US" />
            <Picker.Item label="Great Britain" value="GB" />
            <Picker.Item label="Brazil" value="BR" />
            <Picker.Item label="Italy" value="IT" />
            <Picker.Item label="Spain" value="ES" />
            <Picker.Item label="France" value="FR" />
          </Picker>
        </View>
        <View style={styles.inputGroup}>
          <Text style={styles.label}>Currency</Text>
          <Picker
            selectedValue={tmpConfig?.currency}
            onValueChange={(value: string) =>
              setTmpConfig({ ...tmpConfig, currency: value })
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
            selectedValue={tmpConfig?.theme ? 'dark' : 'light'}
            onValueChange={(value: string) =>
              setTmpConfig({
                ...tmpConfig,
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
    maxWidth: '70%',
    marginBottom: 16,
  },
  label: {
    fontSize: 16,
    textAlign: 'center',
  },
})
