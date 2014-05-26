/*
 * Title:        CloudSim Toolkit
 * Description:  CloudSim (Cloud Simulation) Toolkit for Modeling and Simulation of Clouds
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2009-2010, The University of Melbourne, Australia
 */

package org.cloudbus.cloudsim;


import java.io.IOException;

import java.util.Vector;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
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
public class GenerateHostTopology {

	// TopoType = "TREE", "FTREE", "VL2"(CLOS), "BCUBE"
	// generate the type of topology according to the input type and the num of servers
	public static void GenerateTopo( String TopoType, int ServerNum) throws IOException
	{
		int p0; // number of ports on first level switches connected to servers  
		int p1; // number of ports on second level switches connected to first level 

		int UseDefaultPorts = 1;
		//cout << "Please select how to generate number of ports on first level switches and on second level switches: 1 means use default values, 0 means input manually. " << endl;
		//cin >> UseDefaultPorts;
	//	if(UseDefaultPorts == 0)
	//	{
			//cout << "Please input the num of ports on first level switches connected to servers: better can be divided by 4" << endl;
		//	cin >> p0;
			//cout << "Please input the num of ports on second level switched connected to first level: better can be divided by 4" << endl;
		//	cin >> p1;
		//}
	//	else
		{
			// test shows that set p0 = 16 and p1 = 48 is feasible for large scale generation
			p0 = 16;
			p1 = 48;
		}

		// generate the topology according to input type
		if( "TREE" == TopoType )
		{
			String DesFile = "tree" + ServerNum;
			Log.printLine(DesFile);
			GenerateTree(DesFile, ServerNum, p0, p1);
			//File file=new File(DesFile);
			//FileReader fr=new FileReader(file);
			//BufferedReader br=new BufferedReader(fr);
			//String TextLine;
			//while((TextLine= br.readLine())!=null )
			//{
				//parseNodeString(TextLine,graph);
			//	Log.printLine(TextLine);
			//}
			
		}
		else if( "FTREE" == TopoType )
		{
			String DesFile = "ftree" + ServerNum;
			int k =  (int) Math.ceil( Math.pow( (double)(4 * ServerNum), (double)1/(double)3 ) );
			GenerateFatTree(DesFile, ServerNum, k);
		}
		else if( "CLOS" == TopoType )
		{
			String DesFile = "VL2" + ServerNum;
			GenerateVL2(DesFile, ServerNum, p0, p1);
		}
		else if( "BCUBE" == TopoType )
		{
			String DesFile = "bcube" + ServerNum;
			int best = -1;
			int bp0=0, bk=0, k;
			for(p0 = 4; p0 <= 8; p0++)
			{
				k = (int) Math.ceil( Math.log( (double)ServerNum ) / Math.log( (double)p0 )  - 1 );
				if( -1 == best || best > (int)Math.pow( (double)p0, (double)(k+1) ) - ServerNum )
				{
					best = (int)Math.pow( (double)p0, (double)(k+1) ) - ServerNum;
					bp0 = p0;
					bk = k;
				}
			}
			p0 = bp0;
			k = bk;

			GenerateBCube(DesFile, ServerNum, k, p0);
		}
		else
		{
			Log.printLine( "your input is invalid!! Please select input one type only in the fourth: ");
			Log.printLine( "TREE  FTREE CLOS BCUBE");
			return;
		}
		
	}

	// generate a tree topology
	// a tree topology that corresponds to the num of servers
	private static void GenerateTree(String DesFile, int ServerNum, int p0, int p1) throws IOException
	{
		
		  File f = new File(DesFile);
		  FileWriter fw = new FileWriter(f);
		  BufferedWriter bw = new BufferedWriter(fw);
		 // String str = "Hello World!!!";
		//  byte b[] = str.getBytes();//字符串转换成byte数组
		  //out.write(b);
		  //out.close();
		//static File file = new File(file);
		for(int i=0; i<ServerNum; i++)
		{
			for(int j=0; j<ServerNum; j++)
			{
				if( i == j )
					continue;
				bw.write(i+"\t"+j+"\t");
				//TreeFile << i << "\t" << j << "\t";
				if( (int)(i / p0) == (int)(j / p0) )
					bw.write("1\n");
				else if( (int)(i / p0 / p1) == (int)(j / p0 / p1) )
					bw.write("3\n");
				else
					bw.write("5\n");
			}
		}
		bw.flush();
		 bw.close();
		//out.close();
	}

	// generate a fat tree topology
	// a fat tree topology that corresponds to the num of servers
	private static void GenerateFatTree(String DesFile, int ServerNum, int k) throws IOException
	{
		 File f = new File(DesFile);
		  FileWriter fw = new FileWriter(f);
		  BufferedWriter bw = new BufferedWriter(fw);
		for(int i=0; i<ServerNum; i++)
		{
			for(int j=0; j<ServerNum; j++)
			{
				if( i == j )
					continue;
				bw.write(i+"\t"+j+"\t");
				if( 2 * i / k == 2 * j / k )
					bw.write("1\n");
				else if( 4 * i / k / k == 4 * j / k / k )
					bw.write("3\n");
				else
					bw.write("5\n");
			}
		}
		bw.flush();
		 bw.close();
	}

	// generate a VL2 topology
	// output : a VL2 topology file that corresponds to the num of Servers
	private static void GenerateVL2(String DesFile, int ServerNum, int p0, int p1) throws IOException 
	{
		File f = new File(DesFile);
		  FileWriter fw = new FileWriter(f);
		  BufferedWriter bw = new BufferedWriter(fw);
		for(int i=0; i<ServerNum; i++)
		{
			for(int j=0; j<ServerNum; j++)
			{
				if( i == j )
					continue;
				bw.write(i+"\t"+j+"\t");			
				if( (int)((double)i / (double)p0) == (int)((double)j / (double)p0) )
					bw.write("1\n");
				else if( (int)((double)i / (double)p0 / (double)p1) == (int)((double)j / (double)p0 /(double)p1) ) 
					bw.write("3\n");
				else
					bw.write("5\n");
			}
		}
		bw.flush();
		 bw.close();
	}

	// generate a BCube topology
	// output: a BCube topology file that corresponds to the num of Servers
	private static void GenerateBCube(String DesFile, int ServerNum, int k, int p0) throws IOException
	{
		File f = new File(DesFile);
		  FileWriter fw = new FileWriter(f);
		  BufferedWriter bw = new BufferedWriter(fw);
		for(int i=0; i<ServerNum; i++)
		{
			for(int j=0; j<ServerNum; j++)
			{
				if( i == j )
					continue;
				bw.write(i+"\t"+j+"\t");	
				
				Vector<Integer> I= new Vector<Integer>(k);
				Vector<Integer> J=new Vector<Integer>(k) ;

				GetAddr(i, k, p0, I);
				GetAddr(j, k, p0, J);

				int hamming = 0;
				for(int l=0; l <= k; l++)
				{
					if( I.elementAt(l)!= J.elementAt(l) )
						hamming++;
				}
				int c = 2*hamming - 1;
				bw.write( c + "\n");

				//delete [] I;
				//delete [] J;
			}
		}
		bw.flush();
		 bw.close();
	}

	// input: a, k, p0
	// output : A
	private static void GetAddr(int a, int k, int p0, Vector<Integer> A)
	{
		int s = 0;
		for(int l=k; l>=0; l--)
		{
			A.setElementAt((int) ( (a-s) / Math.pow((double)p0, (double)l)),l);
			s += A.elementAt(l) * Math.pow( (double)p0, (double)l );
			if(A.elementAt(l) >= p0 )
				return;
		}
		return;
	}
	public static void main(String[] args) throws IOException
	{   Log.printLine("HostTopo Start");
	  //   TopopysicalGraph graph=new TopopysicalGraph();
	    GenerateHostTopology.GenerateTopo("TREE",10);
	    File file=new File("tree10");
		FileReader fr=new FileReader(file);
		BufferedReader br=new BufferedReader(fr);
		String TextLine;
		while((TextLine= br.readLine())!=null )
		{
			//parseNodeString(TextLine,graph);
			Log.printLine(TextLine);
		}

	
		br.close();
	}
	//String DesFile = "tree" +GenerateHostTopology. itos(10);
	//GeneratePMdistanceSet("TREE", graph,DesFile, 10);
	//Iterator<TopopysicalLink> iter = graph.getLinkIterator();
	//while(iter.hasNext()){
	//	TopopysicalLink edge = iter.next();
	//	Log.printLine(edge.getSrcNodeID()+" "+edge.getDestNodeID()+" " +edge.getLinkDelay());
		//}
	
}

