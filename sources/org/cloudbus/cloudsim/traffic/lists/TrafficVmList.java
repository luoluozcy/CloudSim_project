/*
 * Title:        CloudSim Toolkit
 * Description:  CloudSim (Cloud Simulation) Toolkit for Modeling and Simulation of Clouds
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2009-2010, The University of Melbourne, Australia
 */

package org.cloudbus.cloudsim.traffic.lists;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.cloudbus.cloudsim.NetworkTopology;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.lists.VmList;
import org.cloudbus.cloudsim.traffic.TrafficVm;

/**
 * PowerVmList is a collection of operations on lists of power-enabled VMs.
 *
 * @author		Anton Beloglazov
 * @since		CloudSim Toolkit 2.0
 */
public class TrafficVmList extends VmList {

	/**
	 * Sort by cpu utilization.
	 *
	 * @param vmList the vm list
	 */
	public static void sortByCommTraffic(final List<TrafficVm> vmList) {
    	Collections.sort(vmList, new Comparator<TrafficVm>() {
           @Override
		public int compare(TrafficVm a, TrafficVm b) throws ClassCastException {
        	   Double aTraffic=getCommVMID(vmList,a.getId());
        	   Double bTraffic=getCommVMID(vmList,b.getId());
               //Double aUtilization = a.getTotalUtilizationOfCpuMips(CloudSim.clock());
              // Double bUtilization = b.getTotalUtilizationOfCpuMips(CloudSim.clock());
               ///return bUtilization.compareTo(aUtilization);
        	   return bTraffic.compareTo(aTraffic);
           }
		});
	}
	
	public static double getCommVMID(List<TrafficVm> vmList, int vmid) {
		//double BwMatrix[][]=NetworkTopology.getBwMatrix();
		double traffic=0.0;
		int VmNum=vmList.size();
		
		for(int i=0;i< VmNum;i++)
		traffic+=NetworkTopology.getBw(i, vmid)+NetworkTopology.getBw (vmid,i);
		return traffic;		
		//return TrafficPeList.getTraffic((List<TrafficPe>) getPeList());

}
}
