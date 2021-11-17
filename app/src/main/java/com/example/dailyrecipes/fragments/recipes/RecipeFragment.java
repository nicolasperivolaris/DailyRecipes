package com.example.dailyrecipes.fragments.recipes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.dailyrecipes.R;
import com.example.dailyrecipes.model.Recipe;
import com.example.dailyrecipes.model.RecipesFactory;
import com.example.dailyrecipes.queries.ingredients.RecipeIngredientsQuery;
import com.example.dailyrecipes.queries.recipes.SaveRecipeQuery;
import com.example.dailyrecipes.utils.ConnectionManager;
import com.example.dailyrecipes.fragments.ingredients.IngredientsAdapter;

public class RecipeFragment extends Fragment {
    private ConnectionManager connection;
    private int multiplier = 1;
    private IngredientsAdapter ingredientsAdapter;
    private Recipe recipe;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.show_recipe, container, false);
        connection = new ViewModelProvider(requireActivity()).get(ConnectionManager.class);
        initSpinner(view);
        initEditMode(view);
        initSaveButton(view);

        if(getArguments() != null) {
            if (getArguments().containsKey("recipe")) {
                recipe = (Recipe) getArguments().get("recipe");
                connection.make(new RecipeIngredientsQuery(recipe, (res) -> setRecipe(view, res)));
            }
            else {
                recipe = RecipesFactory.instance.newInstance();
                setRecipe(view, recipe);
            }

            if(getArguments().containsKey("editable"))
                ((SwitchCompat) view.findViewById(R.id.edit_sw)).setChecked(getArguments().getBoolean("editable"));
            else
                ((SwitchCompat) view.findViewById(R.id.edit_sw)).setChecked(false);
        }else {
            view.findViewById(R.id.recipeName_et).setFocusable(false);
            view.findViewById(R.id.recipeName_et).setFocusableInTouchMode(false);
            view.findViewById(R.id.description_et).setFocusable(false);
            view.findViewById(R.id.description_et).setFocusableInTouchMode(false);
        }

        return view;
    }

    private void initSaveButton(View view) {
        view.findViewById(R.id.save_bt).setOnClickListener(v -> {
            recipe.setName(((EditText)view.findViewById(R.id.recipeName_et)).getText().toString());
            recipe.setDescription(((EditText)view.findViewById(R.id.description_et)).getText().toString());
            recipe.setMultiplier(Integer.parseInt(((TextView)view.findViewById(R.id.amount_tv)).getText().toString()));
            SaveRecipeQuery query = new SaveRecipeQuery(this::savedCallBack, recipe);
            connection.make(query);
            Toast.makeText(getContext(), "Saving...", Toast.LENGTH_SHORT);
        });
    }

    private void savedCallBack(Integer errorCode) {
        requireActivity().runOnUiThread(() -> {
            if (errorCode != 0) Toast.makeText(getContext(), "Error while saving", Toast.LENGTH_LONG);
            else Toast.makeText(getContext(), "Saved", Toast.LENGTH_LONG);
        });
    }

    private void initEditMode(View view) {
        view.findViewById(R.id.add_bt).setOnClickListener(v -> ingredientsAdapter.addRow());
        ((SwitchCompat) view.findViewById(R.id.edit_sw)).setOnCheckedChangeListener((buttonView, isChecked) -> {
            ingredientsAdapter.setEditable(isChecked);
            view.findViewById(R.id.recipeName_et).setFocusable(isChecked);
            view.findViewById(R.id.recipeName_et).setFocusableInTouchMode(isChecked);
            view.findViewById(R.id.description_et).setFocusable(isChecked);
            view.findViewById(R.id.description_et).setFocusableInTouchMode(isChecked);
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

    private void setRecipe(View view, Recipe result) {
        multiplier = result.getMultiplier();

        getActivity().runOnUiThread(() -> {
            ((EditText) view.findViewById(R.id.recipeName_et)).setText(result.getName());
            ((EditText) view.findViewById(R.id.description_et)).setText(result.getDescription());
            ingredientsAdapter = new IngredientsAdapter(getContext(), result.getIngredients(), multiplier);

            ListView list = view.findViewById(R.id.ingredients_list);
            list.setAdapter(ingredientsAdapter);
        });
    }
}
