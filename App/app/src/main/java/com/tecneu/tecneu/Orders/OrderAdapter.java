package com.tecneu.tecneu.Orders;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.tecneu.tecneu.R;
import com.tecneu.tecneu.models.OrderInfo;
import com.tecneu.tecneu.services.OnRequest;
import com.tecneu.tecneu.services.OrderService;

import java.util.ArrayList;
import java.util.List;

public class OrderAdapter extends ArrayAdapter<OrderInfo> {

    private Context mContext;
    private List<OrderInfo> orderList = new ArrayList<>();

    public OrderAdapter(@NonNull Context context, @LayoutRes int resource, ArrayList<OrderInfo> list) {
        super(context, 0, list);
        mContext = context;
        orderList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.fragment_order_item, parent, false);

        OrderInfo currentOrder = orderList.get(position);

        TextView quantity = listItem.findViewById(R.id.fragment_order_item_quantity);
        quantity.setText(String.format("Cantidad: %d", currentOrder.quantity));

        TextView name = listItem.findViewById(R.id.fragment_order_item_name);
        name.setText(String.format("Nombre: %s", currentOrder.name));

        TextView price = listItem.findViewById(R.id.fragment_order_item_price);
        price.setText(String.format("Precio: %d", currentOrder.price));

        listItem.setOnClickListener(v -> {
            AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
            alert.setTitle("¿Eliminar producto de la orden?");

            alert.setPositiveButton("Eliminar", (dialog, which) -> {
                OrderService.deleteOrderProduct(mContext, currentOrder.idOrderProduct, new OnRequest() {
                    @Override
                    public void onSuccess(Object result) {
                        Toast.makeText(mContext, "Producto eliminado, refresca el menú para mostrar la información actualizada", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError() {
                        Toast.makeText(mContext, "No se pudo eliminar el producto", Toast.LENGTH_SHORT).show();
                    }
                });
            });
            alert.setNegativeButton("Cancelar", null);
            alert.show();
        });

        return listItem;
    }
}
