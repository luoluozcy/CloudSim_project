package org.cloudbus.cloudsim.traffic;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicy;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.CloudSimTags;
import org.cloudbus.cloudsim.core.SimEvent;
import org.cloudbus.cloudsim.core.predicates.PredicateType;
import org.cloudbus.cloudsim.examples.traffic.SingleThreshold;

/**
 * TrafficDatacenter is a class that enables simulation of power-aware data centers.
 *
 * @author		Anton Beloglazov
 * @since		CloudSim Toolkit 2.0
 */
public class TrafficDatacenter extends Datacenter {

	/** The power. */
	private double traffic;
    private List<TrafficVm> TotalvmList;
	/** The disable migrations. */
	private boolean disableMigrations;

	/** The cloudlet submited. */
	private double cloudletSubmitted;

	/** The migration count. */
	private int migrationCount;

	/**
	 * Instantiates a new datacenter.
	 *
	 * @param name the name
	 * @param characteristics the res config
	 * @param schedulingInterval the scheduling interval
	 * @param utilizationBound the utilization bound
	 * @param vmAllocationPolicy the vm provisioner
	 * @param storageList the storage list
	 *
	 * @throws Exception the exception
	 */
	public TrafficDatacenter(
			String name,
			DatacenterCharacteristics characteristics,
			TrafficVmAllocation vmAllocationPolicy,
			List<Storage> storageList,
			double schedulingInterval) throws Exception {
		super(name, characteristics, vmAllocationPolicy, storageList, schedulingInterval);

		setTraffic(0.0);
		setDisableMigrations(false);
		setCloudletSubmitted(-1);
		setMigrationCount(0);
	}
	
	public List<TrafficVm> getVmList() {
		return (List<TrafficVm>) this.vmList;
	}
	
	public List<TrafficVm> getTotalVmList() {
		return (List<TrafficVm>) SingleThreshold.TotalvmList;
	}
	/**
	 * Updates processing of each cloudlet running in this TrafficDatacenter. It is necessary because
	 * Hosts and VirtualMachines are simple objects, not entities. So, they don't receive events
	 * and updating cloudlets inside them must be called from the outside.
	 *
	 * @pre $none
	 * @post $none
	 */
	@Override
	protected void updateCloudletProcessing() {
		if (getCloudletSubmitted() == -1 || getCloudletSubmitted() == CloudSim.clock()) {
			CloudSim.cancelAll(getId(), new PredicateType(CloudSimTags.VM_DATACENTER_EVENT));
			schedule(getId(), getSchedulingInterval(), CloudSimTags.VM_DATACENTER_EVENT);
			return;
		}
		double currentTime = CloudSim.clock();
		double timeframeTraffic = 0.0;

		// if some time passed since last processing
		if (currentTime > getLastProcessTime()) {
		double timeDiff = currentTime - getLastProcessTime();
			double minTime = Double.MAX_VALUE;

			Log.printLine("\n");

			for (TrafficHost host : this.<TrafficHost>getHostList()) {

				Log.formatLine("%.2f: Host #%d", CloudSim.clock(), host.getId());

				double hostTraffic = 0.0;

				if (host.getUtilizationOfCpu() > 0) {
					try {  
						Log.print("in the TafficDatacenter, the TotalvmList is ");
						  for(TrafficVm vm1:getVmList())        
						     Log.print(vm1.getId()+" ");
						  Log.printLine();
						hostTraffic = host.getTraffic(getVmList());
						timeframeTraffic += hostTraffic;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				Log.formatLine("%.2f: Host #%d utilization is %.2f%%", CloudSim.clock(), host.getId(), host.getUtilizationOfCpu() * 100);
				Log.formatLine("%.2f: Host #%d traffic is %.2f W*sec", CloudSim.clock(), host.getId(), hostTraffic);
			}

			Log.formatLine("\n%.2f: Consumed energy is %.2f W*sec\n", CloudSim.clock(), timeframeTraffic);

			Log.printLine("\n\n--------------------------------------------------------------\n\n");

			for (TrafficHost host : this.<TrafficHost>getHostList()) {
				Log.formatLine("\n%.2f: Host #%d", CloudSim.clock(), host.getId());

				double time = host.updateVmsProcessing(currentTime); // inform VMs to update processing
				if (time < minTime) {
					minTime = time;
				}

				Log.formatLine("%.2f: Host #%d utilization is %.2f%%", CloudSim.clock(), host.getId(), host.getUtilizationOfCpu() * 100);
			}

			setTraffic(getTraffic() + timeframeTraffic);
			
			/** Remove completed VMs **/
			for (TrafficHost host : this.<TrafficHost>getHostList()) {
				for (Vm vm : host.getCompletedVms()) {
					getVmAllocationPolicy().deallocateHostForVm(vm);
					getVmList().remove(vm);
					Log.printLine("VM #" + vm.getId() + " has been deallocated from host #" + host.getId());
				}
			}

			Log.printLine();
			Log.printLine("  in trafficDatacenter, the updateCloudletProcessing,isDisableMigrations "+isDisableMigrations());
			if (!isDisableMigrations()) {
				List<Map<String, Object>> migrationMap = getVmAllocationPolicy().optimizeAllocation(getVmList());
           Log.printLine("  migrationMap " +migrationMap.size());
				for (Map<String, Object> migrate : migrationMap) {
					Vm vm = (Vm) migrate.get("vm");
					TrafficHost targetHost = (TrafficHost) migrate.get("host");
					TrafficHost oldHost = (TrafficHost) vm.getHost();
				//	TrafficHost oldHost = (TrafficHost) getVmAllocationPolicy().getHost(vm);
					Log.printLine(" targetHost"+targetHost.getId()+ " oldHost"+oldHost.getId());
                    Log.printLine("vm "+vm.getId()+" oldHost"+ oldHost.getId()+" targetHost"+targetHost.getId());
					targetHost.addMigratingInVm(vm);

					if (oldHost == null) {
						Log.formatLine("%.2f: Migration of VM #%d to Host #%d is started", CloudSim.clock(), vm.getId(), targetHost.getId());
				} 
					else {
						Log.formatLine("%.2f: Migration of VM #%d from Host #%d to Host #%d is started", CloudSim.clock(), vm.getId(), oldHost.getId(), targetHost.getId());
					}

					incrementMigrationCount();

					vm.setInMigration(true);

					/** VM migration delay = RAM / bandwidth + C    (C = 10 sec) **/
					send(getId(), vm.getRam() / ((double) vm.getBw() / 8000) + 0, CloudSimTags.VM_MIGRATE, migrate);
				}
			}

			// schedules an event to the next time
			if (minTime != Double.MAX_VALUE) {
				CloudSim.cancelAll(getId(), new PredicateType(CloudSimTags.VM_DATACENTER_EVENT));
				send(getId(), getSchedulingInterval(), CloudSimTags.VM_DATACENTER_EVENT);
			}

			setLastProcessTime(currentTime);
		}
	}

	/* (non-Javadoc)
	 * @see cloudsim.Datacenter#processCloudletSubmit(cloudsim.core.SimEvent, boolean)
	 */
	@Override
	protected void processCloudletSubmit(SimEvent ev, boolean ack) {
		super.processCloudletSubmit(ev, ack);
		setCloudletSubmitted(CloudSim.clock());
	}

	/**
	 * Gets the power.
	 *
	 * @return the power
	 */
	public double getTraffic() {
		return traffic;
	}

	/**
	 * Sets the power.
	 *
	 * @param power the new power
	 */
	protected void setTraffic(double traffic) {
		this.traffic = traffic;
	}

	/**
	 * Checks if TrafficDatacenter is in migration.
	 *
	 * @return true, if TrafficDatacenter is in migration
	 */
	protected boolean isInMigration() {
		boolean result = false;
		for (Vm vm : getVmList()) {
			if (vm.isInMigration()) {
				result = true;
				break;
			}
		}
		return result;
	}

	/**
	 * Gets the under allocated mips.
	 *
	 * @return the under allocated mips
	 */
	public Map<String, List<List<Double>>> getUnderAllocatedMips() {
		Map<String, List<List<Double>>> underAllocatedMips = new HashMap<String, List<List<Double>>>();
		for (TrafficHost host : this.<TrafficHost>getHostList()) {
			for (Entry<String, List<List<Double>>> entry : host.getUnderAllocatedMips().entrySet()) {
				if (!underAllocatedMips.containsKey(entry.getKey())) {
					underAllocatedMips.put(entry.getKey(), new ArrayList<List<Double>>());
				}
				underAllocatedMips.get(entry.getKey()).addAll(entry.getValue());

			}
		}
		return underAllocatedMips;
	}

	/**
	 * Checks if is disable migrations.
	 *
	 * @return true, if is disable migrations
	 */
	public boolean isDisableMigrations() {
		return disableMigrations;
	}

	/**
	 * Sets the disable migrations.
	 *
	 * @param disableMigrations the new disable migrations
	 */
	public void setDisableMigrations(boolean disableMigrations) {
		this.disableMigrations = disableMigrations;
	}

	/**
	 * Checks if is cloudlet submited.
	 *
	 * @return true, if is cloudlet submited
	 */
	protected double getCloudletSubmitted() {
		return cloudletSubmitted;
	}

	/**
	 * Sets the cloudlet submited.
	 *
	 * @param cloudletSubmitted the new cloudlet submited
	 */
	protected void setCloudletSubmitted(double cloudletSubmitted) {
		this.cloudletSubmitted = cloudletSubmitted;
	}

	/**
	 * Gets the migration count.
	 *
	 * @return the migration count
	 */
	public int getMigrationCount() {
		return migrationCount;
	}

	/**
	 * Sets the migration count.
	 *
	 * @param migrationCount the new migration count
	 */
	protected void setMigrationCount(int migrationCount) {
		this.migrationCount = migrationCount;
	}

	/**
	 * Increment migration count.
	 */
	protected void incrementMigrationCount() {
		setMigrationCount(getMigrationCount() + 1);
	}


}

 

