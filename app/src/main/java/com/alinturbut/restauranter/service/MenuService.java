package com.alinturbut.restauranter.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.alinturbut.restauranter.helper.RESTCaller;
import com.alinturbut.restauranter.model.Category;
import com.alinturbut.restauranter.model.Drink;
import com.alinturbut.restauranter.model.Food;
import com.alinturbut.restauranter.model.HttpRequestMethod;
import com.alinturbut.restauranter.model.MenuItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.alinturbut.restauranter.helper.ApiUrls.ALL;
import static com.alinturbut.restauranter.helper.ApiUrls.API_PORT;
import static com.alinturbut.restauranter.helper.ApiUrls.CATEGORY_ADDRESS;
import static com.alinturbut.restauranter.helper.ApiUrls.CURRENT_LOCALHOST_IP;
import static com.alinturbut.restauranter.helper.ApiUrls.DRINK_ADDRESS;
import static com.alinturbut.restauranter.helper.ApiUrls.FIND_BY_CATEGORY_ID;
import static com.alinturbut.restauranter.helper.ApiUrls.FOOD_ADDRESS;
import static com.alinturbut.restauranter.helper.ApiUrls.HTTP;
import static com.alinturbut.restauranter.helper.ApiUrls.URL_DOTS;
import static com.alinturbut.restauranter.helper.ApiUrls.URL_SLASH;

public class MenuService extends IntentService {
    public static final String INTENT_CATEGORY_PUBLISH = "alinturbut.restauranter.publish.categories";
    public static final String INTENT_MENU_ITEMS_PUBLISH = "alinturbut.restauranter.publish.menu.items";
    public static final String ALL_CATEGORIES = "alinturbut.restauranter.allcategories";
    public static final String ALL_MENU_ITEMS = "alinturbut.restauranter.allmenuitems";
    public static final String INTENT_GET_CATEGORIES = "alinturbut.restauranter.get.categories";
    public static final String INTENT_GET_MENU_ITEMS = "alinturbut.restauranter.get.menu.items";

    public MenuService() {
        super("MenuService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getStringExtra("Action");
        switch (action) {
            case INTENT_GET_CATEGORIES:
                getAllCategoriesAndPublish();
                break;
            case INTENT_GET_MENU_ITEMS:
                String categoryId = intent.getStringExtra("Category");
                getAllMenuItemsAndPublish(categoryId);
                break;
        }
    }

    private void getAllMenuItemsAndPublish(String categoryId) {
        ArrayList<MenuItem> allMenuItems = new ArrayList<>();
        allMenuItems.addAll(getFoodbyCategoryId(categoryId));
        allMenuItems.addAll(getDrinkbyCategoryId(categoryId));

        publishMenuItems(allMenuItems);
    }

    public void getAllCategoriesAndPublish() {
        ArrayList<Category> allMenuCategories = new ArrayList<>();
        RESTCaller networkRestCaller = new RESTCaller();
        networkRestCaller.setUrl(HTTP + CURRENT_LOCALHOST_IP + URL_DOTS +
                API_PORT + URL_SLASH + CATEGORY_ADDRESS + URL_SLASH + ALL);
        networkRestCaller.setHttpRequestMethod(HttpRequestMethod.GET);

        JSONObject response = networkRestCaller.executeCall();
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Category>>(){}.getType();
        try {
            allMenuCategories = (ArrayList<Category>) gson.fromJson(response.get("categories").toString(), listType);
        } catch (JSONException e) {
            Log.e("MenuService", "An error has occured while parsing the received JSON");
        }

        publishCategories(allMenuCategories);
    }

    public List<Food> getFoodbyCategoryId(String categoryId) {
        ArrayList<Food> allFoodCategories = new ArrayList<>();
        RESTCaller networkRestCaller = new RESTCaller();
        networkRestCaller.setHttpRequestMethod(HttpRequestMethod.GET);
        networkRestCaller.setUrl(HTTP + CURRENT_LOCALHOST_IP + URL_DOTS + API_PORT + URL_SLASH + FOOD_ADDRESS + URL_SLASH +
                FIND_BY_CATEGORY_ID);
        networkRestCaller.addParam("categoryId", categoryId);

        JSONObject response = networkRestCaller.executeCall();
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Food>>(){}.getType();
        try {
            allFoodCategories = (ArrayList<Food>) gson.fromJson(response.get("foods").toString(), listType);
        } catch (JSONException e) {
            Log.e("MenuService", "An error has occured while parsing the received JSON");
        }

        return allFoodCategories;
    }

    public List<Drink> getDrinkbyCategoryId(String categoryId) {
        ArrayList<Drink> allDrinkCategories = new ArrayList<>();
        RESTCaller networkRestCaller = new RESTCaller();
        networkRestCaller.setHttpRequestMethod(HttpRequestMethod.GET);
        networkRestCaller.setUrl(HTTP + CURRENT_LOCALHOST_IP + URL_DOTS + API_PORT + URL_SLASH + DRINK_ADDRESS + URL_SLASH +
                FIND_BY_CATEGORY_ID);
        networkRestCaller.addParam("categoryId", categoryId);

        JSONObject response = networkRestCaller.executeCall();
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Food>>(){}.getType();
        try {
            allDrinkCategories = (ArrayList<Drink>) gson.fromJson(response.get("drinks").toString(), listType);
        } catch (JSONException e) {
            Log.e("MenuService", "An error has occured while parsing the received JSON");
        }

        return allDrinkCategories;
    }

    private void publishCategories(ArrayList<Category> categories) {
        Intent intent = new Intent(INTENT_CATEGORY_PUBLISH);
        intent.putExtra(ALL_CATEGORIES, categories);
        sendBroadcast(intent);
    }

    private void publishMenuItems(ArrayList<MenuItem> menuItems) {
        Intent intent = new Intent(INTENT_MENU_ITEMS_PUBLISH);
        intent.putExtra(ALL_MENU_ITEMS, menuItems);
        sendBroadcast(intent);
    }

}
