package com.tecneu.tecneu.Users;

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
import com.tecneu.tecneu.models.User;
import com.tecneu.tecneu.services.OnRequest;
import com.tecneu.tecneu.services.UserService;

import org.json.JSONException;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ModifyUserFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ModifyUserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ModifyUserFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private User userToEdit;

    private OnFragmentInteractionListener mListener;

    public ModifyUserFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ModifyUserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ModifyUserFragment newInstance(User user) {
        ModifyUserFragment fragment = new ModifyUserFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, new Gson().toJson(user));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String json = getArguments().getString(ARG_PARAM1);
            userToEdit = new Gson().fromJson(json, User.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_modify_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EditText username = view.findViewById(R.id.fragment_modify_user_username);
        username.setText(userToEdit.username);
        EditText name = view.findViewById(R.id.fragment_modify_user_names);
        name.setText(userToEdit.names);
        EditText surname = view.findViewById(R.id.fragment_modify_user_surname);
        surname.setText(userToEdit.surnames);
        RadioGroup radioGroup = view.findViewById(R.id.fragment_modify_user_permissions);
        if (userToEdit.userType.equals("admin")) {
            radioGroup.check(R.id.fragment_modify_user_permissions_admin);
        } else {
            radioGroup.check(R.id.fragment_modify_user_permissions_standard);
        }
        Button button = view.findViewById(R.id.fragment_modify_user_modify_btn);
        button.setOnClickListener(v -> {
            userToEdit.username = username.getText().toString();
            userToEdit.names = name.getText().toString();
            userToEdit.surnames = surname.getText().toString();

            if (userToEdit.username.isEmpty()) {
                Toast.makeText(getContext(), "Llena el nombre de usuario", Toast.LENGTH_SHORT).show();
                return;
            }

            if (userToEdit.names.isEmpty()) {
                Toast.makeText(getContext(), "Llena el/los nombres de la persona", Toast.LENGTH_SHORT).show();
                return;
            }

            if (userToEdit.surnames.isEmpty()) {
                Toast.makeText(getContext(), "Llena los apellidos", Toast.LENGTH_SHORT).show();
                return;
            }

            switch (radioGroup.getCheckedRadioButtonId()) {
                case R.id.fragment_modify_user_permissions_admin:
                    userToEdit.userType = "admin";
                    break;
                case R.id.fragment_modify_user_permissions_standard:
                    userToEdit.userType = "estandar";
                    break;
            }

            try {
                UserService.modifyUser(getContext(), userToEdit, new OnRequest() {
                    @Override
                    public void onSuccess(Object result) {
                        Toast.makeText(getContext(), "Modificado con exito", Toast.LENGTH_SHORT).show();
                        getActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment, ViewUserFragment.newInstance(1))
                                .commit();
                    }

                    @Override
                    public void onError() {
                        Toast.makeText(getContext(), "No se pudo editar", Toast.LENGTH_SHORT).show();
                        getActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment, ViewUserFragment.newInstance(1))
                                .commit();
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
