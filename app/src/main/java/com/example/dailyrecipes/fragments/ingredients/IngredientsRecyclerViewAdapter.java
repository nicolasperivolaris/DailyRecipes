package com.example.dailyrecipes.fragments.ingredients;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.dailyrecipes.databinding.FragmentShoppingListItemBinding;
import com.example.dailyrecipes.model.Ingredient;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Ingredient}.
 */
public class IngredientsRecyclerViewAdapter extends RecyclerView.Adapter<IngredientsRecyclerViewAdapter.ViewHolder> {

    private List<Ingredient> ingredients;

    public IngredientsRecyclerViewAdapter(List<Ingredient> items) {
        ingredients = items;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentShoppingListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mUnitView.setText(String.valueOf(ingredients.get(position).getUnit().getSymbol()));
        holder.mNameView.setText(String.valueOf(ingredients.get(position).getName()));
        holder.mQuantityView.setText(String.valueOf(ingredients.get(position).getQuantity()));
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mNameView;
        public final TextView mQuantityView;
        public final TextView mUnitView;

        public ViewHolder(FragmentShoppingListItemBinding binding) {
            super(binding.getRoot());
            mNameView = binding.nameTv;
            mQuantityView = binding.quantityTv;
            mUnitView = binding.unitTv;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNameView.getText() + "'";
        }
    }
}