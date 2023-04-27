package com.gr4vy.embedreactnative;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.BaseActivityEventListener;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeArray;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.module.annotations.ReactModule;

import com.gr4vy.android_sdk.models.CartItem;
import com.gr4vy.embedreactnative.EmbedReactNativeEvents;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

@ReactModule(name = EmbedReactNativeModule.NAME)
public class EmbedReactNativeModule extends ReactContextBaseJavaModule {
  public static final String NAME = "EmbedReactNative";
  static final String EXTRA_GR4VY_ID = "EXTRA_GR4VY_ID";
  static final String EXTRA_TOKEN = "EXTRA_TOKEN";
  static final String EXTRA_ENVIRONMENT = "EXTRA_ENVIRONMENT";
  static final String EXTRA_AMOUNT = "EXTRA_AMOUNT";
  static final String EXTRA_CURRENCY = "EXTRA_CURRENCY";
  static final String EXTRA_COUNTRY = "EXTRA_COUNTRY";
  static final String EXTRA_BUYER_ID = "EXTRA_BUYER_ID";
  static final String EXTRA_EXTERNAL_IDENTIFIER = "EXTRA_EXTERNAL_IDENTIFIER";
  static final String EXTRA_STORE = "EXTRA_STORE";
  static final String EXTRA_DISPLAY = "EXTRA_DISPLAY";
  static final String EXTRA_INTENT = "EXTRA_INTENT";
  static final String EXTRA_METADATA = "EXTRA_METADATA";
  static final String EXTRA_THEME = "EXTRA_THEME";
  static final String EXTRA_BUYER_EXTERNAL_IDENTIFIER = "EXTRA_BUYER_EXTERNAL_IDENTIFIER";
  static final String EXTRA_LOCALE = "EXTRA_LOCALE";
  static final String EXTRA_STATEMENT_DESCRIPTOR = "EXTRA_STATEMENT_DESCRIPTOR";
  static final String EXTRA_REQUIRE_SECURITY_CODE = "EXTRA_REQUIRE_SECURITY_CODE";
  static final String EXTRA_SHIPPING_DETAILS_ID = "EXTRA_SHIPPING_DETAILS_ID";
  static final String EXTRA_PAYMENT_SOURCE = "EXTRA_PAYMENT_SOURCE";
  static final String EXTRA_CART_ITEMS = "EXTRA_CART_ITEMS";
  private static final int GR4VY_PAYMENT_SHEET_REQUEST = 1;

  public static <T> T coalesce(T... items) {
    for (T item : items) {
      if (item != null) {
        return item;
      }
    }
    return null;
  }

  public EmbedReactNativeModule(ReactApplicationContext context) {
    super(context);

    ActivityEventListener activityEventListener = new BaseActivityEventListener() {
      @Override
      public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        if (requestCode == GR4VY_PAYMENT_SHEET_REQUEST) {
          if (resultCode == Activity.RESULT_OK) {
            String event = data.getStringExtra(Gr4vyActivity.EXTRA_EVENT);
            String error = data.getStringExtra(Gr4vyActivity.EXTRA_ERROR);

            if (error == null) {
              boolean success = data.getBooleanExtra(Gr4vyActivity.EXTRA_SUCCESS, false);
              String status = data.getStringExtra(Gr4vyActivity.EXTRA_STATUS);
              String transactionId = data.getStringExtra(Gr4vyActivity.EXTRA_TRANSACTION_ID);
              String paymentMethodId = data.getStringExtra(Gr4vyActivity.EXTRA_PAYMENT_METHOD_ID);

              WritableMap result = Arguments.createMap();
              result.putString("name", event);

              WritableMap resultData = Arguments.createMap();
              resultData.putBoolean("success", success);
              resultData.putString("status", status);
              resultData.putString("transactionId", transactionId);
              resultData.putString("paymentMethodId", paymentMethodId);

              result.putMap("data", resultData);

              EmbedReactNativeEvents.sendEvent(context, "onEvent", result);
            }
            else {
              WritableMap result = Arguments.createMap();
              result.putString("name", event);

              WritableMap resultData = Arguments.createMap();
              resultData.putString("message", error);

              result.putMap("data", resultData);

              EmbedReactNativeEvents.sendEvent(context, "onEvent", result);
            }
          } else if (resultCode == Activity.RESULT_CANCELED) {
            // Do nothing
          }
        }
      }
    };

    context.addActivityEventListener(activityEventListener);
  }

  private static WritableArray convertCartItemsToJson(ReadableArray cartItemsArray) {
    WritableArray cartItemsWritableArray = new WritableNativeArray();
    for (int i = 0; i < cartItemsArray.size(); i++) {
      ReadableMap cartItemMap = cartItemsArray.getMap(i);
      WritableMap cartItemWritableMap = new WritableNativeMap();
      cartItemWritableMap.putString("name", cartItemMap.getString("name"));
      cartItemWritableMap.putInt("quantity", cartItemMap.getInt("quantity"));
      cartItemWritableMap.putInt("unitAmount", cartItemMap.getInt("unitAmount"));
      cartItemsWritableArray.pushMap(cartItemWritableMap);
    }
    return cartItemsWritableArray;
  }

  @Override
  @NonNull
  public String getName() {
    return NAME;
  }

  @ReactMethod
  public void showPaymentSheet(ReadableMap config) {
      Log.d("Gr4vy", "showPaymentSheet()");
      ReadableMap emptyMap = Arguments.createMap();

      String gr4vyId = config.getString("gr4vyId");
      String environment = config.getString("environment");
      String token = config.getString("token");
      Double amount = config.getDouble("amount");
      String currency = config.getString("currency");
      String country = config.getString("country");
      String buyerId = config.getString("buyerId");
      String externalIdentifier = config.getString("externalIdentifier");
      String store = config.getString("store");
      String display = config.getString("display");
      String intent = config.getString("intent");
      ReadableMap metadata = coalesce(config.getMap("metadata"), emptyMap);
      ReadableMap theme = coalesce(config.getMap("theme"), emptyMap);
      String buyerExternalIdentifier = config.getString("buyerExternalIdentifier");
      String locale = config.getString("locale");
      ReadableMap statementDescriptor = coalesce(config.getMap("statementDescriptor"), emptyMap);
      Boolean requireSecurityCode = config.hasKey("requireSecurityCode") ? config.getBoolean("requireSecurityCode") : false;
      String shippingDetailsId = config.getString("shippingDetailsId");
      String paymentSource = config.getString("paymentSource");
      ReadableArray cartItems = config.getArray("cartItems");
      Boolean debugMode = config.getBoolean("debugMode");


      ReactApplicationContext context = getReactApplicationContext();
      Intent androidIntent = new Intent(context, Gr4vyActivity.class);

      // `putExtra` doesn't accept ReadableMap, so we have to convert it
      // to the appropriate type (Bundle)

      // Convert ReadableMap(s) to WritableMap(s)
      WritableNativeMap metadataWritableMap = new WritableNativeMap();
      metadataWritableMap.merge(metadata);
      WritableNativeMap themeWritableMap = new WritableNativeMap();
      themeWritableMap.merge(theme);
      WritableNativeMap statementDescriptorWritableMap = new WritableNativeMap();
      statementDescriptorWritableMap.merge(statementDescriptor);

      // Convert WritableMap(s) to Bundle(s)
      Bundle metadataBundle = Arguments.toBundle(metadataWritableMap);
      Bundle themeBundle = Arguments.toBundle(themeWritableMap);
      Bundle statementDescriptorBundle = Arguments.toBundle(statementDescriptorWritableMap);

      androidIntent.putExtra(EXTRA_GR4VY_ID, gr4vyId);
      androidIntent.putExtra(EXTRA_TOKEN, token);
      androidIntent.putExtra(EXTRA_ENVIRONMENT, environment);
      androidIntent.putExtra(EXTRA_AMOUNT, Integer.valueOf(amount.intValue()));
      androidIntent.putExtra(EXTRA_CURRENCY, currency);
      androidIntent.putExtra(EXTRA_COUNTRY, country);
      androidIntent.putExtra(EXTRA_BUYER_ID, buyerId);
      androidIntent.putExtra(EXTRA_EXTERNAL_IDENTIFIER, externalIdentifier);
      androidIntent.putExtra(EXTRA_STORE, store);
      androidIntent.putExtra(EXTRA_DISPLAY, display);
      androidIntent.putExtra(EXTRA_INTENT, intent);
      androidIntent.putExtra(EXTRA_CART_ITEMS, convertCartItemsToJson(cartItems).toString());
      androidIntent.putExtra(EXTRA_PAYMENT_SOURCE, paymentSource);
      androidIntent.putExtra(EXTRA_METADATA, metadataBundle);
      androidIntent.putExtra(EXTRA_THEME, themeBundle);
      androidIntent.putExtra(EXTRA_BUYER_EXTERNAL_IDENTIFIER, buyerExternalIdentifier);
      androidIntent.putExtra(EXTRA_LOCALE, locale);
      androidIntent.putExtra(EXTRA_STATEMENT_DESCRIPTOR, statementDescriptorBundle);
      androidIntent.putExtra(EXTRA_REQUIRE_SECURITY_CODE, requireSecurityCode);
      androidIntent.putExtra(EXTRA_SHIPPING_DETAILS_ID, shippingDetailsId);

      context.startActivityForResult(androidIntent, GR4VY_PAYMENT_SHEET_REQUEST, null);
    }
}
