package com.example.user.simpleui;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by user on 2016/7/14.
 */
@ParseClassName("Drink")
public class Drink extends ParseObject
{
//    private String name;
//    private int mPrice = 0;
//    private int IPrice = 0;

    public void setName(String name) {
        put("name",name);
    }

    public void setmPrice(int mPrice) {
        put("mPrice",mPrice);
    }

    public void setIPrice(int IPrice) {
        put("IPrice",IPrice);
    }

    public String getName() {
        return getString("name");
    }

    public int getmPrice() {
        return getInt("mPrice");
    }

    public int getIPrice() {
        return getInt("IPrice");
    }

    int imageId;


    public JSONObject getJsonObject(){
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("mPrice",getmPrice());
            jsonObject.put("IPrice",getIPrice());
            jsonObject.put("name",getName());//key值是name則可拿出name2來
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static Drink newInstanceWithData(String data){
        Drink drink = new Drink();
        try {
            JSONObject jsonObject = new JSONObject(data);
            drink.setName(jsonObject.getString("name"));
            drink.setIPrice(jsonObject.getInt("IPrice"));
            drink.setmPrice(jsonObject.getInt("mPrice"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return drink;
    }
}
