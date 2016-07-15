package com.example.user.simpleui;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by user on 2016/7/14.
 */
public class Drink
{
    String name;
    int mPrice = 0;
    int IPrice = 0;
    int imageId;

    public JSONObject getJsonObject(){
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("price",mPrice);
            jsonObject.put("name",name);//key值是name則可拿出name2來
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static Drink newInstanceWithData(String data){
        Drink drink = new Drink();
        try {
            JSONObject jsonObject = new JSONObject(data);
            drink.name = jsonObject.getString("name");
            drink.IPrice = jsonObject.getInt("IPrice");
            drink.mPrice = jsonObject.getInt("mPrice");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return drink;
    }
}
