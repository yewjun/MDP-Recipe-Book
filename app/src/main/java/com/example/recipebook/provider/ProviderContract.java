package com.example.recipebook.provider;

import android.net.Uri;

/**
 * Created by user on 25/11/2018.
 */

//Provider Contract class for content provider
public class ProviderContract {

    //recipe book authority provider package
    public static final String AUTHORITY = "com.example.recipebook.provider.MyContentProvider";

    //recipe book's table name
    public static final String PRODUCTS_TABLE = "recipes";

    //recipe book's content uri
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + PRODUCTS_TABLE);
}
