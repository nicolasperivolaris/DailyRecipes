package com.example.dailyrecipes.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.dailyrecipes.R;
import com.example.dailyrecipes.model.Ingredient;

import java.util.List;

public class IngredientsAdapter extends BaseAdapter {
    private final List<Ingredient> ingredients;
    private final LayoutInflater inflater;
    private int multiplier;
    private boolean editable;

    public IngredientsAdapter(Context context, List<Ingredient> ingredients, int multiplier) {
        this.ingredients = ingredients;
        this.inflater = (LayoutInflater.from(context));
        setMultiplier(multiplier);
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    @Override
    public int getCount() {
        return ingredients.size();
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
        notifyDataSetChanged();
    }

    public void setMultiplier(int multiplier) {
        this.multiplier = multiplier;
        notifyDataSetChanged();
    }

    @Override
    public Object getItem(int position) {
        return ingredients.get(position);
    }

    @Override
    public long getItemId(int position) {
        return ingredients.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null)
            view = inflater.inflate(R.layout.show_delete_ingredient_list_item, null, false);
        else view = convertView;
        view.findViewById(R.id.delete_bt).setOnClickListener(v -> {
            ingredients.remove(position);
            notifyDataSetChanged();
        });

        Ingredient ingredient = ingredients.get(position);
        ((TextView) view.findViewById(R.id.item_name_tv)).setText(ingredient.getName());
        ((TextView) view.findViewById(R.id.quantity_tv)).setText(Float.toString(ingredient.getQuantity() * multiplier));
        ((TextView) view.findViewById(R.id.unit_tv)).setText(ingredient.getUnit().getName());

        view.findViewById(R.id.item_name_tv).setFocusable(editable);
        view.findViewById(R.id.quantity_tv).setFocusable(editable);
        view.findViewById(R.id.unit_tv).setFocusable(editable);
        view.findViewById(R.id.delete_bt).setEnabled(editable);

        return view;
    }

    public void addRow() {
        Ingredient ingredient = new Ingredient(-1, "", null, 0);
        ingredients.add(ingredient);
        notifyDataSetChanged();
    }
}
