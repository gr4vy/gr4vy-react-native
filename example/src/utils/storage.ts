import AsyncStorage from '@react-native-async-storage/async-storage'

export const getData = async (item = '@config') => {
  try {
    const jsonValue = await AsyncStorage.getItem(item)
    return jsonValue != null ? JSON.parse(jsonValue) : null
  } catch (e) {
    console.error('Error reading config data', e)
  }
}

export const storeData = async (
  value: Record<string, unknown>,
  item = '@config'
) => {
  try {
    const jsonValue = JSON.stringify(value)
    await AsyncStorage.setItem(item, jsonValue)
  } catch (e) {
    console.error('Error storing config data', e)
  }
}
