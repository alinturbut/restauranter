package com.alinturbut.restauranter.view.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.alinturbut.restauranter.R;
import com.alinturbut.restauranter.service.RestaurantFactsService;
import com.alinturbut.restauranter.service.SharedPreferencesService;
import com.alinturbut.restauranter.view.fragment.MenuCategoryFragment;
import com.alinturbut.restauranter.view.fragment.MenuItemFragment;
import com.alinturbut.restauranter.view.fragment.OrderListFragment;

public class DashboardActivity extends ActionBarActivity implements OrderListFragment.OnFragmentInteractionListener,
        MenuCategoryFragment.OnFragmentInteractionListener, MenuItemFragment.OnFragmentInteractionListener{

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private ListView leftDrawerList;
    private ArrayAdapter<String> navigationDrawerAdapter;
    private String[] leftSliderData = {"Orders", "Menu", "Tables", "Offers", "Restaurant facts", "Sign out"};
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        initView();
        if (toolbar != null) {
           setSupportActionBar(toolbar);
        }
        initDrawer();
        initDatabase();
    }

    private void initView() {
        mTitle = getResources().getString(R.string.app_name);
        leftDrawerList = (ListView) findViewById(R.id.left_drawer);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        navigationDrawerAdapter=new ArrayAdapter<String>( DashboardActivity.this, android.R.layout.simple_list_item_1, leftSliderData);
        leftDrawerList.setAdapter(navigationDrawerAdapter);
        leftDrawerList.setOnItemClickListener(new DrawerItemClickListener());
    }

    private void initDrawer() {

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu();
            }

        };
        drawerLayout.setDrawerListener(drawerToggle);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean drawerOpen = drawerLayout.isDrawerOpen(Gravity.LEFT);
        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initDatabase() {
        Intent intent = new Intent(this, RestaurantFactsService.class);
        intent.putExtra("Action",RestaurantFactsService.INTENT_POPULATE_FACTS_ACTION);
        startService(intent);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);
        }

        public void selectItem(int position) {
            Fragment fragment = new Fragment();
            FragmentManager fragmentManager = getFragmentManager();
            switch(position) {
                case 0:
                    fragment = new OrderListFragment();
                    mTitle = getResources().getString(R.string.title_order_fragment);
                    break;
                case 1:
                    fragment = new MenuCategoryFragment();
                    mTitle = getResources().getString(R.string.title_menu_fragment);
                    break;
                case 4:
                    Intent intent = new Intent(getApplicationContext(), RestaurantFactListActivity.class);
                    startActivity(intent);
                    break;
                case 5:
                    SharedPreferencesService.removeLoggedWaiterSession(getApplicationContext());
                    Toast.makeText(getApplicationContext(), "You logged out!", Toast.LENGTH_SHORT);
                    onSignOut();
                    break;
                default:
            }
            fragmentManager.beginTransaction().replace(R.id.dashboard_frame, fragment).addToBackStack(null).commit();

            leftDrawerList.setItemChecked(position, true);
            drawerLayout.closeDrawers();
        }

    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStackImmediate();
        } else {
            super.onBackPressed();
        }
    }

    private void onSignOut() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    public void onResume() {
        super.onResume();
        this.registerReceiver(startMenuItemFragment, new IntentFilter(MenuItemFragment.START_MENUITEM_FRAGMENT));
    }

    @Override
    public void onPause() {
        super.onPause();
        this.unregisterReceiver(startMenuItemFragment);
    }

    private BroadcastReceiver startMenuItemFragment = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String categoryId = intent.getStringExtra("Category");
            int imageId = intent.getIntExtra("ImageId",0);
            Fragment fragment = MenuItemFragment.newInstance(categoryId,imageId);
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.menu_category_layout, fragment).addToBackStack(null).commit();
        }
    };
}
