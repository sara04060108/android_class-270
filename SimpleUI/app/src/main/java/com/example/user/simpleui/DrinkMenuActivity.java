package com.example.user.simpleui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class DrinkMenuActivity extends AppCompatActivity {

    TextView totalTextView;
    ListView drinkMenuListView;

    String[] names = {"冬瓜紅茶","玫瑰鹽奶蓋紅茶","珍珠紅茶拿鐵","紅茶"};
    int[] mPrice = {25,35,45,35};
    int[] IPrice = {35,45,55,45};
    int[] imageId = {R.drawable.drink1,R.drawable.drink2,R.drawable.drink3,R.drawable.drink4};

    List<Drink> drinks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink_menu);

        Log.d("Debug", "DrinkMainActivity onCreate");

        totalTextView = (TextView)findViewById(R.id.totalTextView);
        drinkMenuListView = (ListView)findViewById(R.id.drinkMenuListView);
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

}
