package com.tecneu.tecneu.Users;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.tecneu.tecneu.R;
import com.tecneu.tecneu.services.OnRequest;
import com.tecneu.tecneu.services.UserService;

import org.json.JSONException;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CreateUserFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CreateUserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateUserFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public CreateUserFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CreateUserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateUserFragment newInstance() {
        CreateUserFragment fragment = new CreateUserFragment();
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
        return inflater.inflate(R.layout.fragment_create_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText username = view.findViewById(R.id.fragment_create_user_username);
        EditText password = view.findViewById(R.id.fragment_create_user_password);
        EditText repeatPassword = view.findViewById(R.id.fragment_create_user_repeat_password);
        TextView text = view.findViewById(R.id.textView2);
        RadioGroup permissions = view.findViewById(R.id.fragment_create_user_permissions);
        if (UserService.getUserType(getContext()).equals("admin")) {
            permissions.setVisibility(View.GONE);
            text.setVisibility(View.GONE);
        }
        Button create = view.findViewById(R.id.fragment_create_user_create_btn);

        create.setOnClickListener((View v) -> {
            if (!password.getText().toString().equals(repeatPassword.getText().toString())) {
                Toast.makeText(getContext(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT)
                        .show();
                return;
            }
            String userType;
            switch (permissions.getCheckedRadioButtonId()) {
                case R.id.fragment_create_user_permissions_admin:
                    userType = "admin";
                    break;
                default:
                    userType = "estandar";
            }
            try {
                UserService
                        .createUser(getContext(),
                                username.getText().toString(),
                                password.getText().toString(),
                                userType,
                                new OnRequest() {
                                    @Override
                                    public void onSuccess(Object result) {
                                        Toast.makeText(getContext(), "Creado con exito", Toast.LENGTH_SHORT)
                                                .show();
                                    }

                                    @Override
                                    public void onError() {
                                        Toast.makeText(getContext(), "No se pudo crear", Toast.LENGTH_SHORT)
                                                .show();
                                    }
                                });
            } catch (JSONException e) {
                Toast.makeText(getContext(), "No se pudo crear el usuario", Toast.LENGTH_SHORT)
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
