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
package org.cloudbus.cloudsim.brite;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import java.io.*;           
import javax.swing.filechooser.*;

import org.cloudbus.cloudsim.brite.Graph.Graph;
import org.cloudbus.cloudsim.brite.Import.BriteImport;
import org.cloudbus.cloudsim.brite.Main.ParseConfFile;
import org.cloudbus.cloudsim.brite.Model.Model;
import org.cloudbus.cloudsim.brite.Model.ModelConstants;
import org.cloudbus.cloudsim.brite.Topology.Topology;
import org.cloudbus.cloudsim.brite.Util.RandomGenManager;
import org.cloudbus.cloudsim.brite.Util.Util;

final class ASPanel extends JPanel implements ActionListener {
	public ASPanel() { super(); this.init(); }


	/************* Generate Config File Routines  ************************/
	public void WriteConf(BufferedWriter bw) {
		if (isReadFromFile) { 
			WriteFileConf(bw);
			return; 
		}
		String modelType = (String) ASModelType.getSelectedItem();
		if (modelType.equals(AS_WAXMAN)) 
			WriteWaxmanConf(bw);
		else if (modelType.equals(AS_BARABASI)) 
			WriteBarabasiConf(bw);
	}

	private double getNumFromTextField(JTextField c, double defVal) {
		if (!c.isEnabled())
			return -1;
		String nstr = c.getText();
		Double n=new Double(defVal);
		try {
			n = new Double(nstr);
		} 
		catch (Exception e) { 
			System.out.println("Exception:  textfield str = " + nstr);
		}
		return n.doubleValue();
	}



	private String getExt(String s) {
		String ext = null;

		int i = s.lastIndexOf('.');
		if (i>0 && i<s.length() -1)
			ext = s.substring(i+1);
		return ext;
	}


	private void WriteFileConf(BufferedWriter bw) {
		int HS = (int) getNumFromTextField(asHS, 1000);
		int LS = (int) getNumFromTextField(asLS, 100);
		double bwMin = getNumFromTextField(asBWMin, 1);
		double bwMax = getNumFromTextField(asBWMax, 1024);
		String bwDistStr = (String) asBWDist.getSelectedItem();
		int bwDist=1;  //initialize to constant
		if (bwDistStr.equals(BW_UNIFORM))
			bwDist=2;
		else if (bwDistStr.equals(BW_EXPONENTIAL))
			bwDist=3;
		else if (bwDistStr.equals(BW_HEAVYTAILED))
			bwDist=4;


		//determine format of input file
		int format=0;
		String extFormat = getExt(file);
		if (extFormat.equals("brite"))	    format = 1;
		else if (extFormat.equals("nlanr")) format = 3;
		else if (extFormat.equals("gtitm")) format= 2;
		else if (extFormat.equals("skitter")) format = 4;
		else if (extFormat.equals("gtts")) format = 5;
		else if (extFormat.equals("inet")) format=6;
		else {
			System.out.println("[ERROR]  : Input file extension: " + extFormat + " not a valid one (or not implemented).");
			System.exit(0);
		}


		try {	
			bw.write("BeginModel");	                
			bw.newLine();
			bw.write("\tName =  8\t\t #AS File = 8, Router File = 7");                
			bw.newLine();
			bw.write("\tFormat = " + format  + "\t\t #BRITE = 1, GT-ITM = 2, NLANR = 3, SKITTER = 4, GT-ITM(TS)=5 Inet = 6");
			bw.newLine();
			bw.write("\tFile = " + file);
			bw.newLine();
			bw.write("\tHS = " + HS+"\t\t #Size of main plane (number of squares)");               
			bw.newLine();
			bw.write("\tLS = " + LS+"\t\t #Size of inner planes (number of squares)");              
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

	private void WriteWaxmanConf(BufferedWriter bw) {
		/*TextField vals*/
		double alpha = getNumFromTextField(asAlpha, 0.15);
		double beta = getNumFromTextField(asBeta, 0.2);
		int m = (int) getNumFromTextField(asm, 2);
		int HS = (int) getNumFromTextField(asHS, 1000);
		int LS = (int) getNumFromTextField(asLS, 100);
		int N = (int) getNumFromTextField(asN, 1000);
		double bwMin = getNumFromTextField(asBWMin, 1.0);
		double bwMax = getNumFromTextField(asBWMax, 1024.0);
		/*ComboBox vals*/
		String nodePlacement = (String) asNodePlacement.getSelectedItem();
		int np = 1;
		if (nodePlacement.equals(NP_HEAVYTAILED))
			np =2;
		String growthType = (String) asGrowthType.getSelectedItem();
		int gt = 1;
		if (growthType.equals(GT_ALL))
			gt = 2;
		String asBWDistStr = (String) asBWDist.getSelectedItem();
		int bwDist=1;  //i.e. CONSTANT
		if (asBWDistStr.equals(BW_UNIFORM))
			bwDist=2;
		else if (asBWDistStr.equals(BW_EXPONENTIAL))
			bwDist=3;
		else if (asBWDistStr.equals(BW_HEAVYTAILED))
			bwDist=4;

		try {	
			bw.write("BeginModel");	                
			bw.newLine();
			bw.write("\tName =  3\t\t #Router Waxman = 1, AS Waxman = 3");                
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
		int m = (int) getNumFromTextField(asm, 2);
		int HS = (int) getNumFromTextField(asHS, 1000);
		int LS = (int) getNumFromTextField(asLS, 100);
		int N = (int) getNumFromTextField(asN, 1000);
		double bwMin = getNumFromTextField(asBWMin, 1.0);
		double bwMax = getNumFromTextField(asBWMax, 1024.0);
		/*ComboBox vals*/
		String nodePlacement = (String) asNodePlacement.getSelectedItem();
		int np = 1;
		if (nodePlacement.equals(NP_HEAVYTAILED))
			np =2;
		String asBWDistStr = (String) asBWDist.getSelectedItem();
		int bwDist=1;  //that is, bandwidth is constant
		if (asBWDistStr.equals(BW_UNIFORM))
			bwDist=2;
		else if (asBWDistStr.equals(BW_EXPONENTIAL))
			bwDist=3;
		else if (asBWDistStr.equals(BW_HEAVYTAILED))
			bwDist=4;

		try {	
			bw.write("BeginModel");	                
			bw.newLine();
			bw.write("\tName =  4\t\t #Router Barabasi=2, AS Barabasi =4");                
			bw.newLine();
			bw.write("\tN = "+N+"\t\t #Number of nodes in graph"); 	                
			bw.newLine();
			bw.write("\tHS = " + HS+"\t\t #Size of main plane (number of squares)");               
			bw.newLine();
			bw.write("\tLS = " + LS+"\t\t #Size of inner planes (number of squares)");              
			bw.newLine();
			bw.write("\tNodePlacement = " + np+"\t #Random = 1, Heavy Tailed = 2"); 	
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



	public void actionPerformed(ActionEvent e) {

		//Soh me da a caixa de dialogo aqui
		if (e.getSource().equals(ImportASTopology)){
			try {
				fc = new JFileChooser();
				fc.addChoosableFileFilter(TopologyFilter.nlanr);
				fc.addChoosableFileFilter(TopologyFilter.gtitm);
				fc.addChoosableFileFilter(TopologyFilter.gtts);
				fc.addChoosableFileFilter(TopologyFilter.brite);
				fc.addChoosableFileFilter(TopologyFilter.inet);
				fc.addActionListener(this);
				//  fc.setAcceptAllFileFilterUsed(false);
				fc.setSize(fc.getPreferredSize());

				int ret = fc.showOpenDialog(this);
				File f = fc.getSelectedFile();
				file = f.getPath();
				ASFileBeingReadLabel.setText("Import from file: "+ f.getName());
				ASFileBeingReadLabel.setVisible(true);
				isReadFromFile = true;
				
				//---De BriteImport.java
				BriteImport bi = new BriteImport(f, ModelConstants.AS_FILE);
				
				Graph g =  bi.parse();
				Topology t = new Topology(g);
				
				//check if our wonderful topology is connected
				Util.MSGN("Checking for connectivity:");
				g = t.getGraph();
				boolean isConnected = (g.isConnected());
				if (isConnected)
					System.out.println("\tConnected");
				else System.out.println("\t***NOT*** Connected");
				
				/////////////
				/*RandomGenManager rgm = new RandomGenManager();
				rgm.parse(seedFile);

				//create our glorious model and give it a random gen manager
				Model m = ParseConfFile.Parse(filename);
				m.setRandomGenManager(rgm);

				//now create our wonderful topology. ie call model.generate()
				Topology t = new Topology(m);

				//check if our wonderful topology is connected
				Util.MSGN("Checking for connectivity:");
				Graph g = t.getGraph();
				boolean isConnected = (g.isConnected());
				if (isConnected)
					System.out.println("\tConnected");
				else System.out.println("\t***NOT*** Connected");
				*/
				/////////////
				
			} catch (Exception exc){
				System.out.println("Nenhum arquivo selecionado " + exc.getMessage());
			}//fim catch
		}
		else if (e.getSource().equals(ASModelType)) {
			ASFileBeingReadLabel.setVisible(false);
			String model = (String) ASModelType.getSelectedItem();
			if (model.equals("Waxman")){
				asNodePlacement.setSelectedIndex(0);     /*RANDOM*/
				asGrowthType.setSelectedIndex(1); /*INCREMENTAL*/
				asConnLocal.setSelectedIndex(1); /*OFF*/
				asPrefConn.setSelectedIndex(0); /*NONE*/

				asAlpha.setEnabled(true);
				JLabel25.setEnabled(true);
				asAlpha.setText("0.15");
				JLabel26.setEnabled(true);
				asBeta.setEnabled(true);
				asBeta.setText("0.2");
				asm.setText("2"); 
				asGamma.setText("NA");
				JLabel27.setEnabled(false);
				asGamma.setEnabled(false);
			}
			else if (model.equals("Barabasi")) {
				asNodePlacement.setSelectedIndex(0);     /*RANDOM*/
				asGrowthType.setSelectedIndex(1); /*INCREMENTAL*/
				asConnLocal.setSelectedIndex(1); /*OFF*/
				asPrefConn.setSelectedIndex(0); /*NONE*/
				asAlpha.setText("NA");
				asBeta.setText("NA");
				asGamma.setText("NA");
				JLabel25.setEnabled(false);
				JLabel26.setEnabled(false);
				JLabel27.setEnabled(false);
				asAlpha.setEnabled(false); /*BARABASI does not take alpha,beta,gamma*/
				asBeta.setEnabled(false);
				asGamma.setEnabled(false);
				asm.setText("2");
			}
		}		
	}

	void EnableBW(boolean b) {
		BWLabel.setEnabled(b);
		asBWDist.setEnabled(b);
		BWMaxLabel.setEnabled(b);
		BWMinLabel.setEnabled(b);
		asBWMax.setEnabled(b);
		asBWMin.setEnabled(b);
	}



	void EnableComponents(boolean b) {
		for (int i=0; i<this.getComponentCount(); ++i) {
			Component c = this.getComponent(i);
			c.setEnabled(b);
		}
		//but keep ConnLocality and gamma disabled
		asGamma.setEnabled(false);
		JLabel27.setEnabled(false);
		asConnLocal.setEnabled(false);
		JLabel29.setEnabled(false);

	}	
	void init() {
		this.setBorder(etchedBorder1);
		this.setLayout(null);
		this.setBackground(new java.awt.Color(204,204,204));
		this.setBounds(2,24,427,261);
		this.setVisible(false);
		JLabel16.setText("AS Topology Parameters");
		this.add(JLabel16);
		JLabel16.setForeground(java.awt.Color.black);
		JLabel16.setBounds(12,12,228,16);
		JLabel17.setText("HS:");
		this.add(JLabel17);
		JLabel17.setForeground(java.awt.Color.black);
		JLabel17.setFont(new Font("SansSerif", Font.PLAIN, 12));
		JLabel17.setBounds(24,48,24,21);
		JLabel18.setText("LS:");
		this.add(JLabel18);
		JLabel18.setForeground(java.awt.Color.black);
		JLabel18.setFont(new Font("SansSerif", Font.PLAIN, 12));              
		JLabel18.setBounds(24,72,24,21);
		JLabel19.setText("N:");
		this.add(JLabel19);
		JLabel19.setForeground(java.awt.Color.black);
		JLabel19.setFont(new Font("SansSerif", Font.PLAIN, 12));              
		JLabel19.setBounds(156,48,36,21);
		JLabel20.setText("Model:");
		this.add(JLabel20);
		JLabel20.setForeground(java.awt.Color.black);
		JLabel20.setFont(new Font("SansSerif", Font.PLAIN, 12)); 
		JLabel20.setBounds(156,72,48,21);
		JLabel21.setText("Model Specific Parameters");
		this.add(JLabel21);
		JLabel21.setForeground(java.awt.Color.black);
		JLabel21.setFont(new Font("SansSerif", Font.PLAIN, 12)); 
		JLabel21.setBounds(12,108,204,16);
		JLabel22.setText("Node Placement:");
		this.add(JLabel22);
		JLabel22.setForeground(java.awt.Color.black);
		JLabel22.setFont(new Font("SansSerif", Font.PLAIN, 12)); 
		JLabel22.setBounds(24,132,108,21);
		JLabel23.setText("Growth Type:");
		this.add(JLabel23);
		JLabel23.setForeground(java.awt.Color.black);
		JLabel23.setFont(new Font("SansSerif", Font.PLAIN, 12)); 
		JLabel23.setBounds(24,156,108,21);
		JLabel24.setText("Pref. Conn:");
		this.add(JLabel24);
		JLabel24.setForeground(java.awt.Color.black);
		JLabel24.setFont(new Font("SansSerif", Font.PLAIN, 12)); 
		JLabel24.setBounds(24,180,108,21);
		JLabel25.setText("alpha:");
		this.add(JLabel25);
		JLabel25.setForeground(java.awt.Color.black);
		JLabel25.setFont(new Font("SansSerif", Font.PLAIN, 12)); 
		JLabel25.setBounds(288,132,55,21);
		JLabel26.setText("beta:");
		this.add(JLabel26);
		JLabel26.setForeground(java.awt.Color.black);
		JLabel26.setFont(new Font("SansSerif", Font.PLAIN, 12)); 
		JLabel26.setBounds(288,156,55,21);
		JLabel27.setText("gamma:");
		this.add(JLabel27);
		JLabel27.setForeground(java.awt.Color.black);
		JLabel27.setFont(new Font("SansSerif", Font.PLAIN, 12));
		JLabel27.setBounds(288,180,55,21);
		JLabel28.setText("m:");
		this.add(JLabel28);
		JLabel28.setForeground(java.awt.Color.black);
		JLabel28.setFont(new Font("SansSerif", Font.PLAIN, 12));
		JLabel28.setBounds(288,204,55,21);
		JLabel29.setText("Conn. Locality:");
		this.add(JLabel29);
		JLabel29.setForeground(java.awt.Color.black);
		JLabel29.setFont(new Font("SansSerif", Font.PLAIN, 12));
		JLabel29.setBounds(24,204,108,21);
		this.add(asNodePlacement);
		asNodePlacement.setFont(new Font("SansSerif", Font.PLAIN, 12));
		asNodePlacement.setBounds(144,132,120,24);
		this.add(asGrowthType);
		asGrowthType.setFont(new Font("SansSerif", Font.PLAIN, 12));
		asGrowthType.setBounds(144,156,120,24);
		this.add(asPrefConn);
		asPrefConn.setFont(new Font("SansSerif", Font.PLAIN, 12));
		asPrefConn.setBounds(144,180,120,24);
		this.add(asConnLocal);
		asConnLocal.setFont(new Font("SansSerif", Font.PLAIN, 12));
		asConnLocal.setBounds(144,204,120,24);
		asAlpha.setBorder(lineBorder1);
		asAlpha.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.TEXT_CURSOR));
		this.add(asAlpha);
		asAlpha.setBounds(360,132,46,18);
		asBeta.setBorder(lineBorder1);
		asBeta.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.TEXT_CURSOR));
		this.add(asBeta);
		asBeta.setBounds(360,156,46,18);
		asGamma.setBorder(lineBorder1);
		asGamma.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.TEXT_CURSOR));
		this.add(asGamma);
		asGamma.setBounds(360,180,46,18);
		asm.setBorder(lineBorder1);
		asm.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.TEXT_CURSOR));
		this.add(asm);
		asm.setBounds(360,204,46,18);
		asHS.setBorder(lineBorder1);
		asHS.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.TEXT_CURSOR));
		this.add(asHS);
		asHS.setBounds(60,48,46,18);
		asLS.setBorder(lineBorder1);
		asLS.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.TEXT_CURSOR));
		this.add(asLS);
		asLS.setBounds(60,72,46,18);
		asN.setBorder(lineBorder1);
		asN.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.TEXT_CURSOR));
		this.add(asN);
		asN.setBounds(204,48,50,18);
		this.add(ASModelType);
		ASModelType.setFont(new Font("SansSerif", Font.PLAIN, 12));
		ASModelType.setBounds(204,72,108,24);
		ASModelType.addActionListener(this);
		ImportASTopology.setHorizontalTextPosition(SwingConstants.RIGHT);
		ImportASTopology.setHorizontalAlignment(SwingConstants.RIGHT);
		ImportASTopology.setText("Import...");
		this.add(ImportASTopology);
		ImportASTopology.setFont(new Font("SansSerif", Font.PLAIN, 12));
		ImportASTopology.setBounds(298,12,122,19);
		ImportASTopology.addActionListener(this);

		this.add(ASFileBeingReadLabel);
		ASFileBeingReadLabel.setForeground(java.awt.Color.black);
		ASFileBeingReadLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
		ASFileBeingReadLabel.setBounds(10,257,396,17);
		ASFileBeingReadLabel.setVisible(false);

		//bandwidth stuff
		BWLabel.setText("Bandwidth Distr:");
		this.add(BWLabel);
		BWLabel.setForeground(java.awt.Color.black);
		BWLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
		BWLabel.setBounds(24, 228, 108, 21);
		this.add(asBWDist);
		asBWDist.setFont(new Font("SansSerif", Font.PLAIN, 12));
		asBWDist.setBounds(144,228,120,24);
		BWMaxLabel.setText("Max BW:");
		this.add(BWMaxLabel);
		BWMaxLabel.setForeground(java.awt.Color.black);
		BWMaxLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
		BWMaxLabel.setBounds(288,228, 55, 21);
		BWMinLabel.setText("Min BW:");
		this.add(BWMinLabel);
		BWMinLabel.setForeground(java.awt.Color.black);
		BWMinLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
		BWMinLabel.setBounds(288,251,  55, 21);
		asBWMax.setBorder(lineBorder1);
		asBWMax.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.TEXT_CURSOR));
		this.add(asBWMax);
		asBWMax.setBounds(360,228,46,18);
		asBWMin.setBorder(lineBorder1);
		asBWMin.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.TEXT_CURSOR));
		this.add(asBWMin);
		asBWMin.setBounds(360,252,46,18);

		/*for Waxman - default model*/
		asNodePlacement.setSelectedIndex(0);     /*RANDOM*/
		asGrowthType.setSelectedIndex(1); /*INCREMENTAL*/
		asConnLocal.setSelectedIndex(0); /*OFF*/
		asPrefConn.setSelectedIndex(0); /*NONE*/
		asAlpha.setText("0.15");
		asBeta.setText("0.2");
		asm.setText("2"); 
		asGamma.setText("NA");
		JLabel27.setEnabled(false);
		asGamma.setEnabled(false);
		asHS.setText("1000");
		asLS.setText("100");
		asN.setText("10000");
		asConnLocal.setEnabled(false);
		JLabel29.setEnabled(false);
		asBWMin.setText("10");
		asBWMax.setText("1024");


	}

	String GT_ALL = "All";
	String GT_INCREMENTAL = "Incremental";

	String NP_RANDOM = "Random";
	String NP_HEAVYTAILED = "Heavy Tailed" ;

	String AS_WAXMAN = "Waxman";
	String AS_BARABASI = "Barabasi";

	String BW_CONSTANT = "Constant";
	String BW_UNIFORM = "Uniform";
	String BW_EXPONENTIAL = "Exponential";
	String BW_HEAVYTAILED = "Heavy Tailed";

	String[] npData = {NP_RANDOM, NP_HEAVYTAILED};
	String[] gtData = {GT_ALL, GT_INCREMENTAL};
	String[] pcData = {"None", "On"};
	String[] clData = {"Off", "On" };
	String[] modelTypeData = {AS_WAXMAN, AS_BARABASI};
	String[] bwData = {BW_CONSTANT, BW_UNIFORM, BW_EXPONENTIAL, BW_HEAVYTAILED};

	JLabel BWLabel = new JLabel();
	JComboBox asBWDist = new JComboBox(bwData);
	JLabel BWMaxLabel = new JLabel();
	JLabel BWMinLabel = new JLabel();
	JTextField asBWMax = new JTextField();
	JTextField asBWMin = new JTextField();
	JLabel JLabel16 = new JLabel();
	JLabel JLabel17 = new JLabel();
	JLabel JLabel18 = new JLabel();
	JLabel JLabel19 = new JLabel();
	JLabel JLabel20 = new JLabel();
	JLabel JLabel21 = new JLabel();
	JLabel JLabel22 = new JLabel();
	JLabel JLabel23 = new JLabel();
	JLabel JLabel24 = new JLabel();
	JLabel JLabel25 = new JLabel();
	JLabel JLabel26 = new JLabel();
	JLabel JLabel27 = new JLabel();
	JLabel JLabel28 = new JLabel();
	JLabel JLabel29 = new JLabel();
	JComboBox asNodePlacement = new JComboBox(npData);
	JComboBox asGrowthType = new JComboBox(gtData);
	JComboBox asPrefConn = new JComboBox(pcData);
	JComboBox asConnLocal = new JComboBox(clData);
	JTextField asAlpha = new JTextField();
	JTextField asBeta = new JTextField();
	JTextField asGamma = new JTextField();
	JTextField asm = new JTextField();
	JComboBox ASModelType = new JComboBox(modelTypeData);
	JButton ImportASTopology = new JButton();
	JLabel ASFileBeingReadLabel = new JLabel();
	JFileChooser fc;
	String file = "";
	boolean isReadFromFile = false;
	JTextField asHS = new JTextField();
	JTextField asLS = new JTextField();
	JTextField asN = new JTextField();
	LineBorder lineBorder1 = new LineBorder(java.awt.Color.black);
	EtchedBorder etchedBorder1 = new EtchedBorder();

}





