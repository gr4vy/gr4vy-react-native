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
          <Tab.Screen
            name="Home"
            component={Home}
            options={{
              title: 'Checkout',
              headerTitle: 'Gr4vy Example App',
              tabBarActiveTintColor: '#1B4889',
              tabBarIconStyle: { display: 'none' },
              tabBarLabelPosition: 'beside-icon',
            }}
          />
          <Tab.Screen
            name="Settings"
            component={Settings}
            options={{
              tabBarActiveTintColor: '#1B4889',
              tabBarIconStyle: { display: 'none' },
              tabBarLabelPosition: 'beside-icon',
            }}
          />
        </Tab.Navigator>
      </NavigationContainer>
    </ConfigProvider>
  )
}
