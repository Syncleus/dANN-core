/******************************************************************************
 *                                                                             *
 *  Copyright: (c) Syncleus, Inc.                                              *
 *                                                                             *
 *  You may redistribute and modify this source code under the terms and       *
 *  conditions of the Open Source Community License - Type C version 1.0       *
 *  or any later version as published by Syncleus, Inc. at www.syncleus.com.   *
 *  There should be a copy of the license included with this file. If a copy   *
 *  of the license is not included you are granted no right to distribute or   *
 *  otherwise use this file except through a legal and valid license. You      *
 *  should also contact Syncleus, Inc. at the information below if you cannot  *
 *  find a license:                                                            *
 *                                                                             *
 *  Syncleus, Inc.                                                             *
 *  2604 South 12th Street                                                     *
 *  Philadelphia, PA 19148                                                     *
 *                                                                             *
 ******************************************************************************/
package com.syncleus.dann.graph.pathfinding.astar;

import java.util.HashSet;
import java.util.Stack;

public class Node
{
	private Node parent = null;
	private double cost = 1.0;
	private HashSet<Node> paths = new HashSet<Node>();
	
	public Node()
	{
	}
	
	public Node(double cost)
	{
		this.cost = cost;
	}
	
	public void connect(Node connectNode)
	{
		this.paths.add(connectNode);
	}
	
	public void disconnect(Node disconnectNode)
	{
		this.paths.remove(disconnectNode);
	}
	
	public double getPathCost()
	{
		if( this.parent == null)
			return this.getCost();
		
		return this.parent.getPathCost() + this.getCost();
	}
	
	public void reset()
	{
		this.parent = null;
	}
	
	public Node[] getReturnPath()
	{
		Stack<Node> returnPath = new Stack<Node>();
		
		Node current = this;
		while(current != null )
		{
			returnPath.add(current);
			current = current.getParent();
		}
		
		Node[] returnValue = new Node[returnPath.size()];
		for( int lcv = returnValue.length - 1; lcv >= 0; lcv--)
		{
			returnValue[lcv] = returnPath.pop();
		}
		
		return returnValue;
	}
	
	public double getCost()
	{
		return cost;
	}
	
	public Node[] getPaths()
	{
		Node[] returnValue = new Node[this.paths.size()];
		this.paths.toArray(returnValue);
		return returnValue;
	}
	
	public Node getParent()
	{
		return parent;
	}
	
	public void setParent(Node parent)
	{
		this.parent = parent;
	}
	
}
