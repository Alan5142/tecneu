package com.tecneu.tecneu;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tecneu.tecneu.Users.CreateUserFragment;
import com.tecneu.tecneu.services.OnRequest;
import com.tecneu.tecneu.services.UserService;

import org.json.JSONException;

import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ConfirmPassword.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ConfirmPassword#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConfirmPassword extends Fragment {

    private EditText _passwordText;
    private Button _continueButton;

    private OnFragmentInteractionListener mListener;

    public ConfirmPassword() {
        // Required empty public constructor
    }

    public static ConfirmPassword newInstance() {
        ConfirmPassword fragment = new ConfirmPassword();
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
        return inflater.inflate(R.layout.fragment_confirm_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        _passwordText = view.findViewById(R.id.confirm_password);
        _continueButton = view.findViewById(R.id.confirm_btn);

        _continueButton.setOnClickListener((View v) -> {
            try {
                UserService.login(getContext(), "",
                        _passwordText.getText().toString(), new OnRequest() {
                            @Override
                            public void onSuccess(Object result) {
                                Objects.requireNonNull(getActivity())
                                        .getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.fragment, ChangePassFragment.newInstance())
                                        .addToBackStack(null)
                                        .commit();
                            }

                            @Override
                            public void onError() {
                                Toast.makeText(getContext(), "Las credenciales estan mal", Toast.LENGTH_SHORT).show();
                            }
                        });
            } catch (JSONException e) {
                Toast.makeText(getContext(), "No se pudo confirmar la contraseña", Toast.LENGTH_SHORT).show();
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

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
