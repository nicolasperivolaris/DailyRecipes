package com.example.dailyrecipes.fragments.ingredients;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.example.dailyrecipes.R;
import com.example.dailyrecipes.model.Ingredient;
import com.example.dailyrecipes.model.IngredientsFactory;
import com.example.dailyrecipes.model.ItemModel;
import com.example.dailyrecipes.model.Unit;
import com.example.dailyrecipes.model.UnitsFactory;
import com.example.dailyrecipes.utils.ItemAdapter;
import com.example.dailyrecipes.utils.PositionedMap;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class IngredientsAdapter extends BaseAdapter {
    private final PositionedMap<Ingredient> ingredientList;
    private final Context context;
    private int multiplier;
    private boolean editable;

    public IngredientsAdapter(Context context, PositionedMap<Ingredient> ingredients, int multiplier) {
        this.ingredientList = ingredients;
        this.context = context;
        setMultiplier(multiplier);
    }

    public PositionedMap<Ingredient> getIngredients() {
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
            view = (LayoutInflater.from(context)).inflate(R.layout.show_delete_ingredient_item_list, parent, false);
        else view = convertView;
        view.findViewById(R.id.delete_bt).setOnClickListener(v -> {
            ingredientList.remove(position);
            notifyDataSetChanged();
        });

        view.findViewById(R.id.choice).setOnClickListener(v->{
            createIngredientsDialogList(position);
        });
        view.findViewById(R.id.unit_et).setOnClickListener(v->{
            createUnitsDialogList(position);
        });

        view.findViewById(R.id.quantity_et).setOnFocusChangeListener((v, hasFocus) -> {
            try {
                if (!hasFocus)
                    ingredientList.get(position).setQuantity(Float.parseFloat(String.valueOf(((EditText) v).getText()))/multiplier);
            }catch (NumberFormatException e){
                e.printStackTrace();
            }
        });

        Ingredient ingredient = ingredientList.get(position);
        ((TextView) view.findViewById(R.id.choice)).setText(ingredient.getName());
        ((EditText) view.findViewById(R.id.quantity_et)).setText(Float.toString(ingredient.getQuantity() * multiplier));
        ((TextView) view.findViewById(R.id.unit_et)).setText(ingredient.getUnit().getSymbol());

        view.findViewById(R.id.choice).setEnabled(editable);
        view.findViewById(R.id.quantity_et).setFocusable(editable);
        view.findViewById(R.id.quantity_et).setFocusableInTouchMode(editable);
        view.findViewById(R.id.unit_et).setClickable(editable);
        view.findViewById(R.id.unit_et).setFocusable(editable);
        view.findViewById(R.id.delete_bt).setEnabled(editable);

        return view;
    }

    public void addRow() {
        Ingredient ingredient = (Ingredient) Ingredient.EMPTY.clone();
        ingredientList.put(ingredient.getId(),ingredient);
        notifyDataSetChanged();
    }

    private void createIngredientsDialogList(int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose an ingredient");
        ItemAdapter itemAdapter = new ItemAdapter(IngredientsFactory.instance.getDataList(),context);
        builder.setAdapter(itemAdapter, (dialog, choice) -> {
            Ingredient i = IngredientsFactory.instance.getDataList().get(choice);
            ingredientList.replace(position, i);
            notifyDataSetChanged();
        });
        builder.setNeutralButton("Create new ingredient",(dialog, which) -> {

        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void createUnitsDialogList(int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose an unit");
        ItemAdapter itemAdapter = new ItemAdapter(UnitsFactory.instance.getDataList(),context);
        builder.setAdapter(itemAdapter, (dialog, which) -> {
            ingredientList.get(position).setUnit((Unit) itemAdapter.getItemByPosition(which));
            notifyDataSetChanged();
        });
        builder.setNeutralButton("Create new unit",(dialog, which) -> {
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}


