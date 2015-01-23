/*******************************************************************************
 * Copyright (C) 2012-2014 CNRS and University of Strasbourg
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
 ******************************************************************************/
package com.github.cetoolbox.fragments.tabs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import java.text.DecimalFormat;
import java.util.Locale;
import com.github.cetoolbox.CEToolboxActivity;
import com.github.cetoolbox.CapillaryElectrophoresis;
import com.github.cetoolbox.R;

public class ViscosityActivity extends Activity implements
		AdapterView.OnItemSelectedListener, View.OnClickListener {

	Button calculate;
	Button reset;
	EditText capillaryLengthValue;
	EditText toWindowLengthValue;
	EditText diameterValue;
	EditText pressureValue;
	EditText detectionTimeValue;
	Spinner pressureSpin;
	int pressureSpinPosition;
	Spinner detectionTimeSpin;
	int detectionTimeSpinPosition;

	CapillaryElectrophoresis capillary;

	Double capillaryLength;
	Double toWindowLength;
	Double diameter;
	Double pressure;
	String pressureUnit;
	Double detectionTime;
	String detectionTimeUnit;

	/** Called when the activity is first created. */
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
		this.setContentView(R.layout.viscosity);

		capillaryLengthValue = (EditText) findViewById(R.id.capillaryLengthValue);
		diameterValue = (EditText) findViewById(R.id.diameterValue);
		toWindowLengthValue = (EditText) findViewById(R.id.toWindowLengthValue);
		pressureValue = (EditText) findViewById(R.id.pressureValue);
		pressureSpin = (Spinner) findViewById(R.id.pressureSpin);
		pressureSpin.setOnItemSelectedListener(this);
		ArrayAdapter<CharSequence> pressureUnitsAdapter = ArrayAdapter
				.createFromResource(this, R.array.pressureUnitArray,
						android.R.layout.simple_spinner_item);
		pressureUnitsAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		pressureSpin.setAdapter(pressureUnitsAdapter);
		detectionTimeValue = (EditText) findViewById(R.id.detectionTimeValue);
		detectionTimeSpin = (Spinner) findViewById(R.id.detectionTimeSpin);
		detectionTimeSpin.setOnItemSelectedListener(this);
		ArrayAdapter<CharSequence> detectionTimeUnitsAdapter = ArrayAdapter
				.createFromResource(this, R.array.detectionTimeUnitArray,
						android.R.layout.simple_spinner_item);
		detectionTimeUnitsAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		detectionTimeSpin.setAdapter(detectionTimeUnitsAdapter);

		calculate = (Button) findViewById(R.id.button1);
		calculate.setOnClickListener(this);
		reset = (Button) findViewById(R.id.button2);
		reset.setOnClickListener(this);

		getGlobalStateValues();

	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);

		capillaryLength = savedInstanceState.getDouble("capillaryLength");
		toWindowLength = savedInstanceState.getDouble("toWindowLength");
		diameter = savedInstanceState.getDouble("diameter");
		pressure = savedInstanceState.getDouble("pressure");
		pressureSpinPosition = savedInstanceState
				.getInt("pressureSpinPosition");
		detectionTime = savedInstanceState.getDouble("detectionTime");
		detectionTimeSpinPosition = savedInstanceState
				.getInt("detectionTimeSpinPosition");

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
		pressure = CEToolboxActivity.fragmentData.getPressure();
		pressureSpinPosition = CEToolboxActivity.fragmentData
				.getPressureSpinPosition();
		detectionTime = CEToolboxActivity.fragmentData.getDetectionTime();
		detectionTimeSpinPosition = CEToolboxActivity.fragmentData
				.getDetectionTimeSpinPosition();

	}

	private void setGlobalStateValues() {
		/* Set GlobalState values */
		CEToolboxActivity.fragmentData.setCapillaryLength(capillaryLength);
		CEToolboxActivity.fragmentData.setToWindowLength(toWindowLength);
		CEToolboxActivity.fragmentData.setDiameter(diameter);
		CEToolboxActivity.fragmentData.setPressure(pressure);
		CEToolboxActivity.fragmentData
				.setPressureSpinPosition(pressureSpinPosition);
		CEToolboxActivity.fragmentData.setDetectionTime(detectionTime);
		CEToolboxActivity.fragmentData
				.setDetectionTimeSpinPosition(detectionTimeSpinPosition);

	}

	private void editTextInitialize() {
		capillaryLengthValue.setText(capillaryLength.toString());
		diameterValue.setText(diameter.toString());
		toWindowLengthValue.setText(toWindowLength.toString());
		pressureValue.setText(pressure.toString());
		pressureSpin.setSelection(pressureSpinPosition);
		detectionTimeValue.setText(detectionTime.toString());
		detectionTimeSpin.setSelection(detectionTimeSpinPosition);

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
		} else if (pressureValue.getText().length() == 0) {
			errorMessage = "The pressure field is empty.";
		} else if (detectionTimeValue.getText().length() == 0) {
			errorMessage = "The detectionTime field is empty.";
		}

		if (errorMessage.length() == 0) {
			if (Double.valueOf(capillaryLengthValue.getText().toString()) == 0) {
				errorMessage = "The capillary length can not be null.";
			} else if (Double.valueOf(toWindowLengthValue.getText().toString()) == 0) {
				errorMessage = "The length to window can not be null.";
			} else if (Double.valueOf(diameterValue.getText().toString()) == 0) {
				errorMessage = "The diameter can not be null.";
			} else if (Double.valueOf(pressureValue.getText().toString()) == 0) {
				errorMessage = "The pressure can not be null.";
			} else if (Double.valueOf(detectionTimeValue.getText().toString()) == 0) {
				errorMessage = "The detection time can not be null.";
			}
		}

		return errorMessage;
	}

	@Override
	public void onClick(View view) {
		if (view == calculate) {
			boolean validatedValues = false;
			Double pressureMBar = 0.0;
			Double detectionTimeSecond = 0.0;
			String errorMessage;

			errorMessage = parseEditTextContent();
			if (errorMessage.length() == 0) {
				validatedValues = true;
			}
			if (validatedValues) {
				/* Parameter validation */
				capillaryLength = Double.valueOf(capillaryLengthValue.getText()
						.toString());
				toWindowLength = Double.valueOf(toWindowLengthValue.getText()
						.toString());
				diameter = Double.valueOf(diameterValue.getText().toString());
				pressure = Double.valueOf(pressureValue.getText().toString());
				if (pressureUnit.compareTo("psi") == 0) {
					pressureMBar = pressure * 6894.8 / 100;
				} else {
					pressureMBar = pressure;
				}
				detectionTime = Double.valueOf(detectionTimeValue.getText()
						.toString());
				if (detectionTimeUnit.compareTo("min") == 0) {
					detectionTimeSecond = detectionTime * 60;
				} else {
					detectionTimeSecond = detectionTime;
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
				editor.putLong("pressure", Double.doubleToLongBits(pressure));
				editor.putInt("pressureSpinPosition", pressureSpinPosition);
				editor.putLong("detectionTime",
						Double.doubleToLongBits(detectionTime));
				editor.putInt("detectionTimeSpinPosition",
						detectionTimeSpinPosition);

				editor.commit();

				capillary = new CapillaryElectrophoresis();
				capillary.setTotalLength(capillaryLength);
				capillary.setToWindowLength(toWindowLength);
				capillary.setDiameter(diameter);
				capillary.setPressure(pressureMBar);
				capillary.setDetectionTime(detectionTimeSecond);

				DecimalFormat doubleDecimalFormat = new DecimalFormat("#.##");
				Double viscosity = capillary.getViscosity(); /* cp */

				/* Build the result window */
				LayoutInflater li = LayoutInflater.from(this);
				View viscosityDetailsView = li.inflate(
						R.layout.viscosityresults, null);

				AlertDialog.Builder builder = new AlertDialog.Builder(this);

				builder.setView(viscosityDetailsView);

				TextView title = new TextView(this);
				title.setText("Viscosity Details");
				title.setTextSize(20);
				title.setBackgroundColor(Color.DKGRAY);
				title.setTextColor(Color.WHITE);
				title.setPadding(10, 10, 10, 10);
				title.setGravity(Gravity.CENTER);
				builder.setCustomTitle(title);

				TextView tvHydrodynamicInjection = (TextView) viscosityDetailsView
						.findViewById(R.id.viscosityValue);

				tvHydrodynamicInjection.setText(doubleDecimalFormat
						.format(viscosity) + " cp");

				builder.setNeutralButton("Close",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dlg, int sumthin) {
								// do nothing – it will close on its own
							}
						});

				builder.show();
			} else {

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
			pressure = 30.0;
			pressureSpinPosition = 0;
			detectionTime = 10.0;
			detectionTimeSpinPosition = 0;

			editTextInitialize();
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		if (parent == pressureSpin) {
			pressureUnit = (String) pressureSpin.getItemAtPosition(position);
			pressureSpinPosition = position;
		} else if (parent == detectionTimeSpin) {
			detectionTimeUnit = (String) detectionTimeSpin
					.getItemAtPosition(position);
			detectionTimeSpinPosition = position;
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
			CEToolboxActivity.fragmentData.setPressure(Double
					.valueOf(pressureValue.getText().toString()));
		} catch (Exception e) {
			CEToolboxActivity.fragmentData.setPressure(pressure);
		}
		CEToolboxActivity.fragmentData
				.setPressureSpinPosition(pressureSpinPosition);
		try {
			CEToolboxActivity.fragmentData.setDetectionTime(Double
					.valueOf(detectionTimeValue.getText().toString()));
		} catch (Exception e) {
			CEToolboxActivity.fragmentData.setDetectionTime(detectionTime);
		}
		CEToolboxActivity.fragmentData
				.setDetectionTimeSpinPosition(detectionTimeSpinPosition);

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
			state.putDouble("pressure",
					Double.valueOf(pressureValue.getText().toString()));
		} catch (Exception e) {
			state.putDouble("pressure", pressure);
		}
		state.putInt("pressureSpinPosition", pressureSpinPosition);
		try {
			state.putDouble("detectionTime",
					Double.valueOf(detectionTimeValue.getText().toString()));
		} catch (Exception e) {
			state.putDouble("detectionTime", detectionTime);
		}
		state.putInt("detectionTimeSpinPosition", detectionTimeSpinPosition);

		super.onSaveInstanceState(state);
	}
}
