package org.cloudbus.cloudsim.brite.Visualizer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.StringTokenizer;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

public class BRITETopology extends JPanel {

	private static String filePath;

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

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

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

			//for the number of edges
			for (int i=0; i<edges.length; i++)
			{
				//construct each edge
				edges[i] = new Edge(file.readLine());
			}


			//draw the edges
			for (int i = 0;i < edges.length;i++) {
				String [] fonts = getToolkit().getFontList();
				Font font = new Font(fonts[2], Font.BOLD, 20);
				g.setFont(font);
				edges[i].paint(g, nodes);
			}//fim for

			//draw the nodes
			for (int i = 0;i < nodes.length;i++)
			{  
/*				String [] fonts = getToolkit().getFontList();
				Font font;
				int font_size = 20;
				int x = 20;
				int y = 25;
				int line_spacing = 25;
				for (int j = 0; j < fonts.length; j++)
				{
					font = new Font(fonts[j], Font.BOLD, font_size);
					g.setFont(font);
					g.drawString(fonts[j], x, y);
					y += line_spacing;
				}		
*/
				String [] fonts = getToolkit().getFontList();
				Font font = new Font(fonts[0], Font.BOLD, 20);
				g.setFont(font);
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

	public static void main(String... args) {
		JFrame frame = new JFrame("REALCLOUDSIM - BRITE TOPOLOGY");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		filePath = args[0];
		String opcao = args[1];

		if (opcao.equals("0")){

			//filePath = "/usr/local/src/workspace/REALCloudSim-1.0/topology.brite";

			final JScrollPane pane = new JScrollPane();
			frame.add(pane);

			final BRITETopology canvas = new BRITETopology();
			canvas.setBackground(Color.WHITE);
			pane.setViewportView(canvas);
			frame.setSize(640, 480);
			frame.setVisible(true);

			try {
				Thread.sleep(6000);
			} catch (InterruptedException ex) {
				//
			}

			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					//Aqui sao ajustadas as dimensoes da imagem
					canvas.setPreferredSize(new Dimension(1000, 1000));
					pane.revalidate();
				}
			});
		}//fim if
		
	}//fim main
}