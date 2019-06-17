package com.example.bmicalculatorproject;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.example.bmicalculatorproject.databinding.ActivityMainBinding;

import static java.lang.String.format;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    public static final double LOWER_LIMIT_OVERWEIGHT = 25.0;
    public static final double UPPER_LIMIT_OF_UNDERWEIGHT = 18.5;
    public static final int UPPER_LIMIT_OVERWEIGHT = 40;
    public static final double LOWER_LIMIT_UNDER_WEIGHT = 16.0;
    public static final String METER = "Meter";
    public static final String FEET = "Feet";
    public static final String INCH = "Inch";
    public static final String CENTIMETER = "Centimeter";
    public static final String KILOGRAM = "Kilogram";
    public static final String POUND = "Pound";
    public static final int HUNDRED = 100;
    public static final String KEYPAD_VISIBLITY = "keypad_visiblity";
    public static final String RESULT_VISIBLITY = "result_visiblity";
    public static final String WEIGHT_VALUE = "weightValue";
    public static final String HEIGHT_VALUE = "heightValue";
    public static final String BMI_RESULT = "bmi_result";
    public static final String BMI_STATE = "bmi_state";
    public static final String BMI_STATE_COLOR = "bmi_state_color";
    public static final String WEIGHT_SCALE = "weight_scale";
    public static final String HEIGHT_SCALE = "height_scale";
    public static final String HEIGHT_ENTRY_COLOR = "height_entry_color";
    public static final String WEIGHT_ENTRY_COLOR = "weight_entry_color";

    private boolean isUserInteracted;
    ActivityMainBinding mBinding;
    String[] weightScale = {"Kilogram", "Pound"};
    String[] heightScale = {"Centimeter", "Meter", "Feet", "Inch"};
    private boolean isHeightEntryClickedFirstTime;
    private boolean isWeightEntryClickedFirstTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        ArrayAdapter<CharSequence> adapterHeight = ArrayAdapter.createFromResource(this,
                R.array.Height, android.R.layout.simple_spinner_item);
        adapterHeight.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBinding.weightSpinner.setAdapter(adapterHeight);

        ArrayAdapter<CharSequence> adapterWeight = ArrayAdapter.createFromResource(this,
                R.array.Weight, android.R.layout.simple_spinner_item);
        adapterWeight.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBinding.weightSpinner.setAdapter(adapterWeight);

        mBinding.weightSpinner.setOnItemSelectedListener(this);
        mBinding.heightSpinner.setOnItemSelectedListener(this);
        if (savedInstanceState != null) {
            mBinding.weightEntry.setText(savedInstanceState.getString(WEIGHT_VALUE));
            mBinding.heightEntry.setText(savedInstanceState.getString(HEIGHT_VALUE));
            mBinding.numpadView.setVisibility(savedInstanceState.getInt(KEYPAD_VISIBLITY));
            mBinding.resultView.setVisibility(savedInstanceState.getInt(RESULT_VISIBLITY));
            mBinding.resultText.setText(savedInstanceState.getString(BMI_RESULT));
            mBinding.bmiStatusView.setText(savedInstanceState.getString(BMI_STATE));
            mBinding.bmiStatusView.setTextColor(savedInstanceState.getInt(BMI_STATE_COLOR));
            mBinding.weightScaleView.setText(savedInstanceState.getString(WEIGHT_SCALE));
            mBinding.heightScaleView.setText(savedInstanceState.getString(HEIGHT_SCALE));
            mBinding.heightEntry.setTextColor(savedInstanceState.getInt(HEIGHT_ENTRY_COLOR));
            mBinding.weightEntry.setTextColor(savedInstanceState.getInt(WEIGHT_ENTRY_COLOR));
        }
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        isUserInteracted = true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(WEIGHT_VALUE, mBinding.weightEntry.getText().toString());
        outState.putString(HEIGHT_VALUE, mBinding.heightEntry.getText().toString());
        outState.putInt(KEYPAD_VISIBLITY, mBinding.numpadView.getVisibility());
        outState.putInt(RESULT_VISIBLITY, mBinding.resultView.getVisibility());
        outState.putString(BMI_RESULT, mBinding.resultText.getText().toString());
        outState.putString(BMI_STATE, mBinding.bmiStatusView.getText().toString());
        outState.putInt(BMI_STATE_COLOR, mBinding.bmiStatusView.getCurrentTextColor());
        outState.putString(WEIGHT_SCALE, mBinding.weightScaleView.getText().toString());
        outState.putString(HEIGHT_SCALE, mBinding.heightScaleView.getText().toString());
        outState.putInt(HEIGHT_ENTRY_COLOR, mBinding.heightEntry.getCurrentTextColor());
        outState.putInt(WEIGHT_ENTRY_COLOR, mBinding.weightEntry.getCurrentTextColor());

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
                onClickNumber(((TextView) v).getText().toString());
                break;
            }
            case R.id.height_entry: {
                mBinding.numpadView.setVisibility(View.VISIBLE);
                mBinding.resultView.setVisibility(View.GONE);
                if (!isHeightEntryActive()) {
                    isHeightEntryClickedFirstTime = true;
                }
                mBinding.heightEntry.setTextColor(ContextCompat.getColor(this, R.color.colorOrange));
                mBinding.weightEntry.setTextColor(ContextCompat.getColor(this, R.color.colorDarkGrey));
                break;
            }
            case R.id.weight_entry: {
                mBinding.numpadView.setVisibility(View.VISIBLE);
                mBinding.resultView.setVisibility(View.GONE);
                if (!isWeightEntryActive()) {
                    isWeightEntryClickedFirstTime = true;
                }
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
                if (bmiValue > UPPER_LIMIT_OVERWEIGHT || bmiValue < LOWER_LIMIT_UNDER_WEIGHT) {
                    Toast.makeText(getApplicationContext(), getString(R.string.invalid_bmi), Toast.LENGTH_SHORT).show();
                } else {

                    mBinding.resultView.setVisibility(View.VISIBLE);
                    mBinding.numpadView.setVisibility(View.GONE);
                    String formattedString = format("%.01f", bmiValue);
                    mBinding.resultText.setText(formattedString);
                    showBmiStatus(bmiValue);
                }
                break;
            }
            case R.id.btn_0: {
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
            case R.id.weight_spinner_layout: {
                mBinding.weightSpinner.performClick();
                break;
            }
            case R.id.height_spinner_layout: {
                mBinding.heightSpinner.performClick();
                break;
            }
            case R.id.height_entry_layout: {
                mBinding.heightEntry.performClick();
                break;
            }
            case R.id.weight_entry_layout: {
                mBinding.weightEntry.performClick();
                break;
            }
        }
    }

    private float performHeightConversion() {
        float heightInCentimeter = (float) 0.0;
        if (mBinding.heightScaleView.getText() == METER) {
            float heightInMeters = Float.parseFloat((String) mBinding.heightEntry.getText());
            heightInCentimeter = heightInMeters * HUNDRED;
        } else if (mBinding.heightScaleView.getText() == FEET) {
            float heightInFeet = Float.parseFloat((String) mBinding.heightEntry.getText());
            heightInCentimeter = (float) (heightInFeet / 0.032808);
        } else if (mBinding.heightScaleView.getText() == INCH) {
            float heightInInch = Float.parseFloat((String) mBinding.heightEntry.getText());
            heightInCentimeter = (float) (heightInInch / 0.39370);
        } else if (mBinding.heightScaleView.getText() == CENTIMETER) {
            heightInCentimeter = Float.parseFloat((String) mBinding.heightEntry.getText());
        }
        return heightInCentimeter;
    }

    private float performWeightConversion() {
        float weightInKilos = (float) 0.0;
        if (mBinding.weightScaleView.getText() == KILOGRAM) {
            weightInKilos = Float.parseFloat((String) mBinding.weightEntry.getText());
        } else if (mBinding.weightScaleView.getText() == POUND) {
            float weightInPounds = Float.parseFloat((String) mBinding.weightEntry.getText());
            weightInKilos = (float) (weightInPounds * 0.45359237);
        }
        return weightInKilos;
    }

    public float calculateBmiValue(float height, float weight) {
        return (weight / (height / 100)) / (height / 100);
    }

    @SuppressLint("SetTextI18n")
    public void onClickNumber(String number) {
        if (isHeightEntryActive()) {
            String previousNumber = (String) mBinding.heightEntry.getText();
            if (previousNumber.equals("0") || previousNumber.equals("") || isHeightEntryClickedFirstTime) {
                isHeightEntryClickedFirstTime = false;
                mBinding.heightEntry.setText(number);
            } else if (previousNumber.contains(".")) {
                int indexOfDecimal = previousNumber.indexOf(".");
                String subString = previousNumber.substring(indexOfDecimal);
                if (subString.length() <= 2) {
                    mBinding.heightEntry.setText(previousNumber + number);
                }
            } else if (previousNumber.length() < 3) {
                mBinding.heightEntry.setText(previousNumber + number);
            }
        }
        if (isWeightEntryActive()) {
            String previousNumber = (String) mBinding.weightEntry.getText();
            if (previousNumber.equals("0") || previousNumber.equals("") || isWeightEntryClickedFirstTime) {
                isWeightEntryClickedFirstTime = false;
                mBinding.weightEntry.setText(number);
            } else if (previousNumber.contains(".")) {
                int indexOfDecimal = previousNumber.indexOf(".");
                String subString = previousNumber.substring(indexOfDecimal);
                if (subString.length() <= 2) {
                    mBinding.weightEntry.setText(previousNumber + number);
                }
            } else if (previousNumber.length() < 3) {
                mBinding.weightEntry.setText(previousNumber + number);
            }
        }
    }

    @SuppressLint("SetTextI18n")
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

    @SuppressLint("SetTextI18n")
    public void onClickNumberZero() {
        String previousNumber = (String) mBinding.heightEntry.getText();
        if (isHeightEntryActive()) {
            if (mBinding.heightEntry.getText() == "0" || mBinding.heightEntry.getText() == "" || isHeightEntryClickedFirstTime) {
                isHeightEntryClickedFirstTime = false;
                mBinding.heightEntry.setText("0");
            } else if (previousNumber.contains(".")) {
                int indexOfDecimal = previousNumber.indexOf(".");
                String subString = previousNumber.substring(indexOfDecimal);
                if (subString.length() <= 2) {
                    mBinding.heightEntry.setText(previousNumber + "0");
                }
            } else if (mBinding.heightEntry.getText().length() < 3) {
                String previousNum = (String) mBinding.heightEntry.getText();
                mBinding.heightEntry.setText(previousNum + "0");
            }
        }
        if (isWeightEntryActive()) {
            if (mBinding.weightEntry.getText() == "0" || mBinding.weightEntry.getText() == "" || isWeightEntryClickedFirstTime) {
                isWeightEntryClickedFirstTime = false;
                mBinding.weightEntry.setText("0");
            } else if (previousNumber.contains(".")) {
                int indexOfDecimal = previousNumber.indexOf(".");
                String subString = previousNumber.substring(indexOfDecimal);
                if (subString.length() <= 2) {
                    mBinding.weightEntry.setText(previousNumber + "0");
                }
            } else if (mBinding.weightEntry.getText().length() < 3) {
                String previousNum = (String) mBinding.weightEntry.getText();
                mBinding.weightEntry.setText(previousNum + "0");
            }
        }
    }

    public void showBmiStatus(float bmiValue) {
        if (bmiValue < UPPER_LIMIT_OF_UNDERWEIGHT) {
            mBinding.bmiStatusView.setText(getString(R.string.underweight));
            mBinding.bmiStatusView.setTextColor(getColor(R.color.colorBlue));
        } else if (bmiValue > LOWER_LIMIT_OVERWEIGHT) {
            mBinding.bmiStatusView.setText(getString(R.string.over_weight));
            mBinding.bmiStatusView.setTextColor(getColor(R.color.colorOrange));
        } else {
            mBinding.bmiStatusView.setText(getString(R.string.normal));
        }
    }

    public boolean isWeightEntryActive() {
        return mBinding.weightEntry.getCurrentTextColor() == ContextCompat.getColor(this, R.color.colorOrange);
    }

    public boolean isHeightEntryActive() {
        return mBinding.heightEntry.getCurrentTextColor() == ContextCompat.getColor(this, R.color.colorOrange);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(!isUserInteracted) return;
        Spinner heightSpinner = (Spinner) parent;
        if (mBinding.heightSpinner == heightSpinner) {
            mBinding.heightScaleView.setText(heightScale[position]);
            mBinding.heightEntry.setTextColor(ContextCompat.getColor(this, R.color.colorOrange));
            mBinding.weightEntry.setTextColor(ContextCompat.getColor(this, R.color.colorDarkGrey));
        } else {
            mBinding.weightScaleView.setText(weightScale[position]);
            mBinding.weightEntry.setTextColor(ContextCompat.getColor(this, R.color.colorOrange));
            mBinding.heightEntry.setTextColor(ContextCompat.getColor(this, R.color.colorDarkGrey));
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}