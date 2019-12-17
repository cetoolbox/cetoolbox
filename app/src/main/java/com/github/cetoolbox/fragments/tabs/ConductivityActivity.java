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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.text.DecimalFormat;
import java.util.Locale;
import com.github.cetoolbox.CEToolboxActivity;
import com.github.cetoolbox.CapillaryElectrophoresis;
import com.github.cetoolbox.R;

public class ConductivityActivity extends Activity implements
		AdapterView.OnItemSelectedListener, View.OnClickListener {

	public static final String PREFS_NAME = "capillary.electrophoresis.toolbox.PREFERENCE_FILE_KEY";

	Button calculate;
	Button reset;
	EditText capillaryLengthValue;
	EditText toWindowLengthValue;
	EditText diameterValue;
	EditText voltageValue;
	EditText electricCurrentValue;

	CapillaryElectrophoresis capillary;

	Double capillaryLength;
	Double toWindowLength;
	Double diameter;
	Double voltage;
	String voltageUnit;
	Double electricCurrent;

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
		this.setContentView(R.layout.conductivity);

		capillaryLengthValue = (EditText) findViewById(R.id.capillaryLengthValue);
		diameterValue = (EditText) findViewById(R.id.diameterValue);
		toWindowLengthValue = (EditText) findViewById(R.id.toWindowLengthValue);
		voltageValue = (EditText) findViewById(R.id.voltageValue);
		electricCurrentValue = (EditText) findViewById(R.id.electricCurrentValue);

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
		voltage = savedInstanceState.getDouble("voltage");
		electricCurrent = savedInstanceState.getDouble("electricCurrent");

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
		electricCurrent = CEToolboxActivity.fragmentData.getElectricCurrent();

	}

	private void setGlobalStateValues() {
		/* Set GlobalState values */
		CEToolboxActivity.fragmentData.setCapillaryLength(capillaryLength);
		CEToolboxActivity.fragmentData.setToWindowLength(toWindowLength);
		CEToolboxActivity.fragmentData.setDiameter(diameter);
		CEToolboxActivity.fragmentData.setVoltage(voltage);
		CEToolboxActivity.fragmentData.setElectricCurrent(electricCurrent);
	}

	private void editTextInitialize() {
		capillaryLengthValue.setText(capillaryLength.toString());
		toWindowLengthValue.setText(toWindowLength.toString());
		diameterValue.setText(diameter.toString());
		voltageValue.setText(voltage.toString());
		electricCurrentValue.setText(electricCurrent.toString());
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
		} else if (electricCurrentValue.getText().length() == 0) {
			errorMessage = "The electricCurrent field is empty.";
		}

		if (errorMessage.length() == 0) {
			if (Double.valueOf(capillaryLengthValue.getText().toString()) == 0) {
				errorMessage = "The capillary length can not be null.";
			} else if (Double.valueOf(toWindowLengthValue.getText().toString()) == 0) {
				errorMessage = "The length to window can not be null.";
			} else if (Double.valueOf(diameterValue.getText().toString()) == 0) {
				errorMessage = "The diameter can not be null.";
			} else if (Double.valueOf(voltageValue.getText().toString()) == 0) {
				errorMessage = "The voltage can not be null.";
			} else if (Double
					.valueOf(electricCurrentValue.getText().toString()) == 0) {
				errorMessage = "The detection time can not be null.";
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
				capillaryLength = Double.valueOf(capillaryLengthValue.getText()
						.toString());
				toWindowLength = Double.valueOf(toWindowLengthValue.getText()
						.toString());
				diameter = Double.valueOf(diameterValue.getText().toString());
				voltage = Double.valueOf(voltageValue.getText().toString());

				electricCurrent = Double.valueOf(electricCurrentValue.getText()
						.toString());

				/* Check the values for incoherence */
				if (toWindowLength > capillaryLength) {
					validatedValues = false;
					errorMessage = "The length to window can not be greater than the capillary length";
				}
			}
			if (validatedValues) {
				/* If all is fine, save the data and compute */
				SharedPreferences preferences = getSharedPreferences(
						PREFS_NAME, 0);
				SharedPreferences.Editor editor = preferences.edit();

				editor.putLong("capillaryLength",
						Double.doubleToLongBits(capillaryLength));
				editor.putLong("toWindowLength",
						Double.doubleToLongBits(toWindowLength));
				editor.putLong("diameter", Double.doubleToLongBits(diameter));
				editor.putLong("voltage", Double.doubleToLongBits(voltage));
				editor.putLong("electricCurrent",
						Double.doubleToLongBits(electricCurrent));

				editor.commit();

				capillary = new CapillaryElectrophoresis();
				capillary.setTotalLength(capillaryLength);
				capillary.setToWindowLength(toWindowLength);
				capillary.setDiameter(diameter);
				capillary.setVoltage(voltage);
				capillary.setElectricCurrent(electricCurrent);

				DecimalFormat doubleDecimalFormat = new DecimalFormat("#.##");
				Double conductivity = capillary.getConductivity(); /* nl */

				/* Build the result window */
				LayoutInflater li = LayoutInflater.from(this);
				View conductivityDetailsView = li.inflate(
						R.layout.conductivityresults, null);

				AlertDialog.Builder builder = new AlertDialog.Builder(this);

				builder.setView(conductivityDetailsView);

				TextView title = new TextView(this);
				title.setText("Conductivity Details");
				title.setTextSize(20);
				title.setBackgroundColor(Color.DKGRAY);
				title.setTextColor(Color.WHITE);
				title.setPadding(10, 10, 10, 10);
				title.setGravity(Gravity.CENTER);
				builder.setCustomTitle(title);

				TextView tvConductivity = (TextView) conductivityDetailsView
						.findViewById(R.id.conductivityValue);
				tvConductivity.setText(doubleDecimalFormat.format(conductivity)
						+ " S/m");

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
			voltage = 30.0;
			electricCurrent = 10.0;

			editTextInitialize();
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
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
			CEToolboxActivity.fragmentData.setElectricCurrent(Double
					.valueOf(electricCurrentValue.getText().toString()));
		} catch (Exception e) {
			CEToolboxActivity.fragmentData.setElectricCurrent(electricCurrent);
		}

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
			state.putDouble("electricCurrent",
					Double.valueOf(electricCurrentValue.getText().toString()));
		} catch (Exception e) {
			state.putDouble("electricCurrent", electricCurrent);
		}

		super.onSaveInstanceState(state);
	}
}
