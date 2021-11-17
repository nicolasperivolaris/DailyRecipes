package com.example.dailyrecipes;

import android.app.Activity;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.dailyrecipes.model.Ingredient;
import com.example.dailyrecipes.model.IngredientsFactory;
import com.example.dailyrecipes.model.RecipesFactory;
import com.example.dailyrecipes.model.UnitsFactory;
import com.example.dailyrecipes.utils.ConnectionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public List<Ingredient> shoppingList;
    public static Activity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        shoppingList = getShoppingList();
        setContentView(R.layout.activity_main);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_recipe_list, R.id.navigation_recipe_list, R.id.navigation_ingredient_list)
                .build();

        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container_view);
        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController((BottomNavigationView) findViewById(R.id.nav_view), navController);


        ConnectionManager connection = new ViewModelProvider(this).get(ConnectionManager.class);
        connection.connect("192.168.2.1", 5500);
        RecipesFactory.instance.ConnectFactory(connection);
        IngredientsFactory.instance.ConnectFactory(connection);
        UnitsFactory.instance.ConnectFactory(connection);
    }

    private List<Ingredient> getShoppingList() {
        List<Ingredient> list = new ArrayList<>();
//todo
        return list;
    }

    @Override
    public boolean onSupportNavigateUp(){
        NavController navController =  ((NavHostFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_container_view)).getNavController();
        return navController.navigateUp() || super.onSupportNavigateUp();
    }
}