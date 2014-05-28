package org.cloudbus.cloudsim.examples.traffic;

/*
 * Title:        CloudSim Toolkit
 * Description:  CloudSim (Cloud Simulation) Toolkit for Modeling and Simulation
 *               of Clouds
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2009, The University of Melbourne, Australia
 */

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.CloudletSchedulerDynamicWorkload;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.GenerateHostTopology;
import org.cloudbus.cloudsim.HostTopology;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.NetworkTopology;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.UtilizationModelStochastic;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmSchedulerTimeShared;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.traffic.TrafficDatacenter;
import org.cloudbus.cloudsim.traffic.TrafficHost;
import org.cloudbus.cloudsim.traffic.TrafficPe;
import org.cloudbus.cloudsim.traffic.TrafficVm;
import org.cloudbus.cloudsim.traffic.TrafficVmAllocation;
import org.cloudbus.cloudsim.traffic.models.TrafficModelLinear;
import org.cloudbus.cloudsim.network.TopopysicalGraph;
import org.cloudbus.cloudsim.network.TopopysicalLink;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;

/**
 * An example of a traffic aware data center. In this example the placement of VMs
 * is continuously adapted using VM migration in order to minimize the number
 * of physical nodes in use, while idle nodes are switched off to save energy.
 * The CPU utilization of each host is kept under the specified utilization threshold.
 */
public class SingleThreshold {

	/** The cloudlet list. */
	private static List<Cloudlet> cloudletList;

	/** The vm list. */
	private static List<TrafficVm> vmList;
	private static double [][] bwMatrix=null;
	private static double [][] DisMatrix=null;
	private static double utilizationThreshold = 0.8;
	private static int HostTopoType=1;
	private static int hostsNumber = 2;
	private static double vmsNumber = 5;
	private static double cloudletsNumber = 5;
	public  static  List<TrafficVm> TotalvmList=new  ArrayList<TrafficVm>();
	/**
	 * Creates main() to run this example.
	 *
	 * @param args the args
	 */
	public static void main(String[] args) {

		Log.printLine("Starting NetVMM example...");

		try {
			// First step: Initialize the CloudSim package. It should be called
			// before creating any entities. We can't run this example without
			// initializing CloudSim first. We will get run-time exception
			// error.
			int num_user = 1; // number of cloud users
			Calendar calendar = Calendar.getInstance();
			boolean trace_flag = false; // mean trace GridSim events

			// Initialize the CloudSim library
			CloudSim.init(num_user, calendar, trace_flag);//初始化GridSim库

			// Second step: Create Datacenters
			// Datacenters are the resource providers in CloudSim. We need at
			// list one of them to run a CloudSim simulation
			TrafficDatacenter datacenter = createDatacenter("Datacenter_0");//创建数据中心，在CLoudSim仿真平台上，一个数据中心由一个或者多个Machine组成，
			                                                                //一个Machine是由一个或者多个PEs或CPUs组成

			// Third step: Create Broker
			DatacenterBroker broker = createBroker();
			int brokerId = broker.getId();//创建代理

			// Fourth step: Create one virtual machine
			vmList = createVms(brokerId);//创建虚拟机，创建时为虚拟机制定Broker，设置VMlist的参数信息，包括MIPS，RAM，BW等
			//创建虚拟机
			for(int i=0;i<vmList.size();i++){
				//TotalvmList.add(vmList.get(i));
			    Log.printLine(vmList.get(i).getId());
			}
			//Log.printLine("TotalList :"+TotalvmList.size());
			//TotalvmList=new List<>
		 // TotalvmList=new List;//(vmList);
			for(int i=0;i<vmList.size();i++){
				//Total
				TotalvmList.add(vmList.get(i));
			    Log.printLine(TotalvmList.get(i));
			}
			// submit vm list to the broker
			broker.submitVmList(vmList);//把待创建VMList发送给broker
		                               	//向代理Broker提交虚拟机列表

			// Fifth step: Create one cloudlet
			cloudletList = createCloudletList(brokerId);//创建云任务，创建时指定任务的用户ID，BrokerID

			// submit cloudlet list to the broker
			broker.submitCloudletList(cloudletList);//将cloudlet和VM绑定，也就是一个任务一个VNM
			                                        //向Broker提交任务列表
			
			//----------------------------------------构建Topu--------------------------------------------//
			
			NetworkTopology.buildNetworkTopology("D:\\eclipse\\cloudsim-net\\topology.brite");
			//NetworkTopology.
			Log.printLine("In SingleThreshold Main, the VMTopo is :");
			bwMatrix=NetworkTopology.getBwMatrix();
			for(int i=0;i<vmList.size();i++)
			for(int j=0;j<vmList.size();j++)
					Log.printLine(bwMatrix[i][j]);
			Log.printLine(NetworkTopology.getVmNum());
			
			Log.printLine("HostTopo Start");
			HostTopology.buildHostPysicalTopology(HostTopoType, hostsNumber);
			DisMatrix=HostTopology.getDistanceMatrix();
				//TopopysicalGraph graph=new TopopysicalGraph();
				//GenerateHostTopology.GenerateTopo("TREE",10);
				//String DesFile = "tree" +10;
				//GeneratePMdistanceSet("TREE", graph,DesFile, 10);
				//Iterator<TopopysicalLink> iter = graph.getLinkIterator();
			//	while(iter.hasNext()){
				//	TopopysicalLink edge = iter.next();
				//	Log.printLine(edge.getSrcNodeID()+" "+edge.getDestNodeID()+" " +edge.getLinkDelay());
				//	}
			for(int i=0;i<hostsNumber;i++)
				for(int j=0;j<hostsNumber;j++)
					Log.printLine(DisMatrix[i][j]);
			
				
			//maps CloudSim entities to BRITE entities
			//PowerDatacenter will correspond to BRITE node 0
			int briteNode=0;
			NetworkTopology.mapNode(datacenter.getId(),briteNode);
            
			//Broker will correspond to BRITE node 3
			briteNode=3;
			NetworkTopology.mapNode(broker.getId(),briteNode);

			//-----------------------------------------------End of Topu----------------------------------------------//
			// Sixth step: Starts the simulation
			
		
			
	double lastClock = CloudSim.startSimulation();//启动仿真

			// Final step: Print results when simulation is over
		
		   //在仿真结束后统计结果
			List<Cloudlet> newList = broker.getCloudletReceivedList();
			Log.printLine("Received " + newList.size() + " cloudlets");

		CloudSim.stopSimulation();

		
			
			 printCloudletList(newList);
			 

			
		    int totalTotalRequested = 0;
		    int totalTotalAllocated = 0;
		    ArrayList<Double> sla = new ArrayList<Double>();
		    int numberOfAllocations = 0;
			for (Entry<String, List<List<Double>>> entry : datacenter.getUnderAllocatedMips().entrySet()) {
			    List<List<Double>> underAllocatedMips = entry.getValue();
			    double totalRequested = 0;
			    double totalAllocated = 0;
			    for (List<Double> mips : underAllocatedMips) {
			    	if (mips.get(0) != 0) {
			    		numberOfAllocations++;
			    		totalRequested += mips.get(0);
			    		totalAllocated += mips.get(1);
			    		double _sla = (mips.get(0) - mips.get(1)) / mips.get(0) * 100;
			    		if (_sla > 0) {
			    			sla.add(_sla);
			    		}
			    	}
				}
			    totalTotalRequested += totalRequested;
			    totalTotalAllocated += totalAllocated;
			}

			double averageSla = 0;
			if (sla.size() > 0) {
			    double totalSla = 0;
			    for (Double _sla : sla) {
			    	totalSla += _sla;
				}
			    averageSla = totalSla / sla.size();
			}
			

			Log.printLine();  
			
			Log.printLine(String.format("Total simulation time: %.2f sec", lastClock));
			Log.printLine(String.format("Network Overhead consumption: %.2f ", datacenter.getTraffic()));
			Log.printLine(String.format("Number of VM migrations: %d", datacenter.getMigrationCount()));
		
			
			Log.printLine(String.format("Number of SLA violations: %d", sla.size()));
			Log.printLine(String.format("SLA violation percentage: %.2f%%", (double) sla.size() * 100 / numberOfAllocations));
		Log.printLine(String.format("Average SLA violation: %.2f%%", averageSla));
			Log.printLine();

		} catch (Exception e) {
			e.printStackTrace();
			Log.printLine("Unwanted errors happen");
		}
		

		Log.printLine("SingleThreshold finished!");
		Log.close();
		
	}

	/**
	 * Creates the cloudlet list.
	 *
	 * @param brokerId the broker id
	 *
	 * @return the cloudlet list
	 */
	private static List<Cloudlet> createCloudletList(int brokerId) {
		List<Cloudlet> list = new ArrayList<Cloudlet>();

		long length = 150000; // 10 min on 250 MIPS
		int pesNumber = 1;
		long fileSize = 300;
		long outputSize = 300;

		for (int i = 0; i < cloudletsNumber; i++) {
			Cloudlet cloudlet = new Cloudlet(i, length, pesNumber, fileSize, outputSize, new UtilizationModelStochastic(), new UtilizationModelStochastic(), new UtilizationModelStochastic());
			cloudlet.setUserId(brokerId);
			cloudlet.setVmId(i);
			list.add(cloudlet);
		}

		return list;
	}

	/**
	 * Creates the vms.
	 *
	 * @param brokerId the broker id
	 *
	 * @return the list< vm>
	 */
	private static List<TrafficVm> createVms(int brokerId) {
		List<TrafficVm> vms = new ArrayList<TrafficVm>();

		// VM description
		int[] mips = { 250, 500, 750, 1000 }; // MIPSRating
		int pesNumber = 1; // number of cpus
		int ram = 128; // vm memory (MB)
		long bw = 2500; // bandwidth
		long size = 2500; // image size (MB)
		String vmm = "Xen"; // VMM name

		for (int i = 0; i < vmsNumber; i++) {
			vms.add(
				(TrafficVm) new TrafficVm(i, brokerId, mips[i % mips.length], pesNumber, ram, bw, size, vmm, new CloudletSchedulerDynamicWorkload(mips[i % mips.length], pesNumber))
			);
		}

		return vms;
	}

	/**
	 * Creates the datacenter.
	 *
	 * @param name the name
	 *
	 * @return the datacenter
	 *
	 * @throws Exception the exception
	 */
	private static TrafficDatacenter createDatacenter(String name) throws Exception {
		// Here are the steps needed to create a TrafficDatacenter:
		// 1. We need to create an object of HostList2 to store
		// our machine
		List<TrafficHost> hostList = new ArrayList<TrafficHost>();

		double maxTraffic = 250; // 250W
		double staticTrafficPercent = 0.7; // 70%

		int[] mips = { 1000, 2000, 3000 };
		int ram = 10000; // host memory (MB)
		long storage = 1000000; // host storage
		int bw = 100000;

		for (int i = 0; i < hostsNumber; i++) {
			// 2. A Machine contains one or more PEs or CPUs/Cores.
			// In this example, it will have only one core.
			// 3. Create PEs and add these into an object of TrafficPeList.
			List<TrafficPe> peList = new ArrayList<TrafficPe>();
			peList.add(new TrafficPe(0, new PeProvisionerSimple(mips[i % mips.length]), new TrafficModelLinear(maxTraffic, staticTrafficPercent))); // need to store TrafficPe id and MIPS Rating

			// 4. Create TrafficHost with its id and list of PEs and add them to the list of machines
			hostList.add(
				new TrafficHost(
					i,
					new RamProvisionerSimple(ram),
					new BwProvisionerSimple(bw),
					storage,
					peList,
					new VmSchedulerTimeShared(peList)
				)
			); // This is our machine
		}

		// 5. Create a DatacenterCharacteristics object that stores the
		// properties of a Grid resource: architecture, OS, list of
		// Machines, allocation policy: time- or space-shared, time zone
		// and its price (G$/TrafficPe time unit).
		String arch = "x86"; // system architecture
		String os = "Linux"; // operating system
		String vmm = "Xen";
		double time_zone = 10.0; // time zone this resource located
		double cost = 3.0; // the cost of using processing in this resource
		double costPerMem = 0.05; // the cost of using memory in this resource
		double costPerStorage = 0.001; // the cost of using storage in this
										// resource
		double costPerBw = 0.0; // the cost of using bw in this resource

		DatacenterCharacteristics characteristics = new DatacenterCharacteristics(
				arch, os, vmm, hostList, time_zone, cost, costPerMem, costPerStorage, costPerBw);

		// 6. Finally, we need to create a TrafficDatacenter object.
		TrafficDatacenter trafficDatacenter = null;
		try {
			trafficDatacenter = new TrafficDatacenter(
					name,
					characteristics,
					new TrafficVmAllocation(hostList, utilizationThreshold, vmList),
					new LinkedList<Storage>(),
					5.0);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return trafficDatacenter;
	}

	// We strongly encourage users to develop their own broker policies, to
	// submit vms and cloudlets according
	// to the specific rules of the simulated scenario
	/**
	 * Creates the broker.
	 *
	 * @return the datacenter broker
	 */
	private static DatacenterBroker createBroker() {
		DatacenterBroker broker = null;
		try {
			broker = new DatacenterBroker("Broker");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return broker;
	}

	/**
	 * Prints the Cloudlet objects.
	 *
	 * @param list list of Cloudlets
	 */
	private static void printCloudletList(List<Cloudlet> list) {
		int size = list.size();
		Cloudlet cloudlet;

		String indent = "\t";
		Log.printLine();
		Log.printLine("========== OUTPUT ==========");
		Log.printLine("Cloudlet ID" + indent + "STATUS" + indent
				+ "Resource ID" + indent + "VM ID" + indent + "Time" + indent
				+ "Start Time" + indent + "Finish Time");

		DecimalFormat dft = new DecimalFormat("###.##");
		for (int i = 0; i < size; i++) {
			cloudlet = list.get(i);
			Log.print(indent + cloudlet.getCloudletId());

			if (cloudlet.getCloudletStatus() == Cloudlet.SUCCESS) {
				Log.printLine(indent + "SUCCESS"
					+ indent + indent + cloudlet.getResourceId()
					+ indent + cloudlet.getVmId()
					+ indent + dft.format(cloudlet.getActualCPUTime())
					+ indent + dft.format(cloudlet.getExecStartTime())
					+ indent + indent + dft.format(cloudlet.getFinishTime())
				);
			}
		}
	}

}
