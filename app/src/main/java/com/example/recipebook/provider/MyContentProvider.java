package com.example.recipebook.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import com.example.recipebook.MyDBHandler;

public class MyContentProvider extends ContentProvider {

    public static final int RECIPES = 1;
    public static final int RECIPE_ID = 2;

    //declare a URI matcher
    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    //register URI matcher table with id & URI matcher table only
    static {
        sURIMatcher.addURI(ProviderContract.AUTHORITY, ProviderContract.PRODUCTS_TABLE, RECIPES);
        sURIMatcher.addURI(ProviderContract.AUTHORITY, ProviderContract.PRODUCTS_TABLE + "/#", RECIPE_ID);
    }

    private MyDBHandler myDB;
    private SQLiteDatabase db;

    public MyContentProvider() {
    }

    @Override
    //handler request for delete row in database
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.

        //get the type by matching uri
        int uriType = sURIMatcher.match(uri);

        //get database to edit
        SQLiteDatabase sqlDB = myDB.getWritableDatabase();
        int rowsDeleted = 0;

        //uri type either is recipe table or recipe id
        switch (uriType) {
            case RECIPES:
                rowsDeleted = sqlDB.delete(MyDBHandler.TABLE_PRODUCTS,
                        selection,
                        selectionArgs);
                break;
            case RECIPE_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(MyDBHandler.TABLE_PRODUCTS,
                            MyDBHandler.COLUMN_ID + "=" + id,
                            null);
                } else {
                    rowsDeleted = sqlDB.delete(MyDBHandler.TABLE_PRODUCTS,
                            MyDBHandler.COLUMN_ID + "=" + id
                                    + " and " + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " +
                        uri);
        }

        //get content resolver
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    //handler request for insert recipe in database
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.

        //get the type by matching uri
        int uriType = sURIMatcher.match(uri);

        //get database to edit
        db = myDB.getWritableDatabase();
        long id = 0;

        //uri type either is recipe table or recipe id
        switch (uriType) {
            case RECIPES:
                id = db.insert(MyDBHandler.TABLE_PRODUCTS,null, values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        //get content resolver
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(ProviderContract.PRODUCTS_TABLE + "/" + id);
    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.

        //declare database
        myDB = new MyDBHandler(getContext(), null, null, 1);
        return false;
    }

    @Override
    //handler query request for find database
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.

        //declare query builder
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(MyDBHandler.TABLE_PRODUCTS);

        //get the type by matching uri
        int uriType = sURIMatcher.match(uri);

        //uri type either is recipe table or recipe id
        switch (uriType) {
            case RECIPE_ID:
                queryBuilder.appendWhere(MyDBHandler.COLUMN_ID + "=" + uri.getLastPathSegment());
                break;
            case RECIPES:
                break;
            default:
                throw new IllegalArgumentException("Unknown URI");
        }

        //use cursor to set notification
        Cursor cursor = queryBuilder.query(myDB.getReadableDatabase(), projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    //handler requests to update one or more rows
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.

        //get the type by matching uri
        int uriType = sURIMatcher.match(uri);

        SQLiteDatabase sqlDB = myDB.getWritableDatabase();
        int rowsUpdated = 0;

        //uri type either is recipe table or recipe id
        switch (uriType) {
            case RECIPES:
                rowsUpdated =
                        sqlDB.update(MyDBHandler.TABLE_PRODUCTS,
                                values,
                                selection,
                                selectionArgs);
                break;
            case RECIPE_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated =
                            sqlDB.update(MyDBHandler.TABLE_PRODUCTS,
                                    values,
                                    MyDBHandler.COLUMN_ID + "=" + id,
                                    null);
                } else {
                    rowsUpdated =
                            sqlDB.update(MyDBHandler.TABLE_PRODUCTS,
                                    values,
                                    MyDBHandler.COLUMN_ID + "=" + id
                                            + " and "
                                            + selection,
                                    selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri,
                null);
        return rowsUpdated;
    }
}
