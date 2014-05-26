package org.cloudbus.cloudsim;

public class TrafficCloudCorrdinator extends CloudCoordinator {
	//private Sensor<Double> sensor=new Sensor<Double>(){
		
		
		
	
	/**
	 * Implements a specific migration policy to be deployed by the cloud coordinator.
	 *
	 * @param result the result vof the last measurement:
	 * -1 if the measurement fell below the lower threshold
	 * +1 if the measurement fell above the higher threshold
	 * @param sensor the sensor
	 *
	 * @pre sensor != null
	 * @post $none
	 */
	@Override
	

	protected void migrate(Sensor<Double> sensor, int result) {
		// TODO Auto-generated method stub
       double tr= sensor.monitor();
       
       
	}

}
