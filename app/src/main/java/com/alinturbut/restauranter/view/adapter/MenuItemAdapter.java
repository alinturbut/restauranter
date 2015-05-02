package com.alinturbut.restauranter.view.adapter;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alinturbut.restauranter.R;
import com.alinturbut.restauranter.model.MenuItem;

import java.util.List;

/**
 * @author alinturbut.
 */
public class MenuItemAdapter extends RecyclerView.Adapter<MenuItemAdapter.MenuItemHolder> {
    List<MenuItem> menuItemList;

    public MenuItemAdapter(List<MenuItem> menuItems, Resources res) {
        this.menuItemList = menuItems;
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
    }

    @Override
    public int getItemCount() {
        return menuItemList.size();
    }

    public class MenuItemHolder extends RecyclerView.ViewHolder {
        private ImageView menuItemImage;
        private TextView menuItemText;

        public MenuItemHolder(View itemView) {
            super(itemView);
            menuItemImage = (ImageView) itemView.findViewById(R.id.menu_item_image);
            menuItemText = (TextView) itemView.findViewById(R.id.menu_item_text);
        }
    }
}
