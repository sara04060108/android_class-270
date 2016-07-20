package com.example.user.simpleui;

import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by user on 2016/7/13.
 */
@ParseClassName("Order")
//Server才會認得這個物件
public class Order extends ParseObject{
//    private String note;
//    private String menuResults;
//    private String storeInfo;

    public String getNote() {
        return getString("note");
    }

    public String getMenuResults() {//防護
        String menuResults = getString("menuResults");
        if(menuResults == null){
            return "";
        }

        return menuResults;
    }

    public String getStoreInfo() {
        return getString("storeInfo");
    }

    public void setNote(String note) {
        put("note",note);
    }

    public void setMenuResults(String menuResults) {
        put("menuResults",menuResults);
    }

    public void setStoreInfo(String storeInfo) {
        put("storeInfo",storeInfo);
    }

    public  String toData(){
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("note",getNote());
            jsonObject.put("menuResults",getMenuResults());
            jsonObject.put("storeInfo",getStoreInfo());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  jsonObject.toString();
    }

    public static Order newInstanceData(String data){
        try {
            JSONObject jsonObject = new JSONObject(data);
            Order order = new Order();
            order.setNote(jsonObject.getString("note"));
            order.setMenuResults(jsonObject.getString("menuResults"));
            order.setStoreInfo(jsonObject.getString("storeInfo"));

            return order;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    //統計杯數
    public  int totalNumber(){
        if(getMenuResults() == null || getMenuResults().equals("")){
            return 0;
        }

        try {
            JSONArray jsonArray = new JSONArray(getMenuResults());
            int totalNumber = 0;
            for(int i=0;i<jsonArray.length();i++){
                String data = jsonArray.getString(i);
                DrinkOrder drinkOrder = DrinkOrder.newInstanceWithData(data);
                //totalNumber += jsonObject.getInt("Number")+jsonObject.getInt("mNumber");
                totalNumber += drinkOrder.INumber+drinkOrder.mNumber;
            }
            return  totalNumber;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void getOrderFromRemote(final FindCallback<Order> callback){

        getQuery().findInBackground(new FindCallback<Order>() {//存在local端
            @Override
            public void done(List<Order> objects, ParseException e) {
                if(e == null){
                    Order.pinAllInBackground("Order",objects);
                    callback.done(objects,e);
                }else {
                    Order.getQuery().fromLocalDatastore().findInBackground(callback);
                }
                //callback.done(objects,e);
            }
        });
    }

    public static ParseQuery<Order> getQuery(){
        return ParseQuery.getQuery(Order.class);
    }
}
