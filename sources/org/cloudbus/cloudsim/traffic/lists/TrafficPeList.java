/*
 * Title:        CloudSim Toolkit
 * Description:  CloudSim (Cloud Simulation) Toolkit for Modeling and Simulation of Clouds
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2009-2010, The University of Melbourne, Australia
 */

package org.cloudbus.cloudsim.traffic.lists;

import java.util.List;

import org.cloudbus.cloudsim.lists.PeList;
import org.cloudbus.cloudsim.traffic.TrafficPe;

/**
 * PowerPeList is a collection of operations on lists of Power-enabled PEs.
 *
 * @author		Anton Beloglazov
 * @since		CloudSim Toolkit 2.0
 */
public class TrafficPeList extends PeList {

	/**
	 * Gets the power. For this moment only consumed by all PEs.
	 *
	 * @param peList the pe list
	 *
	 * @return the power
	 */
	/*public static <T extends TrafficPe> double getTraffic(List<TrafficPe> peList) {
		double traffic = 0;
		for (TrafficPe pe : peList) {
			try {
				traffic += pe.getTrafficModel().getTraffic(pe.getPeProvisioner().getUtilization());
				//traffic +=pe.
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return traffic;
	}
	*/

	public static <T extends TrafficPe> double getLoad(List<TrafficPe> peList) {
		double load = 0;
		for (TrafficPe pe : peList) {
			try {
				load += pe.getTrafficModel().getLoad(pe.getPeProvisioner().getUtilization());
				//traffic +=pe.
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return load;
	}

	/**
	 * Gets the maximum power. For this moment only consumed by all PEs.
	 *
	 * @param peList the pe list
	 *
	 * @return the power
	 */
	public static <T extends TrafficPe> double getMaxLoad(List<TrafficPe> peList) {
    	double load = 0;
    	for (TrafficPe pe : peList) {
    		try {
    			load += pe.getTrafficModel().getLoad(1.0);
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    	}
    	return load;
	}

}
