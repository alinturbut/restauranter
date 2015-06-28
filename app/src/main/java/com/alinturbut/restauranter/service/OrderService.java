package com.alinturbut.restauranter.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.alinturbut.restauranter.helper.ApiUrls;
import com.alinturbut.restauranter.helper.RESTCaller;
import com.alinturbut.restauranter.model.HttpRequestMethod;
import com.alinturbut.restauranter.model.Order;
import com.alinturbut.restauranter.model.OrderType;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * @author alinturbut.
 */
public class OrderService extends IntentService {
    public static final String INTENT_ORDER_PUBLISH = "com.alinturbut.restauranter.service.order.publish";
    public static final String ACTION_ORDER_GET_ACTIVE = "com.alinturbut.restauranter.service.order.get.active";
    public static final String ACTION_SEND_ORDER = "com.alinturbut.restauranter.service.order.send.order";
    public static final String ACTION_ASK_RECEIPT = "com.alinturbut.restauranter.service.order.ask.receipt";
    public static final String ORDERS = "com.alinturbut.restauranter.service.orders";

    public OrderService() {
        super("OrderService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getAction();
        switch(action){
            case ACTION_ORDER_GET_ACTIVE:
                getActiveOrdersAndPublish();
                break;
            case ACTION_SEND_ORDER:
                Order currentOrder = (Order) intent.getExtras().get("Order");
                sendOrder(currentOrder);
                break;
            case ACTION_ASK_RECEIPT:
                String orderId = intent.getStringExtra("orderId");
                String tableId = intent.getStringExtra("tableId");
                askReceipt(orderId, tableId);
                break;
            default:
                break;
        }
    }

    private void getActiveOrdersAndPublish(){
        JSONObject response = RESTCaller.callGetByUrl(ApiUrls.HTTP + ApiUrls.SERVER_IP + ApiUrls.URL_SLASH + ApiUrls
                .ORDER_ADDRESS + ApiUrls.URL_SLASH + ApiUrls.FIND_ACTIVE_ORDERS);
        ArrayList<Order> activeOrders = new ArrayList<>();
        if(response != null) {
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();
            Type orderType = new TypeToken<List<Order>>(){}.getType();
            try {
                activeOrders = (ArrayList<Order>) gson.fromJson(response.get("orders").toString(), orderType);
            }catch(JSONException ex) {
                Log.e("OrderService", "An error has occured while parsing the received JSON");
            }
        }

        publishOrders(activeOrders);
    }

    public void sendOrder(Order orderToSend) {
        RESTCaller caller = new RESTCaller();
        caller.setHttpRequestMethod(HttpRequestMethod.POST);
        caller.setUrl(ApiUrls.HTTP + ApiUrls.SERVER_IP + ApiUrls.URL_SLASH + ApiUrls.ORDER_ADDRESS + ApiUrls
                .URL_SLASH + ApiUrls.MAKE_ORDER);
        orderToSend.setSentTime(DateTime.now());
        orderToSend.setOrderType(OrderType.ACTIVE);
        Gson gson = new Gson();
        caller.addParam("order", gson.toJson(orderToSend));
        caller.addParam("drinks", gson.toJson(orderToSend.getDrinks()));
        caller.addParam("foods", gson.toJson(orderToSend.getFoods()));
        caller.addParam("tableId", gson.toJson(orderToSend.getTableId()));
        caller.addParam("price", gson.toJson(orderToSend.getPrice()));
        caller.addParam("waiterId", gson.toJson(orderToSend.getWaiterId()));
        caller.addParam("orderType", gson.toJson(orderToSend.getOrderType()));
        caller.addParam("sentTime", gson.toJson(orderToSend.getSentTime()));
        JSONObject jsonResponse = caller.executeCall();
        try {
            int responseCode = jsonResponse.getInt("responseCode");
            if(responseCode == HttpURLConnection.HTTP_OK) {
                OrderCachingService.getInstance(SharedPreferencesService.getLoggedWaiter(getApplicationContext())
                        .getId()).clearOrder();
            } else {
                Log.e("OrderFragment", "Response code from make order POST call is: " + responseCode);
            }
        } catch (JSONException e) {
            Log.e("OrderFragment", e.getMessage());
        }
    }

    private void askReceipt(String orderId, String tableId) {
        RESTCaller caller = new RESTCaller();
        caller.setHttpRequestMethod(HttpRequestMethod.POST);
        caller.setUrl(ApiUrls.HTTP + ApiUrls.SERVER_IP + ApiUrls.URL_SLASH + ApiUrls.ORDER_ADDRESS + ApiUrls
                .URL_SLASH + ApiUrls.ASK_RECEIPT);
        caller.addParam("orderId", orderId);
        caller.addParam("tableId", tableId);
        JSONObject response = caller.executeCall();
    }

    private void publishOrders(ArrayList<Order> orders) {
        Intent intent = new Intent(INTENT_ORDER_PUBLISH);
        intent.putExtra(ORDERS, orders);
        sendBroadcast(intent);
    }
}
