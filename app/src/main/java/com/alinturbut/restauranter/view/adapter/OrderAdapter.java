package com.alinturbut.restauranter.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alinturbut.restauranter.R;
import com.alinturbut.restauranter.model.Order;

import java.util.List;

/**
 * @author alinturbut.
 */
public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    List<Order> orderList;

    public OrderAdapter(List<Order> orders) {
        this.orderList = orders;
    }

    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_order_item, parent, false);

        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OrderViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.itemCount.setText(String.valueOf(order.getDrinks().size() + order.getFoods().size()));
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {
        private TextView itemCount;

        public OrderViewHolder(View itemView) {
            super(itemView);
            itemCount = (TextView) itemView.findViewById(R.id.order_item_text);
        }
    }
}
