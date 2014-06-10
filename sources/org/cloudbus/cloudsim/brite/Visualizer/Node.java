package org.cloudbus.cloudsim.brite.Visualizer;

import java.util.StringTokenizer;
import java.awt.*;

/**
 * Representation of a node from a BRITE topology.
 */
public class Node
{
	/**
	 * The id of the node.
	 */
	private int id = -1;

	/**
	 * The x coordinate.
	 */
	private double x = -1.0;

	/**
	 * The y coordinate.
	 */
	private double y = -1.0;

	/**
	 * Which Autonimous System (AS) does this belong to?
	 */
	private int AS = -1;	

	/**
	 * Draw different colours for different AS groups.
	 */
	public final Color colours[] = {Color.black, Color.blue, Color.cyan, Color.gray, Color.green, Color.magenta, Color.orange, Color.pink, Color.red, Color.yellow};

	/**
	 * Default constructor
	 */
	public Node(final String str)
	{
		StringTokenizer t = new StringTokenizer(str);
		id = Integer.parseInt(t.nextToken());
		x = Double.parseDouble(t.nextToken());
		y = Double.parseDouble(t.nextToken());
		//discard the in and out degree.
		t.nextToken();
		t.nextToken();
		AS = Integer.parseInt(t.nextToken());
	}

	/**
	 * Paint this node's position
	 *
	 * @param g Graphics object.
	 */
	public void paint(Graphics g)
	{
		//g.setColor(colours[AS % colours.length]);
		g.setColor(Color.BLACK);
		g.fillOval((int)x - 2, (int)y - 2, 5, 5);
	}

	/**
	 * Return the x value.
	 *
	 * @return x;
	 */
	public double getX()
	{
		return x;
	}

	/**
	 * Return the y value.
	 *
	 * @return y;
	 */
	public double getY()
	{
		return y;
	}
	
	/**
	 * Return the ID value.
	 *
	 * @return id;
	 */
	public double getID()
	{
		return id;
	}
}
