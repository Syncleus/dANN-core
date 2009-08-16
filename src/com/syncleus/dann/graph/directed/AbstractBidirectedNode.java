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
package com.syncleus.dann.graph.directed;

import com.syncleus.dann.graph.AbstractNode;
import com.syncleus.dann.graph.NodePair;
import java.util.List;

public abstract class AbstractBidirectedNode<E extends BidirectedEdge> extends AbstractNode<E> implements BidirectedNode<E>
{
	public int getIndegree()
	{
		int indegree = 0;
		List<E> allEdges = this.getEdges();
		for(E edge : allEdges)
		{
			if(edge.isDirected())
			{
				NodePair nodePair = edge.getNodes();
				if( (nodePair.getLeftNode().equals(this)) && (edge.getLeftEndState() == BidirectedEdge.EndState.Outward) )
					indegree++;
				else if( (nodePair.getRightNode().equals(this)) && (edge.getRightEndState() == BidirectedEdge.EndState.Outward) )
					indegree++;
			}
		}

		return indegree;
	}

	public int getOutdegree()
	{
		int outdegree = 0;
		List<E> allEdges = this.getEdges();
		for(E edge : allEdges)
		{
			if(edge.isDirected())
			{
				NodePair nodePair = edge.getNodes();
				if( (nodePair.getLeftNode().equals(this)) && (edge.getRightEndState() == BidirectedEdge.EndState.Outward) )
					outdegree++;
				else if( (nodePair.getRightNode().equals(this)) && (edge.getLeftEndState() == BidirectedEdge.EndState.Outward) )
					outdegree++;
			}
		}

		return outdegree;
	}
}
