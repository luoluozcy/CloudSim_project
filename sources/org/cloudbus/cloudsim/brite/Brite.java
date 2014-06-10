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
import java.util.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;

import org.cloudbus.cloudsim.brite.Export.OmnetppExport;
import org.cloudbus.cloudsim.brite.Graph.Graph;
import org.cloudbus.cloudsim.brite.Graph.Node;
import org.cloudbus.cloudsim.brite.Import.BriteImport;
import org.cloudbus.cloudsim.brite.Import.ImportConstants;
import org.cloudbus.cloudsim.brite.Model.FileModel;
import org.cloudbus.cloudsim.brite.Model.ModelConstants;
import org.cloudbus.cloudsim.brite.Topology.Topology;
import org.cloudbus.cloudsim.brite.Util.Util;
import org.cloudbus.cloudsim.brite.Visualizer.BRITETopology;

import java.lang.Runtime;   //so that we can call BriteC++ or BriteJava executable in native format
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.io.*;           //to redirect stdout for runtime process (c++ or java)



public final class Brite extends JDialog implements ActionListener, Runnable
{

	public static void main(String args[]){

		Brite b = new Brite();
		b.init();
		b.setVisible(true);

	}//fim main

	//Como em ASPanel.java
	JFileChooser fc;
	String file = "";
	JLabel ASFileBeingReadLabel = new JLabel();
	boolean isReadFromFile = false;

	public void init()
	{
		getContentPane().setLayout(null);
		getContentPane().setBackground(new java.awt.Color(204,204,204));
		//Dimensoes da tela principal
		setSize(494,650);

		JLabel1.setText("Topology Type:");
		getContentPane().add(JLabel1);
		JLabel1.setForeground(java.awt.Color.black);
		JLabel1.setFont(new Font("SansSerif", Font.BOLD,   12));
		JLabel1.setBounds(36,12,156,22);
		getContentPane().add(TopologyType);
		TopologyType.setFont(new Font("SansSerif", Font.PLAIN, 12));
		TopologyType.setBounds(170,12,202,26);
		TopologyType.addActionListener(this);
		getContentPane().add(ePanel);

		/*BEGIN: run C++ or Java exe choice*/
		getContentPane().add(ExeChoicesComboBox);
		ExeChoicesComboBox.setFont(new Font("SansSerif", Font.PLAIN,   12));
		ExeChoicesComboBox.setBounds(220,474,110,21);
		/*END: C++ or Java exe choice*/

		/*BEGIN: Build Topology Button*/
		BuildTopology.setText("Build Topology");
		BuildTopology.setActionCommand("Build Topology");
		BuildTopology.setBorder(lineBorder1);
		getContentPane().add(BuildTopology);
		BuildTopology.setForeground(java.awt.Color.black);
		BuildTopology.setFont(new Font("SansSerif", Font.PLAIN, 12));
		BuildTopology.setBounds(348,474,108,21);
		BuildTopology.addActionListener(this);
		/*END: Build Topology Button*/

		/*BEGIN: Show Topology Button*/
		ShowTopology.setText("Show Topology");
		ShowTopology.setActionCommand("Show Topology");
		ShowTopology.setBorder(lineBorder1);
		getContentPane().add(ShowTopology);
		ShowTopology.setForeground(java.awt.Color.black);
		ShowTopology.setFont(new Font("SansSerif", Font.PLAIN, 12));
		ShowTopology.setBounds(348,500,106,21);
		ShowTopology.addActionListener(this);
		/*END: Show Topology Button*/

		/*BEGIN: Dijkstra BW Button*/
		DijkstraBW.setText("Dijkstra BW");
		DijkstraBW.setActionCommand("Dijkstra BW");
		DijkstraBW.setBorder(lineBorder1);
		getContentPane().add(DijkstraBW);
		DijkstraBW.setForeground(java.awt.Color.black);
		DijkstraBW.setFont(new Font("SansSerif", Font.PLAIN, 12));
		DijkstraBW.setBounds(348,526,106,21);
		DijkstraBW.addActionListener(this);
		/*END: Dijkstra BW Button*/

		/*BEGIN: ExportOmnet Button*/
		ExportOmnet.setText("Export Omnet");
		ExportOmnet.setActionCommand("Export Omnet");
		ExportOmnet.setBorder(lineBorder1);
		getContentPane().add(ExportOmnet);
		ExportOmnet.setForeground(java.awt.Color.black);
		ExportOmnet.setFont(new Font("SansSerif", Font.PLAIN, 12));
		ExportOmnet.setBounds(348,552,106,21);
		ExportOmnet.addActionListener(this);
		/*END: ExportOmnet Button*/		

		getContentPane().add(logo);
		logo.setBorder(null);
		logo.setBounds(389, 2, 67, 65);
		logo.addActionListener(this);

		getContentPane().add(JTabbedPane1);
		JTabbedPane1.setBackground(new java.awt.Color(153,153,153));
		JTabbedPane1.setBounds(24,48,432,315);
		JTabbedPane1.add(asPanel);
		JTabbedPane1.add(rtPanel);
		JTabbedPane1.add(tdPanel);
		JTabbedPane1.add(buPanel);
		JTabbedPane1.setTitleAt(0,"AS");
		JTabbedPane1.setTitleAt(1,"Router");
		JTabbedPane1.setTitleAt(2,"Top Down");
		JTabbedPane1.setTitleAt(3, "Bottom Up");
		JTabbedPane1.setSelectedIndex(0);
		JTabbedPane1.setSelectedComponent(asPanel);
		rtPanel.EnableComponents(false);
		rtDisabled=true;
		hDisabled=true;
		tdPanel.EnableComponents(false);
		buPanel.EnableComponents(false);


		HelpButton.setText("Help");
		HelpButton.setBorder(lineBorder1);
		getContentPane().add(HelpButton);
		HelpButton.setForeground(java.awt.Color.black);
		HelpButton.setFont(new Font("SansSerif", Font.PLAIN, 12));
		HelpButton.setBounds(24, 474, 50,21);
		HelpButton.addActionListener(this);

		LPIButton.setText("Linear Programming");
		LPIButton.setBorder(lineBorder1);
		getContentPane().add(LPIButton);
		LPIButton.setForeground(java.awt.Color.black);
		LPIButton.setFont(new Font("SansSerif", Font.PLAIN, 12));
		LPIButton.setBounds(24, 500, 150, 21);
		LPIButton.addActionListener(this);

		//getContentPane().add(statusDialog);
		//statusDialog.setSize(200, 200);
		//	statusDialog.setVisible(false);


		//create status window where output of executable will be written
		sd.setSize(400,200);
		sd.setVisible(false);

		aboutPanel.setSize(300,300);
		aboutPanel.setVisible(false);


		hPanel.setSize(550,500);
		hPanel.setVisible(false);
		//Boston University Representative Internet Topology Generator (BRITE)
		setTitle("REALCloudSim - BRITE Generator");
	}



	public void actionPerformed(ActionEvent e) { 
		if (e.getSource().equals(HelpButton)) {
			hPanel.setVisible(true);
			return;
		}
		if (e.getSource().equals(logo)) {
			aboutPanel.setVisible(true);
			return;
		}
		String level = (String)TopologyType.getSelectedItem();
		if (e.getSource().equals(TopologyType)) {
			level = (String)TopologyType.getSelectedItem();
			if (level.equals(AS_TOPOLOGY)){
				JTabbedPane1.setSelectedComponent(asPanel);
				rtPanel.EnableComponents(false);
				tdPanel.EnableComponents(false);
				buPanel.EnableComponents(false);
				asPanel.EnableComponents(true);

			}
			else if (level.equals(ROUTER_TOPOLOGY)){
				JTabbedPane1.setSelectedComponent(rtPanel);
				tdPanel.EnableComponents(false);
				buPanel.EnableComponents(false);
				asPanel.EnableComponents(false);
				rtPanel.EnableComponents(true);

			}
			else if (level.equals(TOPDOWN_TOPOLOGY)) {
				JTabbedPane1.setSelectedComponent(tdPanel);
				tdPanel.EnableComponents(true);
				buPanel.EnableComponents(false);
				asPanel.EnableComponents(true);
				asPanel.EnableBW(false);
				rtPanel.EnableComponents(true);
				rtPanel.EnableBW(false);
			}
			else if (level.equals(BOTTOMUP_TOPOLOGY)) {
				JTabbedPane1.setSelectedComponent(buPanel);
				buPanel.EnableComponents(true);
				asPanel.EnableComponents(false);
				tdPanel.EnableComponents(false);
				rtPanel.EnableComponents(true);

			}
		}

		else if (e.getSource().equals(BuildTopology)) {
			String file = ePanel.ExportLocation.getText();

			if (file.equals("") || file==null){
				JOptionPane.showMessageDialog(this, "Error:  Missing Export File", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			if (!ePanel.isBriteFormat() && !ePanel.isOtterFormat()) {
				JOptionPane.showMessageDialog(this, "Error: Must specify atleast one output format", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			sd.getTextArea().setText("");
			if (!sd.isVisible())
				sd.setVisible(true);
			sd.repaint();

			String args = " GUI_GEN.conf  "+file;
			//BuildTopology.setEnabled(false);

			MakeConfFile(level);


			runThread = new Thread(Brite.this);
			runThread.setPriority(Thread.MAX_PRIORITY);
			runThread.start();

			//runExecutable(args);

		} else if (e.getSource().equals(ShowTopology)) {
			//String file = ((String)ePanel.ExportLocation.getText()).trim();	
			String file = ePanel.ExportLocation.getText();
			Util.MSG("Arquivo: "+ file);

			if (file.equals("") || file==null){
				JOptionPane.showMessageDialog(this, "Error:  Missing BRITE File", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			if (!ePanel.isBriteFormat() && !ePanel.isOtterFormat()) {
				JOptionPane.showMessageDialog(this, "Error: Must specify at least one output format", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			sd.getTextArea().setText("");
			if (!sd.isVisible())
				sd.setVisible(true);
			sd.repaint();

			try
			{
				//try to open the file.
				BufferedReader fileBrite = new BufferedReader(new FileReader(file));
				//build the visualization
				BRITETopology b = new BRITETopology();
				String [] args = {file, "0"};
				b.main(args);
				//close the file
				fileBrite.close();
			}
			catch (Exception exc) {
				Util.MSG("Error! could not open file: " + file);
				Util.MSG(exc.getMessage());
			}//fim catch

		} else if (e.getSource().equals(ExportOmnet)){

			String file = ePanel.ExportLocation.getText();
			Util.MSG("Arquivo: "+ file);

			if (file.equals("") || file==null){
				JOptionPane.showMessageDialog(this, "Error:  Missing BRITE File", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			if (!ePanel.isBriteFormat() && !ePanel.isOtterFormat()) {
				JOptionPane.showMessageDialog(this, "Error: Must specify at least one output format", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			sd.getTextArea().setText("");
			if (!sd.isVisible())
				sd.setVisible(true);
			sd.repaint();


			//De OmnetExport.java
			try {
				String routeroras = "";			
				int format = ModelConstants.RT_FILE;
				if (routeroras.equalsIgnoreCase("as"))
					format = ModelConstants.AS_FILE;

				FileModel f = new FileModel(ImportConstants.BRITE_FORMAT, file, format, 0,0,0,0,0);

				Topology t = new Topology(f);
				OmnetppExport oe = new OmnetppExport(t, new File(file+".ned"));
				oe.export();
			} catch (Exception exc){
				JOptionPane.showMessageDialog(this, "Error exporting file:\n" + 
						exc.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				return;				
			}//fim catch


		} else if (e.getSource().equals(DijkstraBW)) {
			try {
				/*				fc = new JFileChooser();
				//fc.addChoosableFileFilter(TopologyFilter.nlanr);
				//fc.addChoosableFileFilter(TopologyFilter.gtitm);
				//fc.addChoosableFileFilter(TopologyFilter.gtts);
				fc.addChoosableFileFilter(TopologyFilter.brite);
				//fc.addChoosableFileFilter(TopologyFilter.inet);
				fc.addActionListener(this);
				//  fc.setAcceptAllFileFilterUsed(false);
				fc.setSize(fc.getPreferredSize());

				int ret = fc.showOpenDialog(this);
				File f = fc.getSelectedFile();
				file = f.getPath();
				ASFileBeingReadLabel.setText("Import from file: "+ f.getName());
				ASFileBeingReadLabel.setVisible(true);
				isReadFromFile = true;
				 */				
				String file = ePanel.ExportLocation.getText();
				Util.MSG("Arquivo: "+ file);

				if (file.equals("") || file==null){
					JOptionPane.showMessageDialog(this, "Error:  Missing BRITE File", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}

				if (!ePanel.isBriteFormat() && !ePanel.isOtterFormat()) {
					JOptionPane.showMessageDialog(this, "Error: Must specify at least one output format", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				sd.getTextArea().setText("");
				if (!sd.isVisible())
					sd.setVisible(true);
				sd.repaint();
				File f = new File(file);
				//try to open the file.
				BufferedReader fileBrite = new BufferedReader(new FileReader(file));
				//build the visualization
				BRITETopology b = new BRITETopology();
				String [] args = {file, "0"};
				b.main(args);
				//close the file
				fileBrite.close();

				//---De BriteImport.java
				//BriteImport closes the file after the reading
				BriteImport bi = new BriteImport(f, ModelConstants.AS_FILE);

				Graph g =  bi.parse();
				Topology t = new Topology(g);

				//check if our wonderful topology is connected
				Util.MSG("Checking for connectivity:");
				g = t.getGraph();
				boolean isConnected = (g.isConnected());
				if (isConnected)
					Util.MSG("\tConnected");
				else Util.MSG("\t***NOT*** Connected");

				//Calcula o Dijkstra para a largura de banda
				DijkstraBW dijkstra = new DijkstraBW();
				Node[] nodes = g.getNodesArray();
				//Exibe todos os nos
				Util.MSG("[Node](id)(x)(y)");
				for (int i=0; i<nodes.length; i++){
					Util.MSG("[" + i + "] " +
							"(id:"+nodes[i].getID()+")" +
							"(x:"+(int)nodes[i].getX()+")" +
							"(y:"+(int)nodes[i].getY()+")");
				}//fim for				

				Node src = nodes[0];
				// d[v] constains shortest distance from source to node v
				HashMap d = dijkstra.runDijkstra(g, src);

				//p[v] constains immediate predecessor of v in shortest path from source.
				HashMap p = dijkstra.getPred();				
				//System.out.println("Predecessor 2: " + ((Node)p.get(nodes[2])).getID());

				/*//Busca a lista a partir do nodo destino ate alcancar a origem
				int destino=2;
				int origem=0;
				int pre=destino;
				System.out.print("Lista de predecessores no caminho mais curto: ");
				System.out.print(pre + " ");
				while(origem!=destino){					
					pre = ((Node)p.get(nodes[destino])).getID();
					System.out.print(pre + " ");
					destino=pre;
				}//fim while
				 */

				//Exibe, para cada nodo, a lista de predecessores
				int destino=0;
				int origem=0;
				int pre=destino;
				//Guarda a lista de nodos do caminho mais curto de cada nodo
				String [] listaNodos = new String[nodes.length];
				int i=0;
				while(i<listaNodos.length){
					listaNodos[i]="";					
					i++;
				}//fim while

				//Ex.: listaNodos = [0 1]
				//                  [0 3 2]
				//                  [0 1 2 4]				                  
				for (i=0; i<nodes.length; i++){
					System.out.print("\nShortest Path to reach node[" + i + "] from node [0]: ");
					destino=i;
					origem=0;
					pre=destino;
					System.out.print(pre + " ");
					listaNodos[i] += pre + " ";
					while(origem!=destino){					
						pre = ((Node)p.get(nodes[destino])).getID();
						System.out.print(pre + " ");
						listaNodos[i] += pre+" ";
						destino=pre;
					}//fim while
				}//fim for
				System.out.println();
				
				//Exibe listaNodos com os menores caminhos
				/*i=0;
				while(i<listaNodos.length){
					System.out.print("["+listaNodos[i]+"]");					
					i++;
				}//fim while
				*/

				//Busca pelos nodos nao utilizados (para desliga-los)
				//
				//Guarda no indice do vetor se ele foi utilizado ou nao				
				String [] enableRouter = new String[nodes.length];
				//Inicializa o vetor
				i=0;
				while(i<enableRouter.length){
					enableRouter[i]="0";
					i++;
				}//fim while		
				
				StringTokenizer t1;
				int indice=0;
				i=0;
				//Nodos que quero verificar (nao quero todos, senao todos os routers serao ligados)
				while(i<(nodes.length)/2){ 
					t1 = new StringTokenizer(listaNodos[i]," ");
					//Para cada token (nodo) verifica se existe em outra lista de caminho mais curto
					while(t1.hasMoreTokens()){
						indice = Integer.parseInt(t1.nextToken());
						//System.out.println("["+indice+"]");
						enableRouter[indice]="1";
					}//fim while
					i++;
				}//fim while

				//Exibe o resultado
				System.out.println("Status dos routers (0=off, 1=on)");
				i=0;
				while(i<enableRouter.length){
					System.out.println("Router " + i + ": " + enableRouter[i]);
					i++;
				}//fim while
				
				
				//d[1]--10
				//d[2]--20
				//d[3]--5 ...
				for (i=1; i<nodes.length; i++){
					Util.MSG("Bandwidth cost to reach node[" + i + "] from node [0]: " + d.get(nodes[i]));
				}//fim for										
				
			} catch (Exception exc){
				Util.MSG("Excecao: " + exc.getMessage());
			}//fim catch

			/*			try
			{

              //Esse trecho tambem funciona

				//De acordo com BriteImport, nao preciso utilizar um modelo,
				//apenas um metodo parser
				//---De BriteImport.java
				BriteImport bi = new BriteImport(f, ModelConstants.AS_FILE);

				Graph g =  bi.parse();
				Topology t = new Topology(g);

				//check if our wonderful topology is connected
				Util.MSGN("Checking for connectivity:");
				g = t.getGraph();
				boolean isConnected = (g.isConnected());
				if (isConnected)
					Util.MSG("\tConnected");
				else Util.MSG("\t***NOT*** Connected");


/*				//Esse trecho comentado funciona,
			 *                mas o ideal eh que seja feito como em BriteImport
			 *                
				//try to open the file.
				BufferedReader fileBrite = new BufferedReader(new FileReader(file+".brite"));
				//Importa o arquivo e o insere em um graph				

				//Insere 
				RandomGenManager rgm = new RandomGenManager();
				rgm.parse("sources/org/cloudbus/cloudsim/brite/seed_file");

				//create our glorious model and give it a random gen manager
				Model m = ParseConfFile.Parse("sources/org/cloudbus/cloudsim/brite/conf_files/ASImport_BRITE.conf");
				m.setRandomGenManager(rgm);

				//now create our wonderful topology. ie call model.generate()
				Topology t = new Topology(m);

				//check if our wonderful topology is connected
				Util.MSGN("Checking for connectivity:");
				Graph g = t.getGraph();
				boolean isConnected = (g.isConnected());
				if (isConnected)
					Util.MSG("\tConnected");
				else Util.MSG("\t***NOT*** Connected");

				//Calcula o Dijkstra para a largura de banda
				DijkstraBW d = new DijkstraBW();
				//d.runDijkstra(g, src);

				//close the file
				fileBrite.close();

			}

			catch (Exception exc) {
				Util.MSG("Error! could not open file: " + file+".brite");
				Util.MSG(exc);
			}//fim catch
			 */			
		} /*else if (e.getSource().equals(DijkstraBW)) {
			try {
				/*				fc = new JFileChooser();
				//fc.addChoosableFileFilter(TopologyFilter.nlanr);
				//fc.addChoosableFileFilter(TopologyFilter.gtitm);
				//fc.addChoosableFileFilter(TopologyFilter.gtts);
				fc.addChoosableFileFilter(TopologyFilter.brite);
				//fc.addChoosableFileFilter(TopologyFilter.inet);
				fc.addActionListener(this);
				//  fc.setAcceptAllFileFilterUsed(false);
				fc.setSize(fc.getPreferredSize());

				int ret = fc.showOpenDialog(this);
				File f = fc.getSelectedFile();
				file = f.getPath();
				ASFileBeingReadLabel.setText("Import from file: "+ f.getName());
				ASFileBeingReadLabel.setVisible(true);
				isReadFromFile = true;
		 */				
		/*String file = ePanel.ExportLocation.getText();
				Util.MSG("Arquivo: "+ file);

				if (file.equals("") || file==null){
					JOptionPane.showMessageDialog(this, "Error:  Missing BRITE File", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}

				if (!ePanel.isBriteFormat() && !ePanel.isOtterFormat()) {
					JOptionPane.showMessageDialog(this, "Error: Must specify at least one output format", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				sd.getTextArea().setText("");
				if (!sd.isVisible())
					sd.setVisible(true);
				sd.repaint();
				File f = new File(file);
				//try to open the file.
				BufferedReader fileBrite = new BufferedReader(new FileReader(file));
				//build the visualization
				BRITETopology b = new BRITETopology();
				String [] args = {file, "0"};
				b.main(args);
				//close the file
				fileBrite.close();

				//---De BriteImport.java
				//BriteImport closes the file after the reading
				BriteImport bi = new BriteImport(f, ModelConstants.AS_FILE);

				Graph g =  bi.parse();
				Topology t = new Topology(g);

				//check if our wonderful topology is connected
				Util.MSG("Checking for connectivity:");
				g = t.getGraph();
				boolean isConnected = (g.isConnected());
				if (isConnected)
					Util.MSG("\tConnected");
				else Util.MSG("\t***NOT*** Connected");

				//Calcula o Dijkstra para a largura de banda
				DijkstraBW dijkstra = new DijkstraBW();
				Node[] nodes = g.getNodesArray();
				//Exibe todos os nos
				Util.MSG("[Node](id)(x)(y)");
				for (int i=0; i<nodes.length; i++){
					Util.MSG("[" + i + "] " +
							"(id:"+nodes[i].getID()+")" +
							"(x:"+(int)nodes[i].getX()+")" +
							"(y:"+(int)nodes[i].getY()+")");
				}//fim for				

				Node src = nodes[0];
				// d[v] constains shortest distance from source to node v
				HashMap d = dijkstra.runDijkstra(g, src);

				for (int i=1; i<nodes.length; i++){
					//d.get(1)
					//d.get(2)...
					Util.MSG("Bandwidth cost to reach node[" + i + "] from node [0]: " + d.get(nodes[i]));
				}//fim for										

			} catch (Exception exc){
				Util.MSG("Excecao: " + exc.getMessage());
			}//fim catch
		 */
		/*			try
			{

              //Esse trecho tambem funciona

				//De acordo com BriteImport, nao preciso utilizar um modelo,
				//apenas um metodo parser
				//---De BriteImport.java
				BriteImport bi = new BriteImport(f, ModelConstants.AS_FILE);

				Graph g =  bi.parse();
				Topology t = new Topology(g);

				//check if our wonderful topology is connected
				Util.MSGN("Checking for connectivity:");
				g = t.getGraph();
				boolean isConnected = (g.isConnected());
				if (isConnected)
					Util.MSG("\tConnected");
				else Util.MSG("\t***NOT*** Connected");


/*				//Esse trecho comentado funciona,
		 *                mas o ideal eh que seja feito como em BriteImport
		 *                
				//try to open the file.
				BufferedReader fileBrite = new BufferedReader(new FileReader(file+".brite"));
				//Importa o arquivo e o insere em um graph				

				//Insere 
				RandomGenManager rgm = new RandomGenManager();
				rgm.parse("sources/org/cloudbus/cloudsim/brite/seed_file");

				//create our glorious model and give it a random gen manager
				Model m = ParseConfFile.Parse("sources/org/cloudbus/cloudsim/brite/conf_files/ASImport_BRITE.conf");
				m.setRandomGenManager(rgm);

				//now create our wonderful topology. ie call model.generate()
				Topology t = new Topology(m);

				//check if our wonderful topology is connected
				Util.MSGN("Checking for connectivity:");
				Graph g = t.getGraph();
				boolean isConnected = (g.isConnected());
				if (isConnected)
					Util.MSG("\tConnected");
				else Util.MSG("\t***NOT*** Connected");

				//Calcula o Dijkstra para a largura de banda
				DijkstraBW d = new DijkstraBW();
				//d.runDijkstra(g, src);

				//close the file
				fileBrite.close();

			}

			catch (Exception exc) {
				Util.MSG("Error! could not open file: " + file+".brite");
				Util.MSG(exc);
			}//fim catch
		 */			
		//} /*else if (e.getSource().equals(LPIButton)) {
		/*			try {				
				A_PO_GA_NS2 lpi = new A_PO_GA_NS2();
			} catch (Exception exc){
				Util.MSG("Excecao: " + exc.getMessage());
			}//fim catch
		}*/
	}

	public void run() {
		String file = ((String)ePanel.ExportLocation.getText()).trim();
		String args = " GUI_GEN.conf " + file;

		runExecutable(args);

	}

	private void MakeConfFile(String topologyType) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File("GUI_GEN.conf")));
			bw.write("#This config file was generated by the GUI. ");
			bw.newLine();  bw.newLine();
			bw.write("BriteConfig");
			bw.newLine(); bw.newLine();
			if (topologyType.equals(TOPDOWN_TOPOLOGY)) {
				tdPanel.WriteConf(bw);
				bw.newLine();
				asPanel.WriteConf(bw);
				bw.newLine();
				rtPanel.WriteConf(bw);
			}
			else if (topologyType.equals(BOTTOMUP_TOPOLOGY)) {
				buPanel.WriteConf(bw);
				bw.newLine();
				rtPanel.WriteConf(bw);
				bw.newLine();
			}
			else if (topologyType.equals(AS_TOPOLOGY))
				asPanel.WriteConf(bw);
			else if (topologyType.equals(ROUTER_TOPOLOGY))
				rtPanel.WriteConf(bw);

			bw.newLine();
			bw.write("BeginOutput");
			bw.newLine();
			bw.write("\tBRITE = ");
			if (ePanel.isBriteFormat())
				bw.write("1 ");
			else bw.write("0 " );
			bw.write("\t #1=output in BRITE format, 0=do not output in BRITE format");
			bw.newLine();
			bw.write("\tOTTER = ");
			if (ePanel.isOtterFormat())
				bw.write("1 " );
			else bw.write("0 ");
			bw.write("\t #1=Enable visualization in otter, 0=no visualization");
			bw.newLine();
			bw.write("EndOutput");
			bw.newLine();
			bw.close();
		}
		catch (IOException e) { 
			Util.MSG("[BRITE ERROR]:  Cannot create config file. " + e);
			return;
		}

	}


	private void runExecutable(String args) {

		//Invoca outo objeto Brite.java de org.cloudbus.cloudsim.brite.Main
		//para exportar a topologia
		String cmdExe ="java -Xmx256M -classpath $CLASSPATH:.:classes/ org.cloudbus.cloudsim.brite.Main.Brite ";
		boolean runC = false;

		/*if ( ((String)ExeChoicesComboBox.getSelectedItem()).equals(CPPEXE)) {
			runC= true;
			cmdExe = "./C++/brite ";
		}
		 */
		Util.MSG(cmdExe);

		String runThis = cmdExe+ args;
		Runtime r= Runtime.getRuntime();
		try {

			if (runC)
				runThis +=" C++/seed_file"; /*C++ version requires a seed file*/
			else
				runThis+=" sources/org/cloudbus/cloudsim/brite/seed_file"; /*java version also requires a java seed*/

			Util.MSG("[MESSAGE]: GUI starting executable: "+runThis); 
			p = r.exec(runThis);
			InputStream in = p.getInputStream();
			BufferedReader brIn = new BufferedReader(new InputStreamReader(in));
			String line;

			JTextArea sdLog = sd.getTextArea();

			while ((line= brIn.readLine())!=null) {
				sdLog.append(line+"\n");
				Rectangle rect = sdLog.getVisibleRect();

				int a = sdLog.getScrollableBlockIncrement(rect, SwingConstants.VERTICAL, 1);
				rect.setLocation((int)rect.getX(), (int)rect.getY()+a);
				sdLog.scrollRectToVisible(rect);
				Util.MSG(line);
			}
			InputStream err = p.getErrorStream();
			BufferedReader brErr = new BufferedReader(new InputStreamReader(err));
			while ((line=brErr.readLine())!=null) {
				sdLog.append(line+"\n");
				Rectangle rect = sdLog.getVisibleRect();
				//sdLog.paintImmediately(sdLog.getVisibleRect());
				int a = sdLog.getScrollableUnitIncrement(rect, SwingConstants.VERTICAL, 1);
				sdLog.scrollRectToVisible(new Rectangle((int) rect.getX(), (int) rect.getY()+a, (int) rect.getWidth(), (int)rect.getHeight()));
				Util.MSG(line);

			}
			sdLog.paintImmediately(sdLog.getVisibleRect());
		}
		catch (Exception e) { 
			JOptionPane.showMessageDialog(this, "An error occured while trying to run executable\n"+e, "Error", JOptionPane.ERROR_MESSAGE);
			Util.MSG("[BRITE ERROR]: An error occured trying to run executable: " +e);

			BuildTopology.setEnabled(true);

			return;
		}

		BuildTopology.setEnabled(true);

	}


	public void processWindowEvent(WindowEvent e) {
		super.processWindowEvent(e);
		if (e.getID() == e.WINDOW_CLOSING){    
			sd.dispose();
			hPanel.dispose();
			//System.exit(0);
		}
	}


	String ROUTER_TOPOLOGY = "1 Level: ROUTER ONLY";
	String AS_TOPOLOGY = "1 Level: AS ONLY";
	String TOPDOWN_TOPOLOGY = "2 Level: TOP-DOWN";
	String BOTTOMUP_TOPOLOGY = "2 Level: BOTTOM-UP";
	String[] TopologyTypeData = {AS_TOPOLOGY, ROUTER_TOPOLOGY, TOPDOWN_TOPOLOGY, BOTTOMUP_TOPOLOGY};
	JComboBox TopologyType = new JComboBox(TopologyTypeData);

	String JAVAEXE = "Use Java Exe";
	//String CPPEXE = "Use C++ Exe";
	//String exeData[] = {CPPEXE, JAVAEXE};
	String exeData[] = {JAVAEXE};
	JComboBox ExeChoicesComboBox = new JComboBox(exeData);

	LineBorder lineBorder1 = new LineBorder(java.awt.Color.black);
	JLabel JLabel1 = new JLabel();

	JButton logo = new JButton(new ImageIcon("sources/org/cloudbus/cloudsim/brite/" + "logo_unicamp.jpg"));
	JButton BuildTopology = new JButton();
	JButton ShowTopology = new JButton();
	JButton DijkstraBW = new JButton();
	JButton ExportOmnet = new JButton();
	JButton HelpButton = new JButton();
	JButton LPIButton = new JButton();

	StatusDialog sd = new StatusDialog(this);

	private Thread runThread = null;
	public Process p=null;

	JTabbedPane JTabbedPane1 = new JTabbedPane();
	ExportPanel ePanel = new ExportPanel();
	HelpPanel hPanel = new HelpPanel(this);  
	AboutPanel aboutPanel = new AboutPanel();
	ASPanel asPanel = new ASPanel();
	RouterPanel rtPanel= new RouterPanel();
	TDPanel tdPanel = new TDPanel(this);
	BUPanel buPanel = new BUPanel(this);
	boolean rtDisabled=false;
	boolean asDisabled=false;
	boolean hDisabled=true;

}

final class ExportPanel  extends JPanel  implements ActionListener {

	EtchedBorder etchedBorder1 = new EtchedBorder();
	LineBorder lineBorder1 = new LineBorder(java.awt.Color.black);
	JLabel JLabel30 = new JLabel();
	JLabel JLabel31 = new JLabel();

	JCheckBox otterFormat, briteFormat;
	JLabel JLabel32 = new JLabel();
	JTextField ExportLocation = new JTextField("/home/lucio/r4.brite");
	JButton ExportLocationBrowse = new JButton();
	JFileChooser fc = new JFileChooser();

	ExportPanel() { this.init(); } 

	public void actionPerformed(ActionEvent e) 
	{ 
		if (e.getSource().equals(ExportLocationBrowse)) {
			try {
				fc.addChoosableFileFilter(TopologyFilter.brite);
				fc.showOpenDialog(this);			   
				ExportLocation.setText(fc.getSelectedFile().getPath());
			} catch(Exception exc){
				Util.MSG("Nenhum arquivo selecionado: " + exc.getMessage());				
			}//fim catch
		}
	}

	public boolean isBriteFormat() {
		return briteFormat.isSelected();
	}

	public boolean isOtterFormat() {
		return otterFormat.isSelected();
	}
	void init() {
		this.setBorder(etchedBorder1);
		this.setLayout(null);
		this.setBounds(24,370,432,96);
		JLabel30.setText("Export Topology");
		this.add(JLabel30);
		JLabel30.setForeground(java.awt.Color.black);
		JLabel30.setBounds(12,12,156,16);
		JLabel31.setText("File Format(s):");
		this.add(JLabel31);
		JLabel31.setForeground(java.awt.Color.black);
		JLabel31.setFont(new Font("SansSerif", Font.PLAIN, 12));
		JLabel31.setBounds(24,60,95,20);

		briteFormat = new JCheckBox("BRITE");
		briteFormat.setSelected(true);
		this.add(briteFormat);
		briteFormat.setFont(new Font("SansSerif", Font.PLAIN, 12));
		briteFormat.setBounds(132, 60, 72, 24);
		briteFormat.addActionListener(this);
		otterFormat = new JCheckBox("OTTER");
		this.add(otterFormat);
		otterFormat.setFont(new Font("SansSerif", Font.PLAIN, 12));
		otterFormat.setBounds(210,60,72,24);
		otterFormat.addActionListener(this);


		JLabel32.setText("Location:");
		this.add(JLabel32);
		JLabel32.setForeground(java.awt.Color.black);
		JLabel32.setFont(new Font("SansSerif", Font.PLAIN, 12));
		JLabel32.setBounds(24,36,96,20);
		ExportLocation.setBorder(lineBorder1);
		ExportLocation.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.TEXT_CURSOR));
		this.add(ExportLocation);
		ExportLocation.setBounds(132,36,156,20);
		ExportLocationBrowse.setText("Browse...");
		ExportLocationBrowse.addActionListener(this);
		this.add(ExportLocationBrowse);
		ExportLocationBrowse.setFont(new Font("SansSerif", Font.PLAIN, 12));
		ExportLocationBrowse.setBounds(300,36,96,19);

	}

}


final class AboutPanel extends JDialog implements ActionListener {
	JEditorPane editPane;
	LineBorder lineBorder1 = new LineBorder(java.awt.Color.black);
	JButton closeB = new JButton();
	JScrollPane scrollPane1;

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(closeB)) {
			setVisible(false);
		}
	}

	public void processWindowEvent(WindowEvent e) {
		super.processWindowEvent(e);
		if (e.getSource() == this && e.getID() == e.WINDOW_CLOSING) {
			setVisible(false);
		}
	}

	public AboutPanel() {
		super();
		super.dialogInit();
		setSize(300,300);
		setResizable(false);


		getContentPane().setLayout(null);
		getContentPane().setBackground(new java.awt.Color(204,204,204));

		getContentPane().add(closeB);
		closeB.setBounds(80, 260, 100, 21);
		closeB.setText("Close Window");
		closeB.setFont(new Font("SansSerif", Font.PLAIN, 10));
		closeB.setBorder(lineBorder1);
		closeB.addActionListener(this);
		closeB.setVisible(true);

		editPane = new JEditorPane();
		String s= null;
		try {
			s="file:"+System.getProperty("user.dir") + "/sources/org/cloudbus/cloudsim/brite/"+
			"about.html";

			editPane.setPage(new java.net.URL(s));
		}
		catch (Exception e) {
			Util.MSG("[BRITE ERROR] Could not read about file " + e);
		}
		editPane.setEditable(false);
		editPane.setBounds(10,10,280,240);
		scrollPane1 = new JScrollPane(editPane, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		getContentPane().add(scrollPane1);
		scrollPane1.setBounds(10, 10, 280, 240);

		setTitle("About BRITE");
		setSize(getPreferredSize());
	}

}




final class HelpPanel extends JDialog  implements ActionListener{
	JButton closeB = new JButton("Close Help Window");

	JScrollPane scrollPane1;
	JEditorPane editPane;
	LineBorder lineBorder1 = new LineBorder(java.awt.Color.black);
	Brite parent =null;

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(closeB)) {
			setVisible(false);
		}
	}

	public void processWindowEvent(WindowEvent e) {
		super.processWindowEvent(e);
		if (e.getSource() == this && e.getID() == e.WINDOW_CLOSING) {
			setVisible(false);
		}
	}

	public JScrollPane getScroll() { return scrollPane1; }
	public JButton getButton() { return closeB; }
	public HelpPanel(Brite parent) {
		super();
		super.dialogInit();
		setSize(500,500);
		setResizable(false);

		this.parent = parent; //we need this because sometimes we need to kill the process while its executing

		getContentPane().setLayout(null);
		getContentPane().setBackground(new java.awt.Color(204,204,204));

		getContentPane().add(closeB);
		closeB.setBounds(200, 470, 100, 21);
		closeB.setText("Close Window");
		closeB.setFont(new Font("SansSerif", Font.PLAIN, 10));
		closeB.setBorder(lineBorder1);
		closeB.addActionListener(this);
		closeB.setVisible(true);

		editPane = new JEditorPane();
		String s= null;
		try {
			s = "file:"+System.getProperty("user.dir") + "/sources/org/cloudbus/cloudsim/brite/"+
			"parameterhelp.html";
			editPane.setPage(new java.net.URL(s));
		}
		catch (Exception e) {
			Util.MSG("[BRITE ERROR] Could not read help file " + e);

		}
		editPane.setEditable(false);
		editPane.setBounds(10,10,480,450);
		scrollPane1 = new JScrollPane(editPane, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		getContentPane().add(scrollPane1);
		scrollPane1.setBounds(10, 10, 480, 450);

		setTitle("BRITE Help ");
		setSize(getPreferredSize());
	}


} 

final class StatusDialog extends JDialog implements ActionListener {
	JButton closeB = new JButton("Close Status Window");
	JButton cancelB = new JButton("Cancel Generation");
	JTextArea statusText = new JTextArea();
	JScrollPane scrollPane1;
	LineBorder lineBorder1 = new LineBorder(java.awt.Color.black);
	Brite parent =null;


	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(closeB)) {
			statusText.setText("");
			setVisible(false);
		}
		if (e.getSource().equals(cancelB)) {
			parent.p.destroy();
			statusText.append("*** Generation Cancelled by user. ***");

		}
	}

	public void processWindowEvent(WindowEvent e) {
		super.processWindowEvent(e);
		if (e.getSource() == this && e.getID() == e.WINDOW_CLOSING) {
			statusText.setText("");
			setVisible(false);
		}
	}

	public JTextArea getTextArea() { return statusText; }
	public JScrollPane getScroll() { return scrollPane1; }
	public JButton getButton() { return closeB; }
	public StatusDialog(Brite parent) {
		super();
		super.dialogInit();
		setSize(400,300);
		setResizable(false);

		this.parent = parent; //we need this because sometimes we need to kill the process while its executing

		getContentPane().setLayout(null);
		getContentPane().setBackground(new java.awt.Color(204,204,204));

		getContentPane().add(closeB);
		closeB.setBounds(200, 170, 100, 21);
		closeB.setText("Close Window");
		closeB.setFont(new Font("SansSerif", Font.PLAIN, 10));
		closeB.setBorder(lineBorder1);
		closeB.addActionListener(this);
		closeB.setVisible(true);

		getContentPane().add(cancelB);
		cancelB.setBounds(75,170, 100, 21);
		cancelB.setText("Cancel Generation");
		cancelB.setFont(new Font("SansSerif", Font.PLAIN, 10));
		cancelB.addActionListener(this);
		cancelB.setBorder(lineBorder1);
		cancelB.setVisible(true);


		statusText.setFont(new Font("SansSerif", Font.PLAIN, 10));
		statusText.setBounds(10,10,380,150);
		statusText.setLineWrap(true);
		statusText.setEditable(false);

		scrollPane1 = new JScrollPane(statusText, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		getContentPane().add(scrollPane1);
		scrollPane1.setBounds(10, 10, 380, 150);
		//	scrollPane1.getViewport().setScrollMode(JViewport.BLIT_SCROLL_MODE);

		setTitle("Status Window");
		setSize(getPreferredSize());
	}

}
