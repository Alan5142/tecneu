package com.tecneu.tecneu;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tecneu.tecneu.ProductFragment.OnListFragmentInteractionListener;
import com.tecneu.tecneu.dummy.DummyContent.DummyItem;
import com.tecneu.tecneu.models.Product;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class ProductsRecyclerViewAdapter extends RecyclerView.Adapter<ProductsRecyclerViewAdapter.ViewHolder> {

    public List<Product> mValues;
    private final OnListFragmentInteractionListener mListener;

    public ProductsRecyclerViewAdapter(List<Product> items, OnListFragmentInteractionListener listener) {
        mValues = items;
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
        holder.mAvailableQuantity.setText(String.format("%s",product.availableQuantity.toString()));
        Picasso.get().load(product.image).into(holder.mImage);

        holder.mView.setOnClickListener(v -> {
            if (null != mListener) {
                // Notify the active callbacks interface (the activity, if the
                // fragment is attached to one) that an item has been selected.
                mListener.onListFragmentInteraction(holder.mItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
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
    }
}
