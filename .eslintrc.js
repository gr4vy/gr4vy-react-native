module.exports = {
  root: true,
  extends: ['@react-native-community', 'prettier'],
  env: {
    jest: true,
  },
  rules: {
    'prettier/prettier': [
      'error',
      {
        quoteProps: 'consistent',
        singleQuote: true,
        tabWidth: 2,
        trailingComma: 'es5',
        useTabs: false,
      },
    ],
  },
}
