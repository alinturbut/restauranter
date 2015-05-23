package com.alinturbut.restauranter.view.fragment;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alinturbut.restauranter.R;
import com.alinturbut.restauranter.model.Category;
import com.alinturbut.restauranter.model.Order;
import com.alinturbut.restauranter.model.Waiter;
import com.alinturbut.restauranter.service.MenuService;
import com.alinturbut.restauranter.service.OrderCachingService;
import com.alinturbut.restauranter.service.SharedPreferencesService;
import com.alinturbut.restauranter.view.adapter.OrderAdapter;

import java.util.ArrayList;
import java.util.List;

public class OrderListFragment extends Fragment {
    private RecyclerView recList;
    private OrderAdapter mOrderAdapter;
    private List<Order> orderList;
    private Waiter loggedWaiter;
    private Context mContext;

    public static OrderListFragment newInstance(Context mContext) {
        OrderListFragment fragment = new OrderListFragment();
        fragment.mContext = mContext;

        return fragment;
    }

    public OrderListFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
        Intent intent = new Intent(getActivity(), MenuService.class);
        intent.putExtra("Action", MenuService.INTENT_GET_CATEGORIES);
        getActivity().startService(intent);
        initView();
    }

    private void initView() {
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        loggedWaiter = SharedPreferencesService.getLoggedWaiter(getActivity().getApplicationContext());

        orderList = new ArrayList<>();
        orderList.add(OrderCachingService.getInstance(loggedWaiter.getId()).getActiveOrder());
        mOrderAdapter = new OrderAdapter(orderList, mContext);
        recList.setAdapter(mOrderAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.fragment_order_list, container, false);
        recList = (RecyclerView) inflatedView.findViewById(R.id.orderList);

        return inflatedView;
    }

    private BroadcastReceiver categoriesReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if(bundle != null) {
                ArrayList<Category> allMenuCategories = (ArrayList<Category>) bundle.get(MenuService.ALL_CATEGORIES);
                Log.d("MenuFragment", allMenuCategories.toString());
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(categoriesReceiver, new IntentFilter(MenuService.INTENT_CATEGORY_PUBLISH));
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(categoriesReceiver);
    }
}
