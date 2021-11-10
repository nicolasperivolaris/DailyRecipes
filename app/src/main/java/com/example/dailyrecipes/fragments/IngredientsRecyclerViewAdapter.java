package com.example.dailyrecipes.fragments;

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

    private final List<Ingredient> mValues;

    public IngredientsRecyclerViewAdapter(List<Ingredient> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentShoppingListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText((int) mValues.get(position).getId());
        holder.mNameView.setText(mValues.get(position).getName());
        holder.mQuantityView.setText((int) mValues.get(position).getQuantity());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mIdView;
        public final TextView mNameView;
        public final TextView mQuantityView;
        public Ingredient mItem;

        public ViewHolder(FragmentShoppingListItemBinding binding) {
            super(binding.getRoot());
            mIdView = binding.itemNumber;
            mNameView = binding.nameTv;
            mQuantityView = binding.quantityTv;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNameView.getText() + "'";
        }
    }
}