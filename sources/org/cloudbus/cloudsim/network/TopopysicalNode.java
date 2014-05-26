/*
 * Title:        CloudSim Toolkit
 * Description:  CloudSim (Cloud Simulation) Toolkit for Modeling and Simulation of Clouds
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2009-2010, The University of Melbourne, Australia
 */

package org.cloudbus.cloudsim.network;

import org.cloudbus.cloudsim.traffic.TrafficHost;

/**
 * Just represents an topological network node
 * retrieves its information from an topological-generated file
 * (eg. topology-generator)
 *
 * @author		Thomas Hohnstein
 * @since		CloudSim Toolkit 1.0
 */
public class TopopysicalNode {

	/**
	 * its the nodes-ID inside this network
	 */
	private int nodeID=0;
   private TrafficHost host;
	/**
	 * describes the nodes-name inside the network
	 */
	//private String nodeName = null;

	/**
	 * representing the x an y world-coordinates
	 */
	//private int worldX = 0;
	//private int worldY = 0;

	/**
	 * constructs an new node
	 */
	public TopopysicalNode(int nodeID){
		//lets initialize all private class attributes
		this.nodeID = nodeID;
		this.host = host.getHostIndexOfID(nodeID);
	}

	/**
	 * constructs an new node including world-coordinates
	 */
	public TopopysicalNode(int nodeID, TrafficHost host){
		//lets initialize all private class attributes
		this.nodeID = nodeID;
		this.host = host;
		//this.worldX = x;
		//this.worldY = y;
	}

	/**
	 * constructs an new node including world-coordinates and the nodeName
	 */
	

	/**
	 * delivers the nodes id
	 * @return just the nodeID
	 */
	public int getNodeID(){
		return nodeID;
	}

	/**
	 * delivers the name of the node
	 * @return name of the node
	 */
	public TrafficHost getHost(){
		return this.host;
	}

	/**
	 * returns the x coordinate of this network-node
	 * @return the x coordinate
	 */
}
