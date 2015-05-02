package com.alinturbut.restauranter.helper.contracts;

import android.provider.BaseColumns;

/**
 * @author alinturbut.
 */
public class RestaurantFactsContract {

    public static abstract class RestaurantFactsEntry implements BaseColumns {
        public static final String TABLE_NAME = "RestaurantFacts";
        public static final String COLUMN_ID = "restaurant_id";
        public static final String COLUMN_NAME = "restaurant_name";
        public static final String COLUMN_CITY = "restaurant_city";
        public static final String COLUMN_STREET = "restaurant_street";
    }
}
