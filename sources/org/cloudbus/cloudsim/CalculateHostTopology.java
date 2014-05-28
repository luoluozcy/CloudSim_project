/*
 * Title:        CloudSim Toolkit
 * Description:  CloudSim (Cloud Simulation) Toolkit for Modeling and Simulation of Clouds
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2009-2010, The University of Melbourne, Australia
 */

package org.cloudbus.cloudsim;

import java.awt.List;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.OutputStream;
import org.cloudbus.cloudsim.network.DelayMatrix_Float;
import org.cloudbus.cloudsim.network.GraphReaderBrite;
import org.cloudbus.cloudsim.network.TopologicalGraph;
import org.cloudbus.cloudsim.network.TopologicalLink;
import org.cloudbus.cloudsim.network.TopologicalNode;
import org.cloudbus.cloudsim.network.TopopysicalGraph;
import org.cloudbus.cloudsim.network.TopopysicalLink;
import org.cloudbus.cloudsim.network.TopopysicalNode;


public class CalculateHostTopology {
	
	//protected static int nextIdx=0;
//	private static boolean networkEnabled = false;
	//protected static DelayMatrix_Float delayMatrix = null;
	//protected static double[][] DistanceMatrix = null;
	//protected static TopopysicalGraph graph = null;
	//protected static Map<Integer, Integer> map = null;

	private static void parseNodeString(String nodeLine,TopopysicalGraph graph){

		StringTokenizer tokenizer = new StringTokenizer(nodeLine);

		//number of node parameters to parse (counts at linestart)
		int parameters = 3;

		//first test to step to the next parsing-state (edges)

		//test against an empty line
		if(!tokenizer.hasMoreElements()){
			//Log.printLine("this line contains no tokens...");
			return;
		}

		//parse this string-line to read all node-parameters
		//NodeID, xpos, ypos, indegree, outdegree, ASid, type(router/AS)

		int src = 0;
		//String nodeLabel = "";
		int des = 0;
		float delay = 0;

		for(int actualParam = 0; tokenizer.hasMoreElements() && actualParam < parameters; actualParam++){
			String token = tokenizer.nextToken();
			switch(actualParam){
				case 0:	//Log.printLine("nodeID: "+token);
						//Log.printLine("nodeLabel: "+token);
						src = Integer.valueOf(token);
						//nodeLabel = Integer.toString(nodeID);
						break;

				case 1:	//Log.printLine("x-Pos: "+token);
						des = Integer.valueOf(token);
						break;

				case 2:	//Log.printLine("y-Pos: "+token);
						delay = Float.valueOf(token);
						break;
			}
		}

		//instanciate an new node-object with previous parsed parameters
		TopopysicalLink topoLink = new TopopysicalLink(src, des, delay);
		//DistanceMatrix[][]
		graph.addLink(topoLink);


	}//parseNodeString-END

	private static void GenerateTreeDistanceSet(String TreeFileName,TopopysicalGraph graph) throws IOException
	{
		File file=new File(TreeFileName);
		FileReader fr=new FileReader(file);
		BufferedReader br=new BufferedReader(fr);
		
		//TreeFileStream.open(TreeFileName.c_str());

		String TextLine;
		while((TextLine= br.readLine())!=null )
		{
			//istringstream StreamLine(TextLine);
			//int Src, Des, Distance;
			//StreamLine >> Src >> Des >> Distance;
			//TreeDistanceMatrix[Src][Des] = (double)Distance;
			parseNodeString(TextLine,graph);
			
		}
     // br.reset();
		br.close();
	}

	private static void GenerateVL2DistanceSet(String VL2FileName,TopopysicalGraph graph) throws IOException
	{
		File file=new File(VL2FileName);
		FileReader fr=new FileReader(file);
		BufferedReader br=new BufferedReader(fr);
		

		String TextLine;
		while((TextLine= br.readLine())!=null )
		{
			parseNodeString(TextLine,graph);
		}

		// br.reset();
			br.close();
	}

	private static void GenerateFatTreeDistanceSet(String FatTreeFileName,TopopysicalGraph graph) throws IOException
	{
		File file=new File(FatTreeFileName);
		FileReader fr=new FileReader(file);
		BufferedReader br=new BufferedReader(fr);
		String TextLine;
		while((TextLine= br.readLine())!=null )
		{
			parseNodeString(TextLine,graph);
		}

		// br.reset();
			br.close();
	}

	private static void GenerateBCubeDistanceSet(String BCubeFileName,TopopysicalGraph graph) throws IOException
	{
		File file=new File(BCubeFileName);
		FileReader fr=new FileReader(file);
		BufferedReader br=new BufferedReader(fr);
		String TextLine;
		while((TextLine= br.readLine())!=null )
		{
			parseNodeString(TextLine,graph);
		}

	//	br.reset();
		br.close();
	}

	public static void GeneratePMdistanceSet(String TopoType,TopopysicalGraph graph, String InfileName, int PMNum) throws IOException
	{
		if(PMNum == 0)
		{
			Log.printLine("the input PM number is 0, cannot allocate memory!");
			return;
		}

		//DistanceMatrix = new double [PMNum][PMNum];
		//for(int i=0; i<PMNum; i++)
		//{
			//DistanceMatrix[i] = new double[PMNum];
		//	memset(DistanceMatrix[i], 0, sizeof(double)*PMNum);
	//	}

		if( "TREE" == TopoType )
		{
			GenerateTreeDistanceSet(InfileName,graph);
			/*cout << "PMNum is: " << endl;
			for(int i=0; i<PMNum; i++)
			{
				for(int j=0; j<PMNum; j++)
				{
					cout << PMDistanceMatrix[i][j] << " ";
				}
				cout << endl;
			}*/
		}

		else if( "FTREE" == TopoType )
			GenerateFatTreeDistanceSet(InfileName, graph);
		else if( "CLOS" == TopoType )
			GenerateVL2DistanceSet(InfileName, graph);
		else if( "BCUBE" == TopoType )
			GenerateBCubeDistanceSet(InfileName, graph);
		else
			return;
	}
	
	public static void main(String[] args) throws IOException
	{   Log.printLine("HostTopo Start");
	TopopysicalGraph graph=new TopopysicalGraph();
	GenerateHostTopology.GenerateTopo("TREE",10);
	String DesFile = "tree" +10;
	GeneratePMdistanceSet("TREE", graph,DesFile, 10);
	Iterator<TopopysicalLink> iter = graph.getLinkIterator();
	while(iter.hasNext()){
		TopopysicalLink edge = iter.next();
		Log.printLine(edge.getSrcNodeID()+" "+edge.getDestNodeID()+" " +edge.getLinkDelay());
		}
	}
	
	
	}
