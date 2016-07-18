package com.example.user.simpleui;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by user on 2016/7/13.
 */
public class Order {
    String note;
    String menuResult;//drinkName改menuresult
    String storeInfo;

    //統計杯數
    public  int totalNumber(){
        if(menuResult == null || menuResult.equals("")){
            return 0;
        }

        try {
            JSONArray jsonArray = new JSONArray(menuResult);
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
}
