package com.alinturbut.restauranter.helper;

/**
 * @author alinturbut.
 */
public class ApiUrls {
    public static int LOCAL_PORT = 8089;
    public static int REMOTE_PORT = 8080;
    public static int API_PORT = REMOTE_PORT;
    public static String API_DOMAIN = "localhost";
    public static String LOCALHOST_IP = "192.168.1.104:8089";
    public static String RESTAURANTER_API = "api-restauranter.rhcloud.com";
    public static String SERVER_IP = RESTAURANTER_API;
    public static String HTTP = "http://";
    public static String LOGIN_ADDRESS = "login";
    public static String CATEGORY_ADDRESS = "category";
    public static String FOOD_ADDRESS = "food";
    public static String DRINK_ADDRESS = "drink";
    public static String TABLE_ADDRESS = "table";
    public static String FIND_BY_CATEGORY_ID = "findByCategoryId";
    public static String FIND_BY_ID = "findById";
    public static String ALL = "all";
    public static String URL_SLASH = "/";
    public static String URL_DOTS = ":";

    public static void setUseLocalhost(boolean use) {
        if (use) {
            SERVER_IP = LOCALHOST_IP;
            API_PORT = LOCAL_PORT;
        } else {
            SERVER_IP = RESTAURANTER_API;
            API_PORT = REMOTE_PORT;
        }
    }
}
