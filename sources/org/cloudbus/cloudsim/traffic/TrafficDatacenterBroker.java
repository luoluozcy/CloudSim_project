package org.cloudbus.cloudsim.traffic;

import java.util.Iterator;
import java.util.List;

import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.NetworkTopology;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.CloudSimTags;
import org.cloudbus.cloudsim.core.SimEvent;
import org.cloudbus.cloudsim.lists.VmList;

public class TrafficDatacenterBroker extends DatacenterBroker {

	public TrafficDatacenterBroker(String name) throws Exception {
		super(name);
		// TODO Auto-generated constructor stub
	}
	
	protected void processVmCreate(SimEvent ev)
	{
		int[] data = (int[]) ev.getData();
		int datacenterId = data[0];
		int vmId = data[1];
		int result = data[2];

		if (result == CloudSimTags.TRUE) {
			getVmsToDatacentersMap().put(vmId, datacenterId);
			getVmsCreatedList().add(VmList.getById(getVmList(), vmId));
			Log.printLine("in the DatacenterBroker, the processVmCreate");
			Log.printLine(CloudSim.clock()+": "+getName()+ ": VM #"+vmId+" has been created in Datacenter #" + datacenterId + ", Host #" + VmList.getById(getVmsCreatedList(), vmId).getHost().getId());
		} else {
			Log.printLine(CloudSim.clock()+": "+getName()+ ": Creation of VM #"+vmId+" failed in Datacenter #" + datacenterId);
		}

		incrementVmsAcks();
		Log.printLine("in Broker ,the VmsACK is "+this.vmsAcks);
		//List<TrafficVm vm=

		if (getVmsCreatedList().size() == getVmList().size() - getVmsDestroyed()) { // all the requested VMs have been created
			{    Log.print("vm create process is completes,and the initial traffic is ");
                 double Totaltraffic=this.getTotalTraffic();
              Log.printLine(Totaltraffic);
				List<TrafficVm> vmList=getVmList();
				Iterator<TrafficVm> iter=vmList.iterator();
				Log.printLine("in the DataBroker, check the VMTopo ");
				while(iter.hasNext())
				{
					Vm vm=iter.next();
					for(int i=0;i<vmList.size();i++)
					  Log.print(NetworkTopology.getBw(vm.getId(), i));
				}
				    
				submitCloudlets();
			}
		} else {
			if (getVmsRequested() == getVmsAcks()) { // all the acks received, but some VMs were not created
				// find id of the next datacenter that has not been tried
				for (int nextDatacenterId : getDatacenterIdsList()) {
					if (!getDatacenterRequestedIdsList().contains(nextDatacenterId)) {
						createVmsInDatacenter(nextDatacenterId);
						return;
					}
				}

				//all datacenters already queried
				if (getVmsCreatedList().size() > 0) { //if some vm were created
					submitCloudlets();
				} else { //no vms created. abort
					Log.printLine("in the DatacenterBroker, the processVmCreate");
					Log.printLine(CloudSim.clock() + ": " + getName() + ": none of the required VMs could be created. Aborting");
					finishExecution();
				}
			}
		}	
		
		
	}
	
	public List<TrafficVm> getVmList() {
		//Log.printLine(vmList.getClass());
		return (List<TrafficVm>) vmList;
	}
	public double getTotalTraffic() {
		  double totaltraffic=0.0;
			for(TrafficVm vm:getVmList())
				totaltraffic+=vm.getTraffic( (TrafficHost) vm.getHost(), vm, getVmList());
			return totaltraffic;
			
		}


}
