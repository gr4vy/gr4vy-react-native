# Gr4vy for React Native

![NPM Version](https://img.shields.io/npm/v/@gr4vy/embed-react-native?color=green)
[![GitHub license](https://img.shields.io/github/license/Naereen/StrapDown.js.svg)](https://github.com/gr4vy/secure-fields/blob/main/LICENSE)

Embed Gr4vy in your React Native app to store card details, authorize payments, and capture a transaction.

## Installation

```sh
yarn add @gr4vy/embed-react-native
# OR
npm install @gr4vy/embed-react-native
```

## Usage

```js
import EmbedReactNative, { EmbedReactNativeEventEmitter, Gr4vyEvent } from '@gr4vy/embed-react-native';

// Listen to events coming from Gr4vy Embed when launched

const onEvent = (event: Gr4vyEvent) => {
  const { name, data } = event
  console[name === 'generalError' ? 'error' : 'log'](name, data)
}

useEffect(() => {
  const onEventSubscription = EmbedReactNativeEventEmitter.addListener(
    'onEvent',
    onEvent
  )

  return () => {
    onEventSubscription.remove()
  }
}, [])

// Launch Gr4vy Embed

const handleCheckout = () => {
  EmbedReactNative.showPaymentSheet({
    gr4vyId: '[GR4VY_ID]',
    environment: 'sandbox',
    token: '[TOKEN]',
    amount: 1299,
    currency: 'USD',
    country: 'US',
    merchantAccountId: 'my-merchant-id', // optional for multi merchant instances
    ...
  })
}

<Button onClick={handleCheckout}>Pay</Button>
```

> **Note**: Replace `[GR4VY_ID]` and `[TOKEN]` with the ID of your instance
> and JWT access token. See any of our [server-side
> SDKs](https://github.com/gr4vy?q=sdk) for more details.

### Configuration

The options passed to `showPaymentSheet` via the configuration object are as follows.

| Key                       | Default value | Description                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           |
| ------------------------- | ------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `gr4vyId`                 | `null`        | **Required** - The library automatically sets the API host to `api.<gr4vyId>.gr4vy.app` and Embed UI host to `embed.<gr4vyId>.gr4vy.app` for production, `api.sandbox.<gr4vyId>.gr4vy.app` and `embed.sandbox.<gr4vyId>.gr4vy.app` for the sandbox environment.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       |
| `environment`             | `production`  | The environment for the request. Can be `sandbox` or `production`.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    |
| `token`                   | `null`        | **Required** - The server-side generated JWT token used to authenticate any of the API. calls.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        |
| `amount`                  | `null`        | **Required** - The amount to authorize or capture in the specified `currency`.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        |
| `currency`                | `null`        | **Required** - A valid, active, 3-character `ISO 4217` currency code to authorize or capture the `amount` for.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        |
| `country`                 | `null`        | **Required** A valid `ISO 3166` country code.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         |
| `buyerId`                 | `null`        | An optional ID for a Gr4vy buyer. The transaction will automatically be associated to a buyer with that ID. If no buyer with this ID exists then it will be ignored.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  |
| `buyerExternalIdentifier` | `null`        | An optional external ID for a Gr4vy buyer. The transaction will automatically be associated to a buyer with that external ID. If no buyer with this external ID exists then it will be ignored. This option is ignored if the `buyerId` is provided.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  |
| `externalIdentifier`      | `null`        | An optional external identifier that can be supplied. This will automatically be associated to any resource created by Gr4vy and can subsequently be used to find a resource by that ID.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              |
| `store`                   | `ask`         | `'ask'`, `true`, `false` - Explicitly store the payment method or ask the buyer, this is used when a `buyerId` or `buyerExternalIdentifier` is provided.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              |
| `display`                 | `all`         | `all`, `addOnly`, `storedOnly`, `supportsTokenization` - Filters the payment methods to show stored methods only, new payment methods only or methods that support tokenization.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      |
| `intent`                  | `authorize`   | `authorize`, `capture` - Defines the intent of this API call. This determines the desired initial state of the transaction.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           |
| `metadata`                | `null`        | An optional object of key/values for transaction metadata. All values should be a string.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             |
| `paymentSource`           | `null`        | `installment`, `moto`, `recurring` - Can be used to signal that Embed is used to capture the first transaction for a subscription or an installment. When used, `store` is implied to be `true` and `display` is implied to be `supportsTokenization`. This means that payment options that do not support tokenization are automatically hidden.                                                                                                                                                                                                                                                                                                                                                                                                     |
| `applePayMerchantId`      | `null`        | The Apple merchant ID to be used for Apple Pay transactions.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          |
| `cartItems`               | `null`        | An optional array of cart item objects, each object must define a `name`, `quantity`, and `unitAmount`.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               |
| `theme`                   | `null`        | Theme customisation options [See Theming Options](https://docs.gr4vy.com/guides/payments/embed/theming#theme-options). The SDK also contains an additional two properties within the `colors` object: `headerBackground` and `headerText`. These are used for the navigation background and foreground colors.                                                                                                                                                                                                                                                                                                                                                                                                                                        |
| `locale`                  | `null`        | An optional locale, this consists of a `ISO 639 Language Code` followed by an optional `ISO 3166 Country Code`, e.g. `en`, `en-gb` or `pt-br`.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        |
| `statementDescriptor`     | `null`        | An optional object with information about the purchase to construct the statement information the buyer will see in their bank statement. Please note support for these fields varies across payment service providers and underlying banks, so Gr4vy can only ensure a best effort approach for each supported platform. <br />As an example, most platforms will only support a concatenation of `name` and `description` fields, truncated to a length of 22 characters. <br />The object can contain `name`, `description`, `phoneNumber`, `city` and `url` keys, with string values. `phoneNumber` should be in E164 format. Gr4vy recommends avoiding characters outside the alphanumeric range and the dot (`.`) to ensure wide compatibility. |
| `requireSecurityCode`     | `false`       | An optional boolean which forces security code to be prompted for stored card payments.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               |
| `shippingDetailsId`       | `null`        | An optional unique identifier of a set of shipping details stored for the buyer.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      |
| `merchantAccountId`       | `null`        | An optional merchant account ID.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      |
| `debugMode`               | `false`       | `true`, `false` - Prints useful debug information to the console.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |

### Events

The library uses event emitters to send specific events happening in Embed back to the consumer (a React Native app). The object `EmbedReactNativeEventEmitter` can be imported and used to listen to such events.

```js
useEffect(() => {
  // Add the listener
  const onEventSubscription = EmbedReactNativeEventEmitter.addListener(
    'onEvent',
    (event: Gr4vyEvent) => console.log(event)
  )

  return () => {
    // Remove the listener on cleanup
    onEventSubscription.remove()
  }
}, [])
```

The following events are emitted:

#### `generalError`

Returns data containing the message of an error that has occurred.

```json
{
  "name": "generalError",
  "data": {
    "message": "Gr4vy Error: Failed to load"
  }
}
```

#### `transactionCreated`

Returns data about a successfully created transaction.

```json
{
  "name": "transactionCreated",
  "data": {
    "success": true,
    "transactionId": "...",
    "status": "capture_succeeded",
    "paymentMethodId": "..."
  }
}
```

#### `transactionFailed`

Returns data about a failed transaction.

```json
{
  "name": "transactionFailed",
  "data": {
    "success": false,
    "transactionId": "...",
    "status": "authorization_failed",
    "paymentMethodId": "..."
  }
}
```

## Apple Pay

To enable Apple Pay in your iOS project, in addition to passing the `applePayMerchantId` configuration option to the `showPaymentSheet` function, you'll also need to enable Apple Pay within the Signing & Capabilities Xcode project settings and set the Apple Pay Merchant IDs. Please ensure your provisioning profiles and signing certificates are updated to contain this valid Apple Merchant ID. The SDK will do various checks to ensure the device is capable of Apple Pay and will be enabled if both the device and merchant IDs are valid.

## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.
