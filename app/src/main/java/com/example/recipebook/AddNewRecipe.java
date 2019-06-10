package com.example.recipebook;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class AddNewRecipe extends AppCompatActivity {

    private static final String TAG = "G53MDP";

    EditText recipeTitleBox;
    EditText recipeInfoBox;
    private MyRecipe similarRecipeTitle;
    private MyRecipe recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_recipe);

        recipeTitleBox = (EditText) findViewById(R.id.EditableTitle);
        recipeInfoBox = (EditText) findViewById(R.id.EditableInfo);
    }

    //when add new recipe button is clicked
    public void addNewRecipe(View view){

        //initiate database handler
        MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);

        //use find recipe function to find the database got similar title or not
        similarRecipeTitle = dbHandler.findRecipe(recipeTitleBox.getText().toString());
        Log.i(TAG, "MyRecipe Object: "+similarRecipeTitle);

        //if database have no similar title, then add the new recipe
        if(similarRecipeTitle == null) {
            recipe = new MyRecipe(recipeTitleBox.getText().toString(), recipeInfoBox.getText().toString());
            dbHandler.addRecipe(recipe);
            recipeTitleBox.setText("");
            recipeInfoBox.setText("");
            Log.i(TAG, "Added Recipe!");
            finish();

        //if database have similar title, ask user to change title
        }else{
            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
            dlgAlert.setMessage("You have duplicated recipe title. Please change the recipe title.");
            dlgAlert.setTitle("Duplicated Title");
            dlgAlert.setPositiveButton("OK", null);
            dlgAlert.setCancelable(true);
            dlgAlert.create().show();
            Log.i(TAG, "Alert Message Box Pop Up!");

            dlgAlert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    //dismiss the dialog
                    Log.i(TAG, "OK button clicked!");
                }
            });
        }
    }

    //back to main page
    public void backButton(View v) {
        finish();
    }
}
