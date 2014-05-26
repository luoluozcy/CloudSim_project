/*
 * Title:        CloudSim Toolkit
 * Description:  CloudSim (Cloud Simulation) Toolkit for Modeling and Simulation of Clouds
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2009-2010, The University of Melbourne, Australia
 */

package org.cloudbus.cloudsim;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.cloudbus.cloudsim.network.TopopysicalGraph;
import org.cloudbus.cloudsim.network.TopopysicalLink;
//import org.cloudbus.cloudsim;
import org.cloudbus.cloudsim.network.TopopysicalNode;

import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;
import org.cloudbus.cloudsim.traffic.TrafficHost;
import org.cloudbus.cloudsim.traffic.TrafficPe;
import org.cloudbus.cloudsim.traffic.models.TrafficModelLinear;

/**
 * NetworkTopology is a class that implements network layer
 * in CloudSim. It reads a BRITE file and generates a
 * topological network from it. Information of this network is
 * used to simulate latency in network traffic of CloudSim.
 * <p>
 * The topology file may contain more nodes the the number of
 * entities in the simulation. It allows for users to increase
 * the scale of the simulation without changing the topology
 * file. Nevertheless, each CloudSim entity must be mapped to
 * one (and only one) BRITE node to allow proper work of the
 * network simulation. Each BRITE node can be mapped to only
 * one entity at a time.
 *
 * @author		Rodrigo N. Calheiros
 * @author		Anton Beloglazov
 * @since		CloudSim Toolkit 1.0
 */
public class HostTopology {

	protected static int nextIdx=0;
	private static boolean networkEnabled = false;
	//protected static DelayMatrix_Float delayMatrix = null;
	protected static double[][] DistanceMatrix = null;
	
	protected static TopopysicalGraph graph = null;
	protected static Map<Integer, Integer> map = null;
	/**
	 * Creates the network topology if file exists and
	 * if file can be succesfully parsed. File is written
	 * in the BRITE format and contains topologycal
	 * information on simulation entities.
	 * @param fileName name of the BRITE file
	 * @throws IOException 
	 * @pre fileName != null
	 * @post $none
	 */
	public static void buildHostPysicalTopology(int Topoflag, int ServerNum) throws IOException {
		String TopoType=null;
		
		// the filename of the generated topo file
		String DesFile=null;
		if(graph==null){
			graph = new TopopysicalGraph();
		}
		setNetworkEnabled();

		//int flag;
		// argv[6] means the type of PM topology: tree, fat tree, vl2, bcube
		//flag = atoi(argv[6]);

		//cout << "please select the type of topology you want to generate: " << endl;
		//cout << "1 is tree, 2 is fat tree, 3 is vl2, 4 is bcube. " << endl;
		//cin >> flag;
		if( Topoflag == 1 )
		{//cout << endl;
			TopoType = "TREE";
			DesFile = "tree" + ServerNum;
		}
		else if( Topoflag == 2 )
		{
			TopoType = "FTREE";
			DesFile = "ftree" + ServerNum;
		}
		else if( Topoflag == 3 )
		{
			TopoType = "CLOS";
			DesFile = "VL2" +ServerNum;
		}
		else if( Topoflag == 4 )
		{
			TopoType = "BCUBE";
			DesFile = "bcube" +ServerNum;
		}

	//	CGlobals::gTapConfig.PMTopoType = TopoType;


		GenerateHostTopology.GenerateTopo(TopoType,ServerNum);
    	
		Log.printLine("HostPysicalTopology file: " + DesFile);

		//try to find the file
		CalculateHostTopology.GeneratePMdistanceSet(TopoType, graph,DesFile, ServerNum);
		 //GraphReaderBrite reader = new GraphReaderBrite();

		//Log.
//	graph =  reader.readGraphFile(fileName);
		map = new HashMap<Integer,Integer>();
		generateHostNode(ServerNum); 
		generateMatrices();
		//double [][] b=getBwMatrix();

	}

	/**
	 * Generates the matrices used internally to set
	 * latency and bandwidth between elements
	 *
	 */
	private static void generateHostNode(int serverNum) {
		List<TrafficHost> hostList = new ArrayList<TrafficHost>();

		double maxPower = 250; // 250W
		double staticPowerPercent = 0.7; // 70%

		int[] mips = { 1000, 2000, 3000 };
		int ram = 10000; // host memory (MB)
		long storage = 1000000; // host storage
		int bw = 100000;

		for (int i = 0; i < serverNum; i++) {
			// 2. A Machine contains one or more PEs or CPUs/Cores.
			// In this example, it will have only one core.
			// 3. Create PEs and add these into an object of PowerPeList.
			List<TrafficPe> peList = new ArrayList<TrafficPe>();
			peList.add(new TrafficPe(0, new PeProvisionerSimple(mips[i % mips.length]), new TrafficModelLinear(maxPower, staticPowerPercent))); // need to store PowerPe id and MIPS Rating


			// 4. Create PowerHost with its id and list of PEs and add them to the list of machines
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
		Iterator<TrafficHost> iter =hostList.iterator();
		Log.printLine(hostList.size());
		while(iter.hasNext()){
			TrafficHost host = iter.next();
			TopopysicalNode topoNode = new TopopysicalNode(host.getId(), host);
			graph.addNode(topoNode);
			Log.printLine(topoNode.getNodeID()+" "+topoNode.getHost().getId());
			}
	}
	
	private static void generateMatrices() {
		//creates the delay matrix
	//	delayMatrix = new DelayMatrix_Float(graph, false);

		//creates the bw matrix
		DistanceMatrix = createDistanceMatrix(graph,false);

		networkEnabled=true;
	}
	
	//public static 

	/**
	 * Adds a new link in the network topology
	 * @param srcId ID of the link's source
	 * @param destId ID of the link's destination
	 * @param bw Link's bandwidth
	 * @param lat link's latency
	 * @pre srcId > 0
	 * @pre destId > 0
	 * @post $none
	 */
	public static void addLink(int srcId, int destId, double lat){

		if(graph==null){
			graph = new TopopysicalGraph();
		}

		if(map==null){
			map = new HashMap<Integer,Integer>();
		}

		//maybe add the nodes
		if(!map.containsKey(srcId)){
			graph.addNode(new TopopysicalNode(nextIdx));
			map.put(srcId, nextIdx);
			nextIdx++;
		}

		if(!map.containsKey(destId)){
			graph.addNode(new TopopysicalNode(nextIdx));
			map.put(destId, nextIdx);
			nextIdx++;
		}

		//generate a new link
		graph.addLink(new TopopysicalLink(map.get(srcId),map.get(destId),(float)lat));

		generateMatrices();

	}

	/**
	 * Creates the matrix containiing the available bandiwdth beteen two nodes
	 * @param graph topological graph describing the topology
	 * @param directed true if the graph is directed; false otherwise
	 * @return the bandwidth graph
	 */
	private static double[][] createDistanceMatrix(TopopysicalGraph graph, boolean directed) {
		int nodes = graph.getNumberOfNodes();

		double[][] mtx = new double[nodes][nodes];

		//cleanup matrix
		for(int i=0;i<nodes;i++){
			for(int j=0;j<nodes;j++){
				mtx[i][j] = 0.0;
			}
		}

		Iterator<TopopysicalLink> iter = graph.getLinkIterator();
		while(iter.hasNext()){
			TopopysicalLink edge = iter.next();

			mtx[edge.getSrcNodeID()][edge.getDestNodeID()] = edge.getLinkDelay();

			if(!directed){
				mtx[edge.getDestNodeID()][edge.getSrcNodeID()] = edge.getLinkDelay();
			}
		}

		return mtx;
	}
	
	public static double [][] getDistanceMatrix(){
		return DistanceMatrix;
	}

	/**
	 * Maps a CloudSim entity to a node in the network topology
	 * @param cloudSimEntityID ID of the entity being mapped
	 * @param briteID ID of the BRITE node that corresponds to the CloudSim entity
	 * @pre cloudSimEntityID >= 0
	 * @pre briteID >= 0
	 * @post $none
	 */
	

	/**
	 * Calculates the delay between two nodes
	 * @param srcID ID of the source node
	 * @param destID ID of the destination node
	 * @return  communication delay between the two nodes
	 * @pre srcID >= 0
	 * @pre destID >= 0
	 * @post $none
	 */
	public static double getDelay(int srcID, int destID){
		if(networkEnabled){
			try{
				//add the network latency
				double delay = DistanceMatrix[srcID][destID];

				return delay;
			} catch (Exception e){
				//in case of error, just keep running and return 0.0
			}
		}
		return 0.0;
	}

	
	/**
	 * This method returns true if network simulation is working. If there were some problem
	 * during creation of network (e.g., during parsing of BRITE file) that does not allow
	 * a proper simulation of the network, this method returns false.
	 * @return $true if network simulation is ok. $false otherwise
	 * @pre $none
	 * @post $none
	 */
	public static boolean isNetworkEnabled(){
		return networkEnabled;
	}
	
	public static void  setNetworkEnabled(){
		networkEnabled=true;
	}
	public static void main(String[] args) throws IOException
	{   Log.printLine("HostTopo Start");
		buildHostPysicalTopology(1,10);
		
	}

}
