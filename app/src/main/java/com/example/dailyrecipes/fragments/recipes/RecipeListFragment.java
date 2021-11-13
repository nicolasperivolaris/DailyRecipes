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

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.dailyrecipes.R;
import com.example.dailyrecipes.model.Recipe;
import com.example.dailyrecipes.queries.recipes.RecipeListQuery;
import com.example.dailyrecipes.utils.ConnectionManager;

import java.util.List;

public class RecipeListFragment extends Fragment {
    ConnectionManager connection;
    private final static String TAG = "HomeFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_list, container, false);

        connection = new ViewModelProvider(requireActivity()).get(ConnectionManager.class);
        RecipeListQuery recipeListQuery = new RecipeListQuery(this::setRecipeList);
        connection.make(recipeListQuery);
        view.findViewById(R.id.add_bt).setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putBoolean("editable", true);
            Navigation.findNavController(v).navigate(R.id.action_navigation_recipe_list_to_navigation_show_recipe, bundle);
        });
        return view;
    }

    private void setRecipeList(List<Recipe> recipeList) {
        requireActivity().runOnUiThread(() -> {
            Recipe[] recipes = new Recipe[recipeList.size()];
            recipes = recipeList.toArray(recipes);
            GridView recipesLV = requireActivity().findViewById(R.id.recipe_list);
            recipesLV.setAdapter(new ImageAdapter(requireActivity(), recipes));
            Recipe[] finalRecipes = recipes;
            recipesLV.setOnItemClickListener((parent, v, position, id) -> {
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("recipe", finalRecipes[position]);
                        Navigation.findNavController(v).navigate(R.id.action_navigation_recipe_list_to_navigation_show_recipe, bundle);
                    }
            );


        });
    }

    class ImageAdapter extends BaseAdapter {
        private final Context mContext;
        private final Recipe[] data;

        public ImageAdapter(Context c, Recipe[] data) {
            mContext = c;
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
            ImageView imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(200, 200));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);

            Thread t = new Thread(() -> {
                Bitmap img = data[position].getImage();
                Drawable image = img == null ? ResourcesCompat.getDrawable(getResources(), R.drawable.nofile, requireActivity().getTheme())
                        : new BitmapDrawable(getResources(), img);
                requireActivity().runOnUiThread(() -> imageView.setImageDrawable(image));
            });
            t.start();

            return imageView;
        }
    }
}
