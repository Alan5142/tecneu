package com.tecneu.tecneu.Providers;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tecneu.tecneu.R;
import com.tecneu.tecneu.Users.ModifyUserFragment;
import com.tecneu.tecneu.models.Provider;
import com.tecneu.tecneu.models.User;
import com.tecneu.tecneu.services.OnRequest;
import com.tecneu.tecneu.services.ProviderService;
import com.tecneu.tecneu.services.UserService;

import org.json.JSONException;

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
        if (getArguments() != null) {
            String json = getArguments().getString(ARG_PARAM1);
            providerToEdit = new Gson().fromJson(json, Provider.class);
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
                ProviderService.modifyProvider(getContext(), providerToEdit, new OnRequest() {
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