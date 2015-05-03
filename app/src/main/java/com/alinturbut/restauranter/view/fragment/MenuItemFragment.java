package com.alinturbut.restauranter.view.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alinturbut.restauranter.R;
import com.alinturbut.restauranter.model.Drink;
import com.alinturbut.restauranter.model.Food;
import com.alinturbut.restauranter.model.MenuItem;
import com.alinturbut.restauranter.model.Waiter;
import com.alinturbut.restauranter.service.MenuService;
import com.alinturbut.restauranter.service.OrderCachingService;
import com.alinturbut.restauranter.service.SharedPreferencesService;
import com.alinturbut.restauranter.view.adapter.MenuItemAdapter;
import com.github.brnunes.swipeablerecyclerview.SwipeableRecyclerViewTouchListener;

import java.util.ArrayList;

public class MenuItemFragment extends Fragment {
    public static final String START_MENUITEM_FRAGMENT = "alinturbut.menuitemfragment.start";
    private OnFragmentInteractionListener mListener;
    private static final String CATEGORY_PARAM = "categoryParam";
    private String categoryId;
    private RecyclerView recList;
    private MenuItemAdapter listAdapter;
    private ArrayList<MenuItem> allMenuItems;
    private Waiter loggedWaiter;

    public static MenuItemFragment newInstance(String categoryId) {
        MenuItemFragment fragment = new MenuItemFragment();
        Bundle args = new Bundle();
        args.putString(CATEGORY_PARAM, categoryId);
        fragment.setArguments(args);

        return fragment;
    }

    public MenuItemFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            this.categoryId = getArguments().getString(CATEGORY_PARAM);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Intent intent = new Intent(getActivity(), MenuService.class);
        intent.putExtra("Action", MenuService.INTENT_GET_MENU_ITEMS);
        intent.putExtra("Category", categoryId);
        getActivity().startService(intent);
        initView();
    }

    private void initView() {
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        loggedWaiter = SharedPreferencesService.getLoggedWaiter(getActivity().getApplicationContext());

        SwipeableRecyclerViewTouchListener swipeTouchListener =
                new SwipeableRecyclerViewTouchListener(recList,
                        new SwipeableRecyclerViewTouchListener.SwipeListener() {
                            @Override
                            public boolean canSwipe(int position) {
                                return true;
                            }

                            @Override
                            public void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    Toast.makeText(getActivity().getApplicationContext(),
                                            "Item removed from order", Toast.LENGTH_SHORT).show();
                                    if(allMenuItems.get(position) instanceof Drink) {
                                        OrderCachingService.getInstance(loggedWaiter.getId()).removeDrinkFromActiveOrder((Drink) allMenuItems.get(position));
                                    } else {
                                        OrderCachingService.getInstance(loggedWaiter.getId()).removeFoodFromActiveOrder((Food) allMenuItems.get(position));

                                    }
                                }
                            }

                            @Override
                            public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    Toast.makeText(getActivity().getApplicationContext(),
                                            "Item added to order", Toast.LENGTH_SHORT).show();
                                    if(allMenuItems.get(position) instanceof Drink) {
                                        OrderCachingService.getInstance(loggedWaiter.getId()).addDrinkToActiveOrder((Drink) allMenuItems.get(position));
                                    } else {
                                        OrderCachingService.getInstance(loggedWaiter.getId()).addFoodToActiveOrder((Food) allMenuItems.get(position));

                                    }
                                }
                            }
                        });

        recList.addOnItemTouchListener(swipeTouchListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.fragment_menu_item, container, false);
        recList = (RecyclerView) inflatedView.findViewById(R.id.menuItemList);
        return inflatedView;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

    private BroadcastReceiver menuItemsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if(bundle != null) {
                allMenuItems = (ArrayList<MenuItem>) bundle.get(MenuService.ALL_MENU_ITEMS);
                Log.d("MenuItemFragment", allMenuItems.toString());
                listAdapter = new MenuItemAdapter(allMenuItems, Resources.getSystem());
                recList.setAdapter(listAdapter);
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(menuItemsReceiver, new IntentFilter(MenuService.INTENT_MENU_ITEMS_PUBLISH));
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(menuItemsReceiver);
    }

}
