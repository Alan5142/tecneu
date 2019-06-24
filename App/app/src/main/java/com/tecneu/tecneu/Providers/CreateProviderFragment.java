package com.tecneu.tecneu.Providers;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.tecneu.tecneu.R;
import com.tecneu.tecneu.Users.CreateUserFragment;
import com.tecneu.tecneu.services.OnRequest;
import com.tecneu.tecneu.services.ProviderService;
import com.tecneu.tecneu.services.UserService;

import org.json.JSONException;

import java.math.BigInteger;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CreateProviderFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CreateProviderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateProviderFragment extends Fragment {
    private CreateProviderFragment.OnFragmentInteractionListener mListener;

    public CreateProviderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CreateUserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateProviderFragment newInstance() {
        CreateProviderFragment fragment = new CreateProviderFragment();
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
        return inflater.inflate(R.layout.fragment_create_provider, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText name = view.findViewById(R.id.fragment_create_provider_name);
        EditText company = view.findViewById(R.id.fragment_create_provider_company);
        EditText email = view.findViewById(R.id.fragment_create_provider_email);
        TextView phone = view.findViewById(R.id.fragment_create_provider_phone);
        Button create = view.findViewById(R.id.fragment_create_provider_create_btn);

        create.setOnClickListener((View v) -> {
            if (name.getText().toString().isEmpty()) {
                Toast.makeText(getContext(), "Llena el nombre", Toast.LENGTH_SHORT).show();
                return;
            }
            if (company.getText().toString().isEmpty()) {
                Toast.makeText(getContext(), "Llena el nombre de la compañia", Toast.LENGTH_SHORT).show();
                return;
            }
            if (email.getText().toString().isEmpty()) {
                Toast.makeText(getContext(), "Llena el email", Toast.LENGTH_SHORT).show();
                return;
            }
            if (phone.getText().toString().isEmpty()) {
                Toast.makeText(getContext(), "Llena el teléfono", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!email.getText().toString().matches("^(([^<>()\\[\\]\\\\.,;:\\s@\"]+(\\.[^<>()\\[\\]\\\\.,;:\\s@\"]+)*)|(\".+\"))@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$")) {
                Toast.makeText(getContext(), "El email no es valido", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                ProviderService
                        .createProvider(getContext(),
                                company.getText().toString(),
                                name.getText().toString(),
                                email.getText().toString(),
                                Long.parseLong(phone.getText().toString()),
                                new OnRequest() {
                                    @Override
                                    public void onSuccess(Object result) {
                                        Toast.makeText(getContext(), "Creado con exito", Toast.LENGTH_SHORT)
                                                .show();
                                        Objects.requireNonNull(getActivity())
                                                .getSupportFragmentManager()
                                                .popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                                    }

                                    @Override
                                    public void onError() {
                                        Toast.makeText(getContext(), "No se pudo crear", Toast.LENGTH_SHORT)
                                                .show();
                                        Objects.requireNonNull(getActivity())
                                                .getSupportFragmentManager()
                                                .popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                                    }
                                });
            } catch (Exception e) {
                Toast.makeText(getContext(), "No se pudo crear el proveedor", Toast.LENGTH_SHORT)
                        .show();
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
        if (context instanceof CreateProviderFragment.OnFragmentInteractionListener) {
            mListener = (CreateProviderFragment.OnFragmentInteractionListener) context;
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