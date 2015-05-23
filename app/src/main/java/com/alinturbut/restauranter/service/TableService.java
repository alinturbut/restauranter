package com.alinturbut.restauranter.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.alinturbut.restauranter.helper.ApiUrls;
import com.alinturbut.restauranter.helper.RESTCaller;
import com.alinturbut.restauranter.model.HttpRequestMethod;
import com.alinturbut.restauranter.model.Table;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class TableService extends IntentService {
    public static final String INTENT_TABLES_PUBLISH = "alinturbut.restauranter.service.tables.service";
    public static final String INTENT_TABLE_PUBLISH = "alinturbut.restauranter.service.table.service";
    public static final String INTENT_TABLE_GET_ALL = "alinturbut.restauranter.service.table.get.all";
    public static final String INTENT_TABLE_GET_BY_ID = "alinturbut.restauranter.service.table.get.by.id";
    public static final String CONTEXT_OPEN_FOR_SHOW = "alinturbut.restauranter.service.table.open.for.show";
    public static final String CONTEXT_OPEN_FOR_ORDER = "alinturbut.restauranter.service.table.open.for.order";
    public static final String ALL_TABLES = "alinturbut.restauranter.all.tables";
    private String context = "";

    public TableService() {
        super("TableService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getStringExtra("Action");
        context = intent.getStringExtra("Context");
        switch(action) {
            case INTENT_TABLE_GET_ALL:
                getAllTablesAndPublish();
                break;
            case INTENT_TABLE_GET_BY_ID:
                String id = intent.getStringExtra("Id");
                getTableByIdAndPublish(id);
                break;
        }
    }

    private void getAllTablesAndPublish() {
        JSONObject response = RESTCaller.callGetByUrl(ApiUrls.HTTP + ApiUrls.SERVER_IP + ApiUrls.URL_SLASH + ApiUrls.TABLE_ADDRESS +
                ApiUrls.URL_SLASH + ApiUrls.ALL);
        ArrayList<Table> allTables = new ArrayList<>();
        if(response != null) {
            Gson gson = new Gson();
            Type tableType = new TypeToken<List<Table>>(){}.getType();
            try {
                allTables = (ArrayList<Table>) gson.fromJson(response.get("tables").toString(), tableType);
            }catch(JSONException ex) {
                Log.e("TableService", "An error has occured while parsing the received JSON");
            }
        }

        publishTables(allTables);
    }

    private void getTableByIdAndPublish(String id) {
        RESTCaller restCaller = new RESTCaller();
        restCaller.addParam("id", id);
        restCaller.setHttpRequestMethod(HttpRequestMethod.GET);
        restCaller.setUrl(ApiUrls.HTTP + ApiUrls.SERVER_IP + ApiUrls.URL_SLASH + ApiUrls.TABLE_ADDRESS + ApiUrls
                .URL_SLASH + ApiUrls.FIND_BY_ID);

        JSONObject response = restCaller.executeCall();
        Table table = null;
        if(response != null) {
            Gson gson = new GsonBuilder().create();
            try {
                table = gson.fromJson(response.get("table").toString(), Table.class);
            } catch (JSONException e) {
                Log.e("TableService", "An error has occured while parsing the received JSON");
            }
        }

        publishTable(table);
    }

    private void publishTables(ArrayList<Table> allTables) {
        Intent intent = new Intent(INTENT_TABLES_PUBLISH);
        intent.putExtra(ALL_TABLES, allTables);
        intent.putExtra("Context", context);
        sendBroadcast(intent);
    }

    private void publishTable(Table table) {
        Intent intent = new Intent(INTENT_TABLE_PUBLISH);
        intent.putExtra(ALL_TABLES, table);
        sendBroadcast(intent);
    }

}
