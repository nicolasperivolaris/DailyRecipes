package com.example.dailyrecipes.fragments.recipes;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.dailyrecipes.MainActivity;
import com.example.dailyrecipes.R;
import com.example.dailyrecipes.fragments.ingredients.IngredientsAdapter;
import com.example.dailyrecipes.model.Ingredient;
import com.example.dailyrecipes.model.IngredientsFactory;
import com.example.dailyrecipes.model.Recipe;
import com.example.dailyrecipes.model.RecipesFactory;
import com.example.dailyrecipes.queries.recipes.SaveRecipeQuery;
import com.example.dailyrecipes.utils.ConnectionManager;
import com.example.dailyrecipes.utils.FTPManager;

import java.io.File;
import java.io.FileNotFoundException;

public class RecipeFragment extends Fragment {
    private ConnectionManager connection;
    private FTPManager ftpManager;
    private int multiplier = 1;
    private IngredientsAdapter ingredientsAdapter;
    private Recipe recipe;
    private ActivityResultLauncher<String> getImage;
    private Uri tempImageUri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.show_recipe2, container, false);
        connection = new ViewModelProvider(requireActivity()).get(ConnectionManager.class);
        ftpManager = new ViewModelProvider(requireActivity()).get(FTPManager.class);
        initSpinner(view);
        initEditMode(view);
        initSaveButton(view);

        if(getArguments() != null) {
            if (getArguments().containsKey("recipe")) {
                recipe = (Recipe) getArguments().get("recipe");
            }
            else {
                recipe = RecipesFactory.newInstance();
            }
            setRecipe(view, recipe);

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
        ImageView image = view.findViewById(R.id.recipe_img);
        Log.i("RecipeFragment", "loading : " + recipe.getImage());
        image.setImageURI(recipe.getImage());
        image.setOnClickListener(v -> getImage.launch("image/*"));
        getImage = registerForActivityResult(new ActivityResultContracts.GetContent(),
                uri -> {
                    image.setImageURI(uri);
                    tempImageUri = uri;
                });

        return view;
    }

    private void initSaveButton(View view) {
        view.findViewById(R.id.save_bt).setOnClickListener(v -> {
            recipe.setName(((EditText)view.findViewById(R.id.recipeName_et)).getText().toString());
            recipe.setDescription(((EditText)view.findViewById(R.id.description_et)).getText().toString());
            recipe.setMultiplier(Integer.parseInt(((TextView)view.findViewById(R.id.amount_tv)).getText().toString()));
            recipe.getIngredients().remove(Ingredient.EMPTY);
            recipe.setImage(tempImageUri);
            recipe.setImageName(new File(tempImageUri.getPath()).getName());
            Toast.makeText(getContext(), "Saving...", Toast.LENGTH_SHORT).show();
            SaveRecipeQuery query = new SaveRecipeQuery(this::savedCallBack, recipe);
            connection.make(query);
            ftpManager.setFile(recipe.getImage());
        });
    }

    private void savedCallBack(Integer errorCode) {
        requireActivity().runOnUiThread(() -> {
            if (errorCode != 0) Toast.makeText(getContext(), "Error while saving", Toast.LENGTH_LONG).show();
            else Toast.makeText(getContext(), "Saved", Toast.LENGTH_LONG).show();
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
        ((TextView) view.findViewById(R.id.amount_tv)).setText(multiplier + "");
        view.findViewById(R.id.plus_bt).setOnClickListener(v -> {
            multiplier++;
            ((TextView) view.findViewById(R.id.amount_tv)).setText(multiplier + "");
            ingredientsAdapter.setMultiplier(multiplier);
        });
        view.findViewById(R.id.min_bt).setOnClickListener(v -> {
            multiplier--;
            ((TextView) view.findViewById(R.id.amount_tv)).setText(multiplier + "");
            ingredientsAdapter.setMultiplier(multiplier);
        });
    }

    private void setRecipe(View view, Recipe result) {
        IngredientsFactory ingredientsFactory = MainActivity.recipesFactory.getIngredientsFactory();
        multiplier = result.getMultiplier();

        requireActivity().runOnUiThread(() -> {
            ((EditText) view.findViewById(R.id.recipeName_et)).setText(result.getName());
            ((EditText) view.findViewById(R.id.description_et)).setText(result.getDescription());

            LinearLayoutCompat list = view.findViewById(R.id.ingredients_list);
            ingredientsAdapter = new IngredientsAdapter(list, getContext(), result.getIngredients(), ingredientsFactory, multiplier, connection);

        });
    }
}
