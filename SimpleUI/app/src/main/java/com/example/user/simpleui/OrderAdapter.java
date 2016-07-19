package com.example.user.simpleui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by user on 2016/7/13.
 */
public class OrderAdapter extends BaseAdapter {

    List<Order> orders;
    LayoutInflater layoutInflater;//可以畫出xmlUI

    public OrderAdapter(Context context, List<Order> orders){
        this.orders = orders;
        this.layoutInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {//幾筆
        return orders.size();
    }

    @Override
    public Object getItem(int position) {//position是index
        return orders.get(position);//要拿到幾筆資料
    }

    @Override
    public long getItemId(int position) {//id用於資料庫
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    //convertView如果已經讀過第0筆，不用重新new一個view
    //Viewgroup 把同樣分類分層
        Holder holder;

        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.listview_order_item,null);//R.layout.item 是int，inflater，把xml檔轉化成一個view，parent=null
            TextView noteTextView = (TextView)convertView.findViewById(R.id.noteTextView);
            TextView storeInfoTextView = (TextView)convertView.findViewById(R.id.storeNameTextView);
            TextView drinkNameTextView = (TextView)convertView.findViewById(R.id.drinkNameTextView);

            holder = new Holder();
            holder.drinkNameTextView = drinkNameTextView;
            holder.noteTextView = noteTextView;
            holder.storeInfoTextView = storeInfoTextView;

            convertView.setTag(holder);
        }else{
            holder = (Holder)convertView.getTag();
        }
        Order order = orders.get(position);
        holder.noteTextView.setText(order.getNote());
        holder.storeInfoTextView.setText(order.getStoreInfo());
        holder.drinkNameTextView.setText(String.valueOf(order.totalNumber()));

        return convertView;
    }
    class Holder{//不用一直findViewById
        TextView noteTextView;
        TextView storeInfoTextView;
        TextView drinkNameTextView;
    }
}
