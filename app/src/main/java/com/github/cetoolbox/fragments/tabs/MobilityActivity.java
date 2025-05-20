/*
 * Copyright (C) 2012-2020 CNRS and University of Strasbourg
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.cetoolbox.fragments.tabs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.github.cetoolbox.CEToolboxActivity;
import com.github.cetoolbox.CapillaryElectrophoresis;
import com.github.cetoolbox.R;

import java.text.DecimalFormat;
import java.util.Locale;

public class MobilityActivity extends Activity implements
        AdapterView.OnItemSelectedListener, View.OnClickListener {
    Button calculate;
    Button reset;
    EditText capillaryLengthValue;
    EditText toWindowLengthValue;
    EditText diameterValue;
    EditText voltageValue;
    EditText electroOsmosisTimeValue;
    Spinner electroOsmosisTimeSpin;
    int electroOsmosisTimeSpinPosition;
    EditText[] timePeakValues = new EditText[CEToolboxActivity.timePeakCount];

    CapillaryElectrophoresis capillary;

    Double capillaryLength;
    Double toWindowLength;
    Double diameter;
    Double voltage;
    Double electroOsmosisTime;
    String electroOsmosisTimeUnit;
    Double[] timePeaks = new Double[CEToolboxActivity.timePeakCount];

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String languageToLoad = "en";
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
        this.setContentView(R.layout.mobility);

        capillaryLengthValue = findViewById(R.id.capillaryLengthValue);
        diameterValue = findViewById(R.id.diameterValue);
        toWindowLengthValue = findViewById(R.id.toWindowLengthValue);
        electroOsmosisTimeValue = findViewById(R.id.electroOsmosisTimeValue);
        electroOsmosisTimeSpin = findViewById(R.id.electroOsmosisTimeSpin);
        electroOsmosisTimeSpin.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> electroOsmosisTimeUnitsAdapter = ArrayAdapter
                .createFromResource(this, R.array.electroOsmosisTimeUnitArray,
                        android.R.layout.simple_spinner_item);
        electroOsmosisTimeUnitsAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        electroOsmosisTimeSpin.setAdapter(electroOsmosisTimeUnitsAdapter);
        voltageValue = findViewById(R.id.voltageValue);

        String peak_name;
        for (int i = 0; i < CEToolboxActivity.timePeakCount; i++) {
            peak_name = String.format("timePeakValue%d", i);
            int resID = getResources().getIdentifier(peak_name,
                    "id", getPackageName());
            timePeakValues[i] = findViewById(resID);
        }

        calculate = findViewById(R.id.button1);
        calculate.setOnClickListener(this);
        reset = findViewById(R.id.button2);
        reset.setOnClickListener(this);

        getGlobalStateValues();
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        capillaryLength = savedInstanceState.getDouble("capillaryLength");
        toWindowLength = savedInstanceState.getDouble("toWindowLength");
        diameter = savedInstanceState.getDouble("diameter");
        electroOsmosisTime = savedInstanceState.getDouble("electroOsmosisTime");
        electroOsmosisTimeSpinPosition = savedInstanceState
                .getInt("electroOsmosisTimeSpinPosition");
        voltage = savedInstanceState.getDouble("voltage");
        String key_name;
        for (int i = 0; i < CEToolboxActivity.timePeakCount; i++) {
            key_name = String.format("timePeak%d", i);
            timePeaks[i] = savedInstanceState.getDouble(key_name);
        }

        /* Set GlobalState values */
        setGlobalStateValues();
    }

    @Override
    public void onResume() {
        super.onResume();

        /* Get GlobalState values */
        getGlobalStateValues();

        /* Initialize content */
        editTextInitialize();
    }

    private void getGlobalStateValues() {
        /* Get GlobalState values */
        capillaryLength = CEToolboxActivity.fragmentData.getCapillaryLength();
        toWindowLength = CEToolboxActivity.fragmentData.getToWindowLength();
        diameter = CEToolboxActivity.fragmentData.getDiameter();
        voltage = CEToolboxActivity.fragmentData.getVoltage();
        electroOsmosisTime = CEToolboxActivity.fragmentData
                .getElectroOsmosisTime();
        electroOsmosisTimeSpinPosition = CEToolboxActivity.fragmentData
                .getElectroOsmosisTimeSpinPosition();
        timePeaks = CEToolboxActivity.fragmentData.getTimePeaks();
    }

    private void setGlobalStateValues() {
        /* Set GlobalState values */
        CEToolboxActivity.fragmentData.setCapillaryLength(capillaryLength);
        CEToolboxActivity.fragmentData.setToWindowLength(toWindowLength);
        CEToolboxActivity.fragmentData.setDiameter(diameter);
        CEToolboxActivity.fragmentData.setVoltage(voltage);
        CEToolboxActivity.fragmentData
                .setElectroOsmosisTime(electroOsmosisTime);
        CEToolboxActivity.fragmentData
                .setElectroOsmosisTimeSpinPosition(electroOsmosisTimeSpinPosition);
        CEToolboxActivity.fragmentData.setTimePeaks(timePeaks);
    }

    private void editTextInitialize() {
        capillaryLengthValue.setText(capillaryLength.toString());
        diameterValue.setText(diameter.toString());
        toWindowLengthValue.setText(toWindowLength.toString());
        voltageValue.setText(voltage.toString());
        electroOsmosisTimeValue.setText(electroOsmosisTime.toString());
        electroOsmosisTimeSpin.setSelection(electroOsmosisTimeSpinPosition);
        for (int i = 0; i < CEToolboxActivity.timePeakCount; i++) {
            timePeakValues[i].setText(timePeaks[i].toString());
        }
    }

    /*
     * Validate the content of the EditText widget
     */
    private String parseEditTextContent() {
        String errorMessage = "";

        /* Verify that no field are empty */
        if (capillaryLengthValue.getText().length() == 0) {
            errorMessage = "The capillary length field is empty.";
        } else if (toWindowLengthValue.getText().length() == 0) {
            errorMessage = "The length to window field is empty.";
        } else if (diameterValue.getText().length() == 0) {
            errorMessage = "The diameter field is empty.";
        } else if (voltageValue.getText().length() == 0) {
            errorMessage = "The voltage field is empty.";
        } else if (electroOsmosisTimeValue.getText().length() == 0) {
            errorMessage = "The electro-osmosis time field is empty.";
        }

        if (errorMessage.isEmpty()) {
            if (Double.valueOf(capillaryLengthValue.getText().toString()) == 0) {
                errorMessage = "The capillary length can not be null.";
            } else if (Double.valueOf(toWindowLengthValue.getText().toString()) == 0) {
                errorMessage = "The length to window can not be null.";
            } else if (Double.valueOf(diameterValue.getText().toString()) == 0) {
                errorMessage = "The diameter can not be null.";
            } else if (Double.valueOf(voltageValue.getText().toString()) == 0) {
                errorMessage = "The voltage can not be null.";
            } else if (Double.valueOf(electroOsmosisTimeValue.getText()
                    .toString()) == 0) {
                errorMessage = "The electro-osmosis time can not be null.";
            }
        }

        return errorMessage;
    }

    @Override
    public void onClick(View view) {
        if (view == calculate) {
            boolean validatedValues = false;
            String errorMessage;

            errorMessage = parseEditTextContent();
            if (errorMessage.length() == 0) {
                validatedValues = true;
            }
            if (validatedValues) {
                /* Parameter validation */
                diameter = Double.valueOf(diameterValue.getText().toString());

                capillaryLength = Double.valueOf(capillaryLengthValue.getText()
                        .toString());
                toWindowLength = Double.valueOf(toWindowLengthValue.getText()
                        .toString());
                voltage = Double.valueOf(voltageValue.getText().toString());
                electroOsmosisTime = Double.valueOf(electroOsmosisTimeValue
                        .getText().toString());
                for (int i = 0; i < CEToolboxActivity.timePeakCount; i++) {
                    timePeaks[i] = Double.valueOf(timePeakValues[i].getText().toString());
                }
                /* Check the values for incoherence */
                if (toWindowLength > capillaryLength) {
                    validatedValues = false;
                    errorMessage = "The length to window can not be greater than the capillary length";
                }
            }
            if (validatedValues) {
                /* If all is fine, save the data and compute */
                SharedPreferences.Editor editor = CEToolboxActivity.preferences
                        .edit();

                editor.putLong("capillaryLength",
                        Double.doubleToLongBits(capillaryLength));
                editor.putLong("toWindowLength",
                        Double.doubleToLongBits(toWindowLength));
                editor.putLong("diameter", Double.doubleToLongBits(diameter));
                editor.putLong("voltage", Double.doubleToLongBits(voltage));
                editor.putLong("electroOsmosisTime",
                        Double.doubleToLongBits(electroOsmosisTime));
                editor.putInt("electroOsmosisTimeSpinPosition",
                        electroOsmosisTimeSpinPosition);
                String key_name;
                for (int i = 0; i < CEToolboxActivity.timePeakCount; i++) {
                    key_name = String.format("timePeak%d", i);
                    editor.putLong(key_name, Double.doubleToLongBits(timePeaks[i]));
                }

                editor.apply();

                capillary = new CapillaryElectrophoresis();
                capillary.setTotalLength(capillaryLength);
                capillary.setToWindowLength(toWindowLength);
                capillary.setDiameter(diameter);
                capillary.setVoltage(voltage);
                Double electroOsmosisTimeSecond;
                if (electroOsmosisTimeUnit.compareTo("min") == 0) {
                    electroOsmosisTimeSecond = electroOsmosisTime * 60;
                } else {
                    electroOsmosisTimeSecond = electroOsmosisTime;
                }
                capillary.setElectroOsmosisTime(electroOsmosisTimeSecond);

                DecimalFormat doubleDecimalScientificFormat = new DecimalFormat("#.##E0");

                /* Compute the microEOF mobility if the electro-osmosis time is not infinite*/
                Double microEOF = 0.0;
                if (electroOsmosisTimeSpinPosition < 2) {
                    microEOF = capillary.getMicroEOF(); /* E-3 cm2/V/s */
                }

                /* Build the result window */
                LayoutInflater li = LayoutInflater.from(this);
                View mobilityDetailsView = li.inflate(R.layout.mobilityresults,
                        null);

                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setView(mobilityDetailsView);

                TextView title = new TextView(this);
                title.setText("Mobility Details");
                title.setTextSize(20);
                title.setBackgroundColor(Color.DKGRAY);
                title.setTextColor(Color.WHITE);
                title.setPadding(10, 10, 10, 10);
                title.setGravity(Gravity.CENTER);
                builder.setCustomTitle(title);
                TextView tvMicroEOF = mobilityDetailsView
                        .findViewById(R.id.microEOFValue);
                tvMicroEOF.setText(doubleDecimalScientificFormat.format(microEOF)
                        + " m2/(V.s)");
                boolean endOfPeaks = false;
                TextView[] tvMicroEFF = new TextView[CEToolboxActivity.timePeakCount];
                String microeff_name;
                Double microEFF;
                for (int i = 0; i < CEToolboxActivity.timePeakCount; i++) {
                    microeff_name = String.format("microEFF%dValue", i + 1);
                    int resID = getResources().getIdentifier(microeff_name,
                            "id", getPackageName());
                    tvMicroEFF[i] = mobilityDetailsView.findViewById(resID);
                    if (timePeaks[i] == 0) {
                        endOfPeaks = true;
                    }
                    if (!endOfPeaks) {
                        microEFF = capillary.getMicroEFF(timePeaks[i] * 60) - microEOF;
                        /* Display the value of mobility for this peak */
                        tvMicroEFF[i].setText(doubleDecimalScientificFormat.format(microEFF) + " m2/(V.s)");
                    } else {
                        tvMicroEFF[i].setText("-");
                        /* Hide the value for this peak */
                    }
                }

                builder.setNeutralButton("Close",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dlg, int sumthin) {
                                // do nothing – it will close on its own
                            }
                        });

                final AlertDialog dialog = builder.create();
                dialog.show();
                final Button neutralButton = dialog.getButton(AlertDialog.BUTTON_NEUTRAL);
                LinearLayout.LayoutParams neutralButtonLL = (LinearLayout.LayoutParams) neutralButton.getLayoutParams();
                neutralButtonLL.width = ViewGroup.LayoutParams.MATCH_PARENT;
                neutralButton.setLayoutParams(neutralButtonLL);            } else {

                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setTitle("Error");
                builder.setMessage(errorMessage);
                builder.setIcon(R.drawable.ic_dialog_error);
                builder.setNeutralButton("Close",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dlg, int sumthin) {
                                // do nothing – it will close on its own
                            }
                        });

                builder.show();
            }
        } else if (view == reset) {
            /* Reset the values to default program settings */
            capillaryLength = 100.0;
            toWindowLength = 100.0;
            diameter = 50.0;
            voltage = 30.0;
            electroOsmosisTime = 1.0;
            electroOsmosisTimeSpinPosition = 0;
            for (int i = 0; i < CEToolboxActivity.timePeakCount; i++) {
                timePeaks[i] = 0.0;
            }
            editTextInitialize();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id) {
        if (parent == electroOsmosisTimeSpin) {
            electroOsmosisTimeUnit = (String) electroOsmosisTimeSpin
                    .getItemAtPosition(position);
            electroOsmosisTimeSpinPosition = position;
            if (position == 2) {
                /* Disable the related editText field */
                electroOsmosisTimeValue = findViewById(R.id.electroOsmosisTimeValue);
                electroOsmosisTimeValue.setEnabled(false);
                electroOsmosisTimeValue.setFocusable(false);
            } else {
                electroOsmosisTimeValue = findViewById(R.id.electroOsmosisTimeValue);
                electroOsmosisTimeValue.setEnabled(true);
                electroOsmosisTimeValue.setFocusableInTouchMode(true);
                electroOsmosisTimeValue.setFocusable(true);
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
    @Override
    public void onPause() {
        try {
            CEToolboxActivity.fragmentData.setCapillaryLength(Double
                    .valueOf(capillaryLengthValue.getText().toString()));
        } catch (Exception e) {
            CEToolboxActivity.fragmentData.setCapillaryLength(capillaryLength);
        }
        try {
            CEToolboxActivity.fragmentData.setToWindowLength(Double
                    .valueOf(toWindowLengthValue.getText().toString()));
        } catch (Exception e) {
            CEToolboxActivity.fragmentData.setToWindowLength(toWindowLength);
        }
        try {
            CEToolboxActivity.fragmentData.setDiameter(Double
                    .valueOf(diameterValue.getText().toString()));
        } catch (Exception e) {
            CEToolboxActivity.fragmentData.setDiameter(diameter);
        }
        try {
            CEToolboxActivity.fragmentData.setVoltage(Double
                    .valueOf(voltageValue.getText().toString()));
        } catch (Exception e) {
            CEToolboxActivity.fragmentData.setVoltage(voltage);
        }
        try {
            CEToolboxActivity.fragmentData.setElectroOsmosisTime(Double
                    .valueOf(electroOsmosisTimeValue.getText().toString()));
        } catch (Exception e) {
            CEToolboxActivity.fragmentData
                    .setElectroOsmosisTime(electroOsmosisTime);
        }
        CEToolboxActivity.fragmentData
                .setElectroOsmosisTimeSpinPosition(electroOsmosisTimeSpinPosition);

        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle state) {
        try {
            state.putDouble("capillaryLength",
                    Double.valueOf(capillaryLengthValue.getText().toString()));
        } catch (Exception e) {
            state.putDouble("capillaryLength", capillaryLength);
        }
        try {
            state.putDouble("toWindowLength",
                    Double.valueOf(toWindowLengthValue.getText().toString()));
        } catch (Exception e) {
            state.putDouble("toWindowLength", toWindowLength);
        }
        try {
            state.putDouble("diameter",
                    Double.valueOf(diameterValue.getText().toString()));
        } catch (Exception e) {
            state.putDouble("diameter", diameter);
        }
        try {
            state.putDouble("voltage",
                    Double.valueOf(voltageValue.getText().toString()));
        } catch (Exception e) {
            state.putDouble("voltage", voltage);
        }
        try {
            state.putDouble("electroOsmosisTime", Double
                    .valueOf(electroOsmosisTimeValue.getText().toString()));
        } catch (Exception e) {
            state.putDouble("electroOsmosisTime", electroOsmosisTime);
        }
        state.putInt("electroOsmosisTimeSpinPosition",
                electroOsmosisTimeSpinPosition);

        super.onSaveInstanceState(state);
    }
}
