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
package com.syncleus.dann.graph.search.optimization;

import com.syncleus.dann.graph.search.LocalSearch;
import com.syncleus.dann.graph.Graph;
import com.syncleus.dann.graph.Weighted;

public class HillClimbingLocalSearch<G extends Graph<N, ?>, N extends Weighted> implements LocalSearch<N>
{
	private G graph;

	public HillClimbingLocalSearch(G graph)
	{
		if( graph == null )
			throw new IllegalArgumentException("graph can not be null");
		this.graph = graph;
	}

	public N search(N startNode)
	{
		if( startNode == null )
			throw new IllegalArgumentException("startNode can not be null");

		N currentNode = startNode;
		do
		{
			N nextNode = null;
			for(N neighbor : graph.getTraversableNeighbors(currentNode))
			{
				if((nextNode == null)||(neighbor.getWeight() > nextNode.getWeight()))
					nextNode = neighbor;
			}

			if((nextNode == null)||(currentNode.getWeight() > nextNode.getWeight()))
				return currentNode;

			currentNode = nextNode;
		} while(currentNode != startNode);

		return null;
	}
}
