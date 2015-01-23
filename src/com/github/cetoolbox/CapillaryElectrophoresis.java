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

/**
 * This class contains several functions for volume computation
 * 
 * @author Jerome Pansanel
 */

public class CapillaryElectrophoresis {

	/* The length of the capillary (centimeter) */
	private double totalLength;

	/* The window length (centimeter) */
	private double toWindowLength;

	/* The capillary inside diameter (micrometer) */
	private double diameter;

	/* The pressure drop across the capillary (mbar) */
	private double pressure;

	/* The time the pressure is applied (second) */
	private double duration;

	/* The buffer viscosity (cp) */
	private double viscosity;

	/* Analyte concentration (mol/l) */
	private double concentration;

	/* Analyte molecular weight (g/mol) */
	private double molecularWeight;

	/* The voltage applied to the capillary (volt) */
	private double voltage;

	/* The voltage applied to the capillary (microampere) */
	private double electricCurrent;

	/* The detection time (s) */
	private double detectionTime;

	/* The electro-osmosis time (s) */
	private double electroOsmosisTime;

	public CapillaryElectrophoresis() {
		this.totalLength = 0.0;
		this.toWindowLength = 0.0;
		this.diameter = 0.0;
		this.pressure = 0.0;
		this.duration = 0.0;
		this.viscosity = 0.0;
		this.concentration = 0.0;
		this.molecularWeight = 0.0;
		this.voltage = 0.0;
		this.electricCurrent = 0.0;
		this.detectionTime = 0.0;
		this.electroOsmosisTime = 0.0;
	}

	public CapillaryElectrophoresis(double pressure, double diameter,
			double duration, double viscosity, double totalLength,
			double toWindowLength, double concentration, double molecularWeight) {
		this.totalLength = totalLength;
		this.toWindowLength = toWindowLength;
		this.diameter = diameter;
		this.pressure = pressure;
		this.duration = duration;
		this.viscosity = viscosity;
		this.concentration = concentration;
		this.molecularWeight = molecularWeight;
		this.voltage = 0.0;
		this.electricCurrent = 0.0;
		this.detectionTime = 0.0;
		this.electroOsmosisTime = 0.0;
	}

	public void setTotalLength(double totalLength) {
		this.totalLength = totalLength;
	}

	public void setToWindowLength(double toWindowLength) {
		this.toWindowLength = toWindowLength;
	}

	public void setDiameter(double diameter) {
		this.diameter = diameter;
	}

	public void setPressure(double pressure) {
		this.pressure = pressure;
	}

	public void setDuration(double duration) {
		this.duration = duration;
	}

	public void setViscosity(double viscosity) {
		this.viscosity = viscosity;
	}

	public void setConcentration(double concentration) {
		this.concentration = concentration;
	}

	public void setMolecularWeight(double molecularWeight) {
		this.molecularWeight = molecularWeight;
	}

	public void setVoltage(double voltage) {
		this.voltage = voltage;
	}

	public void setElectricCurrent(double electricCurrent) {
		this.electricCurrent = electricCurrent;
	}

	public void setDetectionTime(double detectionTime) {
		this.detectionTime = detectionTime;
	}

	public void setElectroOsmosisTime(double electroOsmosisTime) {
		this.electroOsmosisTime = electroOsmosisTime;
	}

	public double getDeliveredVolume() {
		double deliveredVolume;
		deliveredVolume = (pressure * Math.pow(diameter, 4) * Math.PI * duration)
				/ (128 * viscosity * totalLength * Math.pow(10, 5));
		return deliveredVolume;
	}

	public double getCapillaryVolume() {
		double capillaryVolume;
		capillaryVolume = (totalLength * Math.PI * Math.pow(diameter / 2, 2)) / 100;
		return capillaryVolume;
	}

	public double getToWindowVolume() {
		double toWindowVolume;
		toWindowVolume = (toWindowLength * Math.PI * Math.pow(diameter / 2, 2)) / 100;
		return toWindowVolume;
	}

	public double getInjectionPlugLength() {
		double injectionPlugLength;
		injectionPlugLength = (pressure * Math.pow(diameter, 2) * duration)
				/ (32 * viscosity * totalLength * Math.pow(10, 2));
		return injectionPlugLength;
	}

	public double getTimeToReplaceVolume() {
		double timeToReplaceVolume;
		timeToReplaceVolume = (32 * viscosity * Math.pow(totalLength, 2))
				/ (Math.pow(diameter, 2) * Math.pow(10, -3) * pressure);
		return timeToReplaceVolume;
	}

	public double getViscosity() {
		double viscosity;
		viscosity = (pressure * Math.pow(diameter, 2) * detectionTime)
				/ (32 * totalLength * toWindowLength * Math.pow(10, 3));
		return viscosity;
	}

	public double getConductivity() {
		double conductivity;
		conductivity = (4 * totalLength * Math.pow(10, 4) * electricCurrent)
				/ (Math.PI * Math.pow(diameter, 2) * voltage);
		return conductivity;
	}

	public double getFieldStrength() {
		double fieldStrength;
		fieldStrength = voltage / totalLength;
		return fieldStrength;
	}

	public double getMicroEOF() {
		double microEOF;
		microEOF = (totalLength * toWindowLength)
				/ (electroOsmosisTime * voltage);
		return microEOF;
	};

	public double getLengthPerMinute() {
		double lengthPerMinute;
		lengthPerMinute = (60 * getMicroEOF() * getFieldStrength() * Math.pow(
				10, -2));
		return lengthPerMinute;
	}

	public double getFlowRate() {
		double flowRate;
		flowRate = (Math.PI * Math.pow(diameter, 2) * getLengthPerMinute()) / 4;
		return flowRate;
	}
}
