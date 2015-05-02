package com.alinturbut.restauranter.helper;

import android.util.Log;

import static com.alinturbut.restauranter.helper.contracts.RestaurantFactsContract.RestaurantFactsEntry;

/**
 * @author alinturbut.
 */
public class SQLHelper {
    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String PRIMARY_KEY = " PRIMARY KEY";
    private static final String COMMA_SEPARATOR = ",";
    private static final String CREATE_TABLE = "CREATE TABLE ";
    private static final String OPEN_COMMA = " ( ";
    private static final String CLOSE_COMMA = " ) ";
    private static final String DROP_TABLE = "DROP TABLE IF EXISTS ";

    public static String createRestaurantFactsTableString() {
        String tableCreation = CREATE_TABLE + RestaurantFactsEntry.TABLE_NAME + OPEN_COMMA
                + RestaurantFactsEntry.COLUMN_ID + INTEGER_TYPE + PRIMARY_KEY + COMMA_SEPARATOR
                + RestaurantFactsEntry.COLUMN_NAME + TEXT_TYPE + COMMA_SEPARATOR
                + RestaurantFactsEntry.COLUMN_STREET + TEXT_TYPE + COMMA_SEPARATOR
                + RestaurantFactsEntry.COLUMN_CITY + TEXT_TYPE + CLOSE_COMMA;
        Log.i("SQLHelper", "Creating table " + RestaurantFactsEntry.TABLE_NAME + " with SQL: \n" + tableCreation);

        return tableCreation;
    }

    public static String dropTableIfExists(String tableName) {
        String dropTable = DROP_TABLE + tableName;
        Log.i("SQLHelper", "Dropping table: " + tableName + "with SQL: \n" + dropTable);

        return dropTable;
    }
}
