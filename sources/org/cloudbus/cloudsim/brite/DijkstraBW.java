package org.cloudbus.cloudsim.brite;
//-------------------------  Dijkstra Algorithm -----------------------------

//code below lifted mostly from CLR

import java.util.*;
import java.lang.*;


import org.cloudbus.cloudsim.brite.Graph.*;


public final class DijkstraBW {
	private HashMap d;  /** d[v] constains shortest distance from source to node v*/
	private HashMap p;  /** p[v] constains immediate predecessor of v in shortest path from source. */
	Node src;
	Graph g;
	Node[] nodes;
	ArrayList pQ; 
	ArrayList S;


	//------------ access functions ------
	public HashMap getPred() { return p; };
	public HashMap getD() { return d; };

	//Construtor
    public DijkstraBW(){
    	super();    	
    }//fim construtor
	
	
	public HashMap runDijkstra(Graph g,  Node src) {
		this.g = g;
		this.src = src;
		this.nodes = g.getNodesArray();
		InitializeSingleSource(g, src);
		S = new ArrayList(nodes.length);
		while (!pQ.isEmpty()) {
			Node u = (Node) pQ.remove(0);
			S.add(u);
			Node[] neighbors = g.getNeighborsOf(u);
			for (int j=0; j<neighbors.length; ++j) {				
				Edge e = g.getEdge(u, neighbors[j]);
				double w =1.0;
				try {
					//w = (double) e.getEuclideanDist();
					w = (double) e.getBW();
				}
				catch (Exception ex) {
					System.out.println(ex);
				}
				Relax(u, neighbors[j], w);
			}
		}
		return d;
	}

	private void InitializeSingleSource(Graph g, Node s) {
		this.src = src;
		this.g = g;
		d = new HashMap(nodes.length);
		p = new HashMap(nodes.length);
		pQ = new ArrayList(nodes.length);
		Double max = new Double(Double.MAX_VALUE);
		for (int i=0; i<nodes.length; ++i) {
			d.put(nodes[i], max);
			p.put(nodes[i], null);
			if (nodes[i]!=s) pQ.add(nodes[i]);
		}
		d.put(s, new Double(0.0));
		pQ.add(0, s);  //pQ is sorted by distance (changed to bandwidth)
	}


	private void Relax(Node u, Node v, double w) {
		double dv = ((Double) d.get(v)).doubleValue();
		double du = ((Double) d.get(u)).doubleValue();
		if (du!=Double.MAX_VALUE && dv > du + w)  {
			d.put(v, new Double(du+w));
			p.put(v, u);
			UpdatePQ(v, du+w); //ensure that PQ remains sorted
		}
	}



	//-------------------------- Breadth First Search ------------------------
	/**
     Bfs solves SSSP in O(V+E) time and so is faster than Dijkstra when it comes
     to unweighted (or when weight is measured by hop-counts) graphs.  Algorithm more
     or less follows CLR, page 470.
	 */
	public HashMap runBFS(Graph g, Node src) {
		this.g = g;
		this.nodes = g.getNodesArray();
		this.src = src;

		//some initializations:
		HashMap color = new HashMap();	/* 1 = White, 2 = Gray, 3=Black*/
		ArrayList Q = new ArrayList();
		for (int i=0; i<nodes.length; ++i) {
			Node u = nodes[i];
			color.put(u, new Integer(1));
			d.put(u, new Integer(Integer.MAX_VALUE));
			p.put(u, new Integer(-1));
		}
		color.put(src, new Integer(2));
		d.put(src, new Integer(0));
		p.put(src, new Integer(-1));
		Q.add(src);
		//now do bfs:
		while (Q.size()!=0) {
			Node  u = (Node) Q.get(0);
			Node[] adjU = g.getNeighborsOf(u);
			for (int j=0; j<adjU.length; ++j) {
				Node v = adjU[j];
				Integer colV = (Integer) color.get(v);
				if (colV.intValue() == 1)    {
					color.put(v, new Integer(2));
					int du = ( (Integer) d.get(u)).intValue();
					d.put(v, new Integer(du+1));
					p.put(v, u);
					Q.add(v);
				}
			}
			Q.remove(u);
			color.put(u, new Integer(3));
		}
		return d;
	}



	//---------------  Priority Queue Helper Functions  -----------------------

	private void UpdatePQ(Node v, double newWeight) {
		//first check if v is in PQ, if not don't worry about it.
		//** Note that the if v is in S then v is not in PQ.
		int vIndex;
		//its faster to check the smaller arraylist
		if (pQ.size() > S.size())  {  //Check S first since its smaller       
			vIndex = S.indexOf(v);
			if (vIndex!=-1) //S contains it, so pQ doesn't.  No update reqd.
				return;
		}
		vIndex = pQ.indexOf(v);
		if (vIndex == -1) return;
		pQ.remove(vIndex);
		int newIndex = binarySearch(newWeight, 0, pQ.size());
		pQ.add(newIndex, v);
	}

	private int binarySearch(double newWeight, int beginIndex, int endIndex) {
		while (beginIndex!=endIndex  && endIndex>=beginIndex) {
			int mid = (int) (beginIndex+endIndex)/2;
			double midWeight =  ( (Double)d.get((Node)pQ.get(mid))).doubleValue();
			if (midWeight < newWeight) 
				beginIndex = mid+1;
			else if (midWeight > newWeight) 
				endIndex = mid-1;
			else  return mid;  //found!
		}
		//boundary conditions :
		if (endIndex <0) return 0;
		if (endIndex > pQ.size()) return pQ.size();
		if (endIndex!=pQ.size()) {
			double endWeight = ((Double)d.get((Node)pQ.get(endIndex))).doubleValue();
			if ( newWeight>endWeight) 
				return endIndex+1;
		}
		return endIndex;

	} 

}
