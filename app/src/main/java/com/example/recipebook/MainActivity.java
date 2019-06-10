package com.example.recipebook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.time.Instant;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "G53MDP";

    private ListView lv;

    private String selectedRecipeTitle;

    ArrayList<String> allRecipesTitles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //create list view
        setListViewAdapter();
    }

    //add new recipe button clicked
    public void onAddNewClick(View view){
        //pass intent to AddNewRecipe class
        Intent intent = new Intent(MainActivity.this, AddNewRecipe.class);
        startActivity(intent);
    }

    //set up list view
    private void setListViewAdapter() {

        //check the list is empty or not
        Boolean emptyTitle = false;

        //database handler
        final MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);

        //array list to hold recipe titles
        allRecipesTitles = new ArrayList<String>();

        //call find all recipe function to check database
        allRecipesTitles = dbHandler.findAllRecipe();

        Log.i(TAG, String.valueOf(allRecipesTitles.size()));

        //if database have no recipe titles, then print "No Recipe Available"
        if(allRecipesTitles.size() == 0){
            allRecipesTitles.add("No Recipe Available");
            emptyTitle = true;
        }

        lv = (ListView) findViewById(R.id.RecipesListView);

        //set list view
        lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, allRecipesTitles));

        final Boolean finalEmptyTitle = emptyTitle;

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {

                if(finalEmptyTitle){
                    //"No Recipe Available" do nothing
                }else {
                    //get the selected list item integer
                    selectedRecipeTitle = (String) (lv.getItemAtPosition(myItemInt));

                    //pass intent to EditRecipe class
                    Intent intent = new Intent(MainActivity.this, EditRecipe.class);

                    //create bundle and add selected recipe title
                    Bundle bundle = new Bundle();
                    bundle.putString("title", selectedRecipeTitle);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
    }

    //Update the Recipe Title List when on resume
    @Override
    public void onResume() {
        super.onResume();
        setListViewAdapter();
    }
}
