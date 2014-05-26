/*
 * Title:        CloudSim Toolkit
 * Description:  CloudSim (Cloud Simulation) Toolkit for Modeling and Simulation of Clouds
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2009-2010, The University of Melbourne, Australia
 */

package org.cloudbus.cloudsim.network;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * This class represents an graph containing nodes and edges,
 * used for input with an network-layer
 *
 * Graphical-Output Restricions!
 * EdgeColors:	GraphicalProperties.getColorEdge
 * NodeColors:	GraphicalProperties.getColorNode
 *
 * @author		Thomas Hohnstein
 * @since		CloudSim Toolkit 1.0
 */
public class TopopysicalGraph {

	private List<TopopysicalLink> linkList = null;
	private List<TopopysicalNode> nodeList = null;

	/**
	 * just the constructor to create an empty graph-object
	 *
	 */
	public TopopysicalGraph(){
		linkList = new LinkedList<TopopysicalLink>();
		nodeList = new LinkedList<TopopysicalNode>();
	}
	/**
	 * adds an link between two topological nodes
	 * @param edge the topological link
	 */
	public void addLink(TopopysicalLink edge){
		linkList.add(edge);
	}
	/**
	 * adds an Topological Node to this graph
	 * @param node the topological node to add
	 */
	public void addNode(TopopysicalNode node){
		nodeList.add(node);
	}

	/**
	 * returns the number of nodes contained inside the topological-graph
	 * @return number of nodes
	 */
	public int getNumberOfNodes() {
		return nodeList.size();
	}

	/**
	 * returns the number of links contained inside the topological-graph
	 * @return number of links
	 */
	public int getNumberOfLinks(){
		return linkList.size();
	}

	/**
	 * return an iterator through all network-graph links
	 * @return the iterator throug all links
	 */
	public Iterator<TopopysicalLink> getLinkIterator() {
		return linkList.iterator();
	}

	/**
	 * returns an iterator through all network-graph nodes
	 * @return the iterator through all nodes
	 */
	public Iterator<TopopysicalNode> getNodeIterator(){
		return nodeList.iterator();
	}

	/**
	 * prints out all internal node and link information
	 */
	
	
}
