package com.example.dailyrecipes.fragments;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.example.dailyrecipes.R;
import com.example.dailyrecipes.model.Recipe;
import com.example.dailyrecipes.queries.RecipeListQuery;
import com.example.dailyrecipes.utils.ConnectionManager;

public class HomeFragment extends Fragment{
    ConnectionManager connection;
    private RecipeListQuery recipeListQuery;
    private final static String TAG = "HomeFragment";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home2, container, false);

        connection = new ViewModelProvider(requireActivity()).get(ConnectionManager.class);
        recipeListQuery = new RecipeListQuery(() -> setRecipeList());

        Thread t = new Thread(() -> {
            int time = 0; boolean send = false;
            while(time <3000 && !send) {
                if(connection.isReady()) {
                    connection.askServer(recipeListQuery);
                    send = true;
                }
                try {
                    Thread.sleep(time+= 100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        t.start();
        return view;
    }

    private void setRecipeList(){
        requireActivity().runOnUiThread(() -> {
            Recipe[] recipes = new Recipe[recipeListQuery.getData().size()];
            recipes = recipeListQuery.getData().toArray(recipes);
            GridView recipesLV = requireActivity().findViewById(R.id.recipe_list);
            recipesLV.setAdapter(new ImageAdapter(requireActivity(), recipes));
            Recipe[] finalRecipes = recipes;
            recipesLV.setOnItemClickListener((parent, v, position, id) -> {
                Bundle bundle = new Bundle();
                bundle.putInt("id", finalRecipes[position].getId());
                ShowRecipeFragment fragment = new ShowRecipeFragment();
                fragment.setArguments(bundle);
                getParentFragmentManager().beginTransaction()
                        .add(R.id.fragment_container_view, fragment)
                        .setReorderingAllowed(true)
                        .addToBackStack("home") // name can be null
                        .commit();
                    }
            );
        });
    }

    class ImageAdapter extends BaseAdapter {
        private Context mContext;
        private Recipe[] data;
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
                Drawable image = img == null ? ResourcesCompat.getDrawable(getResources(), R.drawable.nofile, getActivity().getTheme())
                        : new BitmapDrawable(getResources(),img);
                requireActivity().runOnUiThread(() -> imageView.setImageDrawable(image));
            });
            t.start();

            return imageView;
        }
    }
}
