package com.gr4vy.embedreactnative;

import androidx.activity.ComponentActivity;
import androidx.activity.result.ActivityResultRegistry;
import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableNativeMap;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableNativeArray;

import org.json.JSONArray;
import org.json.JSONException;

import com.gr4vy.android_sdk.*;
import com.gr4vy.android_sdk.models.*;

import static com.gr4vy.embedreactnative.EmbedReactNativeModule.coalesce;
import static com.gr4vy.embedreactnative.EmbedReactNativeModule.EXTRA_GR4VY_ID;
import static com.gr4vy.embedreactnative.EmbedReactNativeModule.EXTRA_TOKEN;
import static com.gr4vy.embedreactnative.EmbedReactNativeModule.EXTRA_ENVIRONMENT;
import static com.gr4vy.embedreactnative.EmbedReactNativeModule.EXTRA_AMOUNT;
import static com.gr4vy.embedreactnative.EmbedReactNativeModule.EXTRA_CURRENCY;
import static com.gr4vy.embedreactnative.EmbedReactNativeModule.EXTRA_COUNTRY;
import static com.gr4vy.embedreactnative.EmbedReactNativeModule.EXTRA_BUYER_ID;
import static com.gr4vy.embedreactnative.EmbedReactNativeModule.EXTRA_EXTERNAL_IDENTIFIER;
import static com.gr4vy.embedreactnative.EmbedReactNativeModule.EXTRA_STORE;
import static com.gr4vy.embedreactnative.EmbedReactNativeModule.EXTRA_DISPLAY;
import static com.gr4vy.embedreactnative.EmbedReactNativeModule.EXTRA_INTENT;
import static com.gr4vy.embedreactnative.EmbedReactNativeModule.EXTRA_METADATA;
import static com.gr4vy.embedreactnative.EmbedReactNativeModule.EXTRA_THEME;
import static com.gr4vy.embedreactnative.EmbedReactNativeModule.EXTRA_BUYER_EXTERNAL_IDENTIFIER;
import static com.gr4vy.embedreactnative.EmbedReactNativeModule.EXTRA_LOCALE;
import static com.gr4vy.embedreactnative.EmbedReactNativeModule.EXTRA_STATEMENT_DESCRIPTOR;
import static com.gr4vy.embedreactnative.EmbedReactNativeModule.EXTRA_REQUIRE_SECURITY_CODE;
import static com.gr4vy.embedreactnative.EmbedReactNativeModule.EXTRA_SHIPPING_DETAILS_ID;
import static com.gr4vy.embedreactnative.EmbedReactNativeModule.EXTRA_PAYMENT_SOURCE;
import static com.gr4vy.embedreactnative.EmbedReactNativeModule.EXTRA_CART_ITEMS;

import java.util.HashMap;

public class Gr4vyActivity extends ComponentActivity implements Gr4vyResultHandler {
  private Gr4vySDK gr4vySDK;
  private final ActivityResultRegistry activityResultRegistry = this.getActivityResultRegistry();
  static final String EXTRA_EVENT = "EXTRA_EVENT";
  static final String EXTRA_ERROR = "EXTRA_ERROR";
  static final String EXTRA_SUCCESS = "EXTRA_SUCCESS";
  static final String EXTRA_STATUS = "EXTRA_STATUS";
  static final String EXTRA_TRANSACTION_ID = "EXTRA_TRANSACTION_ID";
  static final String EXTRA_PAYMENT_METHOD_ID = "EXTRA_PAYMENT_METHOD_ID";

  String gr4vyId;
  String token;
  String environment;
  int amount;
  String currency;
  String country;
  String buyerId;
  String externalIdentifier;
  String store;
  String display;
  String intent;
  String cartItems;
  String paymentSource;
  HashMap metadata;
  ReadableMap theme;
  String buyerExternalIdentifier;
  String locale;
  ReadableMap statementDescriptor;
  Boolean requireSecurityCode;
  String shippingDetailsId;

  Boolean sdkLaunched = false;

  protected Gr4vyTheme buildTheme(ReadableMap theme) {
    if (theme == null) {
      return null;
    }

    ReadableMap emptyMap = Arguments.createMap();

    ReadableMap fonts = coalesce(theme.getMap("fonts"), emptyMap);
    String fontsBody = fonts.getString("body");

    ReadableMap colors = coalesce(theme.getMap("colors"), emptyMap);
    String colorsText = colors.getString("text");
    String colorsSubtleText = colors.getString("subtleText");
    String colorsLabelText = colors.getString("labelText");
    String colorsPrimary = colors.getString("primary");
    String colorsPageBackground = colors.getString("pageBackground");
    String colorsContainerBackgroundUnchecked = colors.getString("containerBackgroundUnchecked");
    String colorsContainerBackground = colors.getString("containerBackground");
    String colorsContainerBorder = colors.getString("containerBorder");
    String colorsInputBorder = colors.getString("inputBorder");
    String colorsInputBackground = colors.getString("inputBackground");
    String colorsInputText = colors.getString("inputText");
    String colorsInputRadioBorder = colors.getString("inputRadioBorder");
    String colorsInputRadioBorderChecked = colors.getString("inputRadioBorderChecked");
    String colorsDanger = colors.getString("danger");
    String colorsDangerBackground = colors.getString("dangerBackground");
    String colorsDangerText = colors.getString("dangerText");
    String colorsInfo = colors.getString("info");
    String colorsInfoBackground = colors.getString("infoBackground");
    String colorsInfoText = colors.getString("infoText");
    String colorsFocus = colors.getString("focus");
    String colorsHeaderText = colors.getString("headerText");
    String colorsHeaderBackground = colors.getString("headerBackground");

    ReadableMap borderWidths = coalesce(theme.getMap("borderWidths"), emptyMap);
    String borderWidthsContainer = borderWidths.getString("container");
    String borderWidthsInput = borderWidths.getString("input");

    ReadableMap radii = coalesce(theme.getMap("radii"), emptyMap);
    String radiiContainer = radii.getString("container");
    String radiiInput = radii.getString("input");

    ReadableMap shadows = coalesce(theme.getMap("shadows"), emptyMap);
    String shadowsFocusRing = shadows.getString("focusRing");

    return new Gr4vyTheme(
      new Gr4vyFonts(fontsBody),
      new Gr4vyColours(
        colorsText,
        colorsSubtleText,
        colorsLabelText,
        colorsPrimary,
        colorsPageBackground,
        colorsContainerBackgroundUnchecked,
        colorsContainerBackground,
        colorsContainerBorder,
        colorsInputBorder,
        colorsInputBackground,
        colorsInputText,
        colorsInputRadioBorder,
        colorsInputRadioBorderChecked,
        colorsDanger,
        colorsDangerBackground,
        colorsDangerText,
        colorsInfo,
        colorsInfoBackground,
        colorsInfoText,
        colorsFocus,
        colorsHeaderText,
        colorsHeaderBackground
      ),
      new Gr4vyBorderWidths(borderWidthsContainer, borderWidthsInput),
      new Gr4vyRadii(radiiContainer, radiiInput),
      new Gr4vyShadows(shadowsFocusRing)
    );
  }

  protected Gr4vyStatementDescriptor convertStatementDescriptor(ReadableMap statementDescriptor) {
    if (statementDescriptor == null) {
      return null;
    }

    String name = statementDescriptor.getString("name");
    String description = statementDescriptor.getString("description");
    String phoneNumber = statementDescriptor.getString("phoneNumber");
    String city = statementDescriptor.getString("city");
    String url = statementDescriptor.getString("url");

    return new Gr4vyStatementDescriptor(
      name,
      description,
      phoneNumber,
      city,
      url
    );
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Intent intent = getIntent();

    // Convert the JSON string to a ReadableArray
    // String cartItemsJson = intent.getStringExtra(EXTRA_CART_ITEMS);
    // ReadableArray cartItems = null;
    // try {
    //     JSONArray jsonArray = new JSONArray(cartItemsJson);
    //     WritableArray writableArray = Arguments.createArray();

    //     for (int i = 0; i < jsonArray.length(); i++) {
    //         writableArray.pushString(jsonArray.getString(i));
    //     }

    //     WritableNativeArray nativeArray = new WritableNativeArray();
    //     for (int i = 0; i < writableArray.size(); i++) {
    //         nativeArray.pushString(writableArray.getString(i));
    //     }

    //     cartItems = nativeArray;
    // } catch (JSONException e) {
    //     e.printStackTrace();
    // }

    this.gr4vyId = intent.getStringExtra(EXTRA_GR4VY_ID);
    this.token = intent.getStringExtra(EXTRA_TOKEN);
    this.environment = intent.getStringExtra(EXTRA_ENVIRONMENT);
    this.amount = intent.getIntExtra(EXTRA_AMOUNT, 0);
    this.currency = intent.getStringExtra(EXTRA_CURRENCY);
    this.country = intent.getStringExtra(EXTRA_COUNTRY);
    this.buyerId = intent.getStringExtra(EXTRA_BUYER_ID);
    this.externalIdentifier = intent.getStringExtra(EXTRA_EXTERNAL_IDENTIFIER);
    this.store = intent.getStringExtra(EXTRA_STORE);
    this.display = intent.getStringExtra(EXTRA_DISPLAY);
    this.intent = intent.getStringExtra(EXTRA_INTENT);
    // this.cartItems = intent.getStringExtra(EXTRA_CART_ITEMS);
    this.paymentSource = intent.getStringExtra(EXTRA_PAYMENT_SOURCE);
//    this.metadata = (ReadableMap) Arguments.fromBundle(intent.getBundleExtra(EXTRA_METADATA));
    this.theme = (ReadableMap) Arguments.fromBundle(intent.getBundleExtra(EXTRA_THEME));
    this.buyerExternalIdentifier = intent.getStringExtra(EXTRA_BUYER_EXTERNAL_IDENTIFIER);
    this.requireSecurityCode = intent.getExtras().getBoolean(EXTRA_REQUIRE_SECURITY_CODE);
    this.shippingDetailsId = intent.getStringExtra(EXTRA_SHIPPING_DETAILS_ID);
    this.locale = intent.getStringExtra(EXTRA_LOCALE);
    this.statementDescriptor = (ReadableMap) Arguments.fromBundle(intent.getBundleExtra(EXTRA_STATEMENT_DESCRIPTOR));

    // Convert metadata to HashMap
    Bundle metadataBundle = intent.getExtras().getBundle(EXTRA_METADATA);
    HashMap<String, Object> metadataHashMap = new HashMap<>();
    for (String key : metadataBundle.keySet()) {
      Object value = metadataBundle.get(key);
      metadataHashMap.put(key, value);
    }
    this.metadata = metadataHashMap;

    this.gr4vySDK = new Gr4vySDK(activityResultRegistry, this, this);
    getLifecycle().addObserver(this.gr4vySDK);
  }

  @Override
  public void onStart() {
    super.onStart();

    if (sdkLaunched) {
      return;
    }

    PaymentSource paymentSource =
      this.paymentSource != null ?
        PaymentSource.valueOf(this.paymentSource.toUpperCase()) :
        PaymentSource.NOT_SET;

    gr4vySDK.launch(
            this,
            gr4vyId,
            environment,
            token,
            amount,
            currency,
            country,
            buyerId,
            externalIdentifier,
            store,
            display,
            intent,
            null,
            paymentSource,
            metadata,
            buildTheme(theme),
            buyerExternalIdentifier,
            locale,
            convertStatementDescriptor(statementDescriptor),
            requireSecurityCode,
            shippingDetailsId);

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
