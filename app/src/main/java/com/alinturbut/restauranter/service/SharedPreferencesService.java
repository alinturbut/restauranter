package com.alinturbut.restauranter.service;

import android.content.Context;
import android.content.SharedPreferences;

import com.alinturbut.restauranter.model.Waiter;
import com.google.gson.Gson;

/**
 * @author alinturbut.
 */
public class SharedPreferencesService {
    public static final String RESTAURANTER_PREFS = "com.alinturbut.restauranter";
    public static final String LOGGED_WAITER_PREFS = "waiter.logged";
    public static final String LOCALHOST_USE = "localhost.use";

    public static void saveLoggedWaiter(Context ctx, String json) {
        SharedPreferences.Editor editor = ctx.getSharedPreferences(RESTAURANTER_PREFS, Context.MODE_PRIVATE).edit();
        editor.putString(LOGGED_WAITER_PREFS, json);
        editor.apply();
    }

    public static void saveLocalhostUse(Context ctx, boolean use) {
        SharedPreferences.Editor editor = ctx.getSharedPreferences(RESTAURANTER_PREFS, Context.MODE_PRIVATE).edit();
        editor.putBoolean(LOCALHOST_USE, use);
        editor.apply();
    }

    public static boolean getLocalhostUse(Context ctx) {
        SharedPreferences prefs = ctx.getSharedPreferences(RESTAURANTER_PREFS, Context.MODE_PRIVATE);

        return prefs.getBoolean(LOCALHOST_USE, false);
    }

    public static void removeLoggedWaiterSession(Context ctx) {
        SharedPreferences prefs = ctx.getSharedPreferences(RESTAURANTER_PREFS, Context.MODE_PRIVATE);
        prefs.edit().remove(LOGGED_WAITER_PREFS).apply();
    }

    public static Waiter getLoggedWaiter(Context ctx) {
        SharedPreferences prefs = ctx.getSharedPreferences(RESTAURANTER_PREFS, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonWaiter = prefs.getString(LOGGED_WAITER_PREFS, "");
        if(jsonWaiter.length() > 0) {
            return gson.fromJson(jsonWaiter, Waiter.class);
        }

        return null;
    }
}
