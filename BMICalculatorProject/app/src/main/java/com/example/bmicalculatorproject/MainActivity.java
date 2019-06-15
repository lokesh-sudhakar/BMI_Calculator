package com.example.bmicalculatorproject;

import android.databinding.DataBindingUtil;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;


import com.example.bmicalculatorproject.databinding.ActivityMainBinding;

import static java.lang.String.format;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    public static final double LOWER_LIMIT_OVERWEIGHT = 25.0;
    public static final double UPPER_LIMIT_OF_UNDERWEIGHT = 18.5;
    ActivityMainBinding mBinding;
    String[] weightArray={"Kilogram","Pound"};
    String[] heightArray={"Centimeter","Meter","Feet","Inch"};
    private AdapterView<?> parent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        Spinner weightSpinner = (Spinner) findViewById(R.id.weight_spinner);
        Spinner heightSpinner = (Spinner) findViewById(R.id.height_spinner);
        ArrayAdapter<CharSequence> adapterHeight = ArrayAdapter.createFromResource(this,
                R.array.Height,android.R.layout.simple_spinner_item);
        adapterHeight.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        weightSpinner.setAdapter(adapterHeight);
        ArrayAdapter<CharSequence> adapterWeight = ArrayAdapter.createFromResource(this,
                R.array.Weight,android.R.layout.simple_spinner_item);
        adapterWeight.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        weightSpinner.setAdapter(adapterWeight);
        weightSpinner.setOnItemSelectedListener(this);
        heightSpinner.setOnItemSelectedListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_1:
            case R.id.btn_2:
            case R.id.btn_3:
            case R.id.btn_4:
            case R.id.btn_5:
            case R.id.btn_6:
            case R.id.btn_7:
            case R.id.btn_8:
            case R.id.btn_9: {
                onClickNumber(((Button)v).getText().toString());
                break;
            }

            case R.id.height_entry: {
                mBinding.numpadView.setVisibility(View.VISIBLE);
                mBinding.resultView.setVisibility(View.GONE);

                mBinding.heightEntry.setTextColor(ContextCompat.getColor(this, R.color.colorOrange));
                mBinding.weightEntry.setTextColor(ContextCompat.getColor(this, R.color.colorDarkGrey));
                break;
            }
            case R.id.weight_entry: {
                mBinding.numpadView.setVisibility(View.VISIBLE);
                mBinding.resultView.setVisibility(View.GONE);
                mBinding.weightEntry.setTextColor(ContextCompat.getColor(this, R.color.colorOrange));
                mBinding.heightEntry.setTextColor(ContextCompat.getColor(this, R.color.colorDarkGrey));
                break;
            }
            case R.id.btn_all_clear: {
                mBinding.numpadView.setVisibility(View.VISIBLE);
                mBinding.resultView.setVisibility(View.GONE);
                if (isHeightEntryActive()) {
                    mBinding.heightEntry.setText("0");
                }
                if (isWeightEntryActive()) {
                    mBinding.weightEntry.setText("0");
                }
                break;
            }
            case R.id.btn_go: {
                float weight = performWeightConversion();
                float height = performHeightConversion();
                float bmiValue = calculateBmiValue(height, weight);
                if (bmiValue > 40 || bmiValue<16.0){
                    Toast.makeText(getApplicationContext(),getString(R.string.invalid_bmi),Toast.LENGTH_SHORT).show();
                }else{

                    mBinding.resultView.setVisibility(View.VISIBLE);
                    mBinding.numpadView.setVisibility(View.GONE);
                    String formattedString = format("%.01f", bmiValue);
                    mBinding.resultText.setText(formattedString);
                    showBmiStatus(bmiValue);
                }
                break;
            }
            case R.id.btn_0:{
                onClickNumberZero();
                break;
            }
            case R.id.btn_decimal: {
                decimalClicked();
                break;
            }
            case R.id.btn_back_space: {
                onBackSpaceClicked();
                break;
            }
        }
    }

    private Float performHeightConversion(){
        Float heightInCentimeter=(float)0.0;
        if(mBinding.heightScaleView.getText()=="Meter"){
            Float heightInMeters = Float.parseFloat((String)mBinding.heightEntry.getText());
            heightInCentimeter =heightInMeters*100;
        }else if(mBinding.heightScaleView.getText()=="Feet"){
            Float heightInFeet = Float.parseFloat((String)mBinding.heightEntry.getText());
            heightInCentimeter =(float)(heightInFeet/0.032808);
        }else if(mBinding.heightScaleView.getText()=="Inch"){
            Float heightInInch = Float.parseFloat((String)mBinding.heightEntry.getText());
            heightInCentimeter =(float)(heightInInch/0.39370);
        }else if(mBinding.heightScaleView.getText()=="Centimeter"){
            heightInCentimeter = Float.parseFloat((String)mBinding.heightEntry.getText());
        }
        return heightInCentimeter;
    }
    private Float performWeightConversion() {
        Float weightInKilos = (float)0.0;
        if(mBinding.weightScaleView.getText()=="Kilogram"){
            weightInKilos =Float.parseFloat((String)mBinding.weightEntry.getText());
        }else if (mBinding.weightScaleView.getText()=="Pound"){
            Float weightInPounds = Float.parseFloat((String)mBinding.weightEntry.getText());
            weightInKilos =(float)(weightInPounds*0.45359237);
        }
        return weightInKilos;
    }

    public float calculateBmiValue(float height, float weight) {
        return (weight / (height / 100)) / (height / 100);
    }

    public void onClickNumber(String number) {
        if (isHeightEntryActive()) {
            String previousNumber = (String) mBinding.heightEntry.getText();
            if (previousNumber == "0" || previousNumber == "") {
                mBinding.heightEntry.setText(number);
            } else if (previousNumber.contains(".")) {
                int indexOfDecimal = previousNumber.indexOf(".");
                String subString = previousNumber.substring(indexOfDecimal, previousNumber.length());
                if (subString.length() <= 2) {
                    mBinding.heightEntry.setText(previousNumber + number);
                }
            } else if (previousNumber.length() < 3) {
                mBinding.heightEntry.setText(previousNumber + number);
            }
        }
        if (isWeightEntryActive()) {
            String previousNumber = (String) mBinding.weightEntry.getText();
            if (previousNumber == "0" || previousNumber == "") {
                mBinding.weightEntry.setText(number);
            } else if (previousNumber.contains(".")) {
                int indexOfDecimal = previousNumber.indexOf(".");
                String subString = previousNumber.substring(indexOfDecimal, previousNumber.length());
                if (subString.length() <= 2) {
                    mBinding.weightEntry.setText(previousNumber + number);
                }
            } else if (previousNumber.length() < 3) {
                mBinding.weightEntry.setText(previousNumber + number);
            }
        }
    }

    public void decimalClicked() {
        if (isHeightEntryActive()) {
            String previousNumber = (String) mBinding.heightEntry.getText();
            if (!previousNumber.contains(".")) {
                mBinding.heightEntry.setText(previousNumber + ".");
            }
        }
        if (isWeightEntryActive()) {
            String previousNumber = (String) mBinding.weightEntry.getText();
            if (!previousNumber.contains(".")) {
                mBinding.weightEntry.setText(previousNumber + ".");
            }
        }
    }

    public void onBackSpaceClicked() {
        if (isHeightEntryActive()) {
            String previousNumber = (String) mBinding.heightEntry.getText();
            if (previousNumber.length() > 1) {
                String numberAfterBackspace = previousNumber.substring(0, previousNumber.length() - 1);
                mBinding.heightEntry.setText(numberAfterBackspace);
            } else {
                mBinding.heightEntry.setText("0");
            }
        }
        if (isWeightEntryActive()) {
            String previousNumber = (String) mBinding.weightEntry.getText();
            if (previousNumber.length() > 1) {
                String numberAfterBackspace = previousNumber.substring(0, previousNumber.length() - 1);
                mBinding.weightEntry.setText(numberAfterBackspace);
            } else {
                mBinding.weightEntry.setText("0");
            }
        }
    }

    public void onClickNumberZero() {
        if (isHeightEntryActive()) {
            if (mBinding.heightEntry.getText() == "0" || mBinding.heightEntry.getText() == "") {
                mBinding.heightEntry.setText("0");
            } else if (mBinding.heightEntry.getText().length() < 3) {
                String previousNum = (String) mBinding.heightEntry.getText();
                mBinding.heightEntry.setText(previousNum + "0");
            }
        }
        if (isWeightEntryActive()) {
            if (mBinding.weightEntry.getText() == "0" || mBinding.weightEntry.getText() == "") {
                mBinding.weightEntry.setText("0");
            } else if (mBinding.weightEntry.getText().length() < 3) {
                String previousNum = (String) mBinding.weightEntry.getText();
                mBinding.weightEntry.setText(previousNum + "0");
            }
        }
    }

    public void showBmiStatus(float bmiValue){
        if(bmiValue< UPPER_LIMIT_OF_UNDERWEIGHT){
            mBinding.bmiStatusView.setText(getString(R.string.underweight));
            mBinding.bmiStatusView.setTextColor(getColor(R.color.colorBlue));
        }else if (bmiValue> LOWER_LIMIT_OVERWEIGHT){
            mBinding.bmiStatusView.setText(getString(R.string.over_weight));
            mBinding.bmiStatusView.setTextColor(getColor(R.color.colorOrange));
        }else{
            mBinding.bmiStatusView.setText(getString(R.string.normal));
        }
    }

    public boolean isWeightEntryActive(){
        if(mBinding.weightEntry.getCurrentTextColor() == ContextCompat.getColor(this, R.color.colorOrange)){
            return true;
        }else {
            return false;
        }
    }

    public boolean isHeightEntryActive(){
        if (mBinding.heightEntry.getCurrentTextColor() == ContextCompat.getColor(this, R.color.colorOrange)){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner heightSpinner = (Spinner) parent;
        if(mBinding.heightSpinner==heightSpinner){
            mBinding.heightScaleView.setText(heightArray[position]);
        }else{
            mBinding.weightScaleView.setText(weightArray[position]);
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        this.parent = parent;
    }
}
