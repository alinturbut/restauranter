package com.alinturbut.restauranter.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alinturbut.restauranter.R;
import com.alinturbut.restauranter.model.Order;
import com.alinturbut.restauranter.view.fragment.OrderFragment;

import java.util.List;

/**
 * @author alinturbut.
 */
public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private List<Order> orderList;
    private Context mContext;

    public OrderAdapter(List<Order> orders, Context mContext) {
        this.orderList = orders;
        this.mContext = mContext;
    }

    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_order_item, parent, false);

        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OrderViewHolder holder, int position) {
        final Order order = orderList.get(position);
        holder.itemCount.setText(holder.itemCount.getText() + " " + String.valueOf(order.getDrinks().size() + order
                .getFoods().size()));
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderFragment.START_ORDER);
                intent.putExtra("currentOrder", order);

                mContext.sendBroadcast(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {
        private TextView itemCount;
        private CardView layout;

        public OrderViewHolder(View itemView) {
            super(itemView);
            itemCount = (TextView) itemView.findViewById(R.id.order_item_text);
            layout = (CardView) itemView.findViewById(R.id.card_view_order_item);
        }
    }
}
