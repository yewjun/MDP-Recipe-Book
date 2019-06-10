package com.example.recipebook;

import com.example.recipebook.provider.ProviderContract;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.content.ContentResolver;

import java.util.ArrayList;

public class MyDBHandler extends SQLiteOpenHelper{

    private static final String TAG = "G53MDP";
    private ContentResolver myCR;

    //declare database name & table name & database version
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "reccipeDB.db";
    public static final String TABLE_PRODUCTS = "recipes";

    //column to be created in table
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_RECIPETITLE = "recipeTitle";
    public static final String COLUMN_RECIPEINFO = "recipeInfo";

    //database handler constructor
    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);

        //get Content Resolver
        myCR = context.getContentResolver();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //create a table for recipe book with id, title & info columns
        String CREATE_PRODUCTS_TABLE = "CREATE TABLE " +
                TABLE_PRODUCTS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY," +
                COLUMN_RECIPETITLE
                + " TEXT," + COLUMN_RECIPEINFO + " INTEGER" + ")";

        //execute the sql command to create table
        db.execSQL(CREATE_PRODUCTS_TABLE);
        Log.i(TAG, "onCreate DB!");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG, "onUpgrade DB!");

        //execute sql command to drop table if the table is exists
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        onCreate(db);
    }

    //add new row to database
    public void addRecipe(MyRecipe recipe) {
        //declare values as content values
        ContentValues values = new ContentValues();

        //add title & info to content values
        values.put(COLUMN_RECIPETITLE, recipe.getRecipeTitle());
        values.put(COLUMN_RECIPEINFO, recipe.getRecipeInfo());

        //use content resolver to insert recipe
        myCR.insert(ProviderContract.CONTENT_URI, values);
        Log.i(TAG, "Inserted to DB!");

        //using database only, without content resolver
        /*
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_PRODUCTS, null, values);
        Log.i(TAG, "Inserted to DB!");
        db.close();
        */
    }

    //find specific data from database
    public MyRecipe findRecipe(String recipeTitle) {

        //a string projection that include id, title & info
        String[] projection = {COLUMN_ID, COLUMN_RECIPETITLE, COLUMN_RECIPEINFO};

        //check the selected title and recipe title in database
        String selection = "recipeTitle = \"" + recipeTitle + "\"";

        //cursor use content resolver to find the recipe title
        Cursor cursor = myCR.query(ProviderContract.CONTENT_URI, projection, selection, null, null);

        //using database only, without content resolver
        /*
        String query = "SELECT * FROM " + TABLE_PRODUCTS + " WHERE " +
                COLUMN_RECIPETITLE + " = \"" + recipeTitle + "\"";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        */

        //get id, title and info
        MyRecipe recipe = new MyRecipe();
        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            recipe.setID(Integer.parseInt(cursor.getString(0)));
            recipe.setRecipeTitle(cursor.getString(1));
            recipe.setRecipeInfo(cursor.getString(2));
            cursor.close();
            Log.i(TAG, "Found in DB!");
        } else {
            Log.i(TAG, "Not Found in DB!");
            recipe = null;
        }
        //db.close();
        return recipe;
    }

    //delete row in database
    public boolean deleteRecipe(String recipeTitle) {

        //to check data have been deleted or not
        boolean result = false;

        //check the selected title and recipe title in database
        String selection = "recipeTitle = \"" + recipeTitle + "\"";

        //content resolver to delete the recipe in database
        int rowDeleted = myCR.delete(ProviderContract.CONTENT_URI, selection, null);

        //if data is deleted return true
        if(rowDeleted > 0)
            result = true;
        Log.i(TAG, "Deleted in DB!");

        /*
        String query = "SELECT * FROM " + TABLE_PRODUCTS + " WHERE " +
                COLUMN_RECIPETITLE + " = \"" + recipeTitle + "\"";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        MyRecipe recipe = new MyRecipe();
        if (cursor.moveToFirst()) {
            recipe.setID(Integer.parseInt(cursor.getString(0)));
            db.delete(TABLE_PRODUCTS, COLUMN_ID + " = ?",
                    new String[] { String.valueOf(recipe.getID()) });
            cursor.close();
            Log.i(TAG, "Deleted in DB!");
            result = true;
        }
        db.close();
        */
        return result;
    }

    //update to database
    public void updateRecipe(int recipeID, MyRecipe recipe){

        //use content values to store title & info
        ContentValues values = new ContentValues();
        values.put(COLUMN_RECIPETITLE, recipe.getRecipeTitle());
        values.put(COLUMN_RECIPEINFO, recipe.getRecipeInfo());

        //check the selected title and recipe title in database
        String selection = "_id = \"" + recipeID + "\"";

        //content resolver to update the recipe to database
        myCR.update(ProviderContract.CONTENT_URI, values, selection,null);
        Log.i(TAG, "Updated to DB!");

        /*
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(TABLE_PRODUCTS, values, COLUMN_ID+"="+recipeID, null);
        Log.i(TAG, "Updated to DB!");
        db.close();
        */
    }

    //find all titles in database
    public ArrayList<String> findAllRecipe() {

        ArrayList<String> allRecipesTitles = new ArrayList<String>();
        String[] projection = {COLUMN_RECIPETITLE};

        //cursor use content resolver to find the all recipe titles
        Cursor cursor = myCR.query(ProviderContract.CONTENT_URI, projection, null, null,null);

        //add all titles to array list
        if(cursor.moveToFirst()){
            cursor.moveToFirst();
            do{
                allRecipesTitles.add(cursor.getString(0));
            }while(cursor.moveToNext());
            Log.i(TAG, "Cursor Ended");
            cursor.close();
        }

        //return all titles
        return allRecipesTitles;
    }
}
