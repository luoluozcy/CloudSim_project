package org.cloudbus.cloudsim.traffic;

import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.traffic.models.TrafficModel;
import org.cloudbus.cloudsim.provisioners.PeProvisioner;

/**
 * PowerPe class enables simulation of power-aware PEs.
 *
 * @author		Anton Beloglazov
 * @since		CloudSim Toolkit 2.0
 */
public class TrafficPe extends Pe {

	/** The power model. */
	private TrafficModel trafficModel;

	/**
	 * Instantiates a new TrafficPe.
	 *
	 * @param id the id
	 * @param peProvisioner the PowerPe provisioner
	 * @param powerModel the power model
	 */
	public TrafficPe(int id, PeProvisioner peProvisioner, TrafficModel trafficModel) {
		super(id, peProvisioner);
		setTrafficModel(trafficModel);
	}

	/**
	 * Sets the power model.
	 *
	 * @param TrafficModel the new TrafficModel
	 */
	protected void setTrafficModel(TrafficModel trafficModel) {
		this.trafficModel = trafficModel;
	}

	/**
	 * Gets the TrafficModel.
	 *
	 * @return the TrafficModel
	 */
	public TrafficModel getTrafficModel() {
		return trafficModel;
	}

}

