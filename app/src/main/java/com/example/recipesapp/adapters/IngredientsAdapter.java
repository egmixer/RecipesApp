package com.example.recipesapp.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.recipesapp.R;
import com.example.recipesapp.webservice.models.Ingredient;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.ViewHolder> {
    private ArrayList<Ingredient> contentList;

    public IngredientsAdapter() {

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ingredients, parent, false);
        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.ingredientsTitle.setText(contentList.get(position).getIngredient());
        holder.details.setText(contentList.get(position).getQuantity() + " " +
                contentList.get(position).getMeasure());
    }

    @Override
    public int getItemCount() {
        return contentList.size();
    }

    public void updateList(ArrayList<Ingredient> contentList) {
        this.contentList = contentList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ingredient_title_tv)
        TextView ingredientsTitle;
        @BindView(R.id.ingredients_details_tv)
        TextView details;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
