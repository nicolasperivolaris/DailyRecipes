package com.example.dailyrecipes.fragments.ingredients;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.widget.LinearLayoutCompat;

import com.example.dailyrecipes.R;
import com.example.dailyrecipes.model.Ingredient;
import com.example.dailyrecipes.model.IngredientsFactory;
import com.example.dailyrecipes.model.Unit;
import com.example.dailyrecipes.model.UnitsFactory;
import com.example.dailyrecipes.queries.ingredients.SaveIngredientQuery;
import com.example.dailyrecipes.utils.ConnectionManager;
import com.example.dailyrecipes.utils.ItemAdapter;
import com.example.dailyrecipes.utils.PositionedMap;

public class IngredientsAdapter extends BaseAdapter {
    private final IngredientsFactory ingredientsFactory;
    private final PositionedMap<Ingredient> ingredientList;
    private final LinearLayoutCompat layout;
    private final Context context;
    private int multiplier;
    private boolean editable;
    private ConnectionManager connection;

    public IngredientsAdapter(LinearLayoutCompat list, Context context, PositionedMap<Ingredient> ingredients, IngredientsFactory ingredientsFactory, int multiplier, ConnectionManager connection) {
        this.ingredientList = ingredients;
        layout = list;
        this.context = context;
        setMultiplier(multiplier);
        this.ingredientsFactory = ingredientsFactory;
        this.connection = connection;
    }

    public PositionedMap<Ingredient> getIngredients() {
        return ingredientList;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        layout.removeAllViews();
        for (int i = 0; i<ingredientList.size(); i++) {
            layout.addView(getView(i, null, layout), i);
        }
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

        view.findViewById(R.id.choice).setOnClickListener(v-> createIngredientsDialogList(position));
        view.findViewById(R.id.unit_et).setOnClickListener(v-> createUnitsDialogList(position));

        view.findViewById(R.id.quantity_tv).setOnFocusChangeListener((v, hasFocus) -> {
            try {
                if (!hasFocus)
                    ingredientList.get(position).setQuantity(Float.parseFloat(String.valueOf(((EditText) v).getText()))/multiplier);
            }catch (NumberFormatException e){
                e.printStackTrace();
            }
        });

        Ingredient ingredient = ingredientList.get(position);
        ((TextView) view.findViewById(R.id.choice)).setText(ingredient.getName());
        ((EditText) view.findViewById(R.id.quantity_tv)).setText(Float.toString(ingredient.getQuantity() * multiplier));
        ((TextView) view.findViewById(R.id.unit_et)).setText(ingredient.getUnit() != null ? ingredient.getUnit().getSymbol() : "");

        view.findViewById(R.id.choice).setEnabled(editable);
        view.findViewById(R.id.quantity_tv).setFocusable(editable);
        view.findViewById(R.id.quantity_tv).setFocusableInTouchMode(editable);
        view.findViewById(R.id.unit_et).setClickable(editable);
        view.findViewById(R.id.unit_et).setFocusable(editable);
        view.findViewById(R.id.delete_bt).setEnabled(editable);

        if(((EditText) view.findViewById(R.id.quantity_tv)).getText().toString().equals("0.0"))
            ((EditText) view.findViewById(R.id.quantity_tv)).setText("");
        return view;
    }

    public void addRow() {
        Ingredient ingredient = (Ingredient) Ingredient.EMPTY.clone();
        if(!ingredientList.ids().contains(Ingredient.EMPTY.getId()))
            ingredientList.put(ingredient.getId(),ingredient);
        notifyDataSetChanged();
    }

    private void createIngredientsDialogList(int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose an ingredient");
        if(ingredientsFactory.getDataList().size() == 0)
            throw new RuntimeException("list null");
        ItemAdapter itemAdapter = new ItemAdapter(ingredientsFactory.getDataList(),context);
        builder.setAdapter(itemAdapter, (dialog, choice) -> {
            Ingredient i = ingredientsFactory.getDataList().get(choice);
            ingredientList.replace(position, i);
            notifyDataSetChanged();
        });
        builder.setNeutralButton("Create new ingredient",(dialog, which) -> {
            createIngredientCreationDialog();
            dialog.dismiss();
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void createIngredientCreationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Create an ingredient");
        Ingredient newIngredient = IngredientsFactory.newInstance();
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_new_ingredient, null, false);
        builder.setView(dialogView);
        builder.setNegativeButton("Cancel",(dialog, which) -> dialog.dismiss());
        builder.setPositiveButton("Ok",(dialog, which) -> {
            String name = ((EditText)dialogView.findViewById(R.id.name_tv)).getText().toString().trim();
            if(name != null)
            newIngredient.setName(name);
            SaveIngredientQuery save = new SaveIngredientQuery(newIngredient, i -> {});
            connection.make(save);
            ingredientsFactory.update();
            dialog.dismiss();
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void createUnitsDialogList(int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose an unit");
        ItemAdapter itemAdapter = new ItemAdapter(ingredientsFactory.getUnitsFactory().getDataList(),context);
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


