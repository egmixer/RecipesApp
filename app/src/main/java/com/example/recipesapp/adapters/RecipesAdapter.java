package com.example.recipesapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.recipesapp.R;
import com.example.recipesapp.webservice.models.RecipeResponse;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.MyViewHolder> {
    private ArrayList<RecipeResponse> contentList;
    private static OnItemClickListener listener;
    Context context;

    public RecipesAdapter(ArrayList<RecipeResponse> contentList, Context context) {
        this.contentList = contentList;
        this.context = context;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recipe_card, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.recipeTitle.setText(contentList.get(position).getName());
        if (!contentList.get(position).getImage().equals(""))
            Picasso.with(context).load(contentList.get(position).getImage()).into(holder.recipeImage);

    }

    @Override
    public int getItemCount() {
        return contentList.size();
    }

    public void updateList(ArrayList<RecipeResponse> contentList) {
        this.contentList = contentList;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.recipe_tv)
        TextView recipeTitle;
        @BindView(R.id.recipe_iv)
        ImageView recipeImage;
        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(v, getAdapterPosition());
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }
}
