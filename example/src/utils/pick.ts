export const pick = <T extends Record<string, unknown>>(
  object: T,
  keys: Array<keyof T>
) =>
  keys.reduce((newObject: Record<keyof T, unknown>, key: keyof T) => {
    if (object[key]) {
      newObject[key] = object[key]
    }
    return newObject
  }, {} as Record<keyof T, unknown>) as T
