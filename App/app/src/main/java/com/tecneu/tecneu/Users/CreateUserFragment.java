package com.tecneu.tecneu.Users;

import android.app.TimePickerDialog;
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
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.tecneu.tecneu.R;
import com.tecneu.tecneu.models.DaySchedule;
import com.tecneu.tecneu.services.OnRequest;
import com.tecneu.tecneu.services.UserService;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


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
    private List<DaySchedule> daySchedules;
    private boolean daySchedulesFullfiled;

    public CreateUserFragment() {
        // Required empty public constructor
        daySchedules = new ArrayList<>();
        daySchedulesFullfiled = false;
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
        EditText name = view.findViewById(R.id.fragment_create_user_name);
        EditText surname = view.findViewById(R.id.fragment_create_user_surname);
        EditText password = view.findViewById(R.id.fragment_create_user_password);
        EditText repeatPassword = view.findViewById(R.id.fragment_create_user_repeat_password);
        TextView text = view.findViewById(R.id.textView2);
        RadioGroup permissions = view.findViewById(R.id.fragment_create_user_permissions);

        // lunes
        Button monday1 = view.findViewById(R.id.fragment_create_user_monday);

        monday1.setOnClickListener(v -> showDateDialogForDay(0, 1, monday1));

        // martes
        Button tuesday1 = view.findViewById(R.id.fragment_create_user_tuesday);

        tuesday1.setOnClickListener(v -> showDateDialogForDay(1, 1, tuesday1));

        // miercoles
        Button wednesday1 = view.findViewById(R.id.fragment_create_user_wednesday);

        wednesday1.setOnClickListener(v -> showDateDialogForDay(2, 1, wednesday1));

        // jueves
        Button thursday1 = view.findViewById(R.id.fragment_create_user_thursday);
        thursday1.setOnClickListener(v -> showDateDialogForDay(3, 1, thursday1));

        // viernes
        Button friday1 = view.findViewById(R.id.fragment_create_user_friday);

        friday1.setOnClickListener(v -> showDateDialogForDay(4, 1, friday1));

        // sabado
        Button saturday1 = view.findViewById(R.id.fragment_create_user_saturday);

        saturday1.setOnClickListener(v -> showDateDialogForDay(5, 1, saturday1));

        // domingo
        Button sunday1 = view.findViewById(R.id.fragment_create_user_sunday);

        sunday1.setOnClickListener(v -> showDateDialogForDay(6, 1, sunday1));

        // puerta 2

        // lunes
        Button monday2 = view.findViewById(R.id.fragment_create_user_monday_2);

        monday2.setOnClickListener(v -> showDateDialogForDay(0, 2, monday2));

        // martes
        Button tuesday2 = view.findViewById(R.id.fragment_create_user_tuesday_2);
        tuesday2.setOnClickListener(v -> showDateDialogForDay(1, 2, tuesday2));

        // miercoles
        Button wednesday2 = view.findViewById(R.id.fragment_create_user_wednesday_2);

        wednesday2.setOnClickListener(v -> showDateDialogForDay(2, 2, wednesday2));

        // jueves
        Button thursday2 = view.findViewById(R.id.fragment_create_user_thursday_2);

        thursday2.setOnClickListener(v -> showDateDialogForDay(3, 2, thursday2));

        // viernes
        Button friday2 = view.findViewById(R.id.fragment_create_user_friday_2);
        friday2.setOnClickListener(v -> showDateDialogForDay(4, 2, (Button) v));

        // sabado
        view.findViewById(R.id.fragment_create_user_saturday_2).setOnClickListener(v -> showDateDialogForDay(5, 2, (Button)v));

        // domingo
        view.findViewById(R.id.fragment_create_user_sunday_2).setOnClickListener(v -> showDateDialogForDay(6, 2, (Button) v));

        if (UserService.getUserType(getContext()).equals("admin")) {
            permissions.setVisibility(View.GONE);
            text.setVisibility(View.GONE);
        }
        Button create = view.findViewById(R.id.fragment_create_user_create_btn);

        create.setOnClickListener((View v) -> {
            if (password.getText().toString().isEmpty()) {
                Toast.makeText(getContext(), "La contraseña no puede estar vacia", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!password.getText().toString().equals(repeatPassword.getText().toString())) {
                Toast.makeText(getContext(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT)
                        .show();
                return;
            }
            if (username.getText().toString().isEmpty()) {
                Toast.makeText(getContext(), "Llena el nombre de usuario", Toast.LENGTH_SHORT).show();
                return;
            }
            if (name.getText().toString().isEmpty()) {
                Toast.makeText(getContext(), "Llena el/los nombres de la persona", Toast.LENGTH_SHORT).show();
                return;
            }
            if (surname.getText().toString().isEmpty()) {
                Toast.makeText(getContext(), "Llena el/los apellidos de la persona", Toast.LENGTH_SHORT).show();
                return;
            }

            String userType;
            switch (permissions.getCheckedRadioButtonId()) {
                case R.id.fragment_create_user_permissions_admin:
                    userType = "admin";
                    break;
                case R.id.fragment_create_user_permissions_standard:
                    userType = "estandar";
                    break;
                default:
                    Toast.makeText(getContext(), "Llena el permiso", Toast.LENGTH_SHORT).show();
                    return;
            }
            try {
                UserService
                        .createUser(getContext(),
                                username.getText().toString(),
                                password.getText().toString(),
                                userType,
                                name.getText().toString(),
                                surname.getText().toString(),
                                daySchedules,
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
            } catch (JSONException e) {
                Toast.makeText(getContext(), "No se pudo crear el usuario", Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    private void showDateDialogForDay(int day, int door, Button button) {
        TimePickerDialog startDate = new TimePickerDialog(getContext(), (view, hourOfDay, minute) -> {
            DaySchedule schedule = new DaySchedule();
            schedule.startHour = hourOfDay;
            schedule.startMinute = minute;
            schedule.weekDay = day;
            schedule.door = door;

            TimePickerDialog endDate = new TimePickerDialog(getContext(), (v, h, m) -> {
                schedule.endHour = h;
                schedule.endMinute = m;

                if (schedule.startHour < schedule.endHour || (schedule.startHour == schedule.endHour && schedule.startMinute < schedule.endMinute)) {
                    int i;
                    for (i = 0; i < daySchedules.size(); i++) {
                        if (daySchedules.get(i).weekDay == day && daySchedules.get(i).door == door) {
                            break;
                        }
                    }
                    if (i == daySchedules.size())
                        daySchedules.add(schedule);
                    else
                        daySchedules.set(i, schedule);
                    Toast.makeText(getContext(), "Horario ingresado", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(getContext(), "La hora de inicio y fin ingresada no son validas", Toast.LENGTH_SHORT).show();

            }, 0, 0, true);

            endDate.setCancelable(false);
            endDate.show();
            Toast.makeText(getContext(), "Ingresa la hora de fin de acceso", Toast.LENGTH_LONG).show();
        }, 0, 0, true);
        startDate.setCancelable(false);
        startDate.show();
        Toast.makeText(getContext(), "Ingresa la hora de inicio de acceso", Toast.LENGTH_LONG).show();
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
