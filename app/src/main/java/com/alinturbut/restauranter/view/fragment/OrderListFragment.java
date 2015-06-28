package com.alinturbut.restauranter.view.fragment;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alinturbut.restauranter.R;
import com.alinturbut.restauranter.model.Order;
import com.alinturbut.restauranter.model.Waiter;
import com.alinturbut.restauranter.service.OrderCachingService;
import com.alinturbut.restauranter.service.OrderService;
import com.alinturbut.restauranter.service.SharedPreferencesService;
import com.alinturbut.restauranter.view.adapter.OrderAdapter;

import java.util.ArrayList;
import java.util.List;

public class OrderListFragment extends Fragment {
    private RecyclerView mCurrentOrders;
    private OrderAdapter mCurrentOrderAdapter;
    private List<Order> currentOrderList;
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
        initView();
        Intent intent = new Intent(getActivity(), OrderService.class);
        intent.setAction(OrderService.ACTION_ORDER_GET_ACTIVE);
        getActivity().startService(intent);
    }

    private void initView() {
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mCurrentOrders.setLayoutManager(llm);
        loggedWaiter = SharedPreferencesService.getLoggedWaiter(getActivity().getApplicationContext());
        currentOrderList = new ArrayList<>();
        currentOrderList.add(OrderCachingService.getInstance(loggedWaiter.getId()).getActiveOrder());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.fragment_order_list, container, false);
        mCurrentOrders = (RecyclerView) inflatedView.findViewById(R.id.orderList);

        return inflatedView;
    }

    private BroadcastReceiver activeOrdersReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if(bundle != null) {
                currentOrderList.addAll((ArrayList<Order>) bundle.get(OrderService.ORDERS));
                mCurrentOrderAdapter = new OrderAdapter(currentOrderList, mContext);
                mCurrentOrders.setAdapter(mCurrentOrderAdapter);

            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(activeOrdersReceiver, new IntentFilter(OrderService.INTENT_ORDER_PUBLISH));
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(activeOrdersReceiver);
    }
}
