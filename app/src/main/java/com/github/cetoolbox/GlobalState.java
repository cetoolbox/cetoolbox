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
package com.github.cetoolbox;

public class GlobalState {
	private Double capillaryLength;
	private Double toWindowLength;
	private Double diameter;
	private Double duration;
	private Double viscosity;
	private Double pressure;
	private Double concentration;
	private Double molecularWeight;
	private Double voltage;
	private Double detectionTime;
	private Double electricCurrent;
	private Double electroOsmosisTime;
	private int concentrationSpinPosition;
	private int pressureSpinPosition;
	private int detectionTimeSpinPosition;
	private int electroOsmosisTimeSpinPosition;

	public void setCapillaryLength(Double capillaryLength) {
		this.capillaryLength = capillaryLength;
	}

	public Double getCapillaryLength() {
		return capillaryLength;
	}

	public Double getToWindowLength() {
		return toWindowLength;
	}

	public void setDiameter(Double diameter) {
		this.diameter = diameter;
	}

	public Double getDiameter() {
		return diameter;
	}

	public void setDuration(Double duration) {
		this.duration = duration;
	}

	public Double getDuration() {
		return duration;
	}

	public void setViscosity(Double viscosity) {
		this.viscosity = viscosity;
	}

	public Double getViscosity() {
		return viscosity;
	}

	public void setPressure(Double pressure) {
		this.pressure = pressure;
	}

	public Double getPressure() {
		return pressure;
	}

	public void setToWindowLength(Double toWindowLength) {
		this.toWindowLength = toWindowLength;
	}

	public void setConcentration(Double concentration) {
		this.concentration = concentration;
	}

	public Double getConcentration() {
		return concentration;
	}

	public void setMolecularWeight(Double molecularWeight) {
		this.molecularWeight = molecularWeight;
	}

	public Double getMolecularWeight() {
		return molecularWeight;
	}

	public void setVoltage(Double voltage) {
		this.voltage = voltage;
	}

	public Double getVoltage() {
		return voltage;
	}

	public void setDetectionTime(Double detectionTime) {
		this.detectionTime = detectionTime;
	}

	public Double getDetectionTime() {
		return detectionTime;
	}

	public void setElectricCurrent(Double electricCurrent) {
		this.electricCurrent = electricCurrent;
	}

	public Double getElectricCurrent() {
		return electricCurrent;
	}

	public void setElectroOsmosisTime(Double electroOsmosisTime) {
		this.electroOsmosisTime = electroOsmosisTime;
	}

	public Double getElectroOsmosisTime() {
		return electroOsmosisTime;
	}

	public void setConcentrationSpinPosition(int concentrationSpinPosition) {
		this.concentrationSpinPosition = concentrationSpinPosition;
	}

	public int getConcentrationSpinPosition() {
		return concentrationSpinPosition;
	}

	public void setPressureSpinPosition(int pressureSpinPosition) {
		this.pressureSpinPosition = pressureSpinPosition;
	}

	public int getPressureSpinPosition() {
		return pressureSpinPosition;
	}

	public void setDetectionTimeSpinPosition(int detectionTimeSpinPosition) {
		this.detectionTimeSpinPosition = detectionTimeSpinPosition;
	}

	public int getDetectionTimeSpinPosition() {
		return detectionTimeSpinPosition;
	}

	public void setElectroOsmosisTimeSpinPosition(
			int electroOsmosisTimeSpinPosition) {
		this.electroOsmosisTimeSpinPosition = electroOsmosisTimeSpinPosition;
	}

	public int getElectroOsmosisTimeSpinPosition() {
		return electroOsmosisTimeSpinPosition;
	}

}
