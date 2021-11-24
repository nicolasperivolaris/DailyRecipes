package com.example.dailyrecipes.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.dailyrecipes.model.ItemModel;

public class ItemAdapter extends BaseAdapter {

    private PositionedMap<? extends ItemModel> model;
    private LayoutInflater inflater;

    public ItemAdapter(PositionedMap<? extends ItemModel> map, Context context){
        model = map;
        this.inflater = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return model.size();
    }

    @Override
    public ItemModel getItem(int id) {
        return model.get((Integer)id);
    }

    @Override
    public long getItemId(int position) {
        return model.ids().get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        view.setText(getItem((int) getItemId(position)).getName());
        return view;
    }

    public ItemModel getItemByPosition(int which) {
        return getItem((int) getItemId(which));
    }
}