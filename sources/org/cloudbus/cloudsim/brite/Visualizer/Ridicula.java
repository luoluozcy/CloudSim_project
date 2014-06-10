package org.cloudbus.cloudsim.brite.Visualizer;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.StringTokenizer;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Ridicula extends JFrame {
	
	private JSlider diameter;
	private OvalPanel myPanel;
	
	public Ridicula(){
		
		myPanel = new OvalPanel();
		
		diameter = new JSlider( SwingConstants.HORIZONTAL, 200, 10 );
		diameter.setMajorTickSpacing(10);
		diameter.setPaintTicks(true);
		diameter.addChangeListener(
				new ChangeListener(){
			public void stateChanged( ChangeEvent e ){
				myPanel.setDiameter(diameter.getValue());
			}			
		});
		
		Container c = getContentPane();
		c.add(diameter, BorderLayout.SOUTH);
		c.add(myPanel, BorderLayout.CENTER);
		
		setSize(220,270);
		setVisible(true);
				
	}
	
	public static void main(String... args){
		
		new Ridicula();
		
	}

	public class OvalPanel extends JPanel {
		
		private int diameter=10;
		

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
		
		public void paintComponent(Graphics g){
			
			super.paintComponent(g);
			g.fillOval(10,10,diameter,diameter);
			
			try
			{
				//try to open the file.
				BufferedReader file = new BufferedReader(new FileReader("/usr/local/src/workspace/REALCloudSim-1.0/topology.brite"));
				//build the visualization
				//BRITETopology t = new BRITETopology(file);

				//BufferedImage bi = new BufferedImage(500, 500, BufferedImage.TYPE_INT_ARGB);
				//Graphics2D g = bi.createGraphics();
				//g.setBackground(Color.white);
				//g.clearRect(0, 0, width, 500);

				//discard the first 3 lines
				for (int i=0; i<3; i++)
				{
					file.readLine();
				}

				//get the number of nodes
				StringTokenizer t1 = new StringTokenizer(file.readLine(), "( )");
				t1.nextToken();
				numNodes = Integer.parseInt(t1.nextToken());
				System.out.println("numNodes: " + numNodes);
				nodes = new Node[numNodes];

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
				System.out.println("numEdges: " + numEdges);
				edges = new Edge[numEdges];

				//for the number of edges
				for (int i=0; i<edges.length; i++)
				{
					//construct each edge
					edges[i] = new Edge(file.readLine());
				}


				//draw the edges
				for (int i = 0;i < edges.length;i++) {
					edges[i].paint(g, nodes);
				}//fim for

				//draw the nodes
				for (int i = 0;i < nodes.length;i++)
				{
					nodes[i].paint(g);
					g.drawString("(id:"+nodes[i].getID()+")", (int)nodes[i].getX(), (int)nodes[i].getY());
					//g.drawString("(id:"+nodes[i].getID()+")" +
					//"(x:"+(int)nodes[i].getX()+")" +
					//"(y:"+(int)nodes[i].getY()+")", 				
				}
				
				//close the file
				file.close();
			}

			catch (Exception e)
			{
				e.printStackTrace();
			}
			
		}
		
		public void setDiameter(int d){
			
			diameter = (d >= 0 ? d:10);
			repaint();
		}
		
		public Dimension getpreferedSize(){
			
			return new Dimension(200,200);
		}
		
		public Dimension getMinimunSize(){
			return getPreferredSize();
		}
	}
	
	
}
