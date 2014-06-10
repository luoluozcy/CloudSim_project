/****************************************************************************/
/*                  Copyright 2001, Trustees of Boston University.          */
/*                               All Rights Reserved.                       */
/*                                                                          */
/* Permission to use, copy, or modify this software and its documentation   */
/* for educational and research purposes only and without fee is hereby     */
/* granted, provided that this copyright notice appear on all copies and    */
/* supporting documentation.  For any other uses of this software, in       */
/* original or modified form, including but not limited to distribution in  */
/* whole or in part, specific prior permission must be obtained from Boston */
/* University.  These programs shall not be used, rewritten, or adapted as  */
/* the basis of a commercial software or hardware product without first     */
/* obtaining appropriate licenses from Boston University.  Boston University*/
/* and the author(s) make no representations about the suitability of this  */
/* software for any purpose.  It is provided "as is" without express or     */
/* implied warranty.                                                        */
/*                                                                          */
/****************************************************************************/
/*                                                                          */
/*  Author:     Alberto Medina                                              */
/*              Anukool Lakhina                                             */
/*  Title:     BRITE: Boston university Representative Topology gEnerator   */
/*  Revision:  2.0         4/02/2001                                        */
/****************************************************************************/

package org.cloudbus.cloudsim.brite.Main;

import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.brite.Model.*;
import org.cloudbus.cloudsim.brite.Graph.*;
import org.cloudbus.cloudsim.brite.Export.*;
import org.cloudbus.cloudsim.brite.Topology.*;
import org.cloudbus.cloudsim.brite.Util.*;
import org.cloudbus.cloudsim.brite.Import.*;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.HashMap;

final public class Brite { 
     
	public static String runBrite(String topologyType,String modelType,int numNode,String BwType,String NodePlaceType) {
		String filename="";
		String outFile ="";
		String seedFile="";
		//MakeConfFile("AS","Waxman");
		/*String topologyType="AS";
		String modelType="WAXMAN";
		int numNode=100;
		String BwType="UNIFORM";
		String NodePlaceType="Random";
		*/
		/*get config file, get output file*/
		filename ="D:\\cloudsim-2.1.1\\sources\\org\\cloudbus\\cloudsim\\brite\\conf_files\\"+topologyType+modelType+numNode+BwType+NodePlaceType+".conf";
		MakeConfFile(filename,topologyType,modelType,numNode,BwType,NodePlaceType);
		try { 
		
			//filename ="D:\\cloudsim-2.1.1\\sources\\org\\cloudbus\\cloudsim\\brite\\conf_files\\"+topologyType+modelType+numNode+BwType+NodePlaceType+".conf";
			outFile = "D:\\cloudsim-2.1.1\\sources\\org\\cloudbus\\cloudsim\\brite\\"+topologyType+modelType+numNode+BwType+".brite";//args[1];
			seedFile = "D:\\cloudsim-2.1.1\\sources\\org\\cloudbus\\cloudsim\\brite\\seed_file";
		}
		catch (Exception e) { 
			Util.ERR("Usage:  java Main.Main config_file output_file seed_file");
			System.exit(0);
		}

		RandomGenManager rgm = new RandomGenManager();
		rgm.parse(seedFile);


		/*create our glorious model and give it a random gen manager*/
		Model m = ParseConfFile.Parse(filename);
		m.setRandomGenManager(rgm);

		/*now create our wonderful topology. ie call model.generate()*/
		Topology t = new Topology(m);

		/*check if our wonderful topology is connected*/
		Util.MSGN("Checking for connectivity:");
		Graph g = t.getGraph();
		boolean isConnected = (g.isConnected());
		if (isConnected)
			System.out.println("\tConnected");
		else System.out.println("\t***NOT*** Connected");


		/*export to brite format outfile*/
		HashSet exportFormats = ParseConfFile.ParseExportFormats();
		ParseConfFile.close();
		if (exportFormats.contains("BRITE")) {			
			Util.MSG("Exporting Topology in BRITE format to: " + outFile);
			BriteExport be = new BriteExport(t, new File(outFile));
			be.export();
		}
		/*export to otter format outfile*/
		if (exportFormats.contains("OTTER")) {
			Util.MSG("Exporting Topology in Otter Format to: " + outFile+".odf");
			OtterExport oe = new OtterExport(t, new File(outFile+".odf"));
			oe.export();
		}
		/*outputting seed file*/
		Util.MSG("Exporting random number seeds to seedfile");
		rgm.export("last_seed_file", seedFile);
        return outFile;

		/*we're done (and hopefully successfully)*/
		//Util.MSG("Topology Generation Complete.");
		
		//t.dumpToOutput();

	}
	
	 private static void MakeConfFile(String path,String topologyType,String modelType,int numNode,String BwType,String NodePlaceType) {
			try {   //if()
				//File f
				   //BufferedReader br=new BufferedReader(new FileReader(new File("D:\\cloudsim-2.1.1\\sources\\org\\cloudbus\\cloudsim\\brite\\conf_files\\"+topologyType+Model+".conf")));
			     File f=new File(path);
			     Log.printLine(f.length());
			     if(f.length()==0){
			    	// Log.printLine(f.length());
				   BufferedWriter bw = new BufferedWriter(new FileWriter(f));
			   //  if(bw.)
			       //  String line;
			      // while((line=br.readLine()) != null)
			      // {   if(line.contains("Name =")
			    	//	   String Name
			    	//   bw.write(line);
			    	//   bw.newLine();
			    //	}
			     //  br.close();
			       
			       
			       bw.write("#This config file was generated by the GUI. ");
			       bw.newLine();  bw.newLine();
			      bw.write("BriteConfig");
			       //bw.write(br.readLine());
			    bw.newLine(); bw.newLine();
			  //  if (topologyType.equals("topdown")) {
				//tdPanel.WriteConf(bw);
			    	
				//bw.newLine();
				//asPanel.WriteConf(bw);
				//bw.newLine();
				//rtPanel.WriteConf(bw);
			 //   }
			 //   else if (topologyType.equals("bottomUP")) {
			//	buPanel.WriteConf(bw);
			//	bw.newLine();
			//	rtPanel.WriteConf(bw);
				//bw.newLine();
			 //   }
			    if (topologyType.equals("AS")){
				 AS as=new AS( modelType, numNode, BwType, NodePlaceType);
			       as.WriteConf(bw);
			    }
			    else if (topologyType.equals("RT")){
				RT rt=new RT(modelType, numNode, BwType, NodePlaceType);
				rt.WriteConf(bw);
			    }
			
			    bw.newLine();
			    bw.write("BeginOutput");
			    bw.newLine();
			    bw.write("\tBRITE = ");
			   // if (ePanel.isBriteFormat())
				bw.write("1 ");
			   // else bw.write("0 " );
			    bw.write("\t #1=output in BRITE format, 0=do not output in BRITE format");
			    bw.newLine();
			    bw.write("\tOTTER = ");
			   // if (ePanel.isOtterFormat())
				bw.write("1 " );
			   // else bw.write("0 ");
			    bw.write("\t #1=Enable visualization in otter, 0=no visualization");
			    bw.newLine();
			    bw.write("EndOutput");
			    bw.newLine();
			   
			     
			    bw.close();
			    
			     }
			}
			catch (IOException e) { 
			    System.out.println("[BRITE ERROR]:  Cannot create config file. " + e);
			    return;
			}
			
				    
	}


}





