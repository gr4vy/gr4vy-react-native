import type { Gr4vyConfig } from '@gr4vy/embed-react-native'

import React, {
  PropsWithChildren,
  createContext,
  useContext,
  useLayoutEffect,
  useState,
} from 'react'
import { config as defaultConfig } from '../utils/config'
import { getData, storeData } from '../utils/storage'

interface Config {
  config: Gr4vyConfig
  setConfig: (value: Gr4vyConfig) => void
}

export const ConfigContext = createContext<{
  config: Gr4vyConfig
  setConfig: (value: Gr4vyConfig) => void
}>(null as unknown as Config)

export const ConfigProvider = ({ children }: PropsWithChildren<{}>) => {
  const [config, setConfig] = useState<Gr4vyConfig>(defaultConfig)

  const setAndStoreConfig = (value: Gr4vyConfig) => {
    setConfig(value)
    storeData(value)
  }

  useLayoutEffect(() => {
    const getStoredConfig = async () => {
      const data = await getData()
      setConfig({ ...config, ...data })
    }
    getStoredConfig()
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [])

  return (
    <ConfigContext.Provider value={{ config, setConfig: setAndStoreConfig }}>
      {children}
    </ConfigContext.Provider>
  )
}

export const useConfig = () => {
  return useContext(ConfigContext)
}
