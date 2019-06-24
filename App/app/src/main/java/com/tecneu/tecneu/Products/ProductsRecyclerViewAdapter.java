package com.tecneu.tecneu.Products;

import android.app.Activity;
import android.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mercadolibre.android.sdk.ApiRequestListener;
import com.mercadolibre.android.sdk.ApiResponse;
import com.mercadolibre.android.sdk.Meli;
import com.squareup.picasso.Picasso;
import com.tecneu.tecneu.Products.ProductFragment.OnListFragmentInteractionListener;
import com.tecneu.tecneu.R;
import com.tecneu.tecneu.dummy.DummyContent.DummyItem;
import com.tecneu.tecneu.models.Product;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class ProductsRecyclerViewAdapter extends RecyclerView.Adapter<ProductsRecyclerViewAdapter.ViewHolder> {

    public final Activity activity;
    public List<Product> mValues;
    private final OnListFragmentInteractionListener mListener;

    public ProductsRecyclerViewAdapter(List<Product> items, OnListFragmentInteractionListener listener, Activity activity) {
        mValues = items;
        this.activity = activity;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_product, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Product product = mValues.get(position);
        holder.mItem = product;
        holder.mTitle.setText(product.title);
        holder.mPrice.setText(String.format("$%s %s", product.price.toString(), product.currency));
        holder.mAvailableQuantity.setText(String.format("%s", product.availableQuantity.toString()));
        Picasso.get().load(product.image).into(holder.mImage);

        holder.mView.setOnClickListener(holder);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final View mView;
        public final TextView mTitle;
        public final TextView mPrice;
        public final TextView mAvailableQuantity;
        public final ImageView mImage;
        public Product mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTitle = view.findViewById(R.id.fragment_product_title);
            mPrice = view.findViewById(R.id.fragment_product_price);
            mAvailableQuantity = view.findViewById(R.id.fragment_product_available_quantity);
            mImage = view.findViewById(R.id.fragment_product_image);
        }

        @Override
        public String toString() {
            return super.toString();
        }

        @Override
        public void onClick(View view) {
            AlertDialog.Builder alert = new AlertDialog.Builder(activity);
            alert.setTitle("Modificar stock");

            final EditText editText = new EditText(mView.getContext());
            editText.setText(mItem.availableQuantity.toString());


            alert.setView(editText);
            alert.setPositiveButton("Modificar", (dialog, which) -> {
                try {
                    String json = String.format("{\"available_quantity\": %d}", Integer.parseInt(editText.getText().toString()));
                    Meli.asyncPut("/items/" + mItem.id,
                            json,
                            Meli.getCurrentIdentity(activity.getApplicationContext()), new ApiRequestListener() {
                                @Override
                                public void onRequestProcessed(int requestCode, ApiResponse payload) {
                                    if (payload.getContent().contains("authorized")) {
                                        Toast.makeText(activity, "No se pudo modificar el stock", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    Toast.makeText(activity, "Modificado con exito", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onRequestStarted(int requestCode) {

                                }
                            });
                } catch (Exception e) {
                    Log.e("", e.getMessage());
                    Toast.makeText(activity, "Hubo un error al intentar modificar el stock", Toast.LENGTH_SHORT).show();
                }
            });
            alert.setNegativeButton("Cancelar", (dialog, which) -> {
                // se cierra solo
            });
            alert.create().show();
        }
    }
}
