package com.example.dailyrecipes.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dailyrecipes.R;
import com.example.dailyrecipes.fragments.ShowRecipeFragment;
import com.example.dailyrecipes.model.Recipe;

public class CustomRecipeAdapter extends ArrayAdapter<Recipe> {
    private ListView listView;
    private View.OnClickListener listener;
    public CustomRecipeAdapter(Context context, int textViewResourceId, Recipe[] objects, ListView listView) {
        super(context, textViewResourceId, objects);
        this.listView = listView;
        this.listener = listener;
    }


    static class ViewHolder {
        TextView text;
        Button btn;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Recipe recipe = getItem(position);

        View rowView = convertView;
        ViewHolder viewHolder;
        if (rowView == null) {
            LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
            rowView = inflater.inflate(R.layout.show_delete_ingredient_list, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.text = (TextView) rowView.findViewById(R.id.item_name_tv);
            viewHolder.btn = rowView.findViewById(R.id.delete_bt);
            rowView.setTag(viewHolder);
        }
        else viewHolder = (ViewHolder) rowView.getTag();

        viewHolder.text.setText(recipe.getName());
        Bundle bundle = new Bundle();
        bundle.putInt("id", recipe.getId());
        viewHolder.btn.setOnClickListener(v -> ((AppCompatActivity)getContext()).getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container_view, ShowRecipeFragment.class, bundle)
                .setReorderingAllowed(true)
                .addToBackStack("home") // name can be null
                .commit());
        return rowView;
    }
}
