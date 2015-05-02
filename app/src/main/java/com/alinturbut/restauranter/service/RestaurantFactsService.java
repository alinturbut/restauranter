package com.alinturbut.restauranter.service;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.alinturbut.restauranter.helper.database.DatabaseHelper;
import com.alinturbut.restauranter.model.RestaurantFacts;

import static com.alinturbut.restauranter.helper.contracts.RestaurantFactsContract.RestaurantFactsEntry;

public class RestaurantFactsService extends IntentService {
    private DatabaseHelper restauranterDb;
    public static final String INTENT_FACTS_SERVICE = "com.alinturbut.restauranter.service.facts";
    public static final String INTENT_POPULATE_FACTS_ACTION = "intent.populate.facts";
    public static final String INTENT_READ_FACTS = "intent.read.facts";

    public RestaurantFactsService() {
        super("RestaurantFactsService");
    }

    public void populateFacts() {
        SQLiteDatabase db = restauranterDb.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(RestaurantFactsEntry.COLUMN_ID, 1);
        values.put(RestaurantFactsEntry.COLUMN_NAME, "Alma");
        values.put(RestaurantFactsEntry.COLUMN_CITY, "Cluj-Napoca");
        values.put(RestaurantFactsEntry.COLUMN_STREET, "Teodor Mihali 31-35");

        long rowId = db.insert(RestaurantFactsEntry.TABLE_NAME, RestaurantFactsEntry.COLUMN_CITY, values);
    }

    public RestaurantFacts readCurrentRestaurantFact() {
        SQLiteDatabase db = restauranterDb.getReadableDatabase();
        String[] projection = {
                RestaurantFactsEntry.COLUMN_ID,
                RestaurantFactsEntry.COLUMN_NAME,
                RestaurantFactsEntry.COLUMN_STREET,
                RestaurantFactsEntry.COLUMN_CITY
        };

        String sortOrder = RestaurantFactsEntry.COLUMN_NAME + " DESC";

        Cursor c = db.query(RestaurantFactsEntry.TABLE_NAME, projection, null, null, null, null, sortOrder);
        c.moveToFirst();
        RestaurantFacts restaurantFacts = new RestaurantFacts(c.getLong(0), c.getString(1), c.getString(2), c.getString(3));

        return restaurantFacts;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        restauranterDb = new DatabaseHelper(this);
        String currentAction = intent.getStringExtra("Action");
        switch(currentAction) {
            case INTENT_POPULATE_FACTS_ACTION:
                //populateFacts();
                break;
            case INTENT_READ_FACTS:
                publishRestaurantFacts();
        }
    }

    private void publishRestaurantFacts() {
        RestaurantFacts facts = readCurrentRestaurantFact();
        Intent intent = new Intent(INTENT_FACTS_SERVICE);
        intent.putExtra("facts", facts);
        sendBroadcast(intent);
    }
}
