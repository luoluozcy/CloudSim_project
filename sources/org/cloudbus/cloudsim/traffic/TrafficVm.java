package org.cloudbus.cloudsim.traffic;


import java.util.List;

import org.cloudbus.cloudsim.CloudletScheduler;
import org.cloudbus.cloudsim.HostDynamicWorkload;
import org.cloudbus.cloudsim.HostTopology;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.NetworkTopology;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmScheduler;
import org.cloudbus.cloudsim.traffic.lists.TrafficPeList;
import org.cloudbus.cloudsim.traffic.lists.TrafficVmList;
import org.cloudbus.cloudsim.lists.VmList;
import org.cloudbus.cloudsim.provisioners.BwProvisioner;
import org.cloudbus.cloudsim.provisioners.RamProvisioner;

/**
 * PowerHost class enables simulation of power-aware hosts.
 *
 * @author		Anton Beloglazov
 * @since		CloudSim Toolkit 2.0
 */
public class TrafficVm extends Vm {
        
	/**
	 * Instantiates a new host.
	 *
	 * @param id the id
	 * @param cpuProvisioner the cpu provisioner
	 * @param ramProvisioner the ram provisioner
	 * @param bwProvisioner the bw provisioner
	 * @param storage the storage
	 * @param vmScheduler the VM scheduler
	 */
	public TrafficVm(int id, int userId, double mips, int pesNumber, int ram, long bw, long size, String vmm, CloudletScheduler cloudletScheduler){
		super(id, userId, mips, pesNumber, ram, bw,size,vmm,cloudletScheduler);
	}
	/*
	public double getCommVMID(int vmid) {
		//double BwMatrix[][]=NetworkTopology.getBwMatrix();
		double traffic=0.0;
		int VmNum=NetworkTopology.getVmNum();
		for(int i=0;i< VmNum;i++)
		traffic+=NetworkTopology.getBw(i, vmid)+N
		
		
		etworkTopology.getBw (vmid,i);
		return traffic;		
		//return TrafficPeList.getTraffic((List<TrafficPe>) getPeList());
	}
*/
	/**
	 * Gets the power. For this moment only consumed by all PEs.
	 *
	 * @return the power
	 */
	//计算把 VM放到host上的带来的traffic
	
	@SuppressWarnings("unchecked")
	public double getTraffic(TrafficHost host, TrafficVm vm,List<TrafficVm> vmList ) {
		Log.print("in trafficVm, the vmlist ");
		for(int i=0;i<vmList.size();i++)
			Log.print(vmList.get(i).getId()+" ");
<<<<<<< HEAD
		Log.printLine();
		//double BwMatrix[][]=NetworkTopology.getBwMatrix();
		//Log.printLine(NetworkTopology.getVmNum());
=======
		//double BwMatrix[][]=NetworkTopology.getBwMatrix();
		Log.printLine(NetworkTopology.getVmNum());
>>>>>>> luoluo/master
		int VmNum=NetworkTopology.getVmNum();
		Log.printLine("Number of VM in Topo is "+VmNum);
		Log.print("Number of VMList is "+vmList.size()+" ");
		//Iterator<TrafficVm> iter=vmList.iterator();
		for(TrafficVm vm1:vmList)        
		    Log.print(vm1.getId()+" ");	
<<<<<<< HEAD
		Log.printLine();
		Log.printLine("in trafficVm, the vm "+vm.getId()+ " is on host"+host.getId());
		double traffic=0.0;
		for(int i=0;i< vmList.size();i++){
			Log.print("in trafficVm,the Host for VM "+ TrafficVmList.getById(vmList,vmList.get(i).getId()).getId());
			Log.print(" is host "+TrafficVmList.getById(vmList,vmList.get(i).getId()).getHost().getId());
			Log.printLine();
			//VmList.getById(getVmsCreatedList(), vmId).getHost().getId());
			Log.print(vm.getHost().getId()+" "+TrafficVmList.getById(vmList,vmList.get(i).getId()).getHost().getId());
			double dis=HostTopology.getDelay(vm.getHost().getId(), TrafficVmList.getById(vmList,vmList.get(i).getId()).getHost().getId());
		   Log.print(" "+ dis);
		   Log.printLine();
			traffic+=NetworkTopology.getBw(vmList.get(i).getId(), vm.getId())*dis;
		dis=HostTopology.getDelay( TrafficVmList.getById(vmList,vmList.get(i).getId()).getHost().getId(),vm.getHost().getId());
		traffic+=NetworkTopology.getBw (vm.getId(),vmList.get(i).getId())*dis;
=======
		Log.printLine();	
		double traffic=0.0;
		for(int i=0;i< vmList.size();i++){
			Log.print("in trafficVm,the Host for VM is ");
			//VmList.getById(getVmsCreatedList(), vmId).getHost().getId());
			Log.print(vm.getHost().getId()+" "+TrafficVmList.getById(vmList,i).getHost().getId());
			double dis=HostTopology.getDelay(vm.getHost().getId(), TrafficVmList.getById(vmList,i).getHost().getId());
		   Log.print(" "+ dis);
		   Log.printLine();
			traffic+=NetworkTopology.getBw(i, vm.getId())*dis;
		dis=HostTopology.getDelay( TrafficVmList.getById(vmList,i).getHost().getId(),vm.getHost().getId());
		traffic+=NetworkTopology.getBw (vm.getId(),i)*dis;
>>>>>>> luoluo/master
		}
		return traffic;
	}

	/**
	 * Gets the maximum power. For this moment only consumed by all PEs.
	 *
	 * @return the power
	 */
	//@SuppressWarnings("unchecked")
	//public double getMinTraffic() {
    	//return TrafficPeList.getMaxTraffic((List<TrafficPe>) getPeList());
	//}

}
