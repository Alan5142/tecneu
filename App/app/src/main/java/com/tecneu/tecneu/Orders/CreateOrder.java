package com.tecneu.tecneu.Orders;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.tecneu.tecneu.R;
import com.tecneu.tecneu.models.Provider;
import com.tecneu.tecneu.services.OnRequest;
import com.tecneu.tecneu.services.ProviderService;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CreateOrder.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CreateOrder#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateOrder extends Fragment {
    private OnFragmentInteractionListener mListener;

    public CreateOrder() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CreateOrder.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateOrder newInstance() {
        CreateOrder fragment = new CreateOrder(); // hey! :D
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_order, container, false);
    }


    private Integer idProvider = null;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EditText providerName = view.findViewById(R.id.fragment_create_order_provider_search);
        ListView orderInformation = view.findViewById(R.id.fragment_create_order_order_details);
        Button searchProviderButton = view.findViewById(R.id.fragment_create_order_provider_search_btn);
        Button addToOrderButton = view.findViewById(R.id.fragment_create_order_product_add_btn);
        EditText productName = view.findViewById(R.id.fragment_create_order_product_search);
        EditText productQuantity = view.findViewById(R.id.fragment_create_order_product_quantity);
        searchProviderButton.setOnClickListener(v -> {
            AlertDialog.Builder builderSingle = new AlertDialog.Builder(getContext());
            builderSingle.setTitle("Elige proveedor:-");
            ProviderService.getAllProviders(getContext(), new OnRequest() {
                @Override
                public void onSuccess(Object result) {
                    ArrayList<Provider> providers = (ArrayList<Provider>) result;
                    final ArrayAdapter<String> providersAdapter = new ArrayAdapter<>(getContext(), android.R.layout.select_dialog_singlechoice);
                    for (Provider p : providers) {
                        providersAdapter.add(p.companyName);
                    }
                    builderSingle.setNegativeButton("CANCELAR", (dialog, which) -> dialog.dismiss());

                    builderSingle.setAdapter(providersAdapter, (dialog, which) -> {
                        providerName.setText(providers.get(which).companyName);
                        idProvider = which;
                    });
                    builderSingle.show();
                }

                @Override
                public void onError() {
                }
            });
        });
        addToOrderButton.setOnClickListener(v -> {
            try {
                int quantity = Integer.parseInt(productQuantity.getText().toString());
                if (quantity == 0) {
                    Toast.makeText(getContext(), "La cantidad no peude ser 0", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (productName.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "El producto no puede estar vacio", Toast.LENGTH_SHORT).show();
                    return;
                }
                ListAdapter adapter = orderInformation.getAdapter();
                if (adapter == null) {
                    adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1);
                }
                ArrayAdapter<String> adapterString = (ArrayAdapter<String>) adapter;
                adapterString.add(productName.getText().toString() + ", " + providerName.getText().toString() + ", " + quantity);
                orderInformation.setAdapter(adapter);
            } catch(Exception e) {
                Log.e("", e.getMessage());
                Toast.makeText(getContext(), "No se pudo agregar", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
