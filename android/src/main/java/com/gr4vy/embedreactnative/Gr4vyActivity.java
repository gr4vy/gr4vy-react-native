package com.gr4vy.embedreactnative;

import androidx.activity.ComponentActivity;
import androidx.activity.result.ActivityResultRegistry;
import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.WritableMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.core.JsonProcessingException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import com.gr4vy.android_sdk.*;
import com.gr4vy.android_sdk.models.*;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

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
import static com.gr4vy.embedreactnative.EmbedReactNativeModule.EXTRA_MERCHANT_ACCOUNT_ID;
import static com.gr4vy.embedreactnative.EmbedReactNativeModule.EXTRA_DEBUG_MODE;
import static com.gr4vy.embedreactnative.EmbedReactNativeModule.EXTRA_PAYMENT_SOURCE;
import static com.gr4vy.embedreactnative.EmbedReactNativeModule.EXTRA_CART_ITEMS;
import static com.gr4vy.embedreactnative.EmbedReactNativeModule.EXTRA_CONNECTION_OPTIONS_STRING;
import static com.gr4vy.embedreactnative.EmbedReactNativeModule.EXTRA_BUYER;

public class Gr4vyActivity extends ComponentActivity implements Gr4vyResultHandler {
  private Gr4vySDK gr4vySDK;
  private final ActivityResultRegistry activityResultRegistry = this.getActivityResultRegistry();
  static final String EXTRA_EVENT = "EXTRA_EVENT";
  static final String EXTRA_ERROR = "EXTRA_ERROR";
  static final String EXTRA_SUCCESS = "EXTRA_SUCCESS";
  static final String EXTRA_STATUS = "EXTRA_STATUS";
  static final String EXTRA_TRANSACTION_ID = "EXTRA_TRANSACTION_ID";
  static final String EXTRA_PAYMENT_METHOD_ID = "EXTRA_PAYMENT_METHOD_ID";
  static final String EXTRA_APPROVAL_URL = "EXTRA_APPROVAL_URL";

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
  List<CartItem> cartItems;
  PaymentSource paymentSource;
  HashMap metadata;
  Gr4vyTheme theme;
  String buyerExternalIdentifier;
  String locale;
  Gr4vyStatementDescriptor statementDescriptor;
  Boolean requireSecurityCode;
  String shippingDetailsId;
  String merchantAccountId;
  String connectionOptionsString;
  Gr4vyBuyer buyer;
  Boolean debugMode;

  Boolean sdkLaunched = false;

  protected static ObjectMapper objectMapper = new ObjectMapper()
      .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
      .setSerializationInclusion(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL);

  protected static <T> T decode(ReadableMap map, Class<T> type) {
    try {
      String jsonString = objectMapper.writeValueAsString(map.toHashMap());
      return objectMapper.readValue(jsonString, type);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      return null;
    }
  }

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

  protected static List<CartItem> convertCartItems(String cartItemsJson) {
    List<CartItem> cartItemList = new ArrayList<CartItem>();
    try {
      JSONArray cartItemsJsonArray = new JSONArray(cartItemsJson);
      for (int i = 0; i < cartItemsJsonArray.length(); i++) {
        JSONObject cartItemJsonObject = cartItemsJsonArray.getJSONObject(i);
        String name = cartItemJsonObject.getString("name");
        Integer quantity = cartItemJsonObject.getInt("quantity");
        Integer unitAmount = cartItemJsonObject.getInt("unitAmount");
        Integer discountAmount = (Integer) cartItemJsonObject.opt("discountAmount");
        Integer taxAmount = (Integer) cartItemJsonObject.opt("taxAmount");
        String externalIdentifier = (String) cartItemJsonObject.opt("externalIdentifier");
        String sku = (String) cartItemJsonObject.opt("sku");
        String productUrl = (String) cartItemJsonObject.opt("productUrl");
        String imageUrl = (String) cartItemJsonObject.opt("imageUrl");
        String productType = (String) cartItemJsonObject.opt("productType");
        String sellerCountry = (String) cartItemJsonObject.opt("sellerCountry");

        JSONArray categoriesArray = (JSONArray) cartItemJsonObject.opt("categories");
        List<String> categories = null;
        if (categoriesArray != null) {
          categories = new ArrayList<String>();
          for (int j = 0; j < categoriesArray.length(); j++) {
            categories.add(categoriesArray.getString(j));
          }
        }

        CartItem cartItem = new CartItem(
          name,
          quantity,
          unitAmount,
          discountAmount,
          taxAmount,
          externalIdentifier,
          sku,
          productUrl,
          imageUrl,
          categories,
          productType,
          sellerCountry
        );
        cartItemList.add(cartItem);
      }
    } catch (JSONException e) {
      e.printStackTrace();
      return null;
    }
    return cartItemList;
  }

  protected Gr4vyBuyer convertBuyer(ReadableMap source) {
    if (source == null) {
      return null;
    }

    Gr4vyBuyer buyer = decode(source, Gr4vyBuyer.class);

    return buyer;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Intent intent = getIntent();

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
    this.buyerExternalIdentifier = intent.getStringExtra(EXTRA_BUYER_EXTERNAL_IDENTIFIER);
    this.requireSecurityCode = intent.getExtras().getBoolean(EXTRA_REQUIRE_SECURITY_CODE);
    this.shippingDetailsId = intent.getStringExtra(EXTRA_SHIPPING_DETAILS_ID);
    this.merchantAccountId = intent.getStringExtra(EXTRA_MERCHANT_ACCOUNT_ID);
    this.locale = intent.getStringExtra(EXTRA_LOCALE);
    this.connectionOptionsString = intent.getStringExtra(EXTRA_CONNECTION_OPTIONS_STRING);
    this.debugMode = intent.getExtras().getBoolean(EXTRA_DEBUG_MODE);

    // Convert the cartItems JSON string to List<CartItem>
    this.cartItems = convertCartItems(intent.getStringExtra(EXTRA_CART_ITEMS));

    // Convert theme to Gr4vyTheme
    ReadableMap themeMap = Arguments.fromBundle(intent.getBundleExtra(EXTRA_THEME));
    this.theme = buildTheme(themeMap);

    // Convert metadata to HashMap (Gr4vyMetaData typealias)
    Bundle metadataBundle = intent.getExtras().getBundle(EXTRA_METADATA);
    HashMap<String, Object> metadataHashMap = new HashMap<>();
    for (String key : metadataBundle.keySet()) {
      Object value = metadataBundle.get(key);
      metadataHashMap.put(key, value);
    }
    this.metadata = metadataHashMap;

    // Convert statementDescriptor to Gr4vyStatementDescriptor
    ReadableMap statementDescriptorMap = Arguments.fromBundle(intent.getBundleExtra(EXTRA_STATEMENT_DESCRIPTOR));
    this.statementDescriptor = convertStatementDescriptor(statementDescriptorMap);

    // Convert buyer to Gr4vyBuyer
    ReadableMap buyerMap = Arguments.fromBundle(intent.getBundleExtra(EXTRA_BUYER));
    this.buyer = convertBuyer(buyerMap);

    // Set paymentSource according to its type requirements
    String paymentSourceString = intent.getStringExtra(EXTRA_PAYMENT_SOURCE);
    this.paymentSource =
      paymentSourceString != null ?
        PaymentSource.valueOf(paymentSourceString.toUpperCase()) :
        PaymentSource.NOT_SET;

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
            cartItems,
            paymentSource,
            metadata,
            theme,
            buyerExternalIdentifier,
            locale,
            statementDescriptor,
            requireSecurityCode,
            shippingDetailsId,
            merchantAccountId,
            null,
            connectionOptionsString,
            buyer,
            debugMode);

    sdkLaunched = true;
  }

  @Override
  public void onGr4vyEvent(@NonNull Gr4vyEvent gr4vyEvent) {
    Log.d("Gr4vy", "onGr4vyEvent");
    Intent data = new Intent();

    // for live events, emit immediately without closing the activity
    if (gr4vyEvent instanceof Gr4vyEvent.CardDetailsChanged) {
      WritableMap result = Arguments.createMap();
      result.putString("name", "cardDetailsChanged");

      WritableMap resultData = Arguments.createMap();
      resultData.putString("bin", ((Gr4vyEvent.CardDetailsChanged) gr4vyEvent).getBin());
      resultData.putString("cardType", ((Gr4vyEvent.CardDetailsChanged) gr4vyEvent).getCardType());
      resultData.putString("scheme", ((Gr4vyEvent.CardDetailsChanged) gr4vyEvent).getScheme());
      
      result.putMap("data", resultData);

      EmbedReactNativeEvents.sendEvent(EmbedReactNativeModule.reactContext, "onEvent", result);

      return;
    }

    // for others, set the result and close the activity as usual
    if (gr4vyEvent instanceof Gr4vyEvent.TransactionFailed) {
      Log.d("Gr4vy", "Gr4vyEvent.TransactionFailed");

      Log.d("Gr4vy", "success: " + false);
      Log.d("Gr4vy", "status: " + ((Gr4vyEvent.TransactionFailed) gr4vyEvent).getStatus());

      data.putExtra(EXTRA_EVENT, "transactionFailed");
      data.putExtra(EXTRA_SUCCESS, false);
      data.putExtra(EXTRA_STATUS, ((Gr4vyEvent.TransactionFailed) gr4vyEvent).getStatus());

      setResult(RESULT_OK, data);
    }

    sdkLaunched = false;
    finish();
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
      Log.d("Gr4vy", "approvalUrl: " + ((Gr4vyResult.TransactionCreated) gr4vyResult).getApprovalUrl());

      data.putExtra(EXTRA_EVENT, "transactionCreated");
      data.putExtra(EXTRA_SUCCESS, true);
      data.putExtra(EXTRA_STATUS, ((Gr4vyResult.TransactionCreated) gr4vyResult).getStatus());
      data.putExtra(EXTRA_TRANSACTION_ID, ((Gr4vyResult.TransactionCreated) gr4vyResult).getTransactionId());
      data.putExtra(EXTRA_PAYMENT_METHOD_ID, ((Gr4vyResult.TransactionCreated) gr4vyResult).getPaymentMethodId());
      data.putExtra(EXTRA_APPROVAL_URL, ((Gr4vyResult.TransactionCreated) gr4vyResult).getApprovalUrl());

      setResult(RESULT_OK, data);
    }
    else if (gr4vyResult instanceof Gr4vyResult.GeneralError) {
      Log.d("Gr4vy", "Gr4vyResult.GeneralError");

      Log.d("Gr4vy", "error: " + ((Gr4vyResult.GeneralError) gr4vyResult).getReason());

      data.putExtra(EXTRA_EVENT, "generalError");
      data.putExtra(EXTRA_ERROR, ((Gr4vyResult.GeneralError) gr4vyResult).getReason());

      setResult(RESULT_OK, data);
    }
    else if (gr4vyResult instanceof Gr4vyResult.Cancelled) {
      Log.d("Gr4vy", "Gr4vyResult.Cancelled");

      Log.d("Gr4vy", "User cancelled");

      data.putExtra(EXTRA_EVENT, "cancelled");
      data.putExtra(EXTRA_ERROR, "User cancelled");

      setResult(RESULT_OK, data);
    }
    else {
        Log.d("Gr4vy", "An unknown error has occurred.");
    }

    sdkLaunched = false;
    finish();
  }
}
