package com.example.bryce.bookstoreapp2;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.bryce.bookstoreapp2.data.BookStoreContract;

/**
 * {@link ProductCursorAdapter} is an adapter for a list or grid view
 * that uses a {@link Cursor} of product data as its data source. This adapter knows
 * how to create list items for each row of product data in the {@link Cursor}.
 */
public class ProductCursorAdapter extends CursorAdapter {

    public ProductCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    /**
     * This method binds the product data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current product can be set on the name TextView
     * in the list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {

        // Find fields to populate list item layout
        TextView nameTextView = view.findViewById(R.id.name);
        TextView priceTextView = view.findViewById(R.id.price);
        final TextView quantityTextView = view.findViewById(R.id.quantity);

        // find the columns
        int nameColumnIndex = cursor.getColumnIndex(BookStoreContract.Products.COLUMN_NAME);
        int priceColumnIndex = cursor.getColumnIndex(BookStoreContract.Products.COLUMN_PRICE);
        int quantityColumnIndex = cursor.getColumnIndex(BookStoreContract.Products.COLUMN_QUANTITY);
        int idColumnIndex = cursor.getColumnIndex(BookStoreContract.Products._ID);
        // Read the product attributes from the Cursor for the current product
        String productName = cursor.getString(nameColumnIndex);
        String productPrice = cursor.getString(priceColumnIndex);
        final String productQuantity = cursor.getString(quantityColumnIndex);

        // Update the TextViews
        nameTextView.setText(productName);
        priceTextView.setText(productPrice);
        quantityTextView.setText(productQuantity);

        // Implement Sale Button within ListView
        final Button soldButton = view.findViewById(R.id.sell_this_button);
        soldButton.setFocusable(false);
        soldButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                int soldProduct = Integer.parseInt(quantityTextView.getText().toString());
                if (soldProduct > 0) {
                    soldProduct =  soldProduct -1;
                    quantityTextView.setText(Integer.toString(soldProduct));
                }
            }
        });
    }
}


