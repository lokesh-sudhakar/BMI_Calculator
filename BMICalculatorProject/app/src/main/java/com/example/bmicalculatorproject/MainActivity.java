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

import java.util.Locale;

import static java.lang.String.format;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private static final int HUNDRED = 100;
    private static final int UPPER_LIMIT_OVERWEIGHT = 40;
    private static final double LOWER_LIMIT_UNDER_WEIGHT = 16.0;
    private static final double LOWER_LIMIT_OVERWEIGHT = 25.0;
    private static final double UPPER_LIMIT_OF_UNDERWEIGHT = 18.5;
    private static final double DEFAULT_FEET_IN_CENTIMETER = 0.032808;
    private static final double DEFAULT_INCH_TO_CENTIMETER = 0.39370;
    private static final double DEFAULT_POUNDS_IN_KILOS = 0.45359237;

    private static final String METER = "Meter";
    private static final String FEET = "Feet";
    private static final String INCH = "Inch";
    private static final String CENTIMETER = "Centimeter";
    private static final String KILOGRAM = "Kilogram";
    private static final String POUND = "Pound";
    private static final String KEYPAD_VISIBILITY = "keypad_visibility";
    private static final String RESULT_VISIBILITY = "result_visibility";
    private static final String WEIGHT_VALUE = "weightValue";
    private static final String HEIGHT_VALUE = "heightValue";
    private static final String BMI_RESULT = "bmi_result";
    private static final String BMI_STATE = "bmi_state";
    private static final String BMI_STATE_COLOR = "bmi_state_color";
    private static final String WEIGHT_SCALE = "weight_scale";
    private static final String HEIGHT_SCALE = "height_scale";
    private static final String HEIGHT_ENTRY_COLOR = "height_entry_color";
    private static final String WEIGHT_ENTRY_COLOR = "weight_entry_color";
    private static final String ZERO = "0";
    private static final String DECIMAL = ".";

    ActivityMainBinding mBinding;
    String[] weightScale = {"Kilogram", "Pound"};
    String[] heightScale = {"Centimeter", "Meter", "Feet", "Inch"};

    private boolean isUserInteracted;
    private boolean isHeightEntryClickedFirstTime;
    private boolean isWeightEntryClickedFirstTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        initView();

        if (savedInstanceState != null) {
            mBinding.weightEntry.setText(savedInstanceState.getString(WEIGHT_VALUE));
            mBinding.heightEntry.setText(savedInstanceState.getString(HEIGHT_VALUE));
            mBinding.numpadView.setVisibility(savedInstanceState.getInt(KEYPAD_VISIBILITY));
            mBinding.resultView.setVisibility(savedInstanceState.getInt(RESULT_VISIBILITY));
            mBinding.resultText.setText(savedInstanceState.getString(BMI_RESULT));
            mBinding.bmiStatusView.setText(savedInstanceState.getString(BMI_STATE));
            mBinding.bmiStatusView.setTextColor(savedInstanceState.getInt(BMI_STATE_COLOR));
            mBinding.weightScaleView.setText(savedInstanceState.getString(WEIGHT_SCALE));
            mBinding.heightScaleView.setText(savedInstanceState.getString(HEIGHT_SCALE));
            mBinding.heightEntry.setTextColor(savedInstanceState.getInt(HEIGHT_ENTRY_COLOR));
            mBinding.weightEntry.setTextColor(savedInstanceState.getInt(WEIGHT_ENTRY_COLOR));
        }
    }

    private void initView() {
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
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        isUserInteracted = true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(WEIGHT_VALUE, mBinding.weightEntry.getText().toString());
        outState.putString(HEIGHT_VALUE, mBinding.heightEntry.getText().toString());
        outState.putInt(KEYPAD_VISIBILITY, mBinding.numpadView.getVisibility());
        outState.putInt(RESULT_VISIBILITY, mBinding.resultView.getVisibility());
        outState.putString(BMI_RESULT, mBinding.resultText.getText().toString());
        outState.putString(BMI_STATE, mBinding.bmiStatusView.getText().toString());
        outState.putInt(BMI_STATE_COLOR, mBinding.bmiStatusView.getCurrentTextColor());
        outState.putString(WEIGHT_SCALE, mBinding.weightScaleView.getText().toString());
        outState.putString(HEIGHT_SCALE, mBinding.heightScaleView.getText().toString());
        outState.putInt(HEIGHT_ENTRY_COLOR, mBinding.heightEntry.getCurrentTextColor());
        outState.putInt(WEIGHT_ENTRY_COLOR, mBinding.weightEntry.getCurrentTextColor());
        super.onSaveInstanceState(outState);
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
                    mBinding.heightEntry.setText(ZERO);
                }
                if (isWeightEntryActive()) {
                    mBinding.weightEntry.setText(ZERO);
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
                    String formattedString = format(Locale.getDefault(),"%.01f", bmiValue);
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
        switch (mBinding.heightScaleView.getText().toString()) {
            case METER: {

            }
        }
        if (mBinding.heightScaleView.getText().equals(METER)) {
            float heightInMeters = Float.parseFloat((String) mBinding.heightEntry.getText());
            heightInCentimeter = heightInMeters * HUNDRED;
        } else if (mBinding.heightScaleView.getText().equals(FEET)) {
            float heightInFeet = Float.parseFloat((String) mBinding.heightEntry.getText());
            heightInCentimeter = (float) (heightInFeet / DEFAULT_FEET_IN_CENTIMETER);
        } else if (mBinding.heightScaleView.getText().equals(INCH)) {
            float heightInInch = Float.parseFloat((String) mBinding.heightEntry.getText());
            heightInCentimeter = (float) (heightInInch / DEFAULT_INCH_TO_CENTIMETER);
        } else if (mBinding.heightScaleView.getText().equals(CENTIMETER)) {
            heightInCentimeter = Float.parseFloat((String) mBinding.heightEntry.getText());
        }
        return heightInCentimeter;
    }

    private float performWeightConversion() {
        float weightInKilos = (float) 0.0;
        if (mBinding.weightScaleView.getText().equals(KILOGRAM)) {
            weightInKilos = Float.parseFloat((String) mBinding.weightEntry.getText());
        } else if (mBinding.weightScaleView.getText().equals(POUND)) {
            float weightInPounds = Float.parseFloat((String) mBinding.weightEntry.getText());
            weightInKilos = (float) (weightInPounds * DEFAULT_POUNDS_IN_KILOS);
        }
        return weightInKilos;
    }

    public float calculateBmiValue(float height, float weight) {
        return ((weight / (height / HUNDRED)) / (height / HUNDRED));
    }

    @SuppressLint("SetTextI18n")
    public void onClickNumber(String number) {
        if (isHeightEntryActive()) {
            String previousNumber = (String) mBinding.heightEntry.getText();
            if (previousNumber.equals(ZERO) || previousNumber.equals("") || isHeightEntryClickedFirstTime) {
                isHeightEntryClickedFirstTime = false;
                mBinding.heightEntry.setText(number);
            } else if (previousNumber.contains(DECIMAL)) {
                int indexOfDecimal = previousNumber.indexOf(DECIMAL);
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
            if (previousNumber.equals(ZERO) || previousNumber.equals("") || isWeightEntryClickedFirstTime) {
                isWeightEntryClickedFirstTime = false;
                mBinding.weightEntry.setText(number);
            } else if (previousNumber.contains(DECIMAL)) {
                int indexOfDecimal = previousNumber.indexOf(DECIMAL);
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
            if (!previousNumber.contains(DECIMAL)) {
                mBinding.heightEntry.setText(previousNumber + DECIMAL);
            }
        }
        if (isWeightEntryActive()) {
            String previousNumber = (String) mBinding.weightEntry.getText();
            if (!previousNumber.contains(".")) {
                mBinding.weightEntry.setText(previousNumber + DECIMAL);
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
                mBinding.heightEntry.setText(ZERO);
            }
        }
        if (isWeightEntryActive()) {
            String previousNumber = (String) mBinding.weightEntry.getText();
            if (previousNumber.length() > 1) {
                String numberAfterBackspace = previousNumber.substring(0, previousNumber.length() - 1);
                mBinding.weightEntry.setText(numberAfterBackspace);
            } else {
                mBinding.weightEntry.setText(ZERO);
            }
        }
    }

    @SuppressLint("SetTextI18n")
    public void onClickNumberZero() {
        String previousNumber = (String) mBinding.heightEntry.getText();
        if (isHeightEntryActive()) {
            if (mBinding.heightEntry.getText() == ZERO || mBinding.heightEntry.getText() == "" || isHeightEntryClickedFirstTime) {
                isHeightEntryClickedFirstTime = false;
                mBinding.heightEntry.setText(ZERO);
            } else if (previousNumber.contains(DECIMAL)) {
                int indexOfDecimal = previousNumber.indexOf(DECIMAL);
                String subString = previousNumber.substring(indexOfDecimal);
                if (subString.length() <= 2) {
                    mBinding.heightEntry.setText(previousNumber + ZERO);
                }
            } else if (mBinding.heightEntry.getText().length() < 3) {
                String previousNum = (String) mBinding.heightEntry.getText();
                mBinding.heightEntry.setText(previousNum + ZERO);
            }
        }
        if (isWeightEntryActive()) {
            if (mBinding.weightEntry.getText() == ZERO || mBinding.weightEntry.getText() == "" || isWeightEntryClickedFirstTime) {
                isWeightEntryClickedFirstTime = false;
                mBinding.weightEntry.setText(ZERO);
            } else if (previousNumber.contains(DECIMAL)) {
                int indexOfDecimal = previousNumber.indexOf(DECIMAL);
                String subString = previousNumber.substring(indexOfDecimal);
                if (subString.length() <= 2) {
                    mBinding.weightEntry.setText(previousNumber + ZERO);
                }
            } else if (mBinding.weightEntry.getText().length() < 3) {
                String previousNum = (String) mBinding.weightEntry.getText();
                mBinding.weightEntry.setText(previousNum + ZERO);
            }
        }
    }

    public void update(TextView view) {

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
        if (!isUserInteracted) return;
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