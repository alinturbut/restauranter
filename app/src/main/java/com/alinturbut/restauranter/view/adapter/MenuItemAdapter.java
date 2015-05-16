package com.alinturbut.restauranter.view.adapter;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alinturbut.restauranter.R;
import com.alinturbut.restauranter.helper.StringConstants;
import com.alinturbut.restauranter.model.MenuItem;

import java.util.List;

/**
 * @author alinturbut.
 */
public class MenuItemAdapter extends RecyclerView.Adapter<MenuItemAdapter.MenuItemHolder> {
    private List<MenuItem> menuItemList;
    private int imageId;
    private Resources res;

    public MenuItemAdapter(List<MenuItem> menuItems, Resources res, int imageId) {
        this.menuItemList = menuItems;
        this.imageId = imageId;
        this.res = res;
    }

    @Override
    public MenuItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_menu_item, parent, false);

        return new MenuItemHolder(view);
    }

    @Override
    public void onBindViewHolder(MenuItemHolder holder, int position) {
        MenuItem menuItem = menuItemList.get(position);
        holder.menuItemText.setText(menuItem.getName());
        holder.menuItemImage.setImageDrawable(res.getDrawable(imageId));
        holder.priceItemText.setText(menuItem.getPrice() + " " + StringConstants.CURRENCY);
    }

    @Override
    public int getItemCount() {
        return menuItemList.size();
    }

    public class MenuItemHolder extends RecyclerView.ViewHolder {
        private ImageView menuItemImage;
        private TextView menuItemText;
        private TextView priceItemText;

        public MenuItemHolder(View itemView) {
            super(itemView);
            menuItemImage = (ImageView) itemView.findViewById(R.id.menu_item_image);
            menuItemText = (TextView) itemView.findViewById(R.id.menu_item_text);
            priceItemText = (TextView) itemView.findViewById(R.id.menu_item_price);
        }
    }
}
