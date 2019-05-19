package com.tecneu.tecneu.Providers;

import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.tecneu.tecneu.Providers.ProviderFragment.OnListFragmentInteractionListener;
import com.tecneu.tecneu.R;
import com.tecneu.tecneu.dummy.DummyContent.DummyItem;
import com.tecneu.tecneu.models.Provider;
import com.tecneu.tecneu.models.User;
import com.tecneu.tecneu.services.OnRequest;
import com.tecneu.tecneu.services.ProviderService;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class ProviderRecyclerViewAdapter extends RecyclerView.Adapter<ProviderRecyclerViewAdapter.ViewHolder> {

    private List<Provider> mValues;
    private final OnListFragmentInteractionListener mListener;
    private final AppCompatActivity mActivity;

    public ProviderRecyclerViewAdapter(List<Provider> items, OnListFragmentInteractionListener listener, AppCompatActivity activity) {
        mValues = items;
        mListener = listener;
        mActivity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_view_provider, parent, false);
        ViewHolder holder = new ViewHolder(view);
        view.setOnClickListener(v -> {
            AlertDialog.Builder alert = new AlertDialog.Builder(mActivity);
            alert.setTitle(holder.mItem.personName);
            alert.setMessage("Decida que hacer con el proveedor");
            alert.setPositiveButton("Eliminar", (dialog, which) ->
                    ProviderService.deleteProvider(mActivity.getBaseContext(), holder.mItem.id, new OnRequest() {
                        @Override
                        public void onSuccess(Object result) {
                            Toast.makeText(mActivity, "Eliminado", Toast.LENGTH_SHORT).show();
                            ProviderRecyclerViewAdapter list = ProviderRecyclerViewAdapter.this;
                            ProviderService.getAllProviders(mActivity.getBaseContext(), new OnRequest() {
                                @Override
                                public void onSuccess(Object result) {
                                    list.mValues = (List<Provider>) result;
                                    list.notifyDataSetChanged();
                                }

                                @Override
                                public void onError() {

                                }
                            });

                        }

                        @Override
                        public void onError() {
                            Toast.makeText(mActivity, "No se pudo eliminar", Toast.LENGTH_SHORT).show();
                        }
                    }));
            alert.setNeutralButton("Modificar", (dialog, which) -> {
                mActivity
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment, CreateProviderFragment.newInstance())
                        .commit();
            });
            alert.setNegativeButton("Cancelar", null);
            alert.show();
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Provider item = mValues.get(position);
        holder.mItem = item;
        holder.companyName.setText(String.format("Compañia: %s", item.companyName));
        holder.creationDate.setText(String.format("Fecha de creación: %s", item.creationDate.toString()));
        holder.personName.setText(String.format("Nombre: %s", item.personName));
        holder.email.setText(String.format("Email: %s", item.email));
        holder.phoneNumber.setText(String.format("Tel: %s", item.phoneNumber));
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView companyName;
        public final TextView creationDate;
        public final TextView personName;
        public final TextView email;
        public final TextView phoneNumber;

        public Provider mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            companyName = view.findViewById(R.id.fragment_provider_company_name);
            creationDate = view.findViewById(R.id.fragment_provider_creation_date);
            personName = view.findViewById(R.id.fragment_provider_person_name);
            email = view.findViewById(R.id.fragment_provider_email);
            phoneNumber = view.findViewById(R.id.fragment_provider_phone_number);
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }
}
