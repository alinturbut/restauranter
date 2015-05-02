package com.alinturbut.restauranter.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alinturbut.restauranter.R;
import com.alinturbut.restauranter.model.Category;
import com.alinturbut.restauranter.view.fragment.MenuItemFragment;

import java.util.List;

/**
 * @author alinturbut.
 */
public class MenuCategoryListAdapter extends RecyclerView.Adapter<MenuCategoryListAdapter.CategoryViewHolder> {
    private List<Category> categoryList;
    private Resources res;
    private Context context;

    public MenuCategoryListAdapter(List<Category> categoryList, Resources res, Context context) {
        this.res = res;
        this.categoryList = categoryList;
        this.context = context;
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_category_item, viewGroup, false);

        return new CategoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder categoryViewHolder, int i) {
        final Category category = categoryList.get(i);
        categoryViewHolder.categoryText.setText(category.getName());
        categoryViewHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuItemFragment.START_MENUITEM_FRAGMENT);
                intent.putExtra("Category", category.getId());
                context.sendBroadcast(intent);
            }
        });
        switch(category.getName()) {
            case "Pizza":
                int imageId = res.getIdentifier("com.alinturbut.restauranter:drawable/pizza_1.jpg", null, null);
                categoryViewHolder.categoryImage.setImageResource(imageId);
                break;
            case "Cocktails":
                break;
            case "Breakfast":
                break;
            case "Hot Meals":
                break;
            case "Beer":
                break;
            case "Freshners":
                break;
        }
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        protected ImageView categoryImage;
        protected TextView categoryText;
        protected CardView layout;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            categoryImage = (ImageView) itemView.findViewById(R.id.category_image);
            categoryText = (TextView) itemView.findViewById(R.id.category_text);
            layout = (CardView) itemView.findViewById(R.id.card_view_category);
        }
    }
}