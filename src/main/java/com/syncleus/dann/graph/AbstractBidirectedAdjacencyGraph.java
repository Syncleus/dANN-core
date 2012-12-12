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

public abstract class AbstractBidirectedAdjacencyGraph<
        NE extends BidirectedGraph.NodeEndpoint<?>,
        EE extends BidirectedGraph.EdgeEndpoint<?>
        > extends AbstractCloudGraph<NE, EE> implements BidirectedGraph<NE, EE>
{
/*
	protected AbstractBidirectedAdjacencyGraph()
	{
		super();
	}

	protected AbstractBidirectedAdjacencyGraph(final CloudGraph<NE, EE> copyGraph)
	{
		super(copyGraph.getTargets(), copyGraph.getEdges());
	}

	protected AbstractBidirectedAdjacencyGraph(final Set<NE> nodes, final Set<EE> edges)
	{
		super(nodes, edges);
	}
*/
/*
	@Override
	public Set<EE> getInEdges(final NE nodeEndpoint)
	{
		final Set<EE> inEdges = new HashSet<EE>();
		for(final EE edgeEndpoint : this.getEdgeEndpoints())
		{
            if( edgeEndpoint.isTraversableTo(nodeEndpoint) )
                inEdges.add(edgeEndpoint);

//			final List<BidirectedEdge<?>> adjacentNodes = Collections.<BidirectedEdge<?>>singletonList(edgeEndpoint.getTarget());
//			adjacentNodes.remove(nodeEndpoint);
//			final NE adjacentNode = adjacentNodes.get(0);

//			if( edgeEndpoint.isTraversable(adjacentNode) && edgeEndpoint.getTraversableNodes(adjacentNode).contains(nodeEndpoint) )
//				inEdges.add(edgeEndpoint);

		}
		return Collections.unmodifiableSet(inEdges);
	}
*/

	@Override
	protected AbstractBidirectedAdjacencyGraph<NE, EE> clone()
	{
		return (AbstractBidirectedAdjacencyGraph<NE, EE>) super.clone();
	}
}
