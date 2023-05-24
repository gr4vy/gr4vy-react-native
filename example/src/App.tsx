import React from 'react'
import { NavigationContainer } from '@react-navigation/native'
import { createBottomTabNavigator } from '@react-navigation/bottom-tabs'
import { Home } from './screens/Home'
import { Settings } from './screens/Settings'
import { ConfigProvider } from './contexts/Config'

const Tab = createBottomTabNavigator()

export default function App(): JSX.Element {
  return (
    <ConfigProvider>
      <NavigationContainer>
        <Tab.Navigator>
          <Tab.Screen name="Home" component={Home} />
          <Tab.Screen name="Settings" component={Settings} />
        </Tab.Navigator>
      </NavigationContainer>
    </ConfigProvider>
  )
}
