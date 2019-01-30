package com.example.bryce.bookstoreapp2.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;


public class BookStoreProvider extends ContentProvider {


    /**
     * URI matcher code for the content URI for the products table
     */
    public static final int PRODUCTS = 100;
    public static final int PRODUCTS_ID = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // Static initializer. This is run the first time anything is called from this class.
    static {
        sUriMatcher.addURI(BookStoreContract.CONTENT_AUTHORITY, BookStoreContract.PATH_PRODUCTS, PRODUCTS);
        sUriMatcher.addURI(BookStoreContract.CONTENT_AUTHORITY, BookStoreContract.PATH_PRODUCTS + "/#", PRODUCTS_ID);
    }

    public static final String LOG_TAG = BookStoreProvider.class.getSimpleName();

    private ProductsDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new ProductsDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Get readable database
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        // This cursor will hold the result of the query
        Cursor cursor;

        // Figure out if the URI matcher can match the URI to a specific code
        int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                cursor = database.query(BookStoreContract.Products.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case PRODUCTS_ID:

                selection = BookStoreContract.Products._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                // This will perform a query on the products table where the _id equals 3 to return a
                // Cursor containing that row of the table.
                cursor = database.query(BookStoreContract.Products.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        // Set notification URI on the Cursor,
        // so we know what content URI the Cursor was created for.
        // If the data at this URI changes, then we know we need to update the Cursor.
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        // Return the cursor

        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                return insertProduct(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertProduct(Uri uri, ContentValues values) {
        // Check that the name is not null
        String name = values.getAsString(BookStoreContract.Products.COLUMN_NAME);
        if (name == null) {
            throw new IllegalArgumentException("Product requires a name");
        }
        // Check that the price is inserted
        Integer price = values.getAsInteger(BookStoreContract.Products.COLUMN_PRICE);
        if (price == null) {
            throw new IllegalArgumentException("Product requires a price");
        }
        // If the amount is provided, check that it's greater than or equal to 0 kg
        Integer amount = values.getAsInteger(BookStoreContract.Products.COLUMN_QUANTITY);
        if (amount == null || amount < 0) {
            throw new IllegalArgumentException("Product requires a valid amount");
        }
        // If the supplier name is not proviced throw error
        String supplierName = values.getAsString(BookStoreContract.Products.COLUMN_SUPPLIER_NAME);
        if (supplierName == null) {
            throw new IllegalArgumentException("Product requires supplier name");
        }
        // If the supplier phone number is not provided
        Integer phoneNumber = values.getAsInteger(BookStoreContract.Products.COLUMN_SUPPLIER_PHONE_NUMBER);
        if (phoneNumber < 0) {
            throw new IllegalArgumentException("Product requires supplier phone number");
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        // Insert the new product with the given values
        long id = database.insert(BookStoreContract.Products.TABLE_NAME, null, values);
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        // Notify all listeners that the data has changed for the content URI
        getContext().getContentResolver().notifyChange(uri, null);


        // Return the new URI with the ID (of the newly inserted row) appended at the end
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                return updateProduct(uri, contentValues, selection, selectionArgs);
            case PRODUCTS_ID:
                selection = BookStoreContract.Products._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateProduct(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updateProduct(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        if (values.containsKey(BookStoreContract.Products.COLUMN_NAME)) {
            String name = values.getAsString(BookStoreContract.Products.COLUMN_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Product requires a name");
            }
        }

        if (values.containsKey(BookStoreContract.Products.COLUMN_PRICE)) {
            Integer price = values.getAsInteger(BookStoreContract.Products.COLUMN_PRICE);
            if (price == null) {

                throw new IllegalArgumentException("Price required");
            }
        }

        if (values.containsKey(BookStoreContract.Products.COLUMN_QUANTITY)) {
            Integer amount = values.getAsInteger(BookStoreContract.Products.COLUMN_QUANTITY);
            if (amount == null) {

                throw new IllegalArgumentException("Product requires an amount");
            }
        }

        if (values.containsKey(BookStoreContract.Products.COLUMN_SUPPLIER_NAME)) {
            String supplierName = values.getAsString(BookStoreContract.Products.COLUMN_SUPPLIER_NAME);
            if (supplierName == null) {
                throw new IllegalArgumentException("Product requires a user name");
            }
        }

        if (values.containsKey(BookStoreContract.Products.COLUMN_SUPPLIER_PHONE_NUMBER)) {
            String supplierNumber = values.getAsString(BookStoreContract.Products.COLUMN_SUPPLIER_PHONE_NUMBER);
            if (supplierNumber == null) {
                throw new IllegalArgumentException("User phone number required");
            }
        }


        // Otherwise, get writeable database to update the data
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        // Perform the update on the database and get the number of rows affected
        int rowsUpdated = database.update(BookStoreContract.Products.TABLE_NAME, values, selection, selectionArgs);

        // Return the number of rows updated
        return rowsUpdated;

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Get writeable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        // Track the number of rows that were deleted
        int rowsDeleted;
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                // Delete all rows that match the selection and selection args
                rowsDeleted = database.delete(BookStoreContract.Products.TABLE_NAME, selection, selectionArgs);
                break;
            case PRODUCTS_ID:
                // Delete a single row given by the ID in the URI
                selection = BookStoreContract.Products._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(BookStoreContract.Products.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
        // If 1 or more rows were deleted, then notify all listeners that the data at the
        // given URI has changed
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        // Return the number of rows deleted
        return rowsDeleted;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                return BookStoreContract.Products.CONTENT_LIST_TYPE;
            case PRODUCTS_ID:
                return BookStoreContract.Products.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }
}
