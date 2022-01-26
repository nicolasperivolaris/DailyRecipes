package com.example.dailyrecipes.fragments.ingredients;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dailyrecipes.databinding.FragmentAllIngredientBinding;
import com.example.dailyrecipes.model.Ingredient;
import com.example.dailyrecipes.model.IngredientsFactory;

public class SimpleIngredientRecyclerViewAdapter extends RecyclerView.Adapter<SimpleIngredientRecyclerViewAdapter.ViewHolder> {

    private final IngredientsFactory ingredientsFactory;

    public SimpleIngredientRecyclerViewAdapter(IngredientsFactory factory) {
        ingredientsFactory = factory;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentAllIngredientBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = ingredientsFactory.getDataList().get(position);
        holder.nameTV.setText(ingredientsFactory.getDataList().get(position).getName());
    }

    @Override
    public int getItemCount() {
        return ingredientsFactory.getDataList().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView nameTV;
        public Ingredient mItem;

        public ViewHolder(FragmentAllIngredientBinding binding) {
            super(binding.getRoot());
            nameTV = binding.nameTV;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + nameTV.getText() + "'";
        }
    }
}