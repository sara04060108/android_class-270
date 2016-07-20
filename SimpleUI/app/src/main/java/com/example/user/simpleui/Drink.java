package com.example.user.simpleui;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

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

    public ParseFile getImage(){ return getParseFile("image");}

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

    public  static ParseQuery<Drink> getQuery(){return ParseQuery.getQuery(Drink.class);}

    public static void saveDrinkFromRemote(final FindCallback<Drink> callback)
    {
        Drink.getQuery().findInBackground(new FindCallback<Drink>()
        {
            @Override
            public void done(final List<Drink> objects, ParseException e)
            {
                if(e ==null)
                {
                    Drink.unpinAllInBackground("Drink", new DeleteCallback() {//local端先刪掉，以保證跟網路一樣
                        @Override
                        public void done(ParseException e) {
                            if(e==null)
                            {
                                Drink.pinAllInBackground("Drink",objects);//載下來無錯誤的話放入資料
                            }
                        }
                    });
                    callback.done(objects, e);
                }else
                {
                   Drink.getQuery().fromLocalDatastore().findInBackground(callback);
                }
            }
        });
    }


}

