package com.example.user.simpleui;

import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    static final  int REQUEST_CODE_DRINK_MENU_ACTIVITY = 0;

    TextView textView;
    EditText editText;
    RadioGroup radioGroup;
    ListView listView;
    Spinner spinner;

    String selectTea = "black tea";

    String menuResults = "";

    SharedPreferences sharedPreferences;
    //要改資料需要editor
    SharedPreferences.Editor editor;

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

        sharedPreferences = getSharedPreferences("setting", MODE_PRIVATE);//private覆蓋 append疊加
        editor = sharedPreferences.edit();

        editText.setText(sharedPreferences.getString("editText", null));//傳進editText若沒有null

        editText.setOnKeyListener(new View.OnKeyListener() {//監聽editText
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //把打進的字放入sharedPreferences
                String text = editText.getText().toString();
                editor.putString("editText", text);
                editor.commit();//寫進xml
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {//偵測Enter按下去的瞬間(Action down)

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

        String history = Utils.readFile(this,"history");//讀出history
        String[] datas = history.split("\n");//每換一行即為新的訂單
        for(String data:datas){
            Order order = Order.newInstanceData(data);
            if(order != null) {
                orders.add(order);
            }
        }

        setupListView();
        setupSpinner();

        ParseObject parseObject = new ParseObject("Test");//上傳時classname=Test
        parseObject.put("foo", "bar");
        parseObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null)
                    Toast.makeText(MainActivity.this, "上傳成功", Toast.LENGTH_LONG).show();
            }
        });

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Test");//用ParseQuery要資料
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null){
                    for(ParseObject object:objects){
                        Toast.makeText(MainActivity.this,object.getString("foo"),Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        
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
        order.menuResults = menuResults;
        order.storeInfo = (String)spinner.getSelectedItem();

        orders.add(order);

        Utils.writeFile(this, "history", order.toData() + "\n");

        setupListView();

        editText.setText("");//清空editText
        menuResults = "";//每次結束清空字串
    }

    public  void goToMenu(View view){//跳頁
        Intent intent = new Intent();
        intent.setClass(this,DrinkMenuActivity.class);
        startActivityForResult(intent,REQUEST_CODE_DRINK_MENU_ACTIVITY);//知道下一頁會回來至這頁

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_DRINK_MENU_ACTIVITY)
        {
            if(resultCode == RESULT_OK)
            {
                Toast.makeText(this,"完成菜單",Toast.LENGTH_SHORT).show();//3是顯示長度
                menuResults = data.getStringExtra("results");//放入menuResult
            }
        }
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
