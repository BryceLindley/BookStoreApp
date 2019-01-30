package com.example.bryce.bookstoreapp2.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Database helper for BookStore app. Manages database creation and version management.
 */
public class ProductsDbHelper extends SQLiteOpenHelper {
    public static final String LOG_TAG = ProductsDbHelper.class.getSimpleName();
    /**
     * Name of the database file
     */
    private static final String DATABASE_NAME = "products.db";
    /**
     * Database version. If you change the database schema, you must increment the database version.
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * Constructs a new instance of {@link ProductsDbHelper}.
     *
     * @param context of the app
     */
    public ProductsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This is called when the database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a String that contains the SQL statement to create the products table
        String SQL_CREATE_PRODUCTS_TABLE = "CREATE TABLE " + BookStoreContract.Products.TABLE_NAME + " ("
                + BookStoreContract.Products._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + BookStoreContract.Products.COLUMN_NAME + " TEXT NOT NULL, "
                + BookStoreContract.Products.COLUMN_PRICE + " INTEGER, "
                + BookStoreContract.Products.COLUMN_QUANTITY + " INTEGER NOT NULL, "
                + BookStoreContract.Products.COLUMN_SUPPLIER_NAME + " TEXT NOT NULL, "
                + BookStoreContract.Products.COLUMN_SUPPLIER_PHONE_NUMBER + " INTEGER NOT NULL)";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_PRODUCTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}