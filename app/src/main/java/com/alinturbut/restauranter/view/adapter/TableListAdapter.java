package com.alinturbut.restauranter.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alinturbut.restauranter.R;
import com.alinturbut.restauranter.model.Table;
import com.alinturbut.restauranter.model.Waiter;
import com.alinturbut.restauranter.service.OrderCachingService;
import com.alinturbut.restauranter.service.SharedPreferencesService;
import com.alinturbut.restauranter.service.TableService;
import com.alinturbut.restauranter.view.fragment.TableOverviewFragment;

import java.util.List;

/**
 * @author alinturbut.
 */
public class TableListAdapter extends RecyclerView.Adapter<TableListAdapter.TableViewHolder> {
    private List<Table> tableList;
    private Resources res;
    private boolean forOrder;
    private Context mContext;
    private TableOverviewFragment mTableOverview;

    public TableListAdapter(List<Table> tables, Resources rest, boolean forOrder, Context ctx, TableOverviewFragment
            mInstance) {
        this.tableList = tables;
        this.res = rest;
        this.forOrder = forOrder;
        this.mContext = ctx;
        this.mTableOverview = mInstance;
    }

    @Override
    public TableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_table_item, parent, false);

        return new TableViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TableViewHolder holder, int position) {
        Table table = tableList.get(position);
        holder.tableNumber.setText(holder.tableNumber.getText() + String.valueOf(table.getTableNumber()));
        if(table.isOccupied() && res != null) {
            holder.tableImage.setImageDrawable(res.getDrawable(R.drawable.occupied_table));
            holder.occupied.setText(holder.occupied.getText() + "Yes");
        } else if(!table.isOccupied()) {
            holder.tableImage.setImageDrawable(res.getDrawable(R.drawable.empty_table_1));
            holder.occupied.setText(holder.occupied.getText() + "No");
            holder.orderPrice.setText(holder.orderPrice.getText() + "0");
        }

        if(forOrder) {
            holder.tableImage.setOnClickListener(orderChoosing(table));
            holder.layout.setOnClickListener(orderChoosing(table));
        }
    }

    private View.OnClickListener orderChoosing(final Table table) {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Waiter waiter = SharedPreferencesService.getLoggedWaiter(mContext);
                OrderCachingService orderService = OrderCachingService.getInstance(waiter.getId());
                orderService.setTableId(table.get_id());
                Intent intent = new Intent(mContext.getApplicationContext(), TableService.class);
                intent.putExtra("Action", TableService.INTENT_MARK_TABLE_OCCUPIED);
                intent.putExtra("Id", table.get_id());
                intent.putExtra("isOccupied", true);
                mContext.startService(intent);
                Toast.makeText(mContext, "Table number " + table.getTableNumber() + " chosen for order!", Toast
                        .LENGTH_LONG).show();
                mTableOverview.goBack();
            }
        };

        return listener;
    }

    @Override
    public int getItemCount() {
        return tableList.size();
    }

    public class TableViewHolder extends RecyclerView.ViewHolder {
        private TextView tableNumber;
        private ImageView tableImage;
        private TextView occupied;
        private TextView orderPrice;
        private LinearLayout layout;

        public TableViewHolder(View itemView) {
            super(itemView);
            tableNumber = (TextView) itemView.findViewById(R.id.table_number_text);
            tableImage = (ImageView) itemView.findViewById(R.id.table_image);
            occupied = (TextView) itemView.findViewById(R.id.table_occupied);
            orderPrice = (TextView) itemView.findViewById(R.id.table_order_price);
            layout = (LinearLayout) itemView.findViewById(R.id.table_card_layout);
        }
    }
}
