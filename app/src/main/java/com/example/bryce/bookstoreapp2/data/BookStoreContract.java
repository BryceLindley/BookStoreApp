package com.example.bryce.bookstoreapp2.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

// API Contract for the BookstoreApp2

public class BookStoreContract {
    public static final String CONTENT_AUTHORITY = "com.example.android.products";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_PRODUCTS = "products";

    private BookStoreContract() {
    }

    public static final class Products implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PRODUCTS);

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of product.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PRODUCTS;
        /**
         * The MIME type of the {@link #CONTENT_URI} for a single product.
         */

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PRODUCTS;

        public static final String _ID = BaseColumns._ID;
        public static final String TABLE_NAME = "products";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_PRICE = "price";
        public static final String COLUMN_QUANTITY = "quantity";
        public static final String COLUMN_SUPPLIER_NAME = "supplier_name";
        public static final String COLUMN_SUPPLIER_PHONE_NUMBER = "supplier_phone_number";
    }
}
