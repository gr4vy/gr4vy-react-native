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
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.module.annotations.ReactModule;

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
  private Callback successCallback;
  private Callback errorCallback;

  public EmbedReactNativeModule(ReactApplicationContext context) {
    super(context);

    ActivityEventListener activityEventListener = new BaseActivityEventListener() {
      @Override
      public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        if (requestCode == GR4VY_PAYMENT_SHEET_REQUEST) {
          if (resultCode == Activity.RESULT_OK) {
            String error = data.getStringExtra(Gr4vyActivity.EXTRA_ERROR);

            if (error == null) {
              boolean success = data.getBooleanExtra(Gr4vyActivity.EXTRA_SUCCESS, false);
              String status = data.getStringExtra(Gr4vyActivity.EXTRA_STATUS);
              String transactionId = data.getStringExtra(Gr4vyActivity.EXTRA_TRANSACTION_ID);
              String paymentMethodId = data.getStringExtra(Gr4vyActivity.EXTRA_PAYMENT_METHOD_ID);

              WritableMap result = Arguments.createMap();
              result.putBoolean("success", success);
              result.putString("status", status);
              result.putString("transactionId", transactionId);
              result.putString("paymentMethodId", paymentMethodId);

              successCallback.invoke(result);
            }
            else {
              errorCallback.invoke(error);
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
  public void showPaymentSheet(
    String gr4vyId,
    String token,
    Double amount,
    String currency,
    String country,
    String buyerId,
    String environment,
    Boolean debugMode,
    Callback errorCallback,
    Callback successCallback) {
      Log.d("Gr4vy", "showPaymentSheet()");

      ReactApplicationContext context = getReactApplicationContext();
      Intent intent = new Intent(context, Gr4vyActivity.class);

      this.successCallback = successCallback;
      this.errorCallback = errorCallback;

      intent.putExtra(EXTRA_TOKEN, token);
      intent.putExtra(EXTRA_AMOUNT, Integer.valueOf(amount.intValue()));
      intent.putExtra(EXTRA_CURRENCY, currency);
      intent.putExtra(EXTRA_COUNTRY, country);
      intent.putExtra(EXTRA_BUYER_ID, buyerId);

      context.startActivityForResult(intent, GR4VY_PAYMENT_SHEET_REQUEST, null);
    }
}
