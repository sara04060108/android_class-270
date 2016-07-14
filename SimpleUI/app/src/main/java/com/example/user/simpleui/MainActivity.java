package com.example.user.simpleui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    EditText editText;
    RadioGroup radioGroup;
    ListView listView;
    Spinner spinner;

    String selectTea = "black tea";

    List<Order> orders = new ArrayList<>();//放訂單的位置


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        textView = (TextView)findViewById(R.id.textView);
        editText = (EditText)findViewById(R.id.editText);
        radioGroup = (RadioGroup)findViewById(R.id.radioGroup);
        listView = (ListView)findViewById(R.id.listView);
        spinner = (Spinner)findViewById(R.id.spinner);

        editText.setOnKeyListener(new View.OnKeyListener() {//監聽editText
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction()== KeyEvent.ACTION_DOWN){//偵測Enter按下去的瞬間(Action down)

                    submit(v);
                    return true;//攔截這個字，若沒有這行，按Ｅnter仍會跳行
                }
                return false;//不攔截這個字
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {//當勾選改變，則改變觸發事件
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                selectTea = radioButton.getText().toString();
            }
        });

        setupListView();
        setupSpinner();


        Log.d("Debug", "MainActivity OnCreate");

    }

    public void setupListView()
    {
        //String[] data = new String[]{"black tea","grean tea","1","2","3","4","5"};測試data
        //adapter轉換器
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,data);//simple_list_item_1 android自訂UI(第二個是layout檔) 存進layout(第三個)

        OrderAdapter adapter = new OrderAdapter(this,orders);
        listView.setAdapter(adapter);
    }

    public void setupSpinner()
    {
        String[] data = getResources().getStringArray(R.array.storeInfos);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,data);
        spinner.setAdapter(adapter);
    }

    public void submit(View view){
    //OnClick submit
    //要記得buttom傳進來會變view
        //textView.setText("Hello Everyone");
        String text = editText.getText().toString();//須轉型String，並把值放入text

        textView.setText(text);//把text放入textview

        Order order = new Order();
        order.note = text;
        order.drinkName = selectTea;
        order.storeInfo = (String)spinner.getSelectedItem();

        orders.add(order);

        setupListView();

        editText.setText("");//清空editText

    }

    public  void goToMenu(View view){//跳頁
        Intent intent = new Intent();
        intent.setClass(this,DrinkMenuActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStart(){
        super.onStart();
        Log.d("Debug", "MainActivity onStart");

    }
    @Override
    protected void onResume(){
        super.onResume();
        Log.d("Debug", "MainActivity onResume");
    }
    @Override
    protected void onPause(){
        super.onPause();
        Log.d("Debug", "MainActivity onPause");
    }
    @Override
    protected void onStop(){
        super.onStop();
        Log.d("Debug", "MainActivity onStop");
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();;
        Log.d("Debug","MainActivity onDestroy");
    }
    @Override
    protected  void onRestart(){
        super.onRestart();
        Log.d("Debug", "MainActivity onRestart");
    }

}
