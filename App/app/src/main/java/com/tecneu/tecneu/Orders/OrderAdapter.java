package com.tecneu.tecneu.Orders;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tecneu.tecneu.R;
import com.tecneu.tecneu.models.OrderInfo;

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

        return listItem;
    }
}
