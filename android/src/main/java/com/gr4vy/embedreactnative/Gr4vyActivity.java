package com.gr4vy.embedreactnative;

import androidx.activity.ComponentActivity;
import androidx.activity.result.ActivityResultRegistry;
import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.gr4vy.android_sdk.Gr4vyResultHandler;
import com.gr4vy.android_sdk.Gr4vySDK;
import com.gr4vy.android_sdk.models.Gr4vyResult;
import com.gr4vy.android_sdk.models.PaymentSource;

import static com.gr4vy.embedreactnative.EmbedReactNativeModule.EXTRA_TOKEN;
import static com.gr4vy.embedreactnative.EmbedReactNativeModule.EXTRA_AMOUNT;
import static com.gr4vy.embedreactnative.EmbedReactNativeModule.EXTRA_CURRENCY;
import static com.gr4vy.embedreactnative.EmbedReactNativeModule.EXTRA_COUNTRY;

public class Gr4vyActivity extends ComponentActivity implements Gr4vyResultHandler {
  private Gr4vySDK gr4vySDK;
  private final ActivityResultRegistry activityResultRegistry = this.getActivityResultRegistry();
  static final String EXTRA_EVENT = "EXTRA_EVENT";
  static final String EXTRA_ERROR = "EXTRA_ERROR";
  static final String EXTRA_SUCCESS = "EXTRA_SUCCESS";
  static final String EXTRA_STATUS = "EXTRA_STATUS";
  static final String EXTRA_TRANSACTION_ID = "EXTRA_TRANSACTION_ID";
  static final String EXTRA_PAYMENT_METHOD_ID = "EXTRA_PAYMENT_METHOD_ID";

  String token;
  int amount;
  String currency;
  String country;

  Boolean sdkLaunched = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Intent intent = getIntent();
    this.token = intent.getStringExtra(EXTRA_TOKEN);
    this.amount = intent.getIntExtra(EXTRA_AMOUNT, 0);
    this.currency = intent.getStringExtra(EXTRA_CURRENCY);
    this.country = intent.getStringExtra(EXTRA_COUNTRY);

    this.gr4vySDK = new Gr4vySDK(activityResultRegistry, this, this);
    getLifecycle().addObserver(this.gr4vySDK);
  }

  @Override
  public void onStart() {
    super.onStart();

    if (sdkLaunched) {
      return;
    }

    gr4vySDK.launch(
            this,
            token,
            amount,
            currency,
            country,
            null,
            null,
            null,
            null,
            null,
            null,
            PaymentSource.NOT_SET,
            null);
    
    sdkLaunched = true;
  }

  @Override
  public void onGr4vyResult(@NonNull Gr4vyResult gr4vyResult) {
    Log.d("Gr4vy", "onGr4vyResult");
    Intent data = new Intent();

    if (gr4vyResult instanceof Gr4vyResult.TransactionCreated) {
      Log.d("Gr4vy", "Gr4vyResult.TransactionCreated");

      Log.d("Gr4vy", "success: " + true);
      Log.d("Gr4vy", "status: " + ((Gr4vyResult.TransactionCreated) gr4vyResult).getStatus());
      Log.d("Gr4vy", "transactionId: " + ((Gr4vyResult.TransactionCreated) gr4vyResult).getTransactionId());
      Log.d("Gr4vy", "paymentMethodId: " + ((Gr4vyResult.TransactionCreated) gr4vyResult).getPaymentMethodId());

      data.putExtra(EXTRA_EVENT, "transactionCreated");
      data.putExtra(EXTRA_SUCCESS, true);
      data.putExtra(EXTRA_STATUS, ((Gr4vyResult.TransactionCreated) gr4vyResult).getStatus());
      data.putExtra(EXTRA_TRANSACTION_ID, ((Gr4vyResult.TransactionCreated) gr4vyResult).getTransactionId());
      data.putExtra(EXTRA_PAYMENT_METHOD_ID, ((Gr4vyResult.TransactionCreated) gr4vyResult).getPaymentMethodId());

      setResult(RESULT_OK, data);
    }
    else if (gr4vyResult instanceof Gr4vyResult.TransactionFailed) {
      Log.d("Gr4vy", "Gr4vyResult.TransactionFailed");

      Log.d("Gr4vy", "success: " + false);
      Log.d("Gr4vy", "status: " + ((Gr4vyResult.TransactionFailed) gr4vyResult).getStatus());
      Log.d("Gr4vy", "transactionId: " + ((Gr4vyResult.TransactionFailed) gr4vyResult).getTransactionId());
      Log.d("Gr4vy", "paymentMethodId: " + ((Gr4vyResult.TransactionFailed) gr4vyResult).getPaymentMethodId());

      data.putExtra(EXTRA_EVENT, "transactionFailed");
      data.putExtra(EXTRA_SUCCESS, true);
      data.putExtra(EXTRA_STATUS, ((Gr4vyResult.TransactionFailed) gr4vyResult).getStatus());
      data.putExtra(EXTRA_TRANSACTION_ID, ((Gr4vyResult.TransactionFailed) gr4vyResult).getTransactionId());
      data.putExtra(EXTRA_PAYMENT_METHOD_ID, ((Gr4vyResult.TransactionFailed) gr4vyResult).getPaymentMethodId());

      setResult(RESULT_OK, data);
    }
    else if (gr4vyResult instanceof Gr4vyResult.GeneralError) {
      Log.d("Gr4vy", "Gr4vyResult.GeneralError");

      Log.d("Gr4vy", "error: " + ((Gr4vyResult.GeneralError) gr4vyResult).getReason());

      data.putExtra(EXTRA_EVENT, "generalError");
      data.putExtra(EXTRA_ERROR, ((Gr4vyResult.GeneralError) gr4vyResult).getReason());

      setResult(RESULT_OK, data);
    }
    else {
        Log.d("Gr4vy", "An unknown error has occurred.");
    }

    sdkLaunched = false;
    finish();
  }
}