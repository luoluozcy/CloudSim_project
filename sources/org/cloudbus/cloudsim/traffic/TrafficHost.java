package org.cloudbus.cloudsim.traffic;


import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.cloudbus.cloudsim.HostDynamicWorkload;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.VmScheduler;
import org.cloudbus.cloudsim.traffic.lists.TrafficVmList;

import org.cloudbus.cloudsim.traffic.lists.TrafficPeList;
import org.cloudbus.cloudsim.provisioners.BwProvisioner;
import org.cloudbus.cloudsim.provisioners.RamProvisioner;

/**
 * PowerHost class enables simulation of power-aware hosts.
 *
 * @author		Anton Beloglazov
 * @since		CloudSim Toolkit 2.0
 */
public class TrafficHost extends HostDynamicWorkload {
	private static float downthreashod=0, upthreashod=1;
	private static double probabiloity=0.5;
	private float Load;
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
	public TrafficHost(
			int id,
			RamProvisioner ramProvisioner,
			BwProvisioner bwProvisioner,
			long storage,
			List<? extends Pe> peList,
			VmScheduler vmScheduler) {
		super(id, ramProvisioner, bwProvisioner, storage, peList, vmScheduler);
	}

	/**
	 * Gets the power. For this moment only consumed by all PEs.
	 *
	 * @return the power
	 */
	@SuppressWarnings("unchecked")
	public double getTraffic(List<TrafficVm> TotalvmList) {
		//return TrafficPeList.getTraffic((List<TrafficPe>) getPeList());
		double traffic=0.0;
		TrafficHost host=this.getHostIndexOfID(this.getId());
		List<TrafficVm> vmList=new LinkedList<TrafficVm>();
		vmList=host.getVmList();
	//	TotalvmList=
		//vmList=host.
		Iterator<TrafficVm> iter=vmList.iterator();
		while(iter.hasNext()){
			TrafficVm vm = iter.next();
			Log.printLine(" in TaffiHost, the vm "+vm.getId()+ " is on host "+this.getId());
			traffic += vm.getTraffic(host, vm, TotalvmList);	
		}
		return traffic;
	}

	public TrafficHost getHostIndexOfID(int id) {
		if(this.getId()==id)
		return this;
		else return null;
	}
	/**
	 * Gets the maximum power. For this moment only consumed by all PEs.
	 *
	 * @return the power
	 */
	//-----------------------------------------------------------------------------------------------------------------//
	////////////////////////////////////////////以后可以扩展项目，加入负载均衡的研究///////////////////////////////////////
	//获取host上的pe的最大负载
	@SuppressWarnings("unchecked")
	public double getMaxLoad() {
    	return TrafficPeList.getMaxLoad((List<TrafficPe>) getPeList());
	}
	
	//获取host上的pe的所有负载，这里的负载指的是mips
	public double getLoad() {
		return TrafficPeList.getLoad((List<TrafficPe>) getPeList());
}
    
	
	public boolean checkIfMigVm(){
		//double load=getLoad();
	
		double aviblemips=this.getAvailableMips();
		if(aviblemips < 0)
		
    //	if(load > this.getUtilizationMips())
    	//isSuitableForVm
		return true;
		
		else return false;
	}
	//选择一个合适的VM来迁移，直到当前host上的mips不过载
    public List<TrafficVm> selectVmForMigration(){
    	double aviblemips=this.getAvailableMips();
    	List<TrafficVm> vmList=this.getVmList();
    	org.cloudbus.cloudsim.traffic.lists.TrafficVmList.sortByCommTraffic(vmList);
    	
    	Log.printLine("in the TrafficHost selectVmforMigration, after sort by CommTraffic");
    	for(TrafficVm vm:vmList)
    		Log.print(vm.getId()+" ");
    	
    	List<TrafficVm> selectVm = null;
    	//sort(vmList,)
    	int pos=0;
    	if(checkIfMigVm())
        while(aviblemips < 0 )
    	{   TrafficVm vm=vmList.get(pos);
    		aviblemips+=vm.getMips();
    		selectVm.add(vm);
    		pos++;
    	}
    	return selectVm;	
    }
    

   

   public void selectHostForVm(List<TrafficVm> selectVmList, List<TrafficHost> hostList)
    {
	   //selectVmForMigration
	   Iterator<TrafficVm> iter=selectVmList.iterator();
	   //*iter=selectVmList.
	   while(iter.hasNext())
	   { 
		   TrafficVm vm=iter.next();
		   //TrafficVmAllocation.findHostForVm(vm);
		  // *iter
		  // iter.
		   
	   }
	   
    }
	
}
