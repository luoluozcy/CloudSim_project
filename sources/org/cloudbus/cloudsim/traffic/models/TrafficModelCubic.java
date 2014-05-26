/*
 * Title:        CloudSim Toolkit
 * Description:  CloudSim (Cloud Simulation) Toolkit for Modeling and Simulation of Clouds
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2009-2010, The University of Melbourne, Australia
 */

package org.cloudbus.cloudsim.traffic.models;

/**
 * The Class PowerModelCubic.
 *
 * @author		Anton Beloglazov
 * @since		CloudSim Toolkit 2.0
 */
public class TrafficModelCubic implements TrafficModel {

	/** The max power. */
	private double maxLoad;

	/** The constant. */
	private double constant;

	/** The static power. */
	private double staticLoad;

	/**
	 * Instantiates a new power model cubic.
	 *
	 * @param maxPower the max power
	 * @param staticPowerPercent the static power percent
	 */
	public TrafficModelCubic(double maxLoad, double staticLoadPercent) {
		setMaxLoad(maxLoad);
		setStaticLoad(staticLoadPercent * maxLoad);
		setConstant((maxLoad - getStaticLoad()) / Math.pow(100, 3));
	}

	/* (non-Javadoc)
	 * @see gridsim.virtualization.power.PowerModel#getPower(double)
	 */
	@Override
	public double getLoad(double utilization) throws IllegalArgumentException {
		if (utilization < 0 || utilization > 1) {
			throw new IllegalArgumentException("Utilization value must be between 0 and 1");
		}
		if (utilization == 0) {
			return 0;
		}
		return getStaticLoad() + getConstant() * Math.pow(utilization * 100, 3);
	}

	/**
	 * Gets the max power.
	 *
	 * @return the max power
	 */
	protected double getMaxLoad() {
		return maxLoad;
	}

	/**
	 * Sets the max power.
	 *
	 * @param maxPower the new max power
	 */
	protected void setMaxLoad(double maxLoad) {
		this.maxLoad = maxLoad;
	}

	/**
	 * Gets the constant.
	 *
	 * @return the constant
	 */
	protected double getConstant() {
		return constant;
	}

	/**
	 * Sets the constant.
	 *
	 * @param constant the new constant
	 */
	protected void setConstant(double constant) {
		this.constant = constant;
	}

	/**
	 * Gets the static power.
	 *
	 * @return the static power
	 */
	protected double getStaticLoad() {
		return staticLoad;
	}

	/**
	 * Sets the static power.
	 *
	 * @param staticPower the new static power
	 */
	protected void setStaticLoad(double staticLoad) {
		this.staticLoad = staticLoad;
	}

}
