package com.example.recipebook;

public class MyRecipe {

    private int _id;
    private String _recipeTitle;
    private String _recipeInfo;

    //Constructor with no parameter
    public MyRecipe(){

    }

    //Constructor with 3 parameters (Id, Title & Info)
    public MyRecipe(int id, String recipeTitle, String recipeInfo){
        this._id = id;
        this._recipeTitle = recipeTitle;
        this._recipeInfo = recipeInfo;
    }

    //Constructor with 2 parameters (Title & Info)
    public MyRecipe(String recipeTitle, String recipeInfo){
        this._recipeTitle = recipeTitle;
        this._recipeInfo = recipeInfo;
    }

    //get & set ID
    public void setID(int id){
        this._id = id;
    }

    //get & set ID
    public int getID(){
        return this._id;
    }

    //get & set recipe title
    public void setRecipeTitle(String recipeTitle){
        this._recipeTitle = recipeTitle;
    }

    //get & set recipe title
    public String getRecipeTitle(){
        return this._recipeTitle;
    }

    //get & set recipe info
    public void setRecipeInfo(String recipeInfo){
        this._recipeInfo = recipeInfo;
    }

    //get & set recipe info
    public String getRecipeInfo(){
        return this._recipeInfo;
    }
}
