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

import java.util.ArrayList;

public class NodeSet
{
	ArrayList<Node> nodes = new ArrayList<Node>();
	
	public Node peek()
	{
		Node lowestNode = null;
		for(Node node : nodes )
		{
			if(lowestNode == null)
				lowestNode = node;
			else
			{
				if( lowestNode.getPathCost() > node.getPathCost())
					lowestNode = node;
			}
		}
		return lowestNode;
	}
	
	public Node pop()
	{
		Node returnValue = this.peek();
		this.nodes.remove(returnValue);
		return returnValue;
	}
	
	public void add(Node node)
	{
		if( this.nodes.contains(node) == false)
			this.nodes.add(node);
	}
	
	public boolean contains(Node node)
	{
		return this.nodes.contains(node);
	}
	
	public boolean isEmpty()
	{
		return this.nodes.isEmpty();
	}
}
