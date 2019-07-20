package com.tecneu.tecneu.Orders;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.tecneu.tecneu.R;
import com.tecneu.tecneu.models.OrderInfo;
import com.tecneu.tecneu.services.OnRequest;
import com.tecneu.tecneu.services.OrderService;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Order.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Order#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Order extends Fragment {

    private OnFragmentInteractionListener mListener;
    private int orderId;

    public Order() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Order.
     */
    // TODO: Rename and change types and number of parameters
    public static Order newInstance(int orderId) {
        Order fragment = new Order();
        Bundle args = new Bundle();
        args.putInt("orderId", orderId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        orderId = bundle.getInt("orderId");
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_order, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.fragment_order_options:
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setTitle("¿Finalizar?");

                alert.setPositiveButton("Finalizar", (dialog, which) -> {
                    OrderService.modifyOrderStatus(getContext(), orderId, new OnRequest() {
                        @Override
                        public void onSuccess(Object result) {
                            Toast.makeText(getContext(), "Estado cambiado", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError() {
                            Toast.makeText(getContext(), "No se pudo cambiar el estado, probablemente la orden ya fue finalizada", Toast.LENGTH_SHORT).show();
                        }
                    });
                });

                alert.setNegativeButton("Cancelar", null);
                alert.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView orderName = view.findViewById(R.id.order_fragment_id_order);
        orderName.setText(String.format("Orden no. %d", orderId));
        TextView orderTotal = view.findViewById(R.id.order_fragment_total);
        ListView listView = view.findViewById(R.id.order_fragment_list);
        ArrayAdapter<OrderInfo> orderInfoArrayAdapter = new OrderAdapter(getContext(), R.layout.fragment_order_item, new ArrayList<>());
        listView.setAdapter(orderInfoArrayAdapter);
        OrderService.getOrderProducts(getContext(), orderId, new OnRequest() {
            @Override
            public void onSuccess(Object result) {
                ArrayList<OrderInfo> orderInfos = (ArrayList<OrderInfo>) result;
                long total = 0;
                for (OrderInfo orderInfo : orderInfos) {
                    total += orderInfo.price;
                }
                orderTotal.setText(String.format("Total: %d", total));
                orderInfoArrayAdapter.addAll(orderInfos);
                orderInfoArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError() {

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
