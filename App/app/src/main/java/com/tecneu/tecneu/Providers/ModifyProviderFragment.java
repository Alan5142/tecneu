package com.tecneu.tecneu.Providers;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tecneu.tecneu.R;
import com.tecneu.tecneu.models.Provider;
import com.tecneu.tecneu.models.ProviderProduct;
import com.tecneu.tecneu.services.OnRequest;
import com.tecneu.tecneu.services.ProductService;
import com.tecneu.tecneu.services.ProviderService;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ModifyProviderFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ModifyProviderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ModifyProviderFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ArrayList<ProviderProduct> selectedProducts;
    private ArrayList<ProviderProduct> nonSelectedProducts;

    // TODO: Rename and change types of parameters
    private Provider providerToEdit;

    private ModifyProviderFragment.OnFragmentInteractionListener mListener;

    public ModifyProviderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ModifyUserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ModifyProviderFragment newInstance(Provider provider) {
        ModifyProviderFragment fragment = new ModifyProviderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, new Gson().toJson(provider));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        selectedProducts = new ArrayList<>();
        nonSelectedProducts = new ArrayList<>();
        if (getArguments() != null) {
            String json = getArguments().getString(ARG_PARAM1);
            providerToEdit = new Gson().fromJson(json, Provider.class);
            ProviderService.getProviderProducts(getContext(), providerToEdit.companyName, new OnRequest() {
                @Override
                public void onSuccess(Object result) {
                    selectedProducts = (ArrayList<ProviderProduct>) result;
                    ProductService.getProducts(getContext(), new OnRequest() {
                        @Override
                        public void onSuccess(Object result) {
                            ArrayList<ProviderProduct> providerProducts = (ArrayList<ProviderProduct>) result;
                            if (selectedProducts.isEmpty()) {
                                nonSelectedProducts = providerProducts;
                                return;
                            }
                            for (ProviderProduct p : providerProducts) {
                                for (ProviderProduct u : selectedProducts) {
                                    if (p.idProduct != u.idProduct) {
                                        nonSelectedProducts.add(p);
                                    }
                                }
                            }
                        }

                        @Override
                        public void onError() {

                        }
                    });
                }

                @Override
                public void onError() {

                }
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_modify_provider, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EditText personName = view.findViewById(R.id.fragment_modify_provider_name);
        personName.setText(providerToEdit.personName);
        EditText companyName = view.findViewById(R.id.fragment_modify_provider_company);
        companyName.setText(providerToEdit.companyName);
        EditText email = view.findViewById(R.id.fragment_modify_provider_email);
        email.setText(providerToEdit.email);
        EditText phone = view.findViewById(R.id.fragment_modify_provider_phone);
        phone.setText(providerToEdit.phoneNumber);
        Button button = view.findViewById(R.id.fragment_modify_provider_modify_btn);
        button.setOnClickListener(v -> {
            providerToEdit.personName = personName.getText().toString();
            providerToEdit.companyName = companyName.getText().toString();
            providerToEdit.email = email.getText().toString();
            providerToEdit.phoneNumber = phone.getText().toString();

            if (providerToEdit.personName.isEmpty()) {
                Toast.makeText(getContext(), "Llena el nombre de la persona", Toast.LENGTH_SHORT).show();
                return;
            }

            if (providerToEdit.companyName.isEmpty()) {
                Toast.makeText(getContext(), "Llena el nombre de la compañia", Toast.LENGTH_SHORT).show();
                return;
            }

            if (providerToEdit.email.isEmpty()) {
                Toast.makeText(getContext(), "Llena el email", Toast.LENGTH_SHORT).show();
                return;
            }

            if (providerToEdit.phoneNumber.isEmpty()) {
                Toast.makeText(getContext(), "Llena el nombre de la persona", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!providerToEdit.email.matches("^(([^<>()\\[\\]\\\\.,;:\\s@\"]+(\\.[^<>()\\[\\]\\\\.,;:\\s@\"]+)*)|(\".+\"))@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$")) {
                Toast.makeText(getContext(), "El email no es valido", Toast.LENGTH_SHORT).show();
                return;
            }

            if (providerToEdit.phoneNumber.length() != 13) {
                Toast.makeText(getContext(), "El teléfono debe ser de 13 digitos", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                ProviderService.modifyProvider(getContext(),
                        providerToEdit,
                        selectedProducts,
                        nonSelectedProducts,
                        new OnRequest() {
                    @Override
                    public void onSuccess(Object result) {
                        Toast.makeText(getContext(), "Modificado con exito", Toast.LENGTH_SHORT).show();
                        getActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment, ProviderFragment.newInstance())
                                .commit();
                    }

                    @Override
                    public void onError() {
                        Toast.makeText(getContext(), "No se pudo editar", Toast.LENGTH_SHORT).show();
                        Objects.requireNonNull(getActivity())
                                .getSupportFragmentManager()
                                .popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    }
                });
            } catch (JSONException e) {
                Toast.makeText(getContext(), "No se pudo editar", Toast.LENGTH_SHORT).show();
            }
        });

        view.findViewById(R.id.fragment_modify_provider_products).setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Elige un producto para agregar");

            ArrayList<String> productsName = new ArrayList<>();

            for (ProviderProduct databaseProduct : nonSelectedProducts) {
                productsName.add(databaseProduct.name);
            }

            builder.setNegativeButton("Cancelar", ((dialog, which) -> {

            }));

            builder.setItems(productsName.toArray(new String[0]), (dialog, which) -> {
                ProviderProduct product = nonSelectedProducts.get(which);
                AlertDialog.Builder priceBuilder = new AlertDialog.Builder(getContext());
                priceBuilder.setTitle("Precio del proveedor");
                EditText editText = new EditText(getContext());
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                editText.setLayoutParams(lp);

                priceBuilder.setView(editText);

                priceBuilder.setNegativeButton("Cancelar", null);
                priceBuilder.setPositiveButton("Establecer precio", (dialog1, which1) -> {
                    product.price = Integer.valueOf(editText.getText().toString());
                    selectedProducts.add(product);
                    nonSelectedProducts.remove(which);
                    Toast.makeText(getContext(), "Añadido", Toast.LENGTH_SHORT).show();
                });

                priceBuilder.show();
            });
            builder.show();
            /*
            ProductService.getProducts(getContext(), new OnRequest() {
                @Override
                public void onSuccess(Object result) {
                    ArrayList<ProviderProduct> products = (ArrayList<ProviderProduct>) result;
                    ArrayList<String> productsName = new ArrayList<>();

                    for (ProviderProduct databaseProduct : products) {
                        productsName.add(databaseProduct.name);
                    }

                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Elige un producto");

                    builder.setNegativeButton("Cancelar", ((dialog, which) -> {

                    }));

                    builder.setItems(productsName.toArray(new String[0]), (dialog, which) -> {
                        ProviderProduct product = products.get(which);
                        for (int i = 0; i < selectedProducts.size(); i++) {
                            ProviderProduct p = selectedProducts.get(i);
                            if (p.idProduct == product.idProduct) {
                                selectedProducts.remove(i);
                                Toast.makeText(getContext(), "Producto eliminado del vendedor", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                        AlertDialog.Builder priceBuilder = new AlertDialog.Builder(getContext());
                        priceBuilder.setTitle("Precio del proveedor");
                        EditText editText = new EditText(getContext());
                        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.MATCH_PARENT);
                        editText.setLayoutParams(lp);

                        priceBuilder.setView(editText);

                        priceBuilder.setNegativeButton("Cancelar", null);
                        priceBuilder.setPositiveButton("Establecer precio", (dialog1, which1) -> {
                            product.price = Integer.valueOf(editText.getText().toString());
                            selectedProducts.add(product);
                            Toast.makeText(getContext(), "Añadido", Toast.LENGTH_SHORT).show();
                        });

                        priceBuilder.show();
                    });
                    builder.show();
                }

                @Override
                public void onError() {

                }
            });
            */
        });

        view.findViewById(R.id.fragment_modify_provider_remove_products).setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Elige un producto para eliminar");

            ArrayList<String> productsName = new ArrayList<>();

            for (ProviderProduct databaseProduct : selectedProducts) {
                productsName.add(databaseProduct.name);
            }

            builder.setNegativeButton("Cancelar", ((dialog, which) -> {

            }));

            builder.setItems(productsName.toArray(new String[0]), (dialog, which) -> {
                ProviderProduct product = selectedProducts.get(which);

                nonSelectedProducts.add(product);
                selectedProducts.remove(which);
            });
            builder.show();
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
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