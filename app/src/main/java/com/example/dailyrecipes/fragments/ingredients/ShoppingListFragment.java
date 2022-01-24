package com.example.dailyrecipes.fragments.ingredients;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dailyrecipes.MainActivity;
import com.example.dailyrecipes.R;
import com.example.dailyrecipes.model.Day;
import com.example.dailyrecipes.model.Ingredient;
import com.example.dailyrecipes.model.Recipe;
import com.example.dailyrecipes.model.RecipesFactory;
import com.example.dailyrecipes.model.Unit;
import com.example.dailyrecipes.utils.ConnectionManager;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A fragment representing a list of Items.
 */
public class ShoppingListFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private static ConnectionManager connection;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ShoppingListFragment() {}

    @SuppressWarnings("unused")
    public static ShoppingListFragment newInstance(int columnCount, ArrayList<Ingredient> shoppingList) {
        ShoppingListFragment fragment = new ShoppingListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shopping_ingredients_list, container, false);

        HashMap<PairIdUnit, Ingredient> ingredientList = new HashMap<>();

        for (Recipe recipe: MainActivity.recipesFactory.getDataList().values()) {
            if(recipe.getDay().equals(Day.NOT_DAY)) continue;
            for (Ingredient i: recipe.getIngredients().values()) {
                Ingredient temp = ingredientList.get(new PairIdUnit(i.getId(), i.getUnit()));
                if(temp !=null //if ingredientList contains i
                        && temp.getUnit().equals(i.getUnit())) temp.setQuantity(temp.getQuantity() + i.getQuantity());
                else ingredientList.put(new PairIdUnit(i.getId(), i.getUnit()), i);
            }
        }

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new IngredientsRecyclerViewAdapter(new ArrayList<>(ingredientList.values())));
        }
        return view;
    }

    private class PairIdUnit{
        Integer id;
        Unit unit;
        PairIdUnit(Integer id, Unit unit){
            this.id = id;
            this.unit = unit;
        }
    }
}