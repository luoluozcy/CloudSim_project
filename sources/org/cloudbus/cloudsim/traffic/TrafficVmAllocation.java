package org.cloudbus.cloudsim.traffic;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.HostTopology;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.NetworkTopology;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicySimple;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.power.PowerHost;
import org.cloudbus.cloudsim.traffic.TrafficHost;
import org.cloudbus.cloudsim.traffic.lists.TrafficVmList;

/**
 * TrafficVmAllocationPolicySingleThreshold is an VMAllocationPolicy that
 * chooses a host with the least traffic increase due to utilization increase
 * caused by the VM allocation.
 *
 * @author		Anton Beloglazov
 * @since		CloudSim Toolkit 2.0
 */
public class TrafficVmAllocation extends VmAllocationPolicySimple {

	/** The hosts PEs. */
	protected Map<String, Integer> hostsPes;
    
	/** The hosts utilization. */
	protected Map<Integer, Double> hostsUtilization;

	/** The hosts memory. */
	protected Map<Integer, Integer> hostsRam;

	/** The hosts bw. */
	protected Map<Integer, Long> hostsBw;

	/** The old allocation. */
	private List<Map<String, Object>> savedAllocation;

	/** The utilization threshold. */
	private double utilizationThreshold;
	//private  List<TrafficVm> vmList;
	/**
	 * Instantiates a new vM provisioner mpp.
	 *
	 * @param list the list
	 * @param utilizationThreshold the utilization bound
	 */
	public TrafficVmAllocation(List<? extends TrafficHost> list, double utilizationThreshold)  {
		super(list);
		setSavedAllocation(new ArrayList<Map<String,Object>>());
		setUtilizationThreshold(utilizationThreshold);
		//setVmList(vmList);
		 //for(TrafficVm vm1: vmList)
	      //    Log.printLine("TrafficVmAllocation, the vmList is "+vm1.getId());
	}

	/**
	 * Determines a host to allocate for the VM.
	 *
	 * @param vm the vm
	 *
	 * @return the host
	 */
	
	//public void setVmList(List<TrafficVm> vmList)
	//{
	//	this.vmList=vmList;
	//}
	public TrafficHost findHostForVm(TrafficVm vm,List<? extends Vm> vmList) {
		double minTraffic = Double.MAX_VALUE;
	//	Log.printLline();
		TrafficHost allocatedHost = null;

		for (TrafficHost host : this.<TrafficHost>getHostList())//寻找满足阈值要求的host
		{
			if (host.isSuitableForVm(vm))//判断host是否满足条件
			{   
				double maxUtilization = getMaxUtilizationAfterAllocation(host, vm);
				Log.printLine("vm"+ vm.getId()+" host "+host.getId()+" "+ maxUtilization + maxUtilization+"  vm.isRecentlyCreated()"+ vm.isRecentlyCreated());
			if ((!vm.isRecentlyCreated() && maxUtilization > getUtilizationThreshold()) || (vm.isRecentlyCreated() && maxUtilization > 1.0)) {
				//if ((vm.isRecentlyCreated() && maxUtilization > 1.0)) {
				   continue;//设置利用率阈值，如果高于阈值则不符合要求
				}
				try { 
					Log.printLine(" findHostForVm and try");
					double trafficAfterAllocation = getTrafficAfterAllocation(host, vm,vmList);//获得vm放到host上后的traffic
					Log.printLine("vm"+ vm.getId()+" host "+host.getId());
					Log.printLine("trafficAfterAllocation " + trafficAfterAllocation);
					Log.printLine("trafficAfterAllocation "+ trafficAfterAllocation+" minTraffic "+ minTraffic);

					if (trafficAfterAllocation != -1) {
						//double trafficDiff = trafficAfterAllocation - host.getTraffic();
						if (trafficAfterAllocation < minTraffic) {
							minTraffic = trafficAfterAllocation;
							allocatedHost = host;
							Log.printLine("trafficAfterAllocation "+ trafficAfterAllocation+" minTraffic "+ minTraffic+ " allocatedHost"+ allocatedHost.getId());
						}
					}
				} catch (Exception e) {
				}
			}
		}
         
		return allocatedHost;
	}

	/**
	 * Allocates a host for a given VM.
	 *
	 * @param vm VM specification
	 *
	 * @return $true if the host could be allocated; $false otherwise
	 *
	 * @pre $none
	 * @post $none
	 */
	//完成VM到host的初始分配
	public boolean allocateHostForVm(TrafficVm vm,List<TrafficVm> vmList) {
		TrafficHost allocatedHost = findHostForVm(vm,vmList);
		if (allocatedHost != null && allocatedHost.vmCreate(vm)) { //if vm has been succesfully created in the host
			getVmTable().put(vm.getUid(), allocatedHost);//返回的是userid，和被分配的host
			if (!Log.isDisabled()) {
				Log.print(String.format("%.2f: VM #" + vm.getId() + " has been allocated to the host #" + allocatedHost.getId() + "\n", CloudSim.clock()));
			}
			return true;
		}
		return false;
	}

	/**
	 * Releases the host used by a VM.
	 *
	 * @param vm the vm
	 *
	 * @pre $none
	 * @post none
	 */
	public void deallocateHostForVm(Vm vm,List<? extends Vm> vmList) {
		if (getVmTable().containsKey(vm.getUid())) {
			TrafficHost host = (TrafficHost) getVmTable().remove(vm.getUid());
			if (host != null) {
				host.vmDestroy(vm);//释放资源
			}
		}
	}

	/**
	 * Optimize allocation of the VMs according to current utilization.
	 *
	 * @param vmList the vm list
	 * @param utilizationThreshold the utilization bound
	 *
	 * @return the array list< hash map< string, object>>
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> optimizeAllocation(List<? extends Vm> vmList) {
		Log.printLine(" in trafficVMallocation optimizeAllocation " );
		Log.printLine("vmList ");
		for(Vm v:vmList)
		Log.printLine(v.getId());
		
		//List<Map<String, Object>> saveMap = new ArrayList<Map<String, Object>>();
		//for(Map<String, Object> m:saveMap)
		//{
		//	m.
	//	}
		List<Map<String, Object>> migrationMap = new ArrayList<Map<String, Object>>();
		if (vmList.isEmpty()) {
			return migrationMap;
		}
		
		//saveAllocation(vmList);
		saveAllocation(vmList);
		List<TrafficVm> vmsToRestore = new ArrayList<TrafficVm>();
		vmsToRestore.addAll((Collection<? extends TrafficVm>) vmList);

		List<TrafficVm> vmsToMigrate = new ArrayList<TrafficVm>();
		for (Vm vm : vmList) {
			if (vm.isRecentlyCreated() || vm.isInMigration()) {
				continue;
			}
			//获取可以迁移的VM
			vmsToMigrate.add((TrafficVm) vm);
			//Log.printLine("in the TrafficVmAllocaton,the vmlist is "+vm.getId());
			//for(TrafficVm vm1 : vmsToMigrate)
			//	Log.printLine(vm1.getId());
		//	for(int i=0;i<vmsToMigrate.size();i++)
		//		Log.printLine(vmsToMigrate.get(i).getId());
			 // if(vm.getHost()!=null)
			     vm.getHost().vmDestroy(vm);	//这里把vmlist设置为null，其host为null		
		}
		Log.printLine("in the TrafficVmAllocaton, before sort, the vmlist is ");
		for(int i=0;i<vmsToMigrate.size();i++)
			Log.print(vmsToMigrate.get(i).getId());
		TrafficVmList.sortByCommTraffic(vmsToMigrate);
		Log.printLine("in the TrafficVmAllocaton, after sort, the vmlist is ");		
		for(int i=0;i<vmsToMigrate.size();i++)
			Log.print(vmsToMigrate.get(i).getId()+" ");
		//setSavedAllocation();
		for (TrafficHost host : this.<TrafficHost>getHostList()) {
			host.reallocateMigratingVms();
		}
		Log.printLine("in the TrafficVmAllocaton, the vmsToMigrate is ");		
		//List<Map<String, Object>> saveMap =this.getSavedAllocation() ;
		//for (TrafficVm v : vmsToMigrate) {
		//	Log.printLine(v.getId()+"  "+v.getHost().getId());
		//}
		for (TrafficVm vm : vmsToMigrate) {
			TrafficHost oldHost = (TrafficHost) getVmTable().get(vm.getUid());
		//	PowerHost oldHost = (PowerHost) getVmTable().get(vm.getUid());
			TrafficHost allocatedHost = findHostForVm(vm,vmList);
			//Log.printLine("vm "+vm.getId()+" allocatedHost "+allocatedHost.getId());
			Log.printLine("vm oldHost allocatedHost"+vm.getId()+oldHost.getId()+allocatedHost.getId());
			Log.printLine("vm "+vm.getId()+" oldHost "+oldHost.getId()+ " allocatedHost "+allocatedHost.getId());
			
			if (allocatedHost != null && allocatedHost.getId() != oldHost.getId()) {
				allocatedHost.vmCreate(vm);
				Log.printLine("vm oldHost allocatedHost"+vm.getId()+oldHost.getId()+allocatedHost.getId());
				Log.printLine("VM #" + vm.getId() + "original host #"+oldHost.getId()+" ,the host #" + allocatedHost.getId());

				Map<String, Object> migrate = new HashMap<String, Object>();
				migrate.put("vm", vm);
				migrate.put("host", allocatedHost);
				migrationMap.add(migrate);
			}
		}

		restoreAllocation(vmsToRestore, getHostList());
        Log.printLine(" optimizeAllocation migrationMap"+migrationMap.size());
		return migrationMap;
	}

	/**
	 * Save allocation.
	 *
	 * @param vmList the vm list
	 */
	protected void saveAllocation(List<? extends Vm> vmList) {
		getSavedAllocation().clear();
		for (Vm vm : vmList) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("vm", vm);
			map.put("host", vm.getHost());
			getSavedAllocation().add(map);
		}
	}

	/**
	 * Restore allocation.
	 *
	 * @param vmsToRestore the vms to restore
	 */
	protected void restoreAllocation(List<TrafficVm> vmsToRestore, List<Host> hostList) {
		Log.printLine("in the restoreAllocation");
		Log.printLine("restoreAllocation  getSavedAllocation().size()"+ getSavedAllocation().size());
		for (Host host : hostList) {
			host.vmDestroyAll();
			host.reallocateMigratingVms();
		}
		Log.printLine("restoreAllocation"+ getSavedAllocation().size());
		for (Map<String, Object> map : getSavedAllocation()) {
			Vm vm = (Vm) map.get("vm");
			TrafficHost host = (TrafficHost) map.get("host");
			if (!vmsToRestore.contains(vm)) {
				continue;
			}
			if (!host.vmCreate(vm)) {
				Log.printLine("Something is wrong, the VM can's be restored");
				System.exit(0);
			}
			getVmTable().put(vm.getUid(), host);
			Log.printLine("Restored VM #" + vm.getId() + " on host #" + host.getId());
		}
	}

	public double getCommVMID(List<TrafficVm> vmList,int vmid) {
		//double BwMatrix[][]=NetworkTopology.getBwMatrix();
		double traffic=0.0;
		//int VmNum=NetworkTopology.getVmNum();
		
		//for(int i=0;i< VmNum;i++)
		for(TrafficVm vm: vmList)
		traffic+=NetworkTopology.getBw(vm.getId(), vmid)+NetworkTopology.getBw (vmid,vm.getId());
		return traffic;		
		//return TrafficPeList.getTraffic((List<TrafficPe>) getPeList());
	}

	/**
	 * Gets the power. For this moment only consumed by all PEs.
	 *
	 * @return the power
	 */
	//计算把 VM放到host上的带来的traffic
	@SuppressWarnings("unchecked")
	public double getTraffic(TrafficHost host, TrafficVm vm,List<? extends Vm> vmList ) {
		Log.print("in trafficVm, the vmlist "+vmList.size());
		for(int i=0;i<vmList.size();i++)
			Log.print(vmList.get(i).getId()+" ");
		//double BwMatrix[][]=NetworkTopology.getBwMatrix();
		//int VmNum=NetworkTopology.getVmNum();
		//List<Map<String, Object>> saveMap =this.getSavedAllocation() ;
		double traffic=0.0;
		for(int i=0;i< vmList.size();i++){
			Log.printLine("in trafficVm");
			Log.printLine("vm"+vm.getId()+" vmList[i]"+vmList.get(i).getId());
		//	Log.printLine(saveMap.);
			TrafficHost hostsrc = (TrafficHost) getVmTable().get(vm.getUid());
			Log.printLine("hostsrc "+hostsrc.getId()+" ====host"+host.getId());
			Log.printLine("hostsrc"+ hostsrc.getId());
			TrafficVm vm2=(TrafficVm) TrafficVmList.getById(vmList,vmList.get(i).getId());
			TrafficHost hostdes=(TrafficHost) getVmTable().get(vm2.getUid());
				Log.printLine("hostdes "+hostdes.getId());
				
			double dis=HostTopology.getDelay(hostsrc.getId(), hostdes.getId());
		   Log.print(dis);
		   Log.printLine();
			traffic+=NetworkTopology.getBw(vmList.get(i).getId(), vm.getId())*dis;
		//dis=HostTopology.getDelay( TrafficVmList.getById(vmList,i).getHost().getId(),vm.getHost().getId());
		traffic+=NetworkTopology.getBw (vm.getId(),vmList.get(i).getId())*dis;
		}
		return traffic;
	}

	
	/**
	 * Gets the traffic after allocation.
	 *
	 * @param host the host
	 * @param vm the vm
	 *
	 * @return the traffic after allocation
	 */
	protected double getTrafficAfterAllocation(TrafficHost host, TrafficVm vm,List<? extends Vm> vmList) {
		List<Double> allocatedMipsForVm = null;
		TrafficHost allocatedHost = (TrafficHost) vm.getHost();//allocayedHost是VM所放置的host

		if (allocatedHost != null) {
			allocatedMipsForVm = allocatedHost.getAllocatedMipsForVm(vm);//获取原来VM所在host上分配给vm的Mips 的list
		}

		if (!host.allocatePesForVm(vm, vm.getCurrentRequestedMips())) {
			return -1;
		}
          for(Vm vm1: vmList)
          Log.printLine("getTrafficAfterAllocation, the vmList is "+vm1.getId());
		  
          double traffic = getTraffic(host, vm, vmList);
   
		
		//////////////需要改////////
		host.deallocatePesForVm(vm);

		if (allocatedHost != null && allocatedMipsForVm != null) {
			vm.getHost().allocatePesForVm(vm, allocatedMipsForVm);
		}

		return traffic;
	}

	/**
	 * Gets the traffic after allocation.
	 *
	 * @param host the host
	 * @param vm the vm
	 *
	 * @return the traffic after allocation
	 */
	/*protected double getMinTrafficAfterAllocation(TrafficHost host, Vm vm) {
		List<Double> allocatedMipsForVm = null;
		TrafficHost allocatedHost = (TrafficHost) vm.getHost();

		if (allocatedHost != null) {
			allocatedMipsForVm = vm.getHost().getAllocatedMipsForVm(vm);
		}

		if (!host.allocatePesForVm(vm, vm.getCurrentRequestedMips())) {
			return -1;
		}

		double maxUtilization = host.getMaxUtilizationAmongVmsPes(vm);//获得host上利用率最高的pe放置vm，并返回max利用率

		host.deallocatePesForVm(vm);//

		if (allocatedHost != null && allocatedMipsForVm != null) {
			vm.getHost().allocatePesForVm(vm, allocatedMipsForVm);
		}

		return maxUtilization;
	}
*/
	
	protected double getMaxUtilizationAfterAllocation(TrafficHost host, Vm vm) {
		List<Double> allocatedMipsForVm = null;
		TrafficHost allocatedHost = (TrafficHost) vm.getHost();

		if (allocatedHost != null) {
			allocatedMipsForVm = vm.getHost().getAllocatedMipsForVm(vm);
		}

		if (!host.allocatePesForVm(vm, vm.getCurrentRequestedMips())) {
			return -1;
		}

		double maxUtilization = host.getMaxUtilizationAmongVmsPes(vm);

		host.deallocatePesForVm(vm);

		if (allocatedHost != null && allocatedMipsForVm != null) {
			vm.getHost().allocatePesForVm(vm, allocatedMipsForVm);
		}

		return maxUtilization;
	}

	/**
	 * Gets the saved allocation.
	 *
	 * @return the saved allocation
	 */
	protected List<Map<String, Object>> getSavedAllocation() {
		return savedAllocation;
	}

	/**
	 * Sets the saved allocation.
	 *
	 * @param savedAllocation the saved allocation
	 */
	protected void setSavedAllocation(List<Map<String, Object>> savedAllocation) {
		this.savedAllocation = savedAllocation;
	}

	/**
	 * Gets the utilization bound.
	 *
	 * @return the utilization bound
	 */
	protected double getUtilizationThreshold() {
		return utilizationThreshold;
	}

	/**
	 * Sets the utilization bound.
	 *
	 * @param utilizationThreshold the new utilization bound
	 */
	protected void setUtilizationThreshold(double utilizationThreshold) {
		this.utilizationThreshold = utilizationThreshold;
	}

}
