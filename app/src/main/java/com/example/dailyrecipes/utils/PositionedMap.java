package com.example.dailyrecipes.utils;

import androidx.annotation.Nullable;

import com.example.dailyrecipes.model.Ingredient;
import com.example.dailyrecipes.model.ItemModel;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class PositionedMap<T extends ItemModel>{
    private final LinkedList<Integer> keysPositions = new LinkedList<>();
    private final HashMap<Integer, T> items;

    public PositionedMap(){
        items = new HashMap<>();
    }

    public PositionedMap(Map<Integer, T> items) {
        keysPositions.addAll(items.keySet());
        this.items = new HashMap<Integer, T>(items);
    }

    public Collection<T> values(){
        return items.values();
    }

    public List<Integer> ids(){
        return Collections.unmodifiableList(keysPositions);
    }

    public T get(int position) {
        return items.get(keysPositions.get(position));
    }

    public T get(Integer id) {
        return items.get(id);
    }

    public int size(){
        return items.size();
    }

    public T replace(int position, T newValue){
        Integer id = keysPositions.remove(position);
        items.remove(id);
        keysPositions.add(position, newValue.getId());
        return items.put(newValue.getId(), newValue);
    }

    @Nullable
    public T put(Integer key, T value) {
        keysPositions.add(key);
        return items.put(key, value);
    }

    @Nullable
    public T put(int position, T value){
        if(keysPositions.size()>position)
            keysPositions.add(position, value.getId());
        else keysPositions.add(value.getId());
        return items.put(value.getId(), value);
    }



    @Nullable
    public T remove(@Nullable Object key) {
        keysPositions.remove(key);
        return items.remove(key);
    }

    @Nullable
    public T remove(int key) {
        Integer id = keysPositions.remove(key);
        return items.remove(id);
    }
}
