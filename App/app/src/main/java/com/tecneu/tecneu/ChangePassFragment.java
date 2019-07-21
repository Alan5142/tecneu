package com.tecneu.tecneu;

import android.app.AlertDialog;
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
import android.widget.Toast;

import com.tecneu.tecneu.models.User;
import com.tecneu.tecneu.services.OnRequest;
import com.tecneu.tecneu.services.UserService;

import org.json.JSONException;

import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChangePassFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChangePassFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChangePassFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public ChangePassFragment() {
        // Required empty public constructor
    }

    public static ChangePassFragment newInstance() {
        ChangePassFragment fragment = new ChangePassFragment();
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
        return inflater.inflate(R.layout.fragment_change_pass, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EditText password = view.findViewById(R.id.fragment_modify_password_password);
        EditText repeatPassword = view.findViewById(R.id.fragment_modify_password_repeat_password);
        Button changePasswordButton = view.findViewById(R.id.fragment_modify_pasword_accept_btn);

        changePasswordButton.setOnClickListener(v -> {
            if (!password.getText().toString().equals(repeatPassword.getText().toString())) {
                Toast.makeText(v.getContext(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                return;
            }
            if (password.getText().toString().length() < 7) {
                Toast.makeText(v.getContext(), "Longitud minima de 8 caracteres", Toast.LENGTH_SHORT).show();
                return;
            }
            AlertDialog.Builder confirm = new AlertDialog.Builder(getContext());

            confirm.setTitle("¿Estas seguro de que quieres cambiar la contraseña?");

            confirm.setPositiveButton("Si", (dialog, which) -> {
                UserService.getCurrentUser(getContext(), new OnRequest() {
                    @Override
                    public void onSuccess(Object result) {
                        User user = (User) result;
                        user.password = password.getText().toString();
                        try {
                            UserService.modifyUser(getContext(), user, new OnRequest() {
                                @Override
                                public void onSuccess(Object result) {
                                    Utility.hideKeyboardFrom(getContext(), getView());

                                    Objects.requireNonNull(getActivity())
                                            .getSupportFragmentManager()
                                            .beginTransaction()
                                            .replace(R.id.fragment, SettingsFragment.newInstance())
                                            .addToBackStack(null)
                                            .commit();

                                    Toast.makeText(getContext(), "Modificado con exito", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onError() {
                                    Toast.makeText(getContext(), "No se pudo modificar", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "No se pudo modificar", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError() {
                        Toast.makeText(getContext(), "No se pudo modificar", Toast.LENGTH_SHORT).show();
                    }
                });
            });

            confirm.setNegativeButton("No", null);
            confirm.show();
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
