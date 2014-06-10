package org.cloudbus.cloudsim.brite.Visualizer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.StringTokenizer;

public class BRITETopologyPO {

	/**
	 * The number of nodes to be represented by vertices.
	 */
	private int numNodes = 0;

	/**
	 * The number of links to be represented by edges.
	 */
	private int numEdges = 0;

	/**
	 * Array of nodes.
	 */
	private Node nodes[] = null;

	/**
	 * Array of edges.
	 */
	private Edge edges[] = null;	

	public Edge[] getEdges(){
		return edges;
	}//fim getEdges
	
	public Node[] getNodes(){
		return nodes;
	}//fim getNodes
	
	public BRITETopologyPO(String filePath) {

		try
		{
			//try to open the file.
			//BufferedReader file = new BufferedReader(new FileReader("/usr/local/src/workspace/REALCloudSim-1.0/topology.brite"));
			BufferedReader file = new BufferedReader(new FileReader(filePath));
			//build the visualization
			//discard the first 3 lines
			for (int i=0; i<3; i++)
			{
				file.readLine();
			}

			//get the number of nodes
			StringTokenizer t1 = new StringTokenizer(file.readLine(), "( )");
			t1.nextToken();
			numNodes = Integer.parseInt(t1.nextToken());
			//System.out.println("numNodes: " + numNodes);
			nodes = new Node[numNodes];

			//Descricao dos campos
			file.readLine();
			//for the number of nodes
			for (int i=0; i<nodes.length; i++)
			{
				//construct each node
				//
				//Aqui recupera a posicao
				nodes[i] = new Node(file.readLine());
			}

			//discard the two blank lines
			file.readLine( );
			file.readLine( );

			//get the number of edges
			t1 = new StringTokenizer(file.readLine(), "( )");
			t1.nextToken();
			numEdges= Integer.parseInt(t1.nextToken());
			//System.out.println("numEdges: " + numEdges);
			edges = new Edge[numEdges];

			//Descricao dos campos
			file.readLine();
			//for the number of edges
			for (int i=0; i<edges.length; i++)
			{
				//construct each edge
				edges[i] = new Edge(file.readLine());
				//System.out.println("["+i+"][BW:"+edges[i].getBW()+"]");
				//System.out.println("["+i+"][Source:"+edges[i].getSource()+"]");
				//System.out.println("["+i+"][Destination:"+edges[i].getDestination()+"]");
			}

			//close the file
			file.close();
		}

		catch (Exception e)
		{
			e.printStackTrace();
		}
	}//fim construtor

	public static void main(String... args) {

		new BRITETopologyPO(args[0]);

	}//fim main
}