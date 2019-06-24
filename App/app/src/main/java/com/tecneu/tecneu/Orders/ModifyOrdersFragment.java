package com.tecneu.tecneu.Orders;

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

import com.google.gson.Gson;
import com.tecneu.tecneu.R;
import com.tecneu.tecneu.models.Order;

import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ModifyOrdersFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ModifyOrdersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ModifyOrdersFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    private static final String ARG_PARAM1 = "param1";

    public ModifyOrdersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ModifyOrdersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ModifyOrdersFragment newInstance(Order order) {
        ModifyOrdersFragment fragment = new ModifyOrdersFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, new Gson().toJson(order));
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

        return inflater.inflate(R.layout.fragment_main_orders, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        Button viewOrders = view.findViewById(R.id.fragment_main_orders_search_orders_btn);
        Objects.requireNonNull(viewOrders).setOnClickListener((View v) -> Objects.requireNonNull(getActivity())
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment, ViewOrdersFragment.newInstance())
                .commit());
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
