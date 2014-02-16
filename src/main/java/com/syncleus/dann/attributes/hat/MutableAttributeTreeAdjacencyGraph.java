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
package com.syncleus.dann.attributes.hat;

import com.syncleus.dann.graph.DirectedEdge;
import com.syncleus.dann.graph.MutableTreeAdjacencyGraph;

public class MutableAttributeTreeAdjacencyGraph<N extends AbstractHierarchicalAttributePool<T>, E extends DirectedEdge<N>, T> extends MutableTreeAdjacencyGraph<N, E>
{
	/**
	 * Since we need a graph before we can create the nodes, the only way to
	 * instantiate a graph is a mutable graph with a default constructor.
	 */
	public MutableAttributeTreeAdjacencyGraph()
	{
		super();
	}

	@Override
	public boolean add(final E newEdge)
	{
		if( super.add(newEdge) )
		{
			newEdge.getDestinationNode().setParent(newEdge.getSourceNode());
			return true;
		}

		return false;
	}

	@Override
	public boolean remove(final E edgeToRemove)
	{
		if( super.remove(edgeToRemove) )
		{
			edgeToRemove.getDestinationNode().setParent(null);
			return true;
		}

		return false;
	}

}
