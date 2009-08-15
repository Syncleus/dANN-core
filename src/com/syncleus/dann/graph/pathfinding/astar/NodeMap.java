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

import java.util.TreeSet;

public abstract class NodeMap
{
	public NodeMap()
	{
	}
	
	protected abstract Node[] getMap();
	protected abstract Node getStart();
	protected abstract Node getGoal();
	
	private void reset()
	{
		for(Node node : this.getMap())
		{
			node.reset();
		}
	}
	
	public Node[] getPath()
	{
		if( this.getStart() == null)
			throw new IllegalStateException("Start node is null!");
		
		if( this.getGoal() == null )
			throw new IllegalStateException("Goal node is null");
		
		if( this.getMap() == null )
			throw new IllegalStateException("Map is null!");
		
		//create a set of nodes with a path to the start
		//but still needs exploring. The start node is the
		//initial node.
		NodeSet openNodes = new NodeSet();
		openNodes.add(this.getStart());
		
		//all the nodes that have been explored.
		NodeSet closedNodes = new NodeSet();
		
		//now lets start exploring
		while( openNodes.isEmpty() == false )
		{
			Node current = openNodes.pop();
			
			//check if we reached the goal!
			if( current == this.getGoal() )
			{
				Node[] returnPath = current.getReturnPath();
				Node[] forwardPath = new Node[returnPath.length];
				int lcv = returnPath.length - 1;
				for(Node step : returnPath)
				{
					forwardPath[lcv--] = step;
				}
				return forwardPath;
			}
			
			for(Node peerNode : current.getPaths())
			{
				if(closedNodes.contains(peerNode) == false)
					openNodes.add(peerNode);
				
				if(peerNode != this.getStart())
				{
					if( peerNode.getParent() == null )
						peerNode.setParent(current);
					else
					{
						if(peerNode.getPathCost() > (peerNode.getCost() + current.getPathCost()))
							peerNode.setParent(current);
					}
				}
			}
			
			closedNodes.add(current);
			
		}
		
		return null;
	}
}
