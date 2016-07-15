package com.example.user.simpleui;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DrinkMenuActivity extends AppCompatActivity implements  DrinnkOrderDialog.OnDrinkOrderListener{

    TextView totalTextView;
    ListView drinkMenuListView;

    String[] names = {"冬瓜紅茶","玫瑰鹽奶蓋紅茶","珍珠紅茶拿鐵","紅茶"};
    int[] mPrice = {25,35,45,35};
    int[] IPrice = {35,45,55,45};
    int[] imageId = {R.drawable.drink1,R.drawable.drink2,R.drawable.drink3,R.drawable.drink4};

    List<Drink> drinks = new ArrayList<>();
    List<Drink> orders = new ArrayList<>();//使用者按下之後的訂單資料

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink_menu);

        Log.d("Debug", "DrinkMainActivity onCreate");

        totalTextView = (TextView)findViewById(R.id.totalTextView);
        drinkMenuListView = (ListView)findViewById(R.id.drinkMenuListView);

        setData();
        setupDrinkMenuListView();

    }



    private  void  setData(){
        for(int i = 0;i<names.length;i++){
            Drink drink = new Drink();
            drink.name = names[i];
            drink.mPrice = mPrice[i];
            drink.IPrice = IPrice[i];
            drink.imageId = imageId[i];
            drinks.add(drink);
        }
    }

    private void  setupDrinkMenuListView(){
        DrinkAdapter adapter = new DrinkAdapter(this,drinks);
        drinkMenuListView.setAdapter(adapter);

        drinkMenuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {//按下去時的事件O+Alt+Enter
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DrinkAdapter drinkAdapter = (DrinkAdapter)parent.getAdapter();
                Drink drink = (Drink)drinkAdapter.getItem(position);//拿出資訊
                shewDrinkOrderDialog(drink);
                /*orders.add(drink);//丟進orders中
                updateTotal();//呼叫updateTotal*/
            }
        });

    }


    public  void shewDrinkOrderDialog(Drink drink){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();//一筆交易

        DrinnkOrderDialog dialog = DrinnkOrderDialog.newInstance("","");
        Fragment prev = getFragmentManager().findFragmentByTag("DrinkOrderDialog");
        if(prev != null){
            ft.remove(prev);
        }

        ft.addToBackStack(null);//支援back鍵

        dialog.show(ft,"DrinkOrderDialog");
    }

    public void  updateTotal(){
        int total = 0;
        for(Drink drink:orders){
            total = total+drink.mPrice;
        }

        totalTextView.setText(String.valueOf(total));
    }

    public void done(View view){//view是button的變數
        Intent intent = new Intent();//intent帶回去會有限制
        //{"name":"Black Tea"}把每一個drink轉成jseropject(Drink.java)
        JSONArray jsonArray = new JSONArray();
        for(Drink drink:orders)
        {
            JSONObject jsonObject = drink.getJsonObject();
            jsonArray.put(jsonObject);
        }
        intent.putExtra("results",jsonArray.toString());
        setResult(RESULT_OK,intent);
        finish();//結束就要直接回上一頁
    }
    @Override
    protected void onStart(){
        super.onStart();
        Log.d("Debug", "DrinkMainActivity onStart");

    }
    @Override
    protected void onResume(){
        super.onResume();
        Log.d("Debug", "DrinkMainActivity onResume");
    }
    @Override
    protected void onPause(){
        super.onPause();
        Log.d("Debug", "DrinkMainActivity onPause");
    }
    @Override
    protected void onStop(){
        super.onStop();
        Log.d("Debug", "DrinkMainActivity onStop");
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();;
        Log.d("Debug","DrinkMainActivity onDestroy");
    }
    @Override
    protected  void onRestart(){
        super.onRestart();
        Log.d("Debug", "DrinkMainActivity onRestart");
    }

    @Override
    public void onDrinkOrderFinish(Uri uri) {

    }
}
