package com.example.dailyrecipes.fragments.ingredients;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.dailyrecipes.R;
import com.example.dailyrecipes.model.Ingredient;
import com.example.dailyrecipes.model.IngredientsFactory;

import java.util.List;

public class IngredientsAdapter extends BaseAdapter {
    private final List<Ingredient> ingredientList;
    private final LayoutInflater inflater;
    private int multiplier;
    private boolean editable;

    public IngredientsAdapter(Context context, List<Ingredient> ingredients, int multiplier) {
        this.ingredientList = ingredients;
        this.inflater = (LayoutInflater.from(context));
        setMultiplier(multiplier);
    }

    public List<Ingredient> getIngredients() {
        return ingredientList;
    }

    @Override
    public int getCount() {
        return ingredientList.size();
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
        return ingredientList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return ingredientList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null)
            view = inflater.inflate(R.layout.show_delete_ingredient_item_list, parent, false);
        else view = convertView;
        view.findViewById(R.id.delete_bt).setOnClickListener(v -> {
            ingredientList.remove(position);
            notifyDataSetChanged();
        });

        view.findViewById(R.id.choice).setOnClickListener(v->{
            createDialogList(position);
        });

        //((TextView) view.findViewById(R.id.unit_tv));

        Ingredient ingredient = ingredientList.get(position);
        ((TextView) view.findViewById(R.id.choice)).setText(ingredient.getName());
        ((TextView) view.findViewById(R.id.quantity_tv)).setText(Float.toString(ingredient.getQuantity() * multiplier));
        ((TextView) view.findViewById(R.id.unit_tv)).setText(ingredient.getUnit().getName());

        view.findViewById(R.id.choice).setEnabled(editable);
        view.findViewById(R.id.quantity_tv).setFocusable(editable);
        view.findViewById(R.id.unit_tv).setFocusable(editable);
        view.findViewById(R.id.quantity_tv).setFocusableInTouchMode(editable);
        view.findViewById(R.id.unit_tv).setFocusableInTouchMode(editable);
        view.findViewById(R.id.delete_bt).setEnabled(editable);

        return view;
    }

    public void addRow() {
        Ingredient ingredient = (Ingredient) Ingredient.EMPTY.clone();
        ingredientList.add(ingredient);
        notifyDataSetChanged();
    }

    private void createDialogList(int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(inflater.getContext());
        builder.setTitle("Choose an ingredient");

        List<Ingredient> allIngredients = IngredientsFactory.GetIngredientList();
        if(allIngredients.size() >0 )
            builder.setItems(IngredientsFactory.GetIngredientsNames(), (dialog, i) -> {
                ingredientList.remove(position);
                ingredientList.add(position, allIngredients.get(i));
                notifyDataSetChanged();
            });

        builder.setNeutralButton("Create new ingredient",(dialog, which) -> {

        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
