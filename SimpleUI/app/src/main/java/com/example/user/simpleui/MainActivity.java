package com.example.user.simpleui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    EditText editText;
    RadioGroup radioGroup;

    String selectSet = "男";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        textView = (TextView)findViewById(R.id.textView);
        editText = (EditText)findViewById(R.id.editText);
        radioGroup = (RadioGroup)findViewById(R.id.radioGroup);

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
                if(checkedId == R.id.maleradioButton){
                    selectSet = "男";
                }else if(checkedId == R.id.femaleradioButton ){
                    selectSet = "女";
                }
            }
        });
    }

    public void submit(View view){
    //OnClick submit
    //要記得buttom傳進來會變view
        //textView.setText("Hello Everyone");
        String text = editText.getText().toString();//須轉型String，並把值放入text

        text = text+"  性別:"+selectSet;

        textView.setText(text);//把text放入textview

        editText.setText("");//清空editText
    }
}
