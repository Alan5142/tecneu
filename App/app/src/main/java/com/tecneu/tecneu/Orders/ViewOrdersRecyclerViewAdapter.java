package com.tecneu.tecneu.Orders;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.tecneu.tecneu.Orders.ViewOrdersFragment.OnListFragmentInteractionListener;
import com.tecneu.tecneu.Providers.ModifyProviderFragment;
import com.tecneu.tecneu.Providers.ProviderRecyclerViewAdapter;
import com.tecneu.tecneu.R;
import com.tecneu.tecneu.dummy.DummyContent.DummyItem;
import com.tecneu.tecneu.models.Order;
import com.tecneu.tecneu.models.Provider;
import com.tecneu.tecneu.services.OnRequest;
import com.tecneu.tecneu.services.OrderService;
import com.tecneu.tecneu.services.ProviderService;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class ViewOrdersRecyclerViewAdapter extends RecyclerView.Adapter<ViewOrdersRecyclerViewAdapter.ViewHolder>
        implements Filterable {

    private List<Order> mValues;
    private List<Order> mValuesFiltered;
    private final OnListFragmentInteractionListener mListener;
    AppCompatActivity activity;

    ViewOrdersRecyclerViewAdapter(List<Order> items, OnListFragmentInteractionListener listener, AppCompatActivity Activity) {
        activity = Activity;
        mValues = items;
        mValuesFiltered = mValues;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_view_orders, parent, false);
        ViewHolder holder = new ViewHolder(view);
        view.setOnClickListener(v -> {
            AlertDialog.Builder alert = new AlertDialog.Builder(activity);
            alert.setTitle(holder.mItem.tracking_number);
            alert.setMessage("Decida que hacer con la orden");
            alert.setPositiveButton("Eliminar", (dialog, which) ->
                    ProviderService.deleteProvider(activity.getBaseContext(), holder.mItem.idOrder, new OnRequest() {
                        @Override
                        public void onSuccess(Object result) {
                            Toast.makeText(activity, "Eliminado", Toast.LENGTH_SHORT).show();
                            ViewOrdersRecyclerViewAdapter list = ViewOrdersRecyclerViewAdapter.this;
                            OrderService.getAllOrders(activity.getBaseContext(), new OnRequest() {
                                @Override
                                public void onSuccess(Object result) {
                                    list.mValues = (List<Order>) result;
                                    list.notifyDataSetChanged();
                                }

                                @Override
                                public void onError() {

                                }
                            });

                        }

                        @Override
                        public void onError() {
                            Toast.makeText(activity, "No se pudo eliminar", Toast.LENGTH_SHORT).show();
                        }
                    }));
            alert.setNeutralButton("Modificar", (dialog, which) -> {
                activity
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment, ModifyOrdersFragment.newInstance(holder.mItem))
                        .commit();
            });
            alert.setNegativeButton("Cancelar", null);
            alert.show();
        });
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mModificationDate.setText("Fecha de modificación: " + holder.mItem.modification_date.toString());
        holder.mCreationDate.setText("Fecha de creación: " + holder.mItem.creation_date.toString());
        holder.mPersonReceiving.setText("Persona que recibirá: " + holder.mItem.personR);
        holder.mTrackingNumber.setText("C. de rastreo: " + holder.mItem.tracking_number);
        holder.mPaymentMethod.setText("Método de pago: " + holder.mItem.payment);
    }

    @Override
    public int getItemCount() {
        return mValuesFiltered.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public Order mItem;
        public final TextView mTrackingNumber;
        public final TextView mPersonReceiving;
        public final TextView mPaymentMethod;
        public final TextView mCreationDate;
        public final TextView mModificationDate;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTrackingNumber = view.findViewById(R.id.tracking_number);
            mPersonReceiving = view.findViewById(R.id.person_recieving);
            mPaymentMethod = view.findViewById(R.id.payment_method);
            mCreationDate = view.findViewById(R.id.creation_date);
            mModificationDate = view.findViewById(R.id.modification_date);
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    mValuesFiltered = mValues;
                } else {
                    List<Order> filteredList = new ArrayList<>();
                    for (Order row : mValues) {
                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.tracking_number.toLowerCase().contains(charString.toLowerCase()) ||
                                row.personR.contains(charString.toLowerCase()) ||
                                row.payment.contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    mValuesFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mValuesFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mValuesFiltered = (List<Order>) filterResults.values;

                // refresh the list with filtered data
                notifyDataSetChanged();
            }
        };
    }
}
