package com.tecneu.tecneu;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mercadolibre.android.sdk.ApiRequestListener;
import com.mercadolibre.android.sdk.ApiResponse;
import com.mercadolibre.android.sdk.Identity;
import com.mercadolibre.android.sdk.Meli;
import com.tecneu.tecneu.dummy.DummyContent;
import com.tecneu.tecneu.dummy.DummyContent.DummyItem;
import com.tecneu.tecneu.models.Product;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ProductFragment extends Fragment {

    // TODO: Customize parameter argument names
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ProductFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ProductFragment newInstance() {
        ProductFragment fragment = new ProductFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            Identity identity = Meli.getCurrentIdentity(context);
            if (identity == null) {
                Toast.makeText(context, "No estas autorizado", Toast.LENGTH_SHORT).show();
            } else {
                // TODO cambiar por autorizado
                // ApiResponse response = Meli.getAuth("/users/250734247/items/search", Meli.getCurrentIdentity(context));
                Meli.asyncGet("/sites/MLM/search?seller_id=250734247", new ApiRequestListener() {
                    @Override
                    public void onRequestProcessed(int requestCode, ApiResponse response) {
                        if (response.getContent() != null) {
                            ArrayList<Product> products = new ArrayList<>();
                            JsonObject json = new JsonParser().parse(response.getContent()).getAsJsonObject();
                            JsonArray items = json.getAsJsonArray("results");
                            for (JsonElement element : items) {
                                JsonObject productObject = element.getAsJsonObject();
                                Product product = new Product();
                                product.currency = productObject.get("currency_id").getAsString();
                                product.price = productObject.get("price").getAsBigDecimal();
                                product.id = productObject.get("id").getAsString();
                                product.title = productObject.get("title").getAsString();
                                product.availableQuantity = productObject.get("available_quantity").getAsBigDecimal();
                                product.image = productObject.get("thumbnail").getAsString();
                                products.add(product);
                            }
                            ProductsRecyclerViewAdapter adapter = (ProductsRecyclerViewAdapter) recyclerView.getAdapter();
                            if (adapter != null) {
                                adapter.mValues = products;
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }

                    @Override
                    public void onRequestStarted(int requestCode) {

                    }
                });
            }
            recyclerView.setAdapter(new ProductsRecyclerViewAdapter(new ArrayList<>(), mListener));
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Product item);
    }
}
