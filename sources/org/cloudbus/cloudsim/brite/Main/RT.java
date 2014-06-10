package org.cloudbus.cloudsim.brite.Main;

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
/*  Author:     Anukool Lakhina                                             */
/*              Alberto Medina                                              */
/*  Title:     BRITE: Boston university Representative Topology gEnerator   */
/*  Revision:  2.0         4/02/2001                                        */
/****************************************************************************/
//package GUI;


//import javax.swing.border.LineBorder;
import java.io.*;           

public final class RT{
    
	private String modelType;
	int numNode;
	String BwType;
	String NodePlaceType;
    String growthType;
    RT(String mt,int n,String bt,String nt){
		modelType=mt;
		numNode=n;
		BwType=bt;
		NodePlaceType=nt;	
	}
    public void WriteConf(BufferedWriter bw) {
	
	//String modelType = (String) ModelType.getSelectedItem();
	if (modelType.equals("WAXMAN")) 
	    WriteWaxmanConf(bw);
	else if (modelType.equals("BARABASI")) 
	    WriteBarabasiConf(bw);
    }


    private void WriteWaxmanConf(BufferedWriter bw) {
	/*TextField vals*/
	double alpha =  0.15;
	double beta =0.2;
	int m =  2;
	int HS =  1000;
	int LS = 100;
	int N =  numNode;
	double bwMin =  1;
	double bwMax = 1024;
	/*ComboBox vals*/
	//String nodePlacement = (String) npComboBox.getSelectedItem();
	int np = 1;
	if (NodePlaceType.equals("heavyTailed"))
	    np =2;
	//String growthType = (String) asGrowthType.getSelectedItem();
	int gt = 1;
	if (growthType.equals("GT_ALL"))
	   gt = 2;
	//String asBWDistStr = (String) asBWDist.getSelectedItem();
	int bwDist=1;  //i.e. CONSTANT
	if (BwType.equals("UNIFORM"))
	   bwDist=2;
	else if (BwType.equals("EXPONENTIAL"))
	    bwDist=3;
	else if (BwType.equals("HEAVYTAILED"))
	    bwDist=4;
	
	
	try {	
	    bw.write("BeginModel");	                
	    bw.newLine();
	    bw.write("\tName =  1\t\t #Router Waxman=2, AS Waxman =3");                
	    bw.newLine();
	    bw.write("\tN = "+N+"\t\t #Number of nodes in graph"); 	                
	    bw.newLine();
	    bw.write("\tHS = " + HS+"\t\t #Size of main plane (number of squares)");               
	    bw.newLine();
	    bw.write("\tLS = " + LS+"\t\t #Size of inner planes (number of squares)");              
	    bw.newLine();
	    bw.write("\tNodePlacement = " + np+"\t #Random = 1, Heavy Tailed = 2"); 	
	    bw.newLine();
	    bw.write("\tGrowthType = " + gt+"\t\t #Incremental = 1, All = 2"); 	        
	    bw.newLine();
	    bw.write("\tm = "+m+"\t\t\t #Number of neighboring node each new node connects to.");                   
	    bw.newLine();
	    bw.write("\talpha = " + alpha+"\t\t #Waxman Parameter"); 	
	    bw.newLine();
	    bw.write("\tbeta = " + beta+"\t\t #Waxman Parameter");           
	    bw.newLine();
	    bw.write("\tBWDist = " + bwDist+"\t\t #Constant = 1, Uniform =2, HeavyTailed = 3, Exponential =4");      
	    bw.newLine();
	    bw.write("\tBWMin = " + bwMin);         
	    bw.newLine();
	    bw.write("\tBWMax = " + bwMax);         
	    bw.newLine();
	    bw.write("EndModel"); bw.newLine();
	    
	}    
	catch (IOException e) {
	    System.out.println("[BRITE ERROR]: Could not create config file. " + e);
	    System.exit(0);
	}
	
	
    }
    

    private void WriteBarabasiConf(BufferedWriter bw) {
	/*TextField vals*/
    	double alpha =  0.15;
    	double beta =0.2;
    	int m =  2;
    	int HS =  1000;
    	int LS = 100;
    	int N =  numNode;
    	double bwMin =  1;
    	double bwMax = 1024;
    	/*ComboBox vals*/
    	//String nodePlacement = (String) npComboBox.getSelectedItem();
    	int np = 1;
    	if (NodePlaceType.equals("heavyTailed"))
    	    np =2;
    	//String growthType = (String) asGrowthType.getSelectedItem();
    	int gt = 1;
    	if (growthType.equals("GT_ALL"))
    	   gt = 2;
    	//String asBWDistStr = (String) asBWDist.getSelectedItem();
    	int bwDist=1;  //i.e. CONSTANT
    	if (BwType.equals("UNIFORM"))
    	   bwDist=2;
    	else if (BwType.equals("EXPONENTIAL"))
    	    bwDist=3;
    	else if (BwType.equals("HEAVYTAILED"))
    	    bwDist=4;
    	
	try {	
	    bw.write("BeginModel");	                
	    bw.newLine();
	    bw.write("\tName =  2\t\t #Router Barabasi=2, AS Barabasi =4");                
	    bw.newLine();
	    bw.write("\tN = "+N+"\t\t #Number of nodes in graph"); 	                
	    bw.newLine();
	    bw.write("\tHS = " + HS+"\t\t #Size of main plane (number of squares)");               
	    bw.newLine();
	    bw.write("\tLS = " + LS+"\t\t #Size of inner planes (number of squares)");              
	    bw.newLine();
	    bw.write("\tNodePlacement = " + np+"\t\t #Random = 1, Heavy Tailed = 2"); 	
	    bw.newLine();
	    bw.write("\tm = "+m+"\t\t\t #Number of neighboring node each new node connects to.");                   
	    bw.newLine();
	    bw.write("\tBWDist = " + bwDist+"\t\t #Constant = 1, Uniform =2, HeavyTailed = 3, Exponential =4");      
	    bw.newLine();
	    bw.write("\tBWMin = " + bwMin);         
	    bw.newLine();
	    bw.write("\tBWMax = " + bwMax);         
	    bw.newLine();
	    bw.write("EndModel"); bw.newLine();
	}    
	catch (IOException e) {
	    System.out.println("[BRITE ERROR]: Could not create config file. " + e);
	    System.exit(0);
	}
	
    }
  
}



