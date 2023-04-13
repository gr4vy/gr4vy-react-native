package com.gr4vy.embedreactnative;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.BaseActivityEventListener;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.module.annotations.ReactModule;

import com.gr4vy.embedreactnative.EmbedReactNativeEvents;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

@ReactModule(name = EmbedReactNativeModule.NAME)
public class EmbedReactNativeModule extends ReactContextBaseJavaModule {
  public static final String NAME = "EmbedReactNative";
  static final String EXTRA_TOKEN = "EXTRA_TOKEN";
  static final String EXTRA_AMOUNT = "EXTRA_AMOUNT";
  static final String EXTRA_CURRENCY = "EXTRA_CURRENCY";
  static final String EXTRA_COUNTRY = "EXTRA_COUNTRY";
  static final String EXTRA_BUYER_ID = "EXTRA_BUYER_ID";
  private static final int GR4VY_PAYMENT_SHEET_REQUEST = 1;

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

  @Override
  @NonNull
  public String getName() {
    return NAME;
  }

  @ReactMethod
  public void showPaymentSheet(ReadableMap config) {
      Log.d("Gr4vy", "showPaymentSheet()");

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
      ReadableMap metadata = config.getMap("metadata");
      String paymentSource = config.getString("paymentSource");
      ReadableArray cartItems = config.getArray("cartItems");
      Boolean debugMode = config.getBoolean("debugMode");

      ReactApplicationContext context = getReactApplicationContext();
      Intent androidIntent = new Intent(context, Gr4vyActivity.class);

      androidIntent.putExtra(EXTRA_TOKEN, token);
      androidIntent.putExtra(EXTRA_AMOUNT, Integer.valueOf(amount.intValue()));
      androidIntent.putExtra(EXTRA_CURRENCY, currency);
      androidIntent.putExtra(EXTRA_COUNTRY, country);
      androidIntent.putExtra(EXTRA_BUYER_ID, buyerId);

      context.startActivityForResult(androidIntent, GR4VY_PAYMENT_SHEET_REQUEST, null);
    }
}
