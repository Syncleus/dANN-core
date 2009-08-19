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
package com.syncleus.dann.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class AbstractBidirectedGraph extends AbstractGraph implements BidirectedGraph
{
	public List<? extends BidirectedEdge> getEdges()
	{
		List<BidirectedEdge> allEdges = new ArrayList<BidirectedEdge>();
		Set<? extends BidirectedNode> allNodes = this.getNodes();
		Set<BidirectedNode> traversedNodes = new HashSet<BidirectedNode>();
		for(BidirectedNode node : allNodes)
		{
			List<? extends BidirectedEdge> currentEdges = node.getEdges();

			for(BidirectedEdge edge : currentEdges)
			{
				NodePair<? extends BidirectedNode> currentNodePair = edge.getNodePair();
				if((!traversedNodes.contains(currentNodePair.getLeftNode()))&&(!traversedNodes.contains(currentNodePair.getRightNode())))
					allEdges.add(edge);
			}

			traversedNodes.add(node);
		}

		return Collections.unmodifiableList(allEdges);
	}

	public boolean isStronglyConnected()
	{
		return false;
	}

	public Set<? extends BidirectedGraph> getStrongComponents()
	{
		return null;
	}

	public boolean isPolytree()
	{
		return false;
	}

	public Set<? extends BidirectedGraph> getConnectedComponents()
	{
		return null;
	}
}
