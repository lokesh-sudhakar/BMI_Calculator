package com.example.bmicalculatorproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView heightEntry;
    private TextView weightEntry;
    private LinearLayout numberPad;
    private LinearLayout resultView;
    private Button buttonGO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        heightEntry=(TextView)findViewById(R.id.height_entry);
        weightEntry=(TextView)findViewById(R.id.weight_entry);
        numberPad=(LinearLayout)findViewById(R.id.numpad_view);
        resultView=(LinearLayout)findViewById(R.id.result_view);
        buttonGO=(Button)findViewById(R.id.btn_go);
        heightEntry.setOnClickListener(this);
        buttonGO.setOnClickListener(this);
    }
    public void onClick(View v)
    {
        if(v==heightEntry){
            numberPad.setVisibility(View.VISIBLE);
            resultView.setVisibility(View.GONE);
            heightEntry.setTextColor(getResources().getColor(R.color.colorOrange));
            weightEntry.setTextColor(getResources().getColor(R.color.colorDarkGrey));
        }
        if (v==buttonGO){
            resultView.setVisibility(View.VISIBLE);
            numberPad.setVisibility(View.GONE);
        }
    }
    public View getactiveTextView(){
        if(heightEntry.getCurrentTextColor()-(16777215+1)==R.color.colorOrange){
            return heightEntry;
        }else{
            return  weightEntry;
        }
    }



}
