package org.cloudbus.cloudsim;

public class trafficSensor<T extends Number> implements Sensor<T> {
	/**
	 * Sets the data center the sensor monitors
	 * @param dataCenter data center monitored by this sensor
	 */
	public void setDatacenter(FederatedDatacenter datacenter)
	{
		
		
	}
	/**
	 * Updates internal measurement of the monitored feature
	 * @return -1 if current measurements falls below the minumum threshold,
	 *         +1 if measurement falls above the maximum threshold,
	 *          0 if the measurement is within the specified interval
	 */
	public int monitor()
	{
		
		return 0;
		
	}
	
	

	/**
	 * Sets the upper limit of the feature monitored by the sensor
	 * @param value maximum value allowed to this sensor
	 */
	public void setUpperThreshold(T value)
	{
		
		
	}

	/**
	 * Sets the lower limit of the feature monitored by the sensor
	 * @param value minimum value allowed to this sensor
	 */
	public void setLowerThreshold(T value)
	{
		
		
	}
}
