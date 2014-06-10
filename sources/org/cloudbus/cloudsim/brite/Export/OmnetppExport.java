package org.cloudbus.cloudsim.brite.Export;

import java.io.*;
import java.util.*;

import org.cloudbus.cloudsim.brite.Topology.*;
import org.cloudbus.cloudsim.brite.Model.*;
import org.cloudbus.cloudsim.brite.Import.ImportConstants;
import org.cloudbus.cloudsim.brite.Util.*;

/**
 * Export filter for OMNeT++
 */
public class OmnetppExport {

	private Topology t;
	private BufferedWriter bw;
	private BufferedReader br;
	
	//private String arquivoINI = "/usr/local/inetCloud/examples/ethernet/cloudBio/realcloud.ini";

	public OmnetppExport(Topology t, File outFile) {
		this.t = t;
		try {
			bw = new BufferedWriter(new FileWriter(outFile));
		}
		catch (IOException e) {
			Util.ERR("Error creating BufferedWriter in OmnetppExport: " +e);
		}
	}	

	public void export() throws Exception {
		Util.MSG("Producing export file for OMNeT++ ");
		org.cloudbus.cloudsim.brite.Graph.Graph g = t.getGraph();

		org.cloudbus.cloudsim.brite.Graph.Node[] nodes = g.getNodesArray();

		Arrays.sort(nodes, org.cloudbus.cloudsim.brite.Graph.Node.IDcomparator);
		org.cloudbus.cloudsim.brite.Graph.Edge[] edges = g.getEdgesArray();
		//Arrays.sort(edges, Edge.SrcIDComparator);
		Arrays.sort(edges, org.cloudbus.cloudsim.brite.Graph.Edge.IDcomparator);

		int maxid=0;
		for (int i=0; i<nodes.length; i++)
		{
			int id = nodes[i].getID();
			if (id>maxid)
				maxid = id;
		}
		int in_lastused[] = new int[maxid+1];
		int out_lastused[] = new int[maxid+1];
		for (int i=0; i<maxid; i++)
		{
			in_lastused[i] = 0;
			out_lastused[i] = 0;
		}

		//Package
		bw.write("package inet.examples.ethernet.cloudBio;\n\n");
		
		//Imports
		bw.write("import inet.networklayer.autorouting.FlatNetworkConfigurator;\n");
		bw.write("import inet.nodes.inet.StandardHost;\n");
		bw.write("import ned.DatarateChannel;\n");
		bw.write("import inet.nodes.inet.Router;\n");		
		bw.write("import inet.nodes.ethernet.EtherSwitch;\n");
		
		bw.write("\n\n@display(\"bgb=1022,590\");\n");
		
		bw.write("//\n");
		bw.write("// OMNeT++ network description -- exported from BRITE topology\n");
		bw.write("// Generator Model Used:\n");
		StringTokenizer st = new StringTokenizer(t.getModel().toString(), "\n");
		while (st.hasMoreTokens())
		{
			bw.write("//   "+st.nextToken()+"\n");
		}
		bw.write("//\n");
		bw.write("\n\n");

		bw.write("//\n");
		bw.write("// Prototype for modules that can be used as node in a BRITENetwork\n");
		bw.write("//\n");
/*		bw.write("simple Router {\n");
		bw.write("  parameters:\n");
		bw.write("      @display(\"i=device/server_l\");\n");
		bw.write("  gates:\n");
		bw.write("      input in[];\n");
		bw.write("      output out[];\n");
		bw.write("}\n");
*/		
		bw.write("\n");

		bw.write("//\n");
		bw.write("// Topology exported from BRITE. Actual module type to be used as nodes\n");
		bw.write("// in the network should be supplied in the 'nodetype' parameter.\n");
		bw.write("//\n");
		bw.write("network realcloud {\n");
		bw.write("  submodules:\n");
		int x, y;
		for (int i=0; i<nodes.length; i++) {
			org.cloudbus.cloudsim.brite.Graph.Node n = nodes[i];
			org.cloudbus.cloudsim.brite.Graph.NodeConf nc = nodes[i].getNodeConf();
			x = nc.getX();
			y = nc.getY();
			
			//getID eh incrementado a cada nova topologia,
			//ou seja, se para 10 nodos foi gerada uma sequencia de 1 a 9, 
			//na proxima geracao o ID do primeiro nodo comeca em 10
			//
			//TODO: Uma solucao eh reiniciar o programa caso se deseje gerar
			//sequencias de IDs para os nos a partir do indice 0
			
			bw.write("    node"+n.getID()+": StandardHost {\n");
			bw.write("      @display(\"p="+x+","+y+";i=device/server_l\");\n } \n");
			
			//Para cada nodo cria um roteador, mesmo que ele nao seja utilizado
			bw.write("    router"+n.getID()+": Router {\n");
			bw.write("      @display(\"p="+x+","+y+"\");\n } \n");
			
		}//fim for

		//Insere o modulo para configuracao dinamica de IPs (rede flat)
		bw.write("\nconfigurator: FlatNetworkConfigurator {}\n\n");
		
		bw.write("  connections:\n");
		
		int srcID, destID;
		for (int i=0; i<edges.length; ++i) {
			org.cloudbus.cloudsim.brite.Graph.Edge e = edges[i];
			
			srcID = e.getSrc().getID();
			destID = e.getDst().getID();

			//int srcGateIndex = in_lastused[srcID]++;
			//int dstGateIndex = out_lastused[destID]++;
			
 			//Liga o no source ao roteador
			bw.write("    node" + srcID + ".pppg++ ");			
			bw.write("<--> { delay=" + e.getDelay() + "us; datarate=" + e.getBW() + "Mbps; } <-->");			
			bw.write("    router" + srcID + ".pppg++;\n");
			
			//Liga os roteadores dos dominios
			bw.write("    router" + srcID + ".pppg++ ");
			bw.write("<--> { delay=" + e.getDelay() + "us; datarate=" + e.getBW() + "Mbps; } <-->");
			bw.write("    router" + destID + ".pppg++;\n");
			
			//Liga o no destino ao roteador
			bw.write("    router" + destID + ".pppg++ ");
			bw.write("<--> { delay=" + e.getDelay() + "us; datarate=" + e.getBW() + "Mbps; } <-->");
			bw.write(" node" + destID + ".pppg++;\n");
			
		}//fim for
		
		bw.write("}\n");
		bw.write("\n");
		bw.close();//fecha o fluxo do arquivo
		
		Util.MSG("... DONE.");
	}
	
	//De: BriteExport
/*	public void export() throws Exception {
		Graph g = t.getGraph();
		try {

			bw.write("Topology: ( " + g.getNumNodes() + " Nodes, " + g.getNumEdges()+ " Edges )");
			bw.newLine();
			bw.write(t.getModel().toString());
			bw.newLine();
			bw.write("Nodes: ( "+g.getNumNodes()+" )");
			bw.newLine();

			//output nodes
			// ArrayList nodes = g.getNodesVector();

			Node[] nodes = g.getNodesArray();
			Arrays.sort(nodes, Node.IDcomparator);

			for (int i=0; i< nodes.length; ++i) {
				Node n =  nodes[i];
				int x = (int) ((NodeConf) n.getNodeConf()).getX();
				int y = (int)  ((NodeConf)n.getNodeConf()).getY();
				int specificNodeType=-1;
				int ASid = -1;
				int outdegree = n.getOutDegree();
				int indegree = n.getInDegree();
				int nodeID = n.getID();

				if (n.getNodeConf() instanceof RouterNodeConf) { 
					ASid = ((RouterNodeConf)n.getNodeConf()).getCorrAS();
					specificNodeType = ((RouterNodeConf)n.getNodeConf()).getType();
				}
				if (n.getNodeConf() instanceof ASNodeConf) {
					specificNodeType = ((ASNodeConf)n.getNodeConf()).getType();
					ASid = nodeID;
				}

				bw.write(nodeID + "\t" + x + "\t" +y+ "\t" + indegree+ "\t" +outdegree+"\t"+ASid);

				if (n.getNodeConf() instanceof RouterNodeConf) {
					if (specificNodeType == ModelConstants.RT_LEAF)
						bw.write("\tRT_LEAF");
					else if (specificNodeType == ModelConstants.RT_BORDER) 
						bw.write("\tRT_BORDER");
					else if (specificNodeType == ModelConstants.RT_STUB)
						bw.write("\tRT_STUB");
					else if (specificNodeType == ModelConstants.RT_BACKBONE) 
						bw.write("\tRT_BACKBONE");
					else if (specificNodeType == ModelConstants.NONE)
						bw.write("\tRT_NONE");
				}
				else if (n.getNodeConf() instanceof ASNodeConf) {
					if (specificNodeType == ModelConstants.AS_LEAF)
						bw.write("\tAS_LEAF");
					else if (specificNodeType == ModelConstants.AS_BORDER) 
						bw.write("\tAS_BORDER");
					else if (specificNodeType == ModelConstants.AS_STUB)
						bw.write("\tAS_STUB");
					else if (specificNodeType == ModelConstants.AS_BACKBONE) 
						bw.write("\tAS_BACKBONE");
					else if (specificNodeType == ModelConstants.NONE)
						bw.write("\tAS_NONE");
				}


				bw.newLine();
			}
			bw.newLine();
			bw.newLine();
			//output edges

			Edge[] edges = g.getEdgesArray();
			//    ArrayList edges = g.getEdgesVector();
			bw.write("Edges: ( "+edges.length+" )");
			bw.newLine();

			Arrays.sort(edges, Edge.IDcomparator);
			for (int i=0; i<edges.length; ++i) {
				Edge e = (Edge) edges[i];
				Node src = e.getSrc();
				Node dst = e.getDst();
				float dist = e.getEuclideanDist();
				float delay = 0; //dist/299792458; //divide by speed of light
				if (e.getEdgeConf() instanceof ASEdgeConf)
					//valor original -1
					//troquei para 100ms
					delay = 200;
				int asFrom= src.getID();
				int asTo = dst.getID();
				if (src.getNodeConf() instanceof RouterNodeConf)
					asFrom  =((RouterNodeConf) src.getNodeConf()).getCorrAS();
				if (dst.getNodeConf() instanceof RouterNodeConf)
					asTo  =((RouterNodeConf) dst.getNodeConf()).getCorrAS();

				bw.write(e.getID() + "\t" + src.getID() + "\t" + dst.getID());
				bw.write("\t"+ dist + "\t" +delay+ "\t" + e.getBW()+"*");
				bw.write("\t"+ asFrom + "\t" + asTo);


				if (e.getEdgeConf() instanceof ASEdgeConf) {
					int specificEdgeType = ((ASEdgeConf)e.getEdgeConf()).getType();
					if (specificEdgeType == ModelConstants.E_AS_STUB)
						bw.write("\tE_AS_STUB");
					else if (specificEdgeType == ModelConstants.E_AS_BORDER)
						bw.write("\tE_AS_BORDER");
					else if (specificEdgeType == ModelConstants.NONE)
						bw.write("\tE_AS_NONE");
					else //backbone
						bw.write("\tE_AS_BACKBONE_LINK");
				}
				else  //we have a router{
					int specificEdgeType = ((RouterEdgeConf)e.getEdgeConf()).getType();
					if (specificEdgeType == ModelConstants.E_RT_STUB)
						bw.write("\tE_RT_STUB");
					else if (specificEdgeType == ModelConstants.E_RT_BORDER)
						bw.write("\tE_RT_BORDER");
					else if (specificEdgeType == ModelConstants.NONE)
						bw.write("\tE_RT_NONE");
					else //backbone
						bw.write("\tE_RT_BACKBONE");
				}

				if (e.getDirection() == GraphConstants.DIRECTED) 
					bw.write("\tD");
				else bw.write("\tU");

				bw.newLine();

			}
			bw.close();
		}
		catch (Exception e) {
			System.out.println("[BRITE ERROR]: Error exporting to file. " + e);
			e.printStackTrace();
			System.exit(0);

		}
	}
*/	
	///
	
/*	public static void convert(String briteFile, int format) throws Exception {
		
		//--De ParseConfFile.java
		//int format = (int) ((Double)params.get("Format")).doubleValue();
		String briteFile = (String) params.get("File");
		
		int HS = (int) ((Double)params.get("HS")).doubleValue();
		int LS =  (int)  ((Double)params.get("LS")).doubleValue();
		int bwDist = (int)  ((Double)params.get("BWDist")).doubleValue();
		float bwMax =(float) ((Double)params.get("BWMax")).doubleValue();
		float bwMin = (float) ((Double)params.get("BWMin")).doubleValue();

		//public FileModel(
		//int fileFormat, 
		//String filename, 
		//int type //ASorRT, 
		//int HS, int LS, int BWDist, float BWMin, float BWMax)
		FileModel f = new FileModel(
				ImportConstants.BRITE_FORMAT, 
				briteFile,
				format,
				HS,
				LS,
				bwDist, bwMin,
				bwMax);
		if (f == null) 
			  Util.ERR("Could not create an imported file model from the configuration file. ");
			
		Topology t = new Topology(f);
		OmnetppExport oe = new OmnetppExport(t, new File(briteFile+"_omnetpp.ned"));
		oe.export();
	}
*/

	public static void main(String args[]) throws Exception {
		String briteFile = "/home/lucio/topologia.brite";
		String routeroras = "";
		
/*		try {
			briteFile = args[0];
			routeroras = args[1];
		}
		catch (Exception e) {
			Util.ERR("Usage:  java Export.OmnetppExport <brite-format-file> RT {| AS}");
		}
*/
		int format = ModelConstants.RT_FILE;
		if (routeroras.equalsIgnoreCase("as"))
			format = ModelConstants.AS_FILE;

		FileModel f = new FileModel(ImportConstants.BRITE_FORMAT, briteFile, format, 0,0,0,0,0);

		Topology t = new Topology(f);
		OmnetppExport oe = new OmnetppExport(t, new File(briteFile+".ned"));
		oe.export();
	}

}