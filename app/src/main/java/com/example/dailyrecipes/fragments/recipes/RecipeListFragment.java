package com.example.dailyrecipes.fragments.recipes;

import android.app.AlertDialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.dailyrecipes.MainActivity;
import com.example.dailyrecipes.R;
import com.example.dailyrecipes.model.Day;
import com.example.dailyrecipes.model.Recipe;
import com.example.dailyrecipes.model.RecipesFactory;
import com.example.dailyrecipes.queries.recipes.UpdateDayQuery;
import com.example.dailyrecipes.utils.ConnectionManager;
import com.example.dailyrecipes.utils.FTPManager;
import com.example.dailyrecipes.utils.PositionedMap;

import java.util.Collection;
import java.util.HashMap;

public class RecipeListFragment extends Fragment {
    private final static String TAG = "HomeFragment";
    private View view;
    private LayoutInflater inflater;
    private final RecipesFactory recipesFactory = MainActivity.recipesFactory;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_recipe_list, container, false);
        this.inflater = inflater;
        recipesFactory.addObserver((o, arg) -> setRecipeList(((HashMap<Integer, Recipe>) arg).values()));

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
        setRecipeList(recipesFactory.getDataList().values());
    }

    @Override
    public void onResume() {
        super.onResume();
        recipesFactory.update();
    }

    private void setRecipeList(Collection<Recipe> recipeList) {
        MainActivity.instance.runOnUiThread(() -> {
            Recipe[] recipes = new Recipe[recipeList.size()];
            recipes = recipeList.toArray(recipes);
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
            View item;
            if(convertView != null )
                item = convertView;
            else
            item = inflater.inflate(R.layout.recipe_item_list, parent, false);
            item.setPadding(8, 8, 8, 8);

            Thread t = new Thread(() -> {
                Recipe recipe = data[position];
                requireActivity().runOnUiThread(() -> {
                    ((TextView)item.findViewById(R.id.name_tv)).setText(recipe.getName());
                    ImageView image = (ImageView) item.findViewById(R.id.recipe_img);
                    try {
                        image.setImageURI(recipe.getImage());
                    }catch (Exception e){ image.setImageURI(Recipe.noImage);}
                    image.setOnClickListener(v -> {
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("recipe", data[position]);
                        bundle.putBoolean("editable", false);
                        Navigation.findNavController(v).navigate(R.id.action_navigation_recipe_list_to_navigation_show_recipe, bundle);
                    });
                    item.findViewById(R.id.day_bt).setOnClickListener(bt -> createDayListDialog(recipe));
                });
            });
            t.start();

            return item;
        }

        private void createDayListDialog(Recipe recipe){
            AlertDialog.Builder builder = new AlertDialog.Builder(inflater.getContext());
            builder.setTitle("Choose a day");
            PositionedMap<Day> days = recipesFactory.getDayFactory().getDataList();
            String[] values = new String[days.size()];
            int i = 0;
            for (Day d : days.values()) {
                values[i] = d.getName();
                i++;
            }

            builder.setSingleChoiceItems(values, days.getPosition(recipe.getDay()), (dialogInterface, position) -> {
                dialogInterface.dismiss();
                recipe.setDay(days.get(position));

                UpdateDayQuery query = new UpdateDayQuery(this::savedCallBack, recipe);
                ConnectionManager connectionManager = new ViewModelProvider(requireActivity()).get(ConnectionManager.class);
                connectionManager.make(query);
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }

        private void savedCallBack(Integer errorCode) {
            requireActivity().runOnUiThread(() -> {
                if (errorCode != 0) Toast.makeText(getContext(), "Error while saving", Toast.LENGTH_LONG).show();
                else Toast.makeText(getContext(), "Saved", Toast.LENGTH_LONG).show();
            });
        }
    }
}
