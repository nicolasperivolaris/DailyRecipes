package com.example.dailyrecipes.fragments.recipes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.dailyrecipes.MainActivity;
import com.example.dailyrecipes.R;
import com.example.dailyrecipes.model.Recipe;
import com.example.dailyrecipes.model.RecipesFactory;

import java.util.ArrayList;
import java.util.List;

public class RecipeListFragment extends Fragment {
    private final static String TAG = "HomeFragment";
    private View view;
    private LayoutInflater inflater;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_recipe_list, container, false);
        this.inflater = inflater;
        RecipesFactory.instance.addObserver((o, arg) -> setRecipeList((List<Recipe>) arg));

        view.findViewById(R.id.add_bt).setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putBoolean("editable", true);
            Navigation.findNavController(v).navigate(R.id.action_navigation_recipe_list_to_navigation_show_recipe, bundle);
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        RecipesFactory factory = RecipesFactory.instance;
        factory.update();
        setRecipeList(factory.getDataList());
    }

    private void setRecipeList(List<Recipe> recipeList) {
        if(recipeList == null) recipeList = new ArrayList<>();
        List<Recipe> finalRecipeList = recipeList;
        MainActivity.instance.runOnUiThread(() -> {
            Recipe[] recipes = new Recipe[finalRecipeList.size()];
            recipes = finalRecipeList.toArray(recipes);
            GridView recipesLV = view.findViewById(R.id.recipe_list);
            recipesLV.setAdapter(new RecipeListAdapter(recipes));
        });
    }

    class RecipeListAdapter extends BaseAdapter {
        private final Recipe[] data;

        public RecipeListAdapter(Recipe[] data) {
            this.data = data;
        }

        public int getCount() {
            return data.length;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            View item = inflater.inflate(R.layout.recipe_item_list, parent, false);
            item.setPadding(8, 8, 8, 8);

            Thread t = new Thread(() -> {
                Recipe recipe = data[position];
                Bitmap img = recipe.getImage();
                Drawable image = img == null ? ResourcesCompat.getDrawable(getResources(), R.drawable.nofile, requireActivity().getTheme())
                        : new BitmapDrawable(getResources(), img);
                requireActivity().runOnUiThread(() -> {
                    ((ImageView)item.findViewById(R.id.recipe_img)).setImageDrawable(image);
                    ((TextView)item.findViewById(R.id.name_tv)).setText(recipe.getName());
                    item.findViewById(R.id.recipe_img).setOnClickListener(v -> {
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("recipe", data[position]);
                        bundle.putBoolean("editable", false);
                        Navigation.findNavController(v).navigate(R.id.action_navigation_recipe_list_to_navigation_show_recipe, bundle);
                    });
                });
            });
            t.start();

            return item;
        }
    }
}
