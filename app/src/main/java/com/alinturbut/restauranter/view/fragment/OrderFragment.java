package com.alinturbut.restauranter.view.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alinturbut.restauranter.R;
import com.alinturbut.restauranter.helper.StringConstants;
import com.alinturbut.restauranter.model.Drink;
import com.alinturbut.restauranter.model.Food;
import com.alinturbut.restauranter.model.Order;
import com.alinturbut.restauranter.model.Table;
import com.alinturbut.restauranter.service.OrderService;
import com.alinturbut.restauranter.service.TableService;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class OrderFragment extends Fragment {
    public static final String START_ORDER = "alinturbut.restauranter.start.order.fragment";
    private Order currentOrder;
    private final int MAX_CHARACTERS_PER_ROW = 66;
    private TextView mOrderItem;
    private TextView mOrderPrice;
    private Button mShowTablesButton;
    private LinearLayout mTableCardLayout;
    private ImageView mTableImage;
    private TextView mTableCardPrice;
    private TextView mTableCardOccupied;
    private TextView mTableNumber;
    private LinearLayout mChooseTableLayout;
    private Button mSendOrderButton;
    private Button mAskReceiptButton;

    public static OrderFragment newInstance(Order currentOrder) {
        OrderFragment fragment = new OrderFragment();
        fragment.currentOrder = currentOrder;

        return fragment;
    }

    public OrderFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_item, container, false);
        mOrderItem = (TextView) view.findViewById(R.id.order_items);
        mOrderPrice = (TextView) view.findViewById(R.id.order_items_total_price);
        mTableCardLayout = (LinearLayout) view.findViewById(R.id.table_card_layout);
        mTableImage = (ImageView) view.findViewById(R.id.table_order_image);
        mTableCardPrice = (TextView) view.findViewById(R.id.table_order_price);
        mTableCardOccupied = (TextView) view.findViewById(R.id.table_order_occupied);
        mChooseTableLayout = (LinearLayout) view.findViewById(R.id.choose_table_layout);
        mSendOrderButton = (Button) view.findViewById(R.id.send_order_button);
        mAskReceiptButton = (Button) view.findViewById(R.id.receipt_button);
        mTableNumber = (TextView) view.findViewById(R.id.table_number_text);

        initTableSection();
        mShowTablesButton = (Button) view.findViewById(R.id.choose_table_button);
        mShowTablesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseTableForOrder();
            }
        });
        mSendOrderButton.setOnClickListener(sendOrderClickListner());
        mAskReceiptButton.setOnClickListener(askReceiptClickListener());

        initOrderItems();

        return view;
    }

    private View.OnClickListener askReceiptClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), OrderService.class);
                intent.setAction(OrderService.ACTION_ASK_RECEIPT);
                intent.putExtra("orderId", currentOrder.getId());
                intent.putExtra("tableId", currentOrder.getTableId());
                getActivity().startService(intent);
            }
        };
    }

    private void initTableSection() {
        if(currentOrder.getTableId() == null) {
            mTableCardLayout.setVisibility(View.INVISIBLE);
            mTableImage.setVisibility(View.INVISIBLE);
            mChooseTableLayout.getLayoutParams().height = mChooseTableLayout.getLayoutParams().height - 120;
        } else {
            Intent intent = new Intent(getActivity(), TableService.class);
            intent.putExtra("Action", TableService.INTENT_TABLE_GET_BY_ID);
            intent.putExtra("Id", currentOrder.getTableId());
            getActivity().startService(intent);
        }
    }

    private void initOrderItems(){
        String rows = "";
        Map<String, Integer> itemCountMap = new HashMap<>();
        int totalPrice = 0;
        for(Food item : currentOrder.getFoods()){
            if(itemCountMap.containsKey(item.getName())){
                itemCountMap.put(item.getName(), itemCountMap.get(item.getName()) + 1);
            } else {
                itemCountMap.put(item.getName(), 1);
            }
            totalPrice += item.getPrice();
        }

        for(Drink item : currentOrder.getDrinks()){
            if(itemCountMap.containsKey(item.getName())){
                itemCountMap.put(item.getName(), itemCountMap.get(item.getName()) + 1);
            } else {
                itemCountMap.put(item.getName(), 1);
            }
            totalPrice += item.getPrice();
        }

        for(Map.Entry<String, Integer> entry : itemCountMap.entrySet()) {
            rows += entry.getKey() + StringUtils.repeat(".", MAX_CHARACTERS_PER_ROW - 3 - entry.getKey().length()) +
                    entry.getValue();
            rows += "\n";
        }
        mOrderPrice.setText(totalPrice + " " + StringConstants.CURRENCY);
        mOrderItem.setText(rows);
        mTableCardPrice.setText(mTableCardPrice.getText().toString() + totalPrice + " " + StringConstants.CURRENCY);
    }

    private void chooseTableForOrder() {
        Intent intent = new Intent(TableOverviewFragment.START_TABLE_FRAGMENT);
        intent.putExtra("Context", TableOverviewFragment.CONTEXT_FOR_ORDER);

        getActivity().sendBroadcast(intent);
        Toast.makeText(getActivity().getApplicationContext(), "Please choose a table for the order.", Toast
                .LENGTH_SHORT)
                .show();
    }

    private View.OnClickListener sendOrderClickListner() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), OrderService.class);
                intent.setAction(OrderService.ACTION_SEND_ORDER);
                intent.putExtra("Order", currentOrder);
                getActivity().startService(intent);
                Toast.makeText(getActivity().getApplicationContext(), "Order sent!", Toast.LENGTH_SHORT).show();
            }
        };
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        getActivity().registerReceiver(findTableBroadcast, new IntentFilter(TableService.INTENT_TABLE_PUBLISH));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        getActivity().unregisterReceiver(findTableBroadcast);
    }

    private BroadcastReceiver findTableBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Table table = (Table) intent.getExtras().get(TableService.ALL_TABLES);
            if(table != null && table.isOccupied()) {
                mTableImage.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.occupied_table));
                mTableCardOccupied.setText(mTableCardOccupied.getText() + " " + "Yes");
                mTableNumber.setText(mTableNumber.getText() + " " + String.valueOf(table.getTableNumber()));
            } else if (table != null) {
                mTableImage.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.empty_table_1));
                mTableCardOccupied.setText(mTableCardOccupied.getText() + " " + "No");
                mTableNumber.setText(mTableNumber.getText() + " " + String.valueOf(table.getTableNumber()));
            }
            mTableCardLayout.setVisibility(View.VISIBLE);
            mTableImage.setVisibility(View.VISIBLE);

        }
    };

}
