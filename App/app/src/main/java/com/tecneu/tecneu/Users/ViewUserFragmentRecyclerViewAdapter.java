package com.tecneu.tecneu.Users;

import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.tecneu.tecneu.R;
import com.tecneu.tecneu.Users.ViewUserFragment.OnListFragmentInteractionListener;
import com.tecneu.tecneu.dummy.DummyContent.DummyItem;
import com.tecneu.tecneu.models.User;
import com.tecneu.tecneu.services.OnRequest;
import com.tecneu.tecneu.services.UserService;

import java.text.MessageFormat;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class ViewUserFragmentRecyclerViewAdapter extends RecyclerView.Adapter<ViewUserFragmentRecyclerViewAdapter.ViewHolder> {

    private List<User> mValues;
    private final OnListFragmentInteractionListener mListener;
    private final AppCompatActivity mActivity;

    public ViewUserFragmentRecyclerViewAdapter(List<User> items, OnListFragmentInteractionListener listener, AppCompatActivity activity) {
        mValues = items;
        mListener = listener;
        mActivity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_view_user_fragment, parent, false);
        ViewHolder holder = new ViewHolder(view);
        view.setOnClickListener(v -> {
            AlertDialog.Builder alert = new AlertDialog.Builder(mActivity);
            alert.setTitle(holder.mItem.username);
            alert.setMessage("Decida que hacer con el usuario");
            /*
            alert.setPositiveButton("Eliminar", (dialog, which) ->
                    UserService.deleteUser(mActivity.getBaseContext(), holder.mItem.id, new OnRequest() {
                @Override
                public void onSuccess(Object result) {
                    Toast.makeText(mActivity, "Eliminado", Toast.LENGTH_SHORT).show();
                    ViewUserFragmentRecyclerViewAdapter list = ViewUserFragmentRecyclerViewAdapter.this;
                    UserService.getAllUsers(mActivity.getBaseContext(), new OnRequest() {
                        @Override
                        public void onSuccess(Object result) {
                            list.mValues = (List<User>) result;
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
            */
            alert.setNeutralButton("Modificar", (dialog, which) -> {
                mActivity
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment, ModifyUserFragment.newInstance(holder.mItem))
                        .commit();
            });
            alert.setNegativeButton("Cancelar", null);
            alert.show();
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.name.setText(String.format("Nombre: %s", mValues.get(position).names));
        holder.username.setText(String.format("Nombre de usuario: %s", mValues.get(position).username));
        holder.surname.setText(MessageFormat.format("Apellidos: {0}", mValues.get(position).surnames));
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView username;
        public final TextView name;
        public final TextView surname;
        public User mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            username = view.findViewById(R.id.user_view_username);
            name = view.findViewById(R.id.user_view_name);
            surname = view.findViewById(R.id.user_view_surname);
        }

        @Override
        @NonNull
        public String toString() {
            return super.toString() + " '" + name.getText() + "'";
        }
    }
}
