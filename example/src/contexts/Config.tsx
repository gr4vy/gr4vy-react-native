import type { Gr4vyConfig } from '@gr4vy/embed-react-native'

import React, {
  PropsWithChildren,
  createContext,
  useContext,
  useEffect,
  useState,
} from 'react'
import { config as defaultConfig, fetchEmbedToken } from '../utils/config'
import { getData, storeData } from '../utils/storage'
import { pick } from '../utils/pick'

interface Config {
  config: Gr4vyConfig
  setConfig: (value: Gr4vyConfig, store?: boolean) => void
}

export const ConfigContext = createContext<{
  config: Gr4vyConfig
  setConfig: (value: Gr4vyConfig, store?: boolean) => void
}>(null as unknown as Config)

export const ConfigProvider = ({ children }: PropsWithChildren<{}>) => {
  const [config, setConfig] = useState<Gr4vyConfig>(defaultConfig)
  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  const { token, ...settings } = config

  const getToken = async () => {
    if (!defaultConfig.token) {
      const data = await fetchEmbedToken(
        pick(config, [
          'amount',
          'currency',
          'buyerId',
          'buyerExternalIdentifier',
          'metadata',
          'cartItems',
          'merchantAccountId',
        ])
      )
      setConfig((prevState) => ({ ...prevState, token: data }))
    }
  }

  const setAndStoreConfig = (value: Gr4vyConfig) => {
    setConfig(value)
    storeData({
      ...value,
      gr4vyId: defaultConfig.gr4vyId,
    })
  }

  useEffect(() => {
    const getStoredConfig = async () => {
      const data = await getData()
      const storedConfig = {
        ...data,
        gr4vyId: defaultConfig.gr4vyId,
      }
      setConfig({ ...config, ...storedConfig })
    }
    getStoredConfig()
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [])

  useEffect(() => {
    getToken()
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [JSON.stringify(settings)])

  return (
    <ConfigContext.Provider value={{ config, setConfig: setAndStoreConfig }}>
      {children}
    </ConfigContext.Provider>
  )
}

export const useConfig = () => {
  return useContext(ConfigContext)
}
