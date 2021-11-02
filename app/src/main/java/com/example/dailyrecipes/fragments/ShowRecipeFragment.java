package com.example.dailyrecipes.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.dailyrecipes.R;
import com.example.dailyrecipes.model.Recipe;
import com.example.dailyrecipes.queries.RecipeIngredientsQuery;
import com.example.dailyrecipes.queries.SaveRecipeQuery;
import com.example.dailyrecipes.utils.ConnectionManager;
import com.example.dailyrecipes.utils.IngredientsAdapter;

public class ShowRecipeFragment extends Fragment {
    private ConnectionManager connection;
    private int multiplier = 1;
    private IngredientsAdapter ingredientsAdapter;
    private Recipe recipe;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.show_recipe, container, false);
        connection = new ViewModelProvider(requireActivity()).get(ConnectionManager.class);
        recipe = (Recipe) getArguments().get("recipe");
        RecipeIngredientsQuery recipeIngredientsQuery = new RecipeIngredientsQuery(recipe, (res) -> setRecipe(res));
        connection.make(recipeIngredientsQuery);

        initSpinner(view);
        initEditMode(view);
        initSaveTransaction(view);

        return view;
    }

    private void initSaveTransaction(View view) {
        view.findViewById(R.id.save_bt).setOnClickListener(v -> {
            SaveRecipeQuery query = new SaveRecipeQuery((result) -> savedCallBack(result), recipe);
            connection.make(query);
        });
    }

    private void savedCallBack(Object result) {
        int errorCode = (int) result;
        if(errorCode != 0) Toast.makeText(getContext(), "Error while saving", Toast.LENGTH_LONG);
        else Toast.makeText(getContext(), "Saved", Toast.LENGTH_LONG);
    }

    private void initEditMode(View view) {
        view.findViewById(R.id.add_bt).setOnClickListener(v->ingredientsAdapter.addRow());
        ((SwitchCompat)view.findViewById(R.id.edit_sw)).setOnCheckedChangeListener((buttonView, isChecked) -> {
            ingredientsAdapter.setEditable(isChecked);
            view.findViewById(R.id.recipeName_tv).setFocusable(false);
            view.findViewById(R.id.add_bt).setEnabled(isChecked);
            view.findViewById(R.id.save_bt).setEnabled(isChecked);
        });
    }

    private void initSpinner(View view) {
        ((TextView) view.findViewById(R.id.amount_tv)).setText(Integer.toString(multiplier));
        view.findViewById(R.id.plus_bt).setOnClickListener(v -> {
            multiplier++;
            ((TextView) view.findViewById(R.id.amount_tv)).setText(Integer.toString(multiplier));
            ingredientsAdapter.setMultiplier(multiplier);
        });
        view.findViewById(R.id.min_bt).setOnClickListener(v -> {
            multiplier--;
            ((TextView) view.findViewById(R.id.amount_tv)).setText(Integer.toString(multiplier));
            ingredientsAdapter.setMultiplier(multiplier);
        });
    }

    private void setRecipe(Object result) {
        Recipe recipe = (Recipe) result;
        multiplier = recipe.getMultiplier();

        getActivity().runOnUiThread(()->{
            ((TextView)getActivity().findViewById(R.id.recipeName_tv)).setText(recipe.getName());
            ingredientsAdapter = new IngredientsAdapter(getContext(), recipe.getIngredients(), multiplier);

            ListView list = getActivity().findViewById(R.id.items_list);
            list.setAdapter(ingredientsAdapter);
        });
    }
}
