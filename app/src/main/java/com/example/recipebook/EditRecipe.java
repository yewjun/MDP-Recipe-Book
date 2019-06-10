package com.example.recipebook;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class EditRecipe extends AppCompatActivity {

    private static final String TAG = "G53MDP";

    EditText recipeTitleBox;
    EditText recipeInfoBox;

    private String selectedRecipeTitle;
    private MyRecipe recipe;
    private int recipeID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_recipe);

        recipeTitleBox = (EditText)findViewById(R.id.EditableTitle);
        recipeInfoBox = (EditText)findViewById(R.id.EditableInfo);

        //get string bundle from Main Activity
        Bundle bundle = getIntent().getExtras();
        selectedRecipeTitle = bundle.getString("title");

        //initiate database and use find recipe function to check database with similar selected recipe
        MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);
        recipe = dbHandler.findRecipe(selectedRecipeTitle);

        //set the text box to the data saved in database
        recipeTitleBox.setText(recipe.getRecipeTitle());
        recipeInfoBox.setText(recipe.getRecipeInfo());
        recipeID = recipe.getID();
    }

    //if update recipe button is clicked
    public void updateRecipe(View view){

        //initiate database handler and update the database with edited info
        MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);
        MyRecipe recipe = new MyRecipe(recipeTitleBox.getText().toString(), recipeInfoBox.getText().toString());
        dbHandler.updateRecipe(recipeID, recipe);
        Log.i(TAG, "Updated Recipe!");
        finish();
    }

    //if delete recipe button is clicked
    public void removeRecipe(View view){

        //initiate database handler
        final MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);

        //a message box will pop out to confirm with user when delete the recipe
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //if Yes button clicked, delete recipe function is called to delete selected recipe
                //if No button clicked, back to EditRecipe interface
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        boolean result = dbHandler.deleteRecipe(recipe.getRecipeTitle());
                        if(result){
                            Log.i(TAG, "Deleted Recipe!");
                            finish();
                        }else{
                            recipeTitleBox.setText("No Match Found");
                        }
                        Log.i(TAG, "YES button clicked!");
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        Log.i(TAG, "NO button clicked!");
                        break;
                }
            }
        };

        //A alert message box with button Yes & No
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setMessage("Are you sure delete \"" +  recipe.getRecipeTitle() + "\" ?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
        Log.i(TAG, "Alert Message Box Pop Up!");
    }

    //let the title and info text box revert back to original content
    public void undoAll(View v) {
        recipeTitleBox.setText(recipe.getRecipeTitle());
        recipeInfoBox.setText(recipe.getRecipeInfo());
    }
}
