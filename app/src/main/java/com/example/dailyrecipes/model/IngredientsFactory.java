package com.example.dailyrecipes.model;

import com.example.dailyrecipes.queries.ingredients.AddIngredientQuery;
import com.example.dailyrecipes.queries.ingredients.IngredientsListQuery;
import com.example.dailyrecipes.utils.ConnectionManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IngredientsFactory {
    private static List<Ingredient> ingredientList = new ArrayList<>();
    private static ConnectionManager connection;

    public static void ConnectIngredientsFactory(ConnectionManager connectionManager){
        ingredientList = new ArrayList<>();
        connection = connectionManager;
        connection.make(new IngredientsListQuery(IngredientsFactory::SetIngredientsList));
    }

    private static void SetIngredientsList(List<Ingredient> ingredients){
        ingredientList = ingredients;
    }

    /**
     * Don't modify the list
     */
    public static List<Ingredient> GetIngredientList() {
        return Collections.unmodifiableList(ingredientList);
    }

    public static String[] GetIngredientsNames(){
        String[] ingredientsNames = new String[ingredientList.size()];
        for (int i = 0; i<ingredientList.size(); i++) {
            ingredientsNames[i] = ingredientList.get(i).getName();
        }
        return ingredientsNames;
    }

    public static void Add(Ingredient ingredient){
        connection.make(new AddIngredientQuery(ingredient, i -> {}));
    }

    public static void Update(Ingredient ingredient){

    }

    public static void Delete(Ingredient ingredient){

    }


}
